# BudgetBuddy — Class Diagrams (Eraser.io / Mermaid)

> Paste each ```mermaid block directly into [Eraser.io](https://app.eraser.io)
> using **New Diagram → Diagram as Code → Mermaid**.
>
> Five separate diagrams:
> 1. Model Layer
> 2. Repository Layer
> 3. Service Layer
> 4. Controller Layer
> 5. Full Overview

---

## Diagram 1 — Model Layer

```mermaid
---
title: "BudgetBuddy | Diagram 1: Model Layer"
---
classDiagram
    direction TB

    class Transaction {
        <<abstract>>
        -Long id
        -double amount
        -LocalDate date
        -String description
        -Long userId
        +Long getId()
        +void setId(Long id)
        +double getAmount()
        +void setAmount(double amount)
        +LocalDate getDate()
        +void setDate(LocalDate date)
        +String getDescription()
        +void setDescription(String description)
        +Long getUserId()
        +void setUserId(Long userId)
        +String getType()*
        +String toString()
    }

    class Income {
        -String source
        +Income(double amount, LocalDate date, String description, String source, Long userId)
        +String getSource()
        +void setSource(String source)
        +String getType()
    }

    class Expense {
        -Long categoryId
        -String categoryName
        +Expense(double amount, LocalDate date, String description, Long categoryId, String categoryName, Long userId)
        +Long getCategoryId()
        +void setCategoryId(Long categoryId)
        +String getCategoryName()
        +void setCategoryName(String categoryName)
        +String getType()
    }

    class Category {
        -Long id
        -String name
        -String description
        +Category(Long id, String name, String description)
        +Long getId()
        +void setId(Long id)
        +String getName()
        +void setName(String name)
        +String getDescription()
        +void setDescription(String description)
    }

    class User {
        -Long id
        -String username
        -String email
        -String password
        +User(Long id, String username, String email, String password)
        +Long getId()
        +void setId(Long id)
        +String getUsername()
        +void setUsername(String username)
        +String getEmail()
        +void setEmail(String email)
        +String getPassword()
        +void setPassword(String password)
    }

    class MonthlyReport {
        -int month
        -int year
        -double totalIncome
        -double totalExpense
        -List~Transaction~ transactions
        +MonthlyReport(int month, int year)
        +void generate(List~Transaction~ allTransactions)
        +int getMonth()
        +int getYear()
        +double getTotalIncome()
        +double getTotalExpense()
        +double getBalance()
        +List~Transaction~ getTransactions()
    }

    %% Inheritance
    Transaction <|-- Income  : extends
    Transaction <|-- Expense : extends

    %% Associations
    Expense       --> Category    : uses
    MonthlyReport --> Transaction : contains
```

---

## Diagram 2 — Repository Layer

```mermaid
---
title: "BudgetBuddy | Diagram 2: Repository Layer"
---
classDiagram
    direction TB

    class IRepository~T~ {
        <<interface>>
        +T save(T entity)
        +List~T~ findAll()
        +Optional~T~ findById(Long id)
        +boolean delete(Long id)
        +T update(T entity)
    }

    class TransactionRepository {
        -List~Transaction~ transactions
        -AtomicLong idCounter
        +Transaction save(Transaction transaction)
        +List~Transaction~ findAll()
        +Optional~Transaction~ findById(Long id)
        +boolean delete(Long id)
        +Transaction update(Transaction transaction)
        +List~Transaction~ findByUserId(Long userId)
        +List~Transaction~ findByUserIdAndDateRange(Long userId, LocalDate start, LocalDate end)
    }

    class CategoryRepository {
        -List~Category~ categories
        -AtomicLong idCounter
        +Category save(Category category)
        +List~Category~ findAll()
        +Optional~Category~ findById(Long id)
        +boolean delete(Long id)
        +Category update(Category category)
    }

    class UserRepository {
        -List~User~ users
        -AtomicLong idCounter
        +User save(User user)
        +List~User~ findAll()
        +Optional~User~ findById(Long id)
        +boolean delete(Long id)
        +User update(User user)
        +Optional~User~ findByUsername(String username)
        +Optional~User~ findByEmail(String email)
    }

    %% Interface implementation
    IRepository~T~ <|.. TransactionRepository : implements
    IRepository~T~ <|.. CategoryRepository    : implements
    IRepository~T~ <|.. UserRepository        : implements
