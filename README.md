
#  Java Inventory Management System


> A console-based Inventory Management System built in Java that demonstrates core Object-Oriented Programming concepts including inheritance, multithreading, file I/O, and custom exception handling.

---

##  Table of Contents

- [Project Description](#-project-description)
- [Features](#-features)
- [Project Structure](#-project-structure)
- [Installation Instructions](#-installation-instructions)
- [Usage Instructions](#-usage-instructions)
- [Screenshots](#-screenshots)
- [License](#-license)

---

## Project Description

The **Java Inventory Management System** is a mini-project developed as part of the **Professional and Web Java (PWJ)** course at **ADIT (A.D. Patel Institute of Technology)**. It simulates a real-world inventory system where users can manage products across categories like Electronics and Groceries.

The system supports:
- Adding products by category
- Searching and filtering inventory
- Processing orders with thread safety
- Persisting inventory data to a local file

**Package:** `in.ac.adit.pwj.miniproject.inventory`

---

##  Features

| Feature | Description |
|---|---|
| Add Products | Add Electronics or Groceries to inventory |
| Display All | View all products with ID, name, category, quantity, and price |
| Search | Search products by keyword (case-insensitive) |
| Filter | Filter products by category |
| Order Processing | Place orders with multithreaded processing and stock validation |
| File Persistence | Save and load inventory data from a `.txt` file |
| Exception Handling | Custom `StockException` for invalid orders |

---

##  Project Structure

```
java-inventory-management/
│
├── src/
│   └── in/ac/adit/pwj/miniproject/inventory/
│       └── MainApp.java          # All classes in single file
│
├── docs/
│   ├── README.md                 # This file
│   ├── TECHNICAL_DOCS.md         # Class & sequence diagrams
│   └── USER_MANUAL.md            # End-user guide
│
├── assets/
│   └── screenshots/              # Application screenshots
│
├── inventory/
│   └── inventory.txt             # Auto-generated data file (on D: drive)
│
└── LICENSE
```

---

##  Installation Instructions

### Prerequisites

Make sure you have the following installed:

- **Java JDK 8 or higher** → [Download here](https://www.oracle.com/java/technologies/downloads/)
- **Git** → [Download here](https://git-scm.com/downloads)
- Any Java IDE (optional): [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/)

### Step 1: Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/java-inventory-management.git
cd java-inventory-management
```

### Step 2: Create the Data Directory

The application saves data to `D:\inventory\inventory.txt` (Windows). Create this folder:

**Windows:**
```
mkdir D:\inventory
```

**Linux/Mac** (you'll need to update the file path in the code):
```bash
mkdir ~/inventory
```

> **Note for Linux/Mac users:** Open `MainApp.java` and change the file path in `saveToFile()` and `loadFromFile()` from `D:\\inventory\\inventory.txt` to your preferred path (e.g., `/home/username/inventory/inventory.txt`).

### Step 3: Compile the Code

```bash
cd src
javac in/ac/adit/pwj/miniproject/inventory/MainApp.java
```

### Step 4: Run the Application

```bash
java in.ac.adit.pwj.miniproject.inventory.MainApp
```

---

##  Usage Instructions

When launched, the application displays a menu:

```
1.Add Product  2.Display  3.Search  4.Filter  5.Order  6.Exit
```

### Adding a Product
Choose option `1` and enter: `ID Name Quantity Price Category`

```
Enter ID Name Qty Price Category(Electronics/Groceries): 101 Laptop 10 45000 Electronics
```

### Displaying All Products
Choose option `2` to list all inventory items.

```
101 Laptop [Electronics] Qty:10 Price:45000.0
102 Rice [Groceries] Qty:50 Price:80.0
```

### Searching a Product
Choose option `3` and enter a keyword:

```
Enter keyword: lap
101 Laptop [Electronics] Qty:10 Price:45000.0
```

### Filtering by Category
Choose option `4` and enter a category name:

```
Enter category: Groceries
102 Rice [Groceries] Qty:50 Price:80.0
```

### Placing an Order
Choose option `5` and enter the product ID and quantity:

```
Enter product ID and quantity: 101 2
Order: Laptop Qty: 2 successful.
```

### Exiting
Choose option `6` to save data and exit:

```
Data saved. Exiting...
```

---

##  Screenshots

### Main Menu
![Main Menu]()

### Adding a Product
![Add Product]()

### Displaying Products
![Display Products]()

### Order Processing
![Order Processing]()


---

##  License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 [Khushi]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software...
```

---

##  Author

**[Khushi]**


