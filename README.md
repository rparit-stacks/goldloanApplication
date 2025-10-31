# Gold Loan Management System

A comprehensive Spring Boot application for managing gold loan operations with RESTful API, role-based authentication, and automated email notifications.

## 🎥 Video Tutorial

**Project Explanation Video:** [Watch Video Here]((https://youtu.be/jstN7swtM1E))

For detailed architecture explanation and service layer implementation, check the video located at `https://youtu.be/jstN7swtM1E`

---

## 📋 Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Security & Roles](#security--roles)
- [Email Service](#email-service)

---

## ✨ Features

- **Full CRUD Operations** for all entities (Users, Customers, Branches, Loan Applications, Documents, Gold Details, Loan Terms, Audits, Valuation Reports)
- **Role-Based Access Control** with Spring Security (ADMIN, MANAGER, OFFICER, AUDITOR)
- **Service Layer Architecture** - Proper service-to-service communication (no direct repository access between services)
- **Automatic Field Population** - Application numbers, stages, createdBy, assignedTo auto-set
- **Email Notifications** - Automated emails for user creation, branch creation, and loan application submission
- **Automatic Calculations** - Gold assessed value calculation (Weight × Purity / 100 × MarketRate)
- **Swagger/OpenAPI Documentation** - Interactive API documentation at `/swagger-ui.html`
- **Global Exception Handling** - Centralized error handling
- **Health Check Endpoints** - Available for all services

---

## 🛠 Technologies

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA** - Database operations
- **Spring Security** - Authentication & Authorization
- **Spring Web** - RESTful API
- **Spring Mail** - Email notifications
- **MySQL** - Database
- **Swagger/OpenAPI** - API documentation
- **Maven** - Dependency management

---

## 🏗 Architecture

### Service Layer Architecture

This project follows a **proper layered architecture** where:
- **Controllers** handle HTTP requests/responses only
- **Services** contain all business logic, validation, and service-to-service communication
- **Repositories** handle database operations only
- **Services communicate with each other**, NOT repositories directly

#### Example Flow:
```
POST /api/loan-applications
→ LoanApplicationController
→ LoanApplicationService
  ├─→ CustomerService (NOT CustomerRepository)
  ├─→ BranchService (NOT BranchRepository)
  ├─→ LoanTermService (NOT LoanTermRepository)
  ├─→ UserService (NOT UserRepository)
  └─→ EmailService (Notification)
→ LoanApplicationRepository.save()
→ Response
```

### Key Services

1. **Independent Services** (No dependencies):
   - `BranchService` - Branch management
   - `CustomerService` - Customer management
   - `LoanTermService` - Loan term configuration

2. **Dependent Services** (Service-to-service communication):
   - `LoanApplicationService` - Central service that coordinates multiple services
   - `DocumentService` - Uses `LoanApplicationService` and `UserService`
   - `GoldDetailService` - Uses `LoanApplicationService`, auto-calculates assessed value
   - `ApplicationAuditService` - Uses `LoanApplicationService` and `UserService`
   - `ValuationReportService` - Uses `LoanApplicationService`
   - `EmailService` - Automated email notifications

---

## 📁 Project Structure

```
src/main/java/com/rps/goldloan/
├── config/
│   ├── OpenApiConfig.java          # Swagger configuration
│   └── SecurityConfig.java          # Spring Security configuration
├── controller/                      # REST Controllers
│   ├── AuthController.java          # Authentication endpoints
│   ├── UserController.java
│   ├── CustomerController.java
│   ├── BranchController.java
│   ├── LoanApplicationController.java
│   ├── LoanTermController.java
│   ├── DocumentController.java
│   ├── GoldDetailController.java
│   ├── ApplicationAuditController.java
│   └── ValuationReportController.java
├── service/                         # Business Logic Layer
│   ├── UserService.java
│   ├── CustomerService.java
│   ├── BranchService.java
│   ├── LoanApplicationService.java
│   ├── LoanTermService.java
│   ├── DocumentService.java
│   ├── GoldDetailService.java
│   ├── ApplicationAuditService.java
│   ├── ValuationReportService.java
│   └── EmailService.java            # Email notifications
├── repository/                      # Data Access Layer
├── entity/                          # JPA Entities
├── dto/                             # Data Transfer Objects
├── exception/                       # Custom Exceptions & Global Exception Handler
├── enums/                           # Enumerations (Role, ApplicationStatus, etc.)
├── security/                        # Spring Security Custom Implementation
│   ├── CustomUserDetails.java
│   └── CustomUserDetailsService.java
└── util/
    └── VideoReference.java          # Video tutorial reference
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+

### Installation

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd GoldLoan
   ```

2. **Configure Database:**
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/goldloan_db?createDatabaseIfNotExist=true
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Configure Email (Optional):**
   Update email settings in `application.properties`:
   ```properties
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password
   ```

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

   Or using Maven:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application:**
   - API Base URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Docs: `http://localhost:8080/api-docs`

---

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user (Public)
- `POST /api/auth/login` - Login (Public)
- `POST /api/auth/logout` - Logout (Authenticated)
- `GET /api/auth/me` - Get current user (Authenticated)

### Users
- `GET /api/users/health` - Health check (Public)
- `POST /api/users` - Create user (Public)
- `GET /api/users/{id}` - Get user by ID (ADMIN)
- `GET /api/users` - Get all users (ADMIN)
- `PUT /api/users/{id}` - Update user (ADMIN)
- `DELETE /api/users/{id}` - Delete user (ADMIN)

### Customers
- `GET /api/customers/health` - Health check (Public)
- `POST /api/customers` - Create customer (ADMIN, MANAGER, OFFICER)
- `GET /api/customers/{id}` - Get customer by ID (ADMIN, MANAGER, OFFICER)
- `GET /api/customers` - Get all customers (ADMIN, MANAGER, OFFICER)
- `PUT /api/customers/{id}` - Update customer (ADMIN, MANAGER, OFFICER)
- `DELETE /api/customers/{id}` - Delete customer (ADMIN, MANAGER, OFFICER)

### Branches
- `GET /api/branches/health` - Health check (Public)
- `POST /api/branches` - Create branch (ADMIN, MANAGER)
- `GET /api/branches/{id}` - Get branch by ID (ADMIN, MANAGER)
- `GET /api/branches` - Get all branches (ADMIN, MANAGER)
- `PUT /api/branches/{id}` - Update branch (ADMIN, MANAGER)
- `DELETE /api/branches/{id}` - Delete branch (ADMIN, MANAGER)

### Loan Applications
- `GET /api/loan-applications/health` - Health check (Public)
- `POST /api/loan-applications` - Create loan application (ADMIN, MANAGER, OFFICER)
- `GET /api/loan-applications/{id}` - Get loan application by ID (ADMIN, MANAGER, OFFICER)
- `GET /api/loan-applications` - Get all loan applications (ADMIN, MANAGER, OFFICER)
- `PUT /api/loan-applications/{id}` - Update loan application (ADMIN, MANAGER, OFFICER)
- `DELETE /api/loan-applications/{id}` - Delete loan application (ADMIN, MANAGER, OFFICER)

### Loan Terms
- `GET /api/loan-terms/health` - Health check (Public)
- `POST /api/loan-terms` - Create loan term (ADMIN, MANAGER)
- `GET /api/loan-terms/{id}` - Get loan term by ID (ADMIN, MANAGER)
- `GET /api/loan-terms` - Get all loan terms (ADMIN, MANAGER)
- `PUT /api/loan-terms/{id}` - Update loan term (ADMIN, MANAGER)
- `DELETE /api/loan-terms/{id}` - Delete loan term (ADMIN, MANAGER)

### Documents
- `GET /api/documents/health` - Health check (Public)
- `POST /api/documents` - Create document (ADMIN, MANAGER, OFFICER, AUDITOR)
- `GET /api/documents/{id}` - Get document by ID (ADMIN, MANAGER, OFFICER, AUDITOR)
- `GET /api/documents` - Get all documents (ADMIN, MANAGER, OFFICER, AUDITOR)
- `GET /api/documents/loan-application/{loanApplicationId}` - Get documents by loan application (ADMIN, MANAGER, OFFICER, AUDITOR)
- `PUT /api/documents/{id}` - Update document (ADMIN, MANAGER, OFFICER, AUDITOR)
- `DELETE /api/documents/{id}` - Delete document (ADMIN, MANAGER, OFFICER, AUDITOR)

### Gold Details
- `GET /api/gold-details/health` - Health check (Public)
- `POST /api/gold-details` - Create gold detail (ADMIN, MANAGER, OFFICER, AUDITOR)
- `GET /api/gold-details/{id}` - Get gold detail by ID (ADMIN, MANAGER, OFFICER, AUDITOR)
- `GET /api/gold-details` - Get all gold details (ADMIN, MANAGER, OFFICER, AUDITOR)
- `GET /api/gold-details/loan-application/{loanApplicationId}` - Get gold details by loan application (ADMIN, MANAGER, OFFICER, AUDITOR)
- `PUT /api/gold-details/{id}` - Update gold detail (ADMIN, MANAGER, OFFICER, AUDITOR)
- `DELETE /api/gold-details/{id}` - Delete gold detail (ADMIN, MANAGER, OFFICER, AUDITOR)

### Application Audits
- `GET /api/application-audits/health` - Health check (Public)
- `POST /api/application-audits` - Create audit (ADMIN, AUDITOR)
- `GET /api/application-audits/{id}` - Get audit by ID (ADMIN, AUDITOR)
- `GET /api/application-audits` - Get all audits (ADMIN, AUDITOR)
- `GET /api/application-audits/loan-application/{loanApplicationId}` - Get audits by loan application (ADMIN, AUDITOR)
- `PUT /api/application-audits/{id}` - Update audit (ADMIN, AUDITOR)
- `DELETE /api/application-audits/{id}` - Delete audit (ADMIN, AUDITOR)

### Valuation Reports
- `GET /api/valuation-reports/health` - Health check (Public)
- `POST /api/valuation-reports` - Create valuation report (ADMIN, MANAGER, OFFICER, AUDITOR)
- `GET /api/valuation-reports/{id}` - Get valuation report by ID (ADMIN, MANAGER, OFFICER, AUDITOR)
- `GET /api/valuation-reports` - Get all valuation reports (ADMIN, MANAGER, OFFICER, AUDITOR)
- `GET /api/valuation-reports/loan-application/{loanApplicationId}` - Get valuation reports by loan application (ADMIN, MANAGER, OFFICER, AUDITOR)
- `PUT /api/valuation-reports/{id}` - Update valuation report (ADMIN, MANAGER, OFFICER, AUDITOR)
- `DELETE /api/valuation-reports/{id}` - Delete valuation report (ADMIN, MANAGER, OFFICER, AUDITOR)

---

## 🔐 Security & Roles

### Authentication
- **HTTP Basic Authentication** is used
- Login and Register endpoints are public
- All other endpoints require authentication

### Roles & Permissions

| Role | Permissions |
|------|------------|
| **ADMIN** | Full access to all endpoints |
| **MANAGER** | Access to most endpoints (except user management) |
| **OFFICER** | Access to customer and loan application endpoints |
| **AUDITOR** | Read-only access to documents, gold details, audits, and valuation reports |

### Role-Based Access by Endpoint:

- **Users**: ADMIN only
- **Branches**: ADMIN, MANAGER
- **Customers**: ADMIN, MANAGER, OFFICER
- **Loan Applications**: ADMIN, MANAGER, OFFICER
- **Loan Terms**: ADMIN, MANAGER
- **Documents**: ADMIN, MANAGER, OFFICER, AUDITOR
- **Gold Details**: ADMIN, MANAGER, OFFICER, AUDITOR
- **Application Audits**: ADMIN, AUDITOR
- **Valuation Reports**: ADMIN, MANAGER, OFFICER, AUDITOR

---

## 📧 Email Service

The `EmailService` automatically sends emails for:

1. **User Creation** - Welcome email when a new user is created
2. **Branch Creation** - Notification email to admin when a new branch is created
3. **Loan Application Submission** - Confirmation email to customer when loan application is submitted

Email configuration is done via `application.properties`. The service uses Spring Mail with SMTP configuration.

---

## 🎯 Key Features Explained

### Automatic Field Population
- **Application Numbers**: Auto-generated in format `APP-YYYYMMDD-XXXXX`
- **Stage**: Auto-set to `"IN REVIEW"` when loan application is created
- **CreatedBy & AssignedTo**: Automatically set to current logged-in user

### Automatic Calculations
- **Gold Assessed Value**: `(Weight × Purity / 100) × MarketRate`
- Calculated automatically in `GoldDetailService` when gold detail is created/updated

### Service-to-Service Communication
All services follow proper architecture:
- ✅ `DocumentService` calls `LoanApplicationService` (not repository)
- ✅ `LoanApplicationService` calls `CustomerService`, `BranchService`, `LoanTermService`, `UserService`
- ✅ Services contain business logic, validation, and coordination

---

## 📝 Notes

- Database schema is auto-generated using `spring.jpa.hibernate.ddl-auto=update`
- Swagger UI is accessible at `/swagger-ui.html` (no authentication required)
- Health check endpoints are public for monitoring purposes
- All validation errors are handled globally via `GlobalExceptionHandler`

---

## 📞 Contact

For questions or support, please refer to the video tutorial at `src/main/utils/IntroVideo.mp4`

---

**Built with ❤️ using Spring Boot**
