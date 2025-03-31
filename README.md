# 💻 **Java Student Management System - CSV Import & Repository Pattern**  
![Java](https://img.shields.io/badge/Java-Repository%20Pattern-blue)  
![Database](https://img.shields.io/badge/Database-CSV-data-green)  
## 📝 **Description**

This project demonstrates an implementation of the **Repository Pattern** in Java for managing student records, integrating data from **CSV** files and performing key operations such as **student registration**, **enrollment in courses**, **sorting**, **filtering**, and **reporting**.

The system is designed to perform the following tasks:
- Load student and course data from CSV files.
- Register new students and enroll them in courses.
- Sort and filter students by specific attributes like last name and student number.
- Generate detailed reports about courses with enrolled students.

### 🛠️ **Technologies Used**

- **Java**: The primary language used for the system implementation, ideal for backend applications due to its performance and robustness.
- **Repository Pattern**: Implementation of the Repository design pattern to abstract data access. This pattern enhances maintainability and scalability of the system by encapsulating data access logic.
- **CSV**: Data is imported from CSV files, which allows simulating a small database and practicing loading data from external sources without complex database setups.
- **DTOs (Data Transfer Objects)**: Using data transfer objects (DTO) to separate business logic from the presentation layer and ensure efficient communication between different layers of the system.
- **Java Lists**: Utilizing Java collections like `List` to efficiently handle student and course records and facilitate
