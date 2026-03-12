# Student Leave Portal (DSA Project)

## Project Description
The **Student Leave Portal** is a console-based Java application designed to manage student leave requests efficiently. It simulates a real-world leave management system where students can register, login, and apply for leaves, while administrators can review, approve, or reject these requests in the order they were received. This project acts as a practical demonstration of core **Data Structures and Algorithms (DSA)**, specifically focusing on linear data structures like Arrays and Queues.

## Data Structures Used
* **`ArrayList` (Java Collections):** Used to act as the central database of the application. All leave requests submitted by students are appended to an `ArrayList`. This allows for fast O(1) time complexity when adding a new request, and dynamic resizing so we don't have to worry about a fixed capacity. We also use `ArrayList` to keep a record of registered users.
* **`Queue` (implemented via `LinkedList`):** Used to manage pending leave applications efficiently. When a new leave is submitted, it is enqueued at the back of the queue. The Admin processes leave approvals following the **FIFO (First-In, First-Out)** principle by dequeuing requests from the front of the queue, ensuring fair and ordered processing.

## Algorithms Used
* **Traversal Algorithm:** We use standard list traversal (Iterating over the ArrayList) to iterate over our leave records while displaying history, showing "My Leave Requests" to the student, and letting the Admin "View All Leave Requests".
* **Linear Search Algorithm:** Used within the Admin panel when searching for a leave request by a student's Roll Number. It iterates through the ArrayList, comparing each record's roll number with the search key to return the matched records. It's also used during student login authentication.

## Features
**Student Features:**
* **Register User:** Create a new student profile with a Username, Password, and Roll Number.
* **Login:** Authenticate the student to keep track of their sessions.
* **Apply for Leave:** Submit leave duration and reason (automatically queued for Admin).
* **View My Leave Requests:** Students can check the status (Pending/Approved/Rejected) of their own past and present applications.

**Admin Features:**
* **View All Leave Requests:** View the entire history of leave applications.
* **Approve Leave:** Dequeue the oldest pending request and mark it as Approved, adding an administrative remark.
* **Reject Leave:** Dequeue the oldest pending request and mark it as Rejected, with reasons provided via remark.
* **Search Leave by Roll Number:** Filter reports to find all requests made by a specific student.

## Folder Structure
```text
DSA_Student_Leave_Portal/
│
├── StudentLeavePortal.java   # Contains the complete Java program logic
└── README.md                 # Project documentation
```

## How to Compile and Run the Program

1. Ensure you have the **Java Development Kit (JDK)** installed on your machine.
2. Open your terminal or command prompt.
3. Navigate to the `DSA_Student_Leave_Portal` folder:
   ```bash
   cd c:/Users/mrsai/OneDrive/Desktop/PETER/DSA_Student_Leave_Portal
   ```
4. Compile the Java file using the `javac` command:
   ```bash
   javac StudentLeavePortal.java
   ```
5. Run the compiled Java program:
   ```bash
   java StudentLeavePortal
   ```
6. Follow the on-screen menu instructions to interact with the system! Tip: Use `admin123` as the admin password.
