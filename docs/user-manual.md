# User Manual — Java Inventory Management System

---

## 1. Installation Guide

### System Requirements

| Requirement | Minimum |
|---|---|
| Operating System | Windows 10 / Ubuntu 20.04 / macOS 11 |
| Java Version | JDK 17 or higher |
| RAM | 256 MB |
| Disk Space | 10 MB |

### Step-by-Step Setup

**Step 1: Install Java JDK**
1. Visit https://www.oracle.com/java/technologies/downloads/
2. Download JDK 17+ for your OS
3. Run the installer
4. Verify installation:
   ```bash
   java -version
   ```
   Expected output: `java version "17.x.x"`

**Step 2: Clone the Repository**
```bash
git clone https://github.com/YOUR_USERNAME/java-inventory-management.git
cd java-inventory-management
```

**Step 3: Create Storage Folder**

Windows:
```cmd
mkdir D:\inventory
```

Linux/Mac (also update the path in source code):
```bash
mkdir ~/inventory
```

**Step 4: Compile**
```bash
javac -d out src/in/ac/adit/pwj/miniproject/inventory/MainApp.java
```

**Step 5: Run**
```bash
java -cp out in.ac.adit.pwj.miniproject.inventory.MainApp
```

---

## 2. User Guide

### Starting the Application

When launched, the app loads any previously saved inventory and shows the main menu:

```
1.Add Product  2.Display  3.Search  4.Filter  5.Order  6.Exit
```

Enter the number for your desired action and press **Enter**.

---

### Action 1: Add a Product

Choose option `1`. You'll be prompted:
```
Enter ID Name Qty Price Category(Electronics/Groceries):
```

Type all values on **one line**, separated by spaces:
```
101 Laptop 10 45000.00 Electronics
```

| Field | Example | Notes |
|---|---|---|
| ID | `101` | Must be a unique integer |
| Name | `Laptop` | Single word only |
| Quantity | `10` | Integer |
| Price | `45000.00` | Decimal allowed |
| Category | `Electronics` | Electronics or Groceries |

Product will be added to inventory immediately.

---

### Action 2: Display All Products

Choose option `2`. All products are listed:

```
101 Laptop [Electronics] Qty:10 Price:45000.0
102 Rice [Groceries] Qty:50 Price:60.5
```

---

### Action 3: Search by Keyword

Choose option `3`. Enter any part of the product name:
```
Enter keyword: lap
→ 101 Laptop [Electronics] Qty:10 Price:45000.0
```

- Search is **case-insensitive**
- Partial matches are supported

---

### Action 4: Filter by Category

Choose option `4`. Enter a category name:
```
Enter category: Electronics
→ 101 Laptop [Electronics] Qty:10 Price:45000.0
→ 103 Phone [Electronics] Qty:5 Price:15000.0
```

Valid categories: `Electronics`, `Groceries`

---

### Action 5: Place an Order

Choose option `5`. Enter the product ID and desired quantity:
```
Enter product ID and quantity: 101 2
→ Order: Laptop Qty: 2 successful.
```

The system will:
- Validate that the product exists
- Check that sufficient stock is available
- Deduct the quantity from inventory
- Record the order in order history

**Error cases:**
```
Order Failed: Invalid Product ID       ← Wrong ID entered
Order Failed: Insufficient stock for Laptop  ← Not enough stock
```

---

### Action 6: Save & Exit

Choose option `6` to save your inventory and exit:
```
Data saved. Exiting...
```

All data is written to `D:\inventory\inventory.txt` and will be reloaded next time.

---

## 3. FAQ

**Q: The app says "No previous data." on startup. Is that an error?**  
A: No. This just means no saved inventory file was found. It's normal when running for the first time.

---

**Q: My product name has two words. How do I add it?**  
A: Currently the app only supports single-word names (uses `sc.next()`). Use underscores: `Gaming_Laptop`.

---

**Q: I'm on Mac/Linux and the file is not being saved. What do I do?**  
A: Open `MainApp.java` and change the file path:
```java
// Change this:
new FileWriter("D:\\inventory\\inventory.txt")
// To this (Linux/Mac):
new FileWriter("/home/yourname/inventory/inventory.txt")
```

---

**Q: What happens if I enter a duplicate Product ID?**  
A: The new product will overwrite the existing one (HashMap behavior). Always use unique IDs.

---

**Q: Can I run multiple orders simultaneously?**  
A: Yes. Each order runs on a separate thread. The `processOrder` method is `synchronized` to ensure thread safety.

---

**Q: The app crashed with a NumberFormatException. Why?**  
A: You likely entered text where a number was expected (e.g., typed `"ten"` instead of `10`). Always enter numeric values for ID, Quantity, and Price.
