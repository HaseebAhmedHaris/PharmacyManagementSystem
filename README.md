# <div align="center">Pharmacy Management System</div>

<div align="center">

**A complete Java-based desktop application for modern pharmacy operations**  
*Built with OOP principles, file persistence, and Java Swing GUI*

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![GUI](https://img.shields.io/badge/GUI-Swing-6DB33F?style=for-the-badge&logo=java)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License](https://img.shields.io/badge/License-Educational-FFD700?style=for-the-badge)](LICENSE)
[![IoBM](https://img.shields.io/badge/IoBM-OOP%20Project-1A237E?style=for-the-badge)](https://iobm.edu.pk/)

</div>

---

## 📌 Overview

The **Pharmacy Management System** replaces manual paper‑based records with a digital solution that manages inventory, automates sales, enforces role‑based access, and issues proactive alerts for stock and expiry. Built as part of the **Object Oriented Programming (CSP‑122)** course at the Institute of Business Management, this project demonstrates all four core OOP concepts in a real‑world, functional application.

> 🚀 **Choose your interface:** Console (terminal) or full GUI (Swing) – both powered by the same backend.

---

## 📦 Inventory Management

- Add, update, delete, and search medicines by name or ID  
- View all medicines in a formatted table with inline low‑stock warnings  
- Track: name, company, price, expiry date, quantity, minimum threshold, formula category, prescription flag  
- **Filter** and **sort** directly in the GUI

---

## 💰 Sales & Billing

- Sell medicines – stock reduces automatically  
- Generate itemised receipts with bill ID, date, and staff name  
- Apply percentage discounts (Admin only)  
- **Process refunds** – restores stock and marks bill as refunded  
- Block sales of expired medicines

---

## 🔍 Alternative Suggestions

- When a medicine is out of stock, the system scans for alternatives with the same **formula category**  
- Displays name, price, and available quantity – no dead ends for customers

---

## 🛡️ Role‑Based Access

<table>
  <tr>
    <th align="left">Role</th>
    <th align="left">Access</th>
  </tr>
  <tr>
    <td align="left"><b>Admin</b></td>
    <td align="left">Delete medicines, change prices, apply discounts, process refunds, view all reports &amp; activity log</td>
  </tr>
  <tr>
    <td align="left"><b>Staff</b></td>
    <td align="left">Sell, search, view inventory, add/update medicines (no deletion or price changes)</td>
  </tr>
</table>

---

## 🔔 Proactive Alerts

- Runs automatically on every startup – before the menu appears  
- Flags: **expired**, **expiring soon** (7‑day window), and **low‑stock** items  
- All alerts saved to a history file with timestamps  
- Admin can re‑run checks at any time

---

## 📊 Sales Reports (Admin only)

- Daily, weekly, and monthly summaries  
- Best‑selling medicine tracker  
- Profit & Loss statement (revenue – discounts – refunds)

---

## 📁 File Persistence & Backups

- All data stored in plain text files – no database required  
- `inventory.txt`, `sales_log.txt`, `activity_log.txt`, `alerts_log.txt`  
- **Automatic dated backup** created on every shutdown (`data/backups/`)

---

## 🧠 OOP Concepts Applied

<table>
  <tr>
    <th align="left">Concept</th>
    <th align="left">Where Used</th>
  </tr>
  <tr>
    <td align="left"><kbd>Encapsulation</kbd></td>
    <td align="left">All classes have <code>private</code> fields with getters/setters</td>
  </tr>
  <tr>
    <td align="left"><kbd>Inheritance</kbd></td>
    <td align="left"><code>Admin</code> and <code>Staff</code> extend abstract <code>User</code></td>
  </tr>
  <tr>
    <td align="left"><kbd>Abstraction</kbd></td>
    <td align="left"><code>User</code> is abstract with <code>showCapabilities()</code></td>
  </tr>
  <tr>
    <td align="left"><kbd>Polymorphism</kbd></td>
    <td align="left"><code>Menu</code> holds <code>User</code> references that resolve to <code>Admin</code> or <code>Staff</code> at runtime</td>
  </tr>
  <tr>
    <td align="left"><kbd>Composition</kbd></td>
    <td align="left"><code>Sales</code> contains an <code>ArrayList&lt;Bill&gt;</code></td>
  </tr>
  <tr>
    <td align="left"><kbd>File I/O</kbd></td>
    <td align="left">All persistence handled by a single <code>FileManager</code> class</td>
  </tr>
</table>

---

## 🛠️ Technology Stack

<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white" height="28"/>
  <img src="https://img.shields.io/badge/Swing-6DB33F?style=flat&logo=java&logoColor=white" height="28"/>
  <img src="https://img.shields.io/badge/IntelliJ-000000?style=flat&logo=intellijidea&logoColor=white" height="28"/>
  <img src="https://img.shields.io/badge/Plain%20Text-555555?style=flat&logo=textpattern&logoColor=white" height="28"/>
</p>

- **Java 17+** – core language
- **Java Swing** – graphical user interface
- **Plain text files** – lightweight, portable persistence (no external DB)
- **IntelliJ IDEA** – project files included, but any IDE works

---

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) **17** or higher
- Git (optional, for cloning)

---

### 📥 Installation

**Step 1:** Clone the repository

git clone https://github.com/HaseebAhmedHarris/PharmacyManagementSystem.git
cd PharmacyManagementSystem


🔨 Compilation

Step 2: Compile all Java files
javac *.java

▶️ How to Run

Step 3: Launch the application
java Launcher 

## You will be prompted to select an interface mode:

<table> <tr> <th align="left">Key</th> <th align="left">Mode</th> </tr> <tr> <td align="left"><kbd>1</kbd></td> <td align="left"><b>Console Mode</b> (terminal)</td> </tr> <tr> <td align="left"><kbd>2</kbd></td> <td align="left"><b>GUI Mode</b> (Swing windows)</td> </tr> </table>

✅ Quick Start
Here's the entire setup process in one go:

# Clone, compile, and run
git clone https://github.com/HaseebAhmedHarris/PharmacyManagementSystem.git
cd PharmacyManagementSystem
javac *.java
java Launcher

🔑 Default Login Credentials
<table> <tr> <th align="left">Role</th> <th align="left">Username</th> <th align="left">Password</th> </tr> <tr> <td align="left"><b>Admin</b></td> <td align="left"><code>admin</code></td> <td align="left"><code>admin123</code></td> </tr> <tr> <td align="left"><b>Admin</b></td> <td align="left"><code>owner</code></td> <td align="left"><code>owner123</code></td> </tr> <tr> <td align="left"><b>Staff</b></td> <td align="left"><code>staff1</code></td> <td align="left"><code>staff123</code></td> </tr> <tr> <td align="left"><b>Staff</b></td> <td align="left"><code>staff2</code></td> <td align="left"><code>staff456</code></td> </tr> </table>

⚠️ The login screen locks after 3 failed attempts.

📂 Data Files
All persistent data lives in the <code>data/</code> folder:

<table> <tr> <th align="left">File</th> <th align="left">Contents</th> <th align="center">Update Frequency</th> </tr> <tr> <td align="left"><code>inventory.txt</code></td> <td align="left">Full medicine list (CSV)</td> <td align="center">On every add/update/delete/sale</td> </tr> <tr> <td align="left"><code>sales_log.txt</code></td> <td align="left">Every transaction (appended)</td> <td align="center">Every sale or refund</td> </tr> <tr> <td align="left"><code>activity_log.txt</code></td> <td align="left">Admin actions with timestamps</td> <td align="center">Every admin action</td> </tr> <tr> <td align="left"><code>alerts_log.txt</code></td> <td align="left">Expiry / low‑stock history</td> <td align="center">Every startup</td> </tr> <tr> <td align="left"><code>backups/</code></td> <td align="left">Dated inventory snapshots</td> <td align="center">Every shutdown</td> </tr> </table>
📁 Project Structure
text
PharmacyManagementSystem/
├── Admin.java
├── AlertSystem.java
├── Bill.java
├── DataLoader.java
├── DateHelper.java
├── FileManager.java
├── Launcher.java          ← Entry point
├── Medicine.java
├── Menu.java              ← Console interface
├── PharmacyGUI.java       ← Swing interface
├── Sales.java
├── Staff.java
├── User.java
├── data/                  ← Created at runtime
│   ├── inventory.txt
│   ├── sales_log.txt
│   ├── activity_log.txt
│   ├── alerts_log.txt
│   └── backups/
├── .gitignore
└── README.md

👥 Team Members
<table> <tr> <th align="left">Name</th> <th align="left">Role / Responsibility</th> </tr> <tr> <td align="left"><b>Syed Muhammad Ahmed</b></td> <td align="left">Team Leader – Core Engine, File I/O, GUI</td> </tr> <tr> <td align="left"><b>Haseeb Ahmed</b></td> <td align="left">Sales, Billing, Reports</td> </tr> <tr> <td align="left"><b>Ali Hassan</b></td> <td align="left">Access Control (User hierarchy)</td> </tr> <tr> <td align="left"><b>Laiba Malik</b></td> <td align="left">Integration, Alerts, Menu</td> </tr> <tr> <td align="left"><b>Zainab Zeeshan</b></td> <td align="left">Documentation, UML, Presentation</td> </tr> </table>
📄 License
This project was developed for educational purposes as part of the Object Oriented Programming (CSP‑122) course at the Institute of Business Management (IoBM) , Spring 2026.

🙏 Acknowledgements
IoBM faculty for their guidance and support.

All team members for their dedication and collaboration.

The OOP principles that turned a problem statement into a working system.

<div align="center"> <sub>Built with ❤️ by the Limitless Group</sub> <br> <sub>© 2026 – Institute of Business Management</sub> </div> 
