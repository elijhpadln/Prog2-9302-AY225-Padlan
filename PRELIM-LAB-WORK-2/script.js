// --- CONFIGURATION ---

// List of authorized users
const AUTHORIZED_USERS = [
    { username: "admin",      password: "password123" },
    { username: "Elijah",   password: "Elijah123" },
    { username: "Lean", password: "Lean123" },
    { username: "Eliska123",  password: "Eliska123" },
    { username: "guest",      password: "guest123" },
];

function handleLogin() {
    // Get Elements
    const userIn = document.getElementById('username');
    const passIn = document.getElementById('password');
    const loginBtn = document.getElementById('loginBtn');
    const resultArea = document.getElementById('result-area');
    const card = document.getElementById('card');

    // Get Values
    const userVal = userIn.value;
    const passVal = passIn.value;

    // Reset Styles
    userIn.classList.remove('error-border');
    passIn.classList.remove('error-border');
    card.classList.remove('shake');

    // --- CHECK CREDENTIALS ---
    const validUser = AUTHORIZED_USERS.find(user => 
        user.username === userVal && user.password === passVal
    );

    if (validUser) {
        // >>> SUCCESS <<<
        playSuccessSound();

        const now = new Date();
        const timestamp = now.toLocaleString();

        document.getElementById('user-display').innerText = userVal;
        document.getElementById('time-display').innerText = timestamp;
        
        resultArea.style.display = 'block';
        
        // Disable Inputs & Button
        loginBtn.innerText = "Success";
        loginBtn.disabled = true;
        userIn.disabled = true;
        passIn.disabled = true;

        downloadFile(userVal, timestamp);

    } else {
        // >>> FAILURE <<<
        playErrorBeep();

        userIn.classList.add('error-border');
        passIn.classList.add('error-border');
        card.classList.add('shake');

        setTimeout(() => {
            card.classList.remove('shake');
        }, 400);

        setTimeout(() => {
            alert("Incorrect Username or Password");
        }, 100);
    }
}

// --- SOUND FUNCTIONS ---

function playSuccessSound() {
    const AudioContext = window.AudioContext || window.webkitAudioContext;
    if (!AudioContext) return;
    const ctx = new AudioContext();
    const notes = [523.25, 659.25, 783.99, 1046.50]; 
    
    notes.forEach((freq, i) => {
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        osc.frequency.value = freq;
        osc.type = 'sine';
        const start = ctx.currentTime + (i * 0.05);
        gain.gain.setValueAtTime(0, start);
        gain.gain.linearRampToValueAtTime(0.1, start + 0.05);
        gain.gain.exponentialRampToValueAtTime(0.001, start + 1.2);
        osc.connect(gain);
        gain.connect(ctx.destination);
        osc.start(start);
        osc.stop(start + 1.2);
    });
}

function playErrorBeep() {
    const AudioContext = window.AudioContext || window.webkitAudioContext;
    if (!AudioContext) return;
    const ctx = new AudioContext();
    const osc = ctx.createOscillator();
    const gain = ctx.createGain();
    osc.frequency.value = 180;
    osc.type = 'sawtooth';
    gain.gain.value = 0.1;
    osc.connect(gain);
    gain.connect(ctx.destination);
    osc.start();
    osc.stop(ctx.currentTime + 0.2);
}

// --- FILE DOWNLOAD ---
function downloadFile(username, timestamp) {
    const content = `Attendance Summary\nUsername: ${username}\nTime: ${timestamp}\nStatus: Present`;
    const blob = new Blob([content], { type: 'text/plain' });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = 'attendance.txt';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

// --- EVENT LISTENERS (Trigger Login on 'Enter') ---

// Wait for DOM to load (ensures elements exist) or place script at end of body
document.addEventListener('DOMContentLoaded', () => {
    const userIn = document.getElementById('username');
    const passIn = document.getElementById('password');

    // Function to check for Enter key
    function checkEnter(event) {
        if (event.key === "Enter") {
            handleLogin();
        }
    }

    // Add listeners to both inputs
    if (userIn) userIn.addEventListener("keypress", checkEnter);
    if (passIn) passIn.addEventListener("keypress", checkEnter);
});