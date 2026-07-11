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

## ✨ Key Features

<details>
<summary><b>📦 Inventory Management</b> (click to expand)</summary>

- Add, update, delete, and search medicines by name or ID  
- View all medicines in a formatted table with inline low‑stock warnings  
- Track: name, company, price, expiry date, quantity, minimum threshold, formula category, prescription flag  
- **Filter** and **sort** directly in the GUI
</details>

<details>
<summary><b>💰 Sales & Billing</b> (click to expand)</summary>

- Sell medicines – stock reduces automatically  
- Generate itemised receipts with bill ID, date, and staff name  
- Apply percentage discounts (Admin only)  
- **Process refunds** – restores stock and marks bill as refunded  
- Block sales of expired medicines
</details>

<details>
<summary><b>🔍 Alternative Suggestions</b> (click to expand)</summary>

- When a medicine is out of stock, the system scans for alternatives with the same **formula category**  
- Displays name, price, and available quantity – no dead ends for customers
</details>

<details>
<summary><b>🛡️ Role‑Based Access</b> (click to expand)</summary>

<table>
  <tr>
    <th>Role</th>
    <th>Access</th>
  </tr>
  <tr>
    <td><b>Admin</b></td>
    <td>Delete medicines, change prices, apply discounts, process refunds, view all reports &amp; activity log</td>
  </tr>
  <tr>
    <td><b>Staff</b></td>
    <td>Sell, search, view inventory, add/update medicines (no deletion or price changes)</td>
  </tr>
</table>
</details>

<details>
<summary><b>🔔 Proactive Alerts</b> (click to expand)</summary>

- Runs automatically on every startup – before the menu appears  
- Flags: **expired**, **expiring soon** (7‑day window), and **low‑stock** items  
- All alerts saved to a history file with timestamps  
- Admin can re‑run checks at any time
</details>

<details>
<summary><b>📊 Sales Reports (Admin only)</b> (click to expand)</summary>

- Daily, weekly, and monthly summaries  
- Best‑selling medicine tracker  
- Profit & Loss statement (revenue – discounts – refunds)
</details>

<details>
<summary><b>📁 File Persistence & Backups</b> (click to expand)</summary>

- All data stored in plain text files – no database required  
- <code>inventory.txt</code>, <code>sales_log.txt</code>, <code>activity_log.txt</code>, <code>alerts_log.txt</code>  
- **Automatic dated backup** created on every shutdown (<code>data/backups/</code>)
</details>

---

## 🧠 OOP Concepts Applied

<table>
  <tr>
    <th>Concept</th>
    <th>Where Used</th>
  </tr>
  <tr>
    <td><kbd>Encapsulation</kbd></td>
    <td>All classes have <code>private</code> fields with getters/setters</td>
  </tr>
  <tr>
    <td><kbd>Inheritance</kbd></td>
    <td><code>Admin</code> and <code>Staff</code> extend abstract <code>User</code></td>
  </tr>
  <tr>
    <td><kbd>Abstraction</kbd></td>
    <td><code>User</code> is abstract with <code>showCapabilities()</code></td>
  </tr>
  <tr>
    <td><kbd>Polymorphism</kbd></td>
    <td><code>Menu</code> holds <code>User</code> references that resolve to <code>Admin</code> or <code>Staff</code> at runtime</td>
  </tr>
  <tr>
    <td><kbd>Composition</kbd></td>
    <td><code>Sales</code> contains an <code>ArrayList&lt;Bill&gt;</code></td>
  </tr>
  <tr>
    <td><kbd>File I/O</kbd></td>
    <td>All persistence handled by a single <code>FileManager</code> class</td>
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

### Installation & Run

```bash
# 1. Clone the repository
git clone https://github.com/HaseebAhmedHarris/PharmacyManagementSystem.git
cd PharmacyManagementSystem

# 2. Compile all Java files
javac *.java

# 3. Launch the application
java Launcher
You will be prompted to select:

<kbd>1</kbd> – Console Mode (terminal)

<kbd>2</kbd> – GUI Mode (Swing windows)

🔑 Default Login Credentials
<table> <tr> <th>Role</th> <th>Username</th> <th>Password</th> </tr> <tr> <td><b>Admin</b></td> <td><code>admin</code></td> <td><code>admin123</code></td> </tr> <tr> <td><b>Admin</b></td> <td><code>owner</code></td> <td><code>owner123</code></td> </tr> <tr> <td><b>Staff</b></td> <td><code>staff1</code></td> <td><code>staff123</code></td> </tr> <tr> <td><b>Staff</b></td> <td><code>staff2</code></td> <td><code>staff456</code></td> </tr> </table>
⚠️ The login screen locks after 3 failed attempts.

📂 Data Files
All persistent data lives in the <code>data/</code> folder:

<table> <tr> <th>File</th> <th>Contents</th> <th>Update Frequency</th> </tr> <tr> <td><code>inventory.txt</code></td> <td>Full medicine list (CSV)</td> <td>On every add/update/delete/sale</td> </tr> <tr> <td><code>sales_log.txt</code></td> <td>Every transaction (appended)</td> <td>Every sale or refund</td> </tr> <tr> <td><code>activity_log.txt</code></td> <td>Admin actions with timestamps</td> <td>Every admin action</td> </tr> <tr> <td><code>alerts_log.txt</code></td> <td>Expiry / low‑stock history</td> <td>Every startup</td> </tr> <tr> <td><code>backups/</code></td> <td>Dated inventory snapshots</td> <td>Every shutdown</td> </tr> </table>
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
<table> <tr> <th>Name</th> <th>Role / Responsibility</th> </tr> <tr> <td><b>Syed Muhammad Ahmed</b></td> <td>Team Leader – Core Engine, File I/O, GUI</td> </tr> <tr> <td><b>Haseeb Ahmed</b></td> <td>Sales, Billing, Reports</td> </tr> <tr> <td><b>Ali Hassan</b></td> <td>Access Control (User hierarchy)</td> </tr> <tr> <td><b>Laiba Malik</b></td> <td>Integration, Alerts, Menu</td> </tr> <tr> <td><b>Zainab Zeeshan</b></td> <td>Documentation, UML, Presentation</td> </tr> </table>
📄 License
This project was developed for educational purposes as part of the Object Oriented Programming (CSP‑122) course at the Institute of Business Management (IoBM) , Spring 2026.

🙏 Acknowledgements
IoBM faculty for their guidance and support.

All team members for their dedication and collaboration.

The OOP principles that turned a problem statement into a working system.

<div align="center"> <sub>Built with ❤️ by the Limitless Group</sub> <br> <sub>© 2026 – Institute of Business Management</sub> </div> ```
