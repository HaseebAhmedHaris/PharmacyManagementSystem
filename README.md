Pharmacy Management System
A complete Java-based desktop application for small pharmacies – built with Object-Oriented Programming principles, file-based persistence, and a modern GUI (Swing). It replaces manual record‑keeping with a digital solution that manages inventory, automates sales, provides role‑based access, and issues proactive alerts for stock and expiry.

<p align="center"> <img src="screenshot.png" alt="GUI Dashboard" width="600"/> </p>
Features
Inventory Management – Add, update, delete, search, and view all medicines with low‑stock warnings.

Sales & Billing – Sell medicines, automatically reduce stock, generate itemised receipts, apply discounts (Admin only), and process refunds.

Alternative Suggestions – When a requested medicine is out of stock, the system suggests alternatives with the same formula category.

Role‑Based Access – Admin (full control: delete, change prices, refunds, reports, activity log) and Staff (sales, search, inventory view/add/update).

Proactive Alerts – Runs on startup: flags expired medicines, expiring‑soon items (7‑day window), and low‑stock products. All alerts are saved to a history file.

Sales Reports – Daily, weekly, monthly summaries, best‑selling medicine, and profit/loss statement (Admin only).

Activity Logging – Every admin action (price changes, deletions, discounts, refunds) is timestamped and stored.

File Persistence – All data is saved to plain text files (inventory.txt, sales_log.txt, activity_log.txt, alerts_log.txt). Automatic dated backups are created on shutdown.

Dual Interface – Choose between a console (terminal) version and a graphical user interface (Swing) at launch.

OOP Concepts Applied
Concept	Where Used
Encapsulation	All classes have private fields with getters/setters.
Inheritance	Admin and Staff extend User; PrescriptionMedicine (planned) could extend Medicine.
Abstraction	User is an abstract class with an abstract showCapabilities() method.
Polymorphism	Menu references User objects that are actually Admin or Staff at runtime.
Composition	Sales contains an ArrayList of Bill objects.
File I/O	FileManager handles all read/write operations.
Technology Stack
Java (JDK 17 or higher recommended)

Java Swing (GUI)

Plain text files (no external database required)

IntelliJ IDEA (project files included, but any IDE or command line works)

Getting Started
Prerequisites
Java Development Kit (JDK) 17+ installed

Git (optional, for cloning)

Installation & Running
Clone the repository (or download the ZIP):

bash
git clone https://github.com/HaseebAhmedHarris/PharmacyManagementSystem.git
cd PharmacyManagementSystem
Compile the Java files:

bash
javac *.java
(If you are in the root folder containing all .java files.)

Run the launcher:

bash
java Launcher
You will be prompted to choose between Console Mode (1) and GUI Mode (2).

Default Login Credentials
Role	Username	Password
Admin	admin	admin123
Admin	owner	owner123
Staff	staff1	staff123
Staff	staff2	staff456
Note: The login screen locks after 3 failed attempts.

Data Files
All data is stored in the data/ folder:

inventory.txt – current medicine list (CSV format)

sales_log.txt – all transactions (appended, never overwritten)

activity_log.txt – admin actions with timestamps

alerts_log.txt – alert history from each startup

backups/ – dated inventory backups (created on shutdown)

Project Structure
text
.
├── Admin.java
├── AlertSystem.java
├── Bill.java
├── DataLoader.java
├── DateHelper.java
├── FileManager.java
├── Launcher.java
├── Medicine.java
├── Menu.java
├── PharmacyGUI.java
├── Sales.java
├── Staff.java
├── User.java
├── data/                (created at runtime)
└── README.md
Team Members
Name	Role / Responsibility
Syed Muhammad Ahmed	Team Leader – Core Engine, File I/O, GUI
Haseeb Ahmed	Sales, Billing, Reports
Ali Hassan	Access Control (User hierarchy)
Laiba Malik	Integration, Alerts, Menu
Zainab Zeeshan	Documentation, UML, Presentation
License
This project was developed as part of the Object Oriented Programming (CSP‑122) course at the Institute of Business Management (IoBM), Spring 2026. It is intended for educational purposes.

Acknowledgements
IoBM faculty for guidance.

All team members for their contributions and collaboration.

The OOP concepts that made this structured design possible.

For any questions or suggestions, please open an issue on GitHub.