```

---

## Diagram 3 — Service Layer

```mermaid
---
title: "BudgetBuddy | Diagram 3: Service Layer"
---
classDiagram
    direction TB

    class TransactionService {
        -TransactionRepository transactionRepository
        -CategoryRepository categoryRepository
        +Transaction addIncome(double amount, LocalDate date, String description, String source, Long userId)
        +Transaction addExpense(double amount, LocalDate date, String description, Long categoryId, Long userId)
        +List~Transaction~ getAllTransactions(Long userId)
        +Optional~Transaction~ getTransactionById(Long id)
        +Transaction updateTransaction(Long id, double amount, LocalDate date, String description)
        +boolean deleteTransaction(Long id)
        +List~Transaction~ filterByCategory(Long userId, Long categoryId)
        +List~Transaction~ filterByDateRange(Long userId, LocalDate startDate, LocalDate endDate)
        +List~Transaction~ filterByType(Long userId, String type)
        +List~Transaction~ searchByKeyword(Long userId, String keyword)
    }

    class CategoryService {
        -CategoryRepository categoryRepository
        +List~Category~ getAllCategories()
        +Optional~Category~ getCategoryById(Long id)
        +Category addCategory(String name, String description)
        +boolean deleteCategory(Long id)
        +Category updateCategory(Long id, String name, String description)
    }

    class ReportService {
        -TransactionRepository transactionRepository
        +MonthlyReport generateMonthlyReport(Long userId, int month, int year)
    }

    class UserService {
        -UserRepository userRepository
        +User register(String username, String email, String password)
        +Optional~User~ login(String username, String password)
        +Optional~User~ getUserById(Long id)
    }

    class TransactionRepository {
        +List~Transaction~ findByUserId(Long userId)
        +List~Transaction~ findByUserIdAndDateRange(Long userId, LocalDate start, LocalDate end)
    }

    class CategoryRepository {
        +Optional~Category~ findById(Long id)
        +List~Category~ findAll()
    }

    class UserRepository {
        +Optional~User~ findByUsername(String username)
        +Optional~User~ findByEmail(String email)
    }

    %% Service → Repository dependencies
    TransactionService --> TransactionRepository : uses
    TransactionService --> CategoryRepository    : uses
    ReportService      --> TransactionRepository : uses
    UserService        --> UserRepository        : uses
    CategoryService    --> CategoryRepository    : uses
```

---

## Diagram 4 — Controller Layer

```mermaid
---
title: "BudgetBuddy | Diagram 4: Controller Layer"
---
classDiagram
    direction TB

    class TransactionController {
        -TransactionService transactionService
        +ResponseEntity getAll(Long userId)
        +ResponseEntity addIncome(Long userId, Map body)
        +ResponseEntity addExpense(Long userId, Map body)
        +ResponseEntity update(Long id, Map body)
        +ResponseEntity delete(Long id)
        +ResponseEntity filter(Long userId, Long categoryId, String startDate, String endDate, String type, String keyword)
    }

    class CategoryController {
        -CategoryService categoryService
        +ResponseEntity getAll()
        +ResponseEntity add(Map body)
        +ResponseEntity delete(Long id)
    }

    class ReportController {
        -ReportService reportService
        +ResponseEntity getMonthlyReport(Long userId, int month, int year)
    }

    class UserController {
        -UserService userService
        +ResponseEntity register(Map body)
        +ResponseEntity login(Map body)
    }

    class TransactionService {
        +Transaction addIncome(double amount, LocalDate date, String description, String source, Long userId)
        +Transaction addExpense(double amount, LocalDate date, String description, Long categoryId, Long userId)
        +List~Transaction~ getAllTransactions(Long userId)
        +Transaction updateTransaction(Long id, double amount, LocalDate date, String description)
        +boolean deleteTransaction(Long id)
        +List~Transaction~ filterByCategory(Long userId, Long categoryId)
    }

    class CategoryService {
        +List~Category~ getAllCategories()
        +Category addCategory(String name, String description)
        +boolean deleteCategory(Long id)
    }

    class ReportService {
        +MonthlyReport generateMonthlyReport(Long userId, int month, int year)
    }

    class UserService {
        +User register(String username, String email, String password)
        +Optional~User~ login(String username, String password)
    }

    %% Controller → Service dependencies
    TransactionController --> TransactionService : calls
    CategoryController    --> CategoryService    : calls
    ReportController      --> ReportService      : calls
    UserController        --> UserService        : calls
