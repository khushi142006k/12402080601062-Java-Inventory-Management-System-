# 🔧 Technical Documentation — Java Inventory Management System

---

## 1. Class Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                        CLASS DIAGRAM                                │
│                  Inventory Management System                        │
└─────────────────────────────────────────────────────────────────────┘

         ┌──────────────────────────────────┐
         │            <<class>>             │
         │             Product              │
         ├──────────────────────────────────┤
         │  # id         : int              │
         │  # name       : String           │
         │  # quantity   : int              │
         │  # price      : double           │
         │  # category   : String           │
         ├──────────────────────────────────┤
         │  + display() : void              │
         └──────────────┬───────────────────┘
                        │
          extends       │       extends
         ┌──────────────┴───────────────────┐
         │                                  │
         ▼                                  ▼
┌─────────────────────┐          ┌─────────────────────┐
│      <<class>>      │          │      <<class>>      │
│     Electronics     │          │      Groceries      │
├─────────────────────┤          ├─────────────────────┤
│                     │          │                     │
├─────────────────────┤          ├─────────────────────┤
│ + Electronics(      │          │ + Groceries(        │
│     id, name,       │          │     id, name,       │
│     qty, price)     │          │     qty, price)     │
└─────────────────────┘          └─────────────────────┘


         ┌──────────────────────────────────┐
         │            <<class>>             │
         │         InventoryManager         │
         ├──────────────────────────────────┤
         │  - inventory    : Map<Int,Prod>  │
         │  - orderHistory : List<String>   │
         ├──────────────────────────────────┤
         │  + addProduct(p) : void          │
         │  + displayProducts() : void      │
         │  + searchProduct(kw) : void      │
         │  + filterByCategory(c) : void    │
         │  + processOrder(id,qty) : void   │──────── throws ────────►
         │  + saveToFile() : void           │
         │  + loadFromFile() : void         │
         └──────────────┬───────────────────┘
                        │
                        │ contains (inner class)
                        │
                        ▼
         ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
              <<inner class / Runnable>>
         │          OrderProcessor          │
          ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
         │  - productId : int               │
         │  - qty       : int               │
          ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
         │  + run() : void                  │
         └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘


         ┌──────────────────────────────────┐
         │           <<exception>>          │
         │          StockException          │
         ├──────────────────────────────────┤
         │  extends Exception               │
         ├──────────────────────────────────┤
         │  + StockException(msg)           │
         └──────────────────────────────────┘


RELATIONSHIPS
─────────────────────────────────────────────────────────────────────
  Electronics      ── extends ──────────────────────────►  Product
  Groceries        ── extends ──────────────────────────►  Product
  InventoryManager ── manages ─────────────────────────►  Product
  InventoryManager ── contains (inner) ────────────────►  OrderProcessor
  InventoryManager ── throws ──────────────────────────►  StockException
  OrderProcessor   ── calls ───────────────────────────►  processOrder()
  MainApp          ── uses ────────────────────────────►  InventoryManager


ACCESS MODIFIERS
─────────────────────────────────────────────────────────────────────
  +  public       #  protected       -  private
```

---

## 2. Sequence Diagrams

### 2a. Add Product Flow

```mermaid
sequenceDiagram
    actor User
    participant MainApp
    participant InventoryManager
    participant Electronics
    participant Groceries

    User->>MainApp: Enter choice 1
    MainApp->>User: Prompt for ID, Name, Qty, Price, Category
    User->>MainApp: Enter product details
    alt Category == Electronics
        MainApp->>Electronics: new Electronics(id, name, qty, price)
        Electronics-->>MainApp: Electronics object
    else Category == Groceries
        MainApp->>Groceries: new Groceries(id, name, qty, price)
        Groceries-->>MainApp: Groceries object
    end
    MainApp->>InventoryManager: addProduct(product)
    InventoryManager->>InventoryManager: inventory.put(id, product)
    InventoryManager-->>MainApp: Done
```

### 2b. Order Processing Flow (Multithreaded)

```mermaid
sequenceDiagram
    actor User
    participant MainApp
    participant Thread
    participant OrderProcessor
    participant InventoryManager

    User->>MainApp: Enter choice 5 (Order)
    MainApp->>User: Prompt for Product ID and Qty
    User->>MainApp: Enter pid and qty
    MainApp->>OrderProcessor: new OrderProcessor(pid, qty)
    MainApp->>Thread: new Thread(orderProcessor)
    MainApp->>Thread: t.start()
    Thread->>OrderProcessor: run()
    OrderProcessor->>InventoryManager: processOrder(productId, qty)
    alt Product not found
        InventoryManager-->>OrderProcessor: throw StockException("Invalid Product ID")
        OrderProcessor-->>Thread: Order Failed
    else Insufficient stock
        InventoryManager-->>OrderProcessor: throw StockException("Insufficient stock")
        OrderProcessor-->>Thread: Order Failed
    else Success
        InventoryManager->>InventoryManager: p.quantity -= qty
        InventoryManager->>InventoryManager: orderHistory.add(order)
        InventoryManager-->>OrderProcessor: "Order successful"
        OrderProcessor-->>Thread: Done
    end
    MainApp->>Thread: t.join()
```

### 2c. Save and Load File Flow

```mermaid
sequenceDiagram
    participant InventoryManager
    participant BufferedWriter
    participant FileSystem

    Note over InventoryManager: Save on Exit (choice 6)
    InventoryManager->>BufferedWriter: new BufferedWriter(FileWriter)
    loop For each Product in inventory
        InventoryManager->>BufferedWriter: write(id,name,qty,price,category)
    end
    BufferedWriter->>FileSystem: inventory.txt written
    BufferedWriter-->>InventoryManager: Close stream

    Note over InventoryManager: Load on Startup
    InventoryManager->>FileSystem: Open inventory.txt
    FileSystem-->>InventoryManager: BufferedReader stream
    loop For each line
        InventoryManager->>InventoryManager: Parse CSV → Product object
        InventoryManager->>InventoryManager: inventory.put(id, product)
    end
```

---

## 3. Key Design Decisions

| Concept | Implementation |
|---|---|
| **Inheritance** | `Electronics` and `Groceries` extend `Product` |
| **Encapsulation** | `inventory` and `orderHistory` are `private` in `InventoryManager` |
| **Custom Exception** | `StockException` extends `Exception` for stock validation |
| **Multithreading** | `OrderProcessor` implements `Runnable`; runs as a separate `Thread` |
| **Thread Safety** | `processOrder()` is `synchronized` to prevent race conditions |
| **File I/O** | `BufferedWriter`/`BufferedReader` for efficient read/write |
| **Collections** | `HashMap` for O(1) product lookup; `ArrayList` for order history |

---

## 4. Exception Handling

| Exception | When Thrown | Handled By |
|---|---|---|
| `StockException("Invalid Product ID")` | Product ID not in inventory | `OrderProcessor.run()` |
| `StockException("Insufficient stock")` | Requested qty > available qty | `OrderProcessor.run()` |
| `IOException` | File not found or unreadable | `saveToFile()` / `loadFromFile()` |
| `InterruptedException` | Thread join interrupted | `main()` catch block |

---

## 5. File Format

Inventory is stored as comma-separated values in `inventory.txt`:

```
101,Laptop,10,45000.0,Electronics
102,Rice,50,60.5,Groceries
103,Phone,5,15000.0,Electronics
```

Format: `id,name,quantity,price,category`
