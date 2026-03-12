import java.util.*;

// Class to represent a student's leave request
class LeaveRequest {
    String studentName;
    String rollNumber;
    String fromDate;
    String toDate;
    String reason;
    String status; // Pending, Approved, Rejected
    String adminRemark;

    public LeaveRequest(String studentName, String rollNumber, String fromDate, String toDate, String reason) {
        this.studentName = studentName;
        this.rollNumber = rollNumber.trim();
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.status = "Pending";
        this.adminRemark = "None";
    }

    @Override
    public String toString() {
        return "--------------------------------------\n" +
               "Student Name  : " + studentName + " (Roll No: " + rollNumber + ")\n" +
               "Duration      : " + fromDate + " to " + toDate + "\n" +
               "Reason        : " + reason + "\n" +
               "Status        : " + status + "\n" +
               "Admin Remark  : " + adminRemark + "\n" +
               "--------------------------------------";
    }
}

// Class to represent a registered User (Student)
class User {
    String username;
    String password;
    String rollNumber;

    public User(String username, String password, String rollNumber) {
        this.username = username;
        this.password = password;
        this.rollNumber = rollNumber;
    }
}

public class StudentLeavePortal {
    // ==========================================
    // DATA STRUCTURES USED
    // ==========================================
    
    // 1. ArrayList: Used to store all leave requests permanently (history).
    // Allows dynamic sizing and O(1) random access.
    static ArrayList<LeaveRequest> allRequests = new ArrayList<>();
    
    // 2. Queue (LinkedList): Used to manage pending leave approvals in FIFO (First-In-First-Out) order.
    // The oldest pending request is addressed first by the admin.
    static Queue<LeaveRequest> pendingApprovals = new LinkedList<>();

    // ArrayList to store registered users
    static ArrayList<User> users = new ArrayList<>();
    static User loggedInUser = null;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Student Leave Portal!");
        
