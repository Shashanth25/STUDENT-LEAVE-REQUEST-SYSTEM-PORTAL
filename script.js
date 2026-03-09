/* CORE LOGIC */
let users = JSON.parse(localStorage.getItem("users")) || [];
let leaveRequests = JSON.parse(localStorage.getItem("leaveData")) || [];
let currentRole = "";

function showSection(id) {
    document.querySelectorAll('.card').forEach(c => c.classList.add('hidden'));
    document.getElementById(id).classList.remove('hidden');
}

function register() {
    let role = document.getElementById("registerRole").value;
    let user = document.getElementById("regUser").value.trim();
    let pass = document.getElementById("regPass").value.trim();
    
    if(!user || !pass) return alert("Fill all fields");
    if(users.find(u => u.username === user)) return alert("Username already exists");
    
    users.push({username:user, password:pass, role:role});
    localStorage.setItem("users", JSON.stringify(users));
    alert("Account created successfully!");
    showSection('loginSection');
}

function login() {
    let role = document.getElementById("loginRole").value;
    let user = document.getElementById("loginUser").value.trim();
    let pass = document.getElementById("loginPass").value.trim();
    
    let valid = users.find(u => u.username === user && u.password === pass && u.role === role);
    if(valid) {
        currentRole = role;
        showSection('portalSection');
        document.getElementById("dashTitle").innerText = (role === "admin") ? "Admin Management" : "Student Dashboard";
        if(role === "admin") {
            document.getElementById("studentUI").classList.add("hidden");
            document.getElementById("adminUI").classList.remove("hidden");
            displayRequests();
        }
    } else {
        alert("Invalid Username or Password");
    }
}

function submitLeave() {
    // FIXED IDs: Matches the 'id' attributes in the HTML tags above
    let nameVal = document.getElementById("applyName").value.trim();
    let rollVal = document.getElementById("applyRoll").value.trim();
    let fromVal = document.getElementById("applyFrom").value;
    let toVal = document.getElementById("applyTo").value;
    let reasonVal = document.getElementById("applyReason").value.trim();

    // Verification check to stop "Fill all fields" error
    if(!nameVal || !rollVal || !fromVal || !toVal || !reasonVal) {
        alert("Fill all fields");
        return;
    }

    // 24-Hour Restriction logic
    let now = Date.now();
    let history = leaveRequests.filter(r => r.roll === rollVal);
    if(history.length > 0) {
        let last = history.reduce((a, b) => a.timestamp > b.timestamp ? a : b);
        if((now - last.timestamp) < 86400000) {
            alert("Only one request allowed per 24 hours.");
            return;
        }
    }

    leaveRequests.push({
        name: nameVal,
        roll: rollVal,
        from: fromVal,
        to: toVal,
        reason: reasonVal,
        status: "Pending", // Forces initial status to Pending
        adminRemark: "",   // Prepared for admin reason
        timestamp: now
    });

    localStorage.setItem("leaveData", JSON.stringify(leaveRequests));
    alert("Request Submitted! Status: Pending.");
}

function displayTable(data) {
    let table = document.getElementById("leaveTable");
    table.classList.remove("hidden");
    table.innerHTML = `<tr>
        <th>Roll</th>
        <th>Dates</th>
        <th>Student Reason</th>
        <th>Status</th>
        <th>Admin Response</th>
        <th>Action</th>
    </tr>`;
    
    data.forEach(req => {
        let row = table.insertRow();
        row.insertCell(0).innerText = req.roll;
        row.insertCell(1).innerText = req.from + " to " + req.to;
        row.insertCell(2).innerText = req.reason;
        
        let sCell = row.insertCell(3);
        sCell.innerText = req.status;
        sCell.className = req.status.toLowerCase();
        
        // Displays the reason given by the admin
        row.insertCell(4).innerText = req.adminRemark || "-";

        let aCell = row.insertCell(5);
        if(currentRole === "admin" && req.status === "Pending") {
            aCell.innerHTML = `
                <input type="text" class="remark-input" id="rem-${req.timestamp}" placeholder="Reason for response">
                <br>
                <button style="padding:4px; font-size:10px; width:45%; background:#5c8f1f;" onclick="updateStat(${req.timestamp},'Approved')">Approve</button>
                <button style="padding:4px; font-size:10px; width:45%; background:red;" onclick="updateStat(${req.timestamp},'Rejected')">Reject</button>
            `;
        } else {
            aCell.innerText = "-";
        }
    });
}

function viewMyRequests() {
    let r = document.getElementById("viewRoll").value.trim();
    if(!r) return alert("Please enter your Roll Number");
    displayTable(leaveRequests.filter(req => req.roll === r));
}

function displayRequests() {
    let s = document.getElementById("search").value.toLowerCase();
    displayTable(leaveRequests.filter(req => req.roll.toLowerCase().includes(s)));
}

function updateStat(t, s) {
    let remark = document.getElementById(`rem-${t}`).value.trim();
    if(!remark) return alert("Please provide a reason for the student."); // Ensures admin gives a reason

    let request = leaveRequests.find(r => r.timestamp === t);
    if(request) {
        request.status = s;
        request.adminRemark = remark; // Saves the admin's response reason
        localStorage.setItem("leaveData", JSON.stringify(leaveRequests));
        displayRequests();
    }
}