// Get all input elements
const attendanceInput = document.getElementById('attendance');
const excusedInput = document.getElementById('excused');
const lab1Input = document.getElementById('lab1');
const lab2Input = document.getElementById('lab2');
const lab3Input = document.getElementById('lab3');
const resultsDiv = document.getElementById('results');
const resultsContent = document.getElementById('resultsContent');

// Add event listeners to all inputs for real-time calculation
[attendanceInput, excusedInput, lab1Input, lab2Input, lab3Input].forEach(input => {
    input.addEventListener('input', enforceValidation);
    input.addEventListener('input', calculateGrade);
});

function enforceValidation(event) {
    const input = event.target;
    let value = parseFloat(input.value);
    
    // Enforce attendance limit (0-5)
    if (input.id === 'attendance') {
        if (value > 5) {
            input.value = 5;
        } else if (value < 0) {
            input.value = 0;
        }
    }
    
    // Enforce excused absences limit (0-5)
    if (input.id === 'excused') {
        if (value > 5) {
            input.value = 5;
        } else if (value < 0) {
            input.value = 0;
        }
    }
    
    // Enforce lab work grade limits (0-100)
    if (input.id === 'lab1' || input.id === 'lab2' || input.id === 'lab3') {
        if (value > 100) {
            input.value = 100;
        } else if (value < 0) {
            input.value = 0;
        }
    }
}

function calculateGrade() {
    let attendanceCount = parseFloat(attendanceInput.value) || 0;
    let excusedAbsences = parseFloat(excusedInput.value) || 0;
    let lab1 = parseFloat(lab1Input.value) || 0;
    let lab2 = parseFloat(lab2Input.value) || 0;
    let lab3 = parseFloat(lab3Input.value) || 0;
    
    // Check if all inputs have values
    if (!attendanceInput.value || !lab1Input.value || !lab2Input.value || !lab3Input.value) {
        resultsContent.innerHTML = '<p class="info">Please enter all values to see results.</p>';
        resultsDiv.classList.add('show');
        return;
    }
    
    // Calculate total absences (absences without excuse letter)
    const maxAttendances = 5;
    let effectiveAttendance = attendanceCount + excusedAbsences;
    
    // Cap effective attendance at max
    if (effectiveAttendance > maxAttendances) {
        effectiveAttendance = maxAttendances;
    }
    
    const totalAbsences = maxAttendances - effectiveAttendance;
    
    // Check if student has 4 or more absences (FAILED)
    if (totalAbsences >= 4) {
        let resultsHTML = `
            <div class="requirement failed">
                <h3>FAILED DUE TO ABSENCES</h3>
                <div class="result-item">
                    <span class="result-label">Present:</span>
                    <span class="result-value">${attendanceCount}</span>
                </div>
                <div class="result-item">
                    <span class="result-label">Excused Absences:</span>
                    <span class="result-value">${excusedAbsences}</span>
                </div>
                <div class="result-item">
                    <span class="result-label">Unexcused Absences:</span>
                    <span class="result-value warning">${totalAbsences}</span>
                </div>
                <p class="warning" style="margin-top: 15px; font-weight: bold;">
                    RESULT: FAILED<br><br>
                    You have 4 or more unexcused absences.<br>
                    According to policy, you automatically fail the course.
                </p>
            </div>
        `;
        
        resultsContent.innerHTML = resultsHTML;
        resultsDiv.classList.add('show');
        return;
    }
    
    // Calculate Lab Work Average
    const labWorkAverage = (lab1 + lab2 + lab3) / 3;
    
    // Calculate Attendance Score
    const attendanceScore = (effectiveAttendance / maxAttendances) * 100;
    
    // Calculate Class Standing
    const classStanding = (attendanceScore * 0.40) + (labWorkAverage * 0.60);
    
    // Calculate Required Prelim Exam Scores
    const requiredForPass = (75 - (classStanding * 0.30)) / 0.70;
    const requiredForExcellent = (100 - (classStanding * 0.30)) / 0.70;
    
    // Determine student's standing
    let standing = '';
    let standingClass = '';
    
    if (classStanding >= 90) {
        standing = 'Excellent! You have a very strong class standing.';
        standingClass = 'excellent';
    } else if (classStanding >= 75) {
        standing = 'Good! You have a passing class standing.';
        standingClass = 'pass';
    } else {
        standing = 'You need to improve your class standing.';
        standingClass = 'warning';
    }
    
    // Build results HTML
    let resultsHTML = `
        <div class="result-item">
            <span class="result-label">Attendance Score:</span>
            <span class="result-value">${attendanceScore.toFixed(2)}</span>
        </div>
        <div class="result-item">
            <span class="result-label">Present:</span>
            <span class="result-value">${attendanceCount}, Excused: ${excusedAbsences}, Unexcused: ${totalAbsences}</span>
        </div>
        <div class="result-item">
            <span class="result-label">Effective Attendance:</span>
            <span class="result-value">${effectiveAttendance.toFixed(0)} out of 5</span>
        </div>
        <div class="result-item">
            <span class="result-label">Lab Work 1 Grade:</span>
            <span class="result-value">${lab1.toFixed(2)}</span>
        </div>
        <div class="result-item">
            <span class="result-label">Lab Work 2 Grade:</span>
            <span class="result-value">${lab2.toFixed(2)}</span>
        </div>
        <div class="result-item">
            <span class="result-label">Lab Work 3 Grade:</span>
            <span class="result-value">${lab3.toFixed(2)}</span>
        </div>
        <div class="result-item">
            <span class="result-label">Lab Work Average:</span>
            <span class="result-value">${labWorkAverage.toFixed(2)}</span>
        </div>
        <div class="result-item">
            <span class="result-label">Class Standing:</span>
            <span class="result-value">${classStanding.toFixed(2)}</span>
        </div>
    `;
    
    // Add passing requirement
    let passMessage = '';
    let passClass = '';
    
    if (requiredForPass <= 0) {
        passMessage = "You've already secured a passing grade!";
        passClass = 'excellent';
    } else if (requiredForPass > 100) {
        passMessage = 'Unfortunately, passing is mathematically impossible with your current class standing.';
        passClass = 'warning';
    } else {
        passMessage = 'You need this score on the Prelim Exam to pass.';
        passClass = '';
    }
    
    resultsHTML += `
        <div class="requirement">
            <h3>To Pass (75%)</h3>
            <div class="score ${passClass}">${requiredForPass.toFixed(2)}</div>
            <p class="${passClass}">${passMessage}</p>
        </div>
    `;
    
    // Add excellent requirement
    let excellentMessage = '';
    let excellentClass = '';
    
    if (requiredForExcellent <= 0) {
        excellentMessage = "You've already secured an excellent grade!";
        excellentClass = 'excellent';
    } else if (requiredForExcellent > 100) {
        excellentMessage = 'This would require more than 100% on the exam.';
        excellentClass = 'info';
    } else {
        excellentMessage = 'You need this score on the Prelim Exam for an excellent grade.';
        excellentClass = '';
    }
    
    resultsHTML += `
        <div class="requirement">
            <h3>To Achieve Excellent (100%)</h3>
            <div class="score ${excellentClass}">${requiredForExcellent.toFixed(2)}</div>
            <p class="${excellentClass}">${excellentMessage}</p>
        </div>
    `;
    
    // Add student standing
    resultsHTML += `
        <div class="requirement">
            <h3>Student Standing</h3>
            <p class="${standingClass}">${standing}</p>
        </div>
    `;
    
    resultsContent.innerHTML = resultsHTML;
    resultsDiv.classList.add('show');
}

// Initial calculation if there are default values
calculateGrade();