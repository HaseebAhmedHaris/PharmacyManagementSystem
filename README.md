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

```bash
git clone https://github.com/HaseebAhmedHarris/PharmacyManagementSystem.git
cd PharmacyManagementSystem

🔨 Compilation

Step 2: Compile all Java files
javac *.java

▶️ How to Run

Step 3: Launch the application
java Launcher
