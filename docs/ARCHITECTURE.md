# Architecture Overview

The **Bazar** E-Commerce backend follows a clean, layered architecture designed for scalability, maintainability, and testability.

## High-Level Architecture

The application is built using **Spring Boot** and follows the standard MVC (Model-View-Controller) pattern, enhanced with a Service Layer and Data Transfer Objects (DTOs) to decouple the internal domain model from the external API.

```mermaid
graph TD
    Client[Client (Web/Mobile)] -->|HTTP Requests| Controller[Controller Layer]
    Controller -->|DTOs| Service[Service Layer]
    Service -->|Entities| Repository[Repository Layer]
    Repository -->|SQL| Database[(MySQL Database)]

    subgraph "Application Core"
        Controller
        Service
        Repository
        Security[Security Filter Chain]
    end

    Client -.->|Auth Token| Security
    Security -.-> Controller
```

## Layers Description

### 1. Controller Layer (`org.ecommerce.project.controller`)
- **Responsibility**: Handles incoming HTTP requests, validates input using Bean Validation, and returns appropriate HTTP responses.
- **Key Components**: `CategoryController`, `ProductController`, `AuthController`, etc.
- **Communication**: Interacts with the Service layer using DTOs (`payload` package). It does not access the database directly.

### 2. Service Layer (`org.ecommerce.project.service`)
- **Responsibility**: Contains the business logic of the application. It handles transactions, complex validations, and orchestrates data flow between the Controller and Repository.
- **Key Components**: `CategoryService`, `ProductService`, `CartService`, `OrderService`.
- **Transformation**: Uses `ModelMapper` to convert between Entities and DTOs.

### 3. Repository Layer (`org.ecommerce.project.repository`)
- **Responsibility**: Abstraction over the data store. Extends `JpaRepository` to provide standard CRUD operations without boilerplate code.
- **Key Components**: `CategoryRepository`, `ProductRepository`, `UserRepository`, etc.

### 4. Model / Domain Layer (`org.ecommerce.project.model`)
- **Responsibility**: Represents the persistent data entities (tables in the database).
- **Key Components**: `User`, `Product`, `Category`, `Cart`, `Order`.
- **Features**: Uses JPA annotations (`@Entity`, `@Table`) for ORM mapping and Lombok (`@Data`) for reducing boilerplate.

### 5. Security Layer (`org.ecommerce.project.security`)
- **Responsibility**: Handles authentication and authorization.
- **Mechanism**: JWT (JSON Web Token) based stateless authentication.
- **Key Components**: `AuthTokenFilter` (intercepts requests), `UserDetailsImpl` (user info wrapper), `WebSecurityConfig` (security rules).

## Exception Handling
Global exception handling is implemented in `MyGlobalExceptionHandler` using `@RestControllerAdvice`. This ensures consistent error responses across the API.

## Payload / DTOs
Data Transfer Objects are used to define the structure of data sent to and received from the API, preventing direct exposure of internal database entities.