```

---

## Diagram 5 — Full Overview (All Layers)

```mermaid
---
title: "BudgetBuddy | Diagram 5: Full Overview"
---
classDiagram
    direction TB

    %% ── MODEL ──────────────────────────────────────────────
    namespace Model {
        class Transaction {
            <<abstract>>
            -Long id
            -double amount
            -LocalDate date
            -String description
            -Long userId
            +String getType()*
            +Long getId()
            +double getAmount()
            +LocalDate getDate()
            +String getDescription()
            +Long getUserId()
        }
        class Income {
            -String source
            +String getSource()
            +String getType()
        }
        class Expense {
            -Long categoryId
            -String categoryName
            +Long getCategoryId()
            +String getCategoryName()
            +String getType()
        }
        class Category {
            -Long id
            -String name
            -String description
            +Long getId()
            +String getName()
        }
        class User {
            -Long id
            -String username
            -String email
            -String password
            +Long getId()
            +String getUsername()
        }
        class MonthlyReport {
            -int month
            -int year
            -double totalIncome
            -double totalExpense
            +void generate(List~Transaction~ allTransactions)
            +double getBalance()
        }
    }

    %% ── REPOSITORY ──────────────────────────────────────────
    namespace Repository {
        class IRepository~T~ {
            <<interface>>
            +T save(T entity)
            +List~T~ findAll()
            +Optional~T~ findById(Long id)
            +boolean delete(Long id)
            +T update(T entity)
        }
        class TransactionRepository {
            +Transaction save(Transaction t)
            +List~Transaction~ findByUserId(Long userId)
            +List~Transaction~ findByUserIdAndDateRange(Long userId, LocalDate start, LocalDate end)
        }
        class CategoryRepository {
            +Category save(Category c)
            +List~Category~ findAll()
        }
        class UserRepository {
            +User save(User u)
            +Optional~User~ findByUsername(String username)
        }
    }

    %% ── SERVICE ─────────────────────────────────────────────
    namespace Service {
        class TransactionService {
            +Transaction addIncome(double amount, LocalDate date, String description, String source, Long userId)
            +Transaction addExpense(double amount, LocalDate date, String description, Long categoryId, Long userId)
            +List~Transaction~ getAllTransactions(Long userId)
            +boolean deleteTransaction(Long id)
            +List~Transaction~ filterByCategory(Long userId, Long categoryId)
            +List~Transaction~ filterByType(Long userId, String type)
            +List~Transaction~ searchByKeyword(Long userId, String keyword)
        }
        class CategoryService {
            +List~Category~ getAllCategories()
            +Category addCategory(String name, String description)
            +boolean deleteCategory(Long id)
        }
        class ReportService {
            +MonthlyReport generateMonthlyReport(Long userId, int month, int year)
        }
        class UserService {
            +User register(String username, String email, String password)
            +Optional~User~ login(String username, String password)
        }
    }

    %% ── CONTROLLER ──────────────────────────────────────────
    namespace Controller {
        class TransactionController {
            +ResponseEntity getAll(Long userId)
            +ResponseEntity addIncome(Long userId, Map body)
            +ResponseEntity addExpense(Long userId, Map body)
            +ResponseEntity update(Long id, Map body)
            +ResponseEntity delete(Long id)
            +ResponseEntity filter(Long userId, Long categoryId, String startDate, String endDate, String type, String keyword)
        }
        class CategoryController {
            +ResponseEntity getAll()
            +ResponseEntity add(Map body)
            +ResponseEntity delete(Long id)
        }
        class ReportController {
            +ResponseEntity getMonthlyReport(Long userId, int month, int year)
        }
        class UserController {
            +ResponseEntity register(Map body)
            +ResponseEntity login(Map body)
        }
    }

    %% ── RELATIONS ───────────────────────────────────────────

    %% Inheritance
    Transaction <|-- Income  : extends
    Transaction <|-- Expense : extends

    %% Interface implementation
    IRepository~T~ <|.. TransactionRepository : implements
    IRepository~T~ <|.. CategoryRepository    : implements
    IRepository~T~ <|.. UserRepository        : implements

    %% Model associations
    Expense       --> Category    : uses
    MonthlyReport --> Transaction : contains

    %% Service → Repository
    TransactionService --> TransactionRepository : uses
    TransactionService --> CategoryRepository    : uses
    ReportService      --> TransactionRepository : uses
    UserService        --> UserRepository        : uses
    CategoryService    --> CategoryRepository    : uses

    %% Controller → Service
    TransactionController --> TransactionService : calls
    CategoryController    --> CategoryService    : calls
    ReportController      --> ReportService      : calls
    UserController        --> UserService        : calls
```