        while (true) {
            System.out.println("\n======================================");
            System.out.println("               MAIN MENU              ");
            System.out.println("======================================");
            System.out.println("1. Register User");
            System.out.println("2. Login " + (loggedInUser != null ? "[Logged in: " + loggedInUser.username + "]" : ""));
            System.out.println("3. Apply for Leave");
            System.out.println("4. View My Leave Requests");
            System.out.println("5. Admin Panel");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1: registerUser(); break;
                case 2: loginUser(); break;
                case 3: applyForLeave(); break;
                case 4: viewMyRequests(); break;
                case 5: adminPanel(); break;
                case 6: exitSystem(); return;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void exitSystem() {
        System.out.println("Exiting System. Goodbye!");
    }

    public static void registerUser() {
        System.out.print("Enter Username (Student Name): ");
        String username = scanner.nextLine();
        System.out.print("Enter Roll Number: ");
        String rollNumber = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        users.add(new User(username, password, rollNumber));
        System.out.println("Registration successful! You can now login.");
    }

    public static void loginUser() {
        if (loggedInUser != null) {
            System.out.println("Already logged in as " + loggedInUser.username + ". Please restart to login as someone else.");
            return;
        }
        System.out.print("Enter Roll Number: ");
        String rollNumber = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // ALGORITHM: Linear Search to find user
        for (User user : users) {
            if (user.rollNumber.equalsIgnoreCase(rollNumber) && user.password.equals(password)) {
                loggedInUser = user;
                System.out.println("Login successful! Welcome " + user.username);
                return;
            }
        }
        System.out.println("Invalid credentials. Try again.");
    }

    public static void applyForLeave() {
        if (loggedInUser == null) {
            System.out.println("Error: Please login first!");
            return;
        }

        System.out.print("Leave From Date (DD/MM/YYYY): ");
        String fromDate = scanner.nextLine();
        System.out.print("Leave To Date   (DD/MM/YYYY): ");
        String toDate = scanner.nextLine();
        System.out.print("Reason for leave: ");
        String reason = scanner.nextLine();

        LeaveRequest request = new LeaveRequest(loggedInUser.username, loggedInUser.rollNumber, fromDate, toDate, reason);
        
        // DSA Concept: ArrayList Insertion
        allRequests.add(request);
        
        // DSA Concept: Queue Enqueue operation (add to rear of queue)
        pendingApprovals.add(request);
        
        System.out.println("Leave request submitted successfully. It is pending admin approval.");
    }

    public static void viewMyRequests() {
        if (loggedInUser == null) {
            System.out.println("Error: Please login first!");
            return;
        }

        System.out.println("\n--- My Leave Requests ---");
        boolean found = false;
        
        // ALGORITHM: Traversal of ArrayList
        for (LeaveRequest req : allRequests) {
            if (req.rollNumber.equalsIgnoreCase(loggedInUser.rollNumber)) {
                System.out.println(req);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("You have no leave requests.");
        }
    }

    public static void adminPanel() {
        System.out.print("Enter Admin Password (hint: admin123): ");
        String pass = scanner.nextLine();
        
        if (!pass.equals("admin123")) {
            System.out.println("Incorrect Admin Password!");
            return;
        }

        while (true) {
            System.out.println("\n======================================");
            System.out.println("             ADMIN PANEL              ");
            System.out.println("======================================");
            System.out.println("Queue Status: " + pendingApprovals.size() + " pending requests");
            System.out.println("1. View All Leave Requests");
            System.out.println("2. Approve Leave");
            System.out.println("3. Reject Leave");
            System.out.println("4. Search Leave by Roll Number");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter choice: ");
            
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1: viewAllRequests(); break;
                case 2: approveLeave(); break;
                case 3: rejectLeave(); break;
                case 4: searchLeaveByRollNumber(); break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    public static void viewAllRequests() {
        System.out.println("\n--- All Leave Requests Database ---");
        if (allRequests.isEmpty()) {
            System.out.println("No leave requests in the system.");
            return;
        }
        
        // ALGORITHM: Traversal (Iterating over ArrayList)
        for (LeaveRequest req : allRequests) {
            System.out.println(req);
        }
    }

    public static void approveLeave() {
        // DSA Concept: Check if Queue is empty
        if (pendingApprovals.isEmpty()) {
            System.out.println("No pending leave requests to approve.");
            return;
        }

        // DSA Concept: Queue Peek operation (getting the front element without removing)
        LeaveRequest req = pendingApprovals.peek();
        
        System.out.println("\n--- Processing Next Request in Queue ---");
        System.out.println(req);
        
        System.out.print("Enter Admin Remark: ");
        String remark = scanner.nextLine();
        
        req.adminRemark = remark;
        req.status = "Approved";
        
        // DSA Concept: Queue Dequeue operation (remove front element after processing)
        pendingApprovals.poll();
        System.out.println("Leave has been Approved.");
    }

    public static void rejectLeave() {
        // DSA Concept: Check if Queue is empty
        if (pendingApprovals.isEmpty()) {
            System.out.println("No pending leave requests to reject.");
            return;
        }

        // DSA Concept: Queue Peek operation (getting the front element without removing)
        LeaveRequest req = pendingApprovals.peek();
        
        System.out.println("\n--- Processing Next Request in Queue ---");
        System.out.println(req);
        
        System.out.print("Enter Admin Remark: ");
        String remark = scanner.nextLine();
        
        req.adminRemark = remark;
        req.status = "Rejected";
        
        // DSA Concept: Queue Dequeue operation (remove front element after processing)
        pendingApprovals.poll();
        System.out.println("Leave has been Rejected.");
    }

    public static void searchLeaveByRollNumber() {
        System.out.print("Enter student Roll Number to search: ");
        String rollToSearch = scanner.nextLine();
        
        System.out.println("\n--- Search Results ---");
        boolean found = false;
        
        // ALGORITHM: Linear Search
        for (LeaveRequest req : allRequests) {
            if (req.rollNumber.equalsIgnoreCase(rollToSearch)) {
                System.out.println(req);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No leave requests found for Roll Number: " + rollToSearch);
        }
    }
}
