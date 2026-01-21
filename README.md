
# 🛒 Bazar – Advanced E-Commerce Backend

**Bazar** is a robust, production-ready **E-Commerce backend** built with the **Spring Boot ecosystem**.  
It serves as a comprehensive API solution for modern retail applications, featuring secure authentication, complex data relationships, and a scalable layered architecture.

---

## 🌟 Key Features

Bazar is engineered to handle the core requirements of a high-traffic e-commerce platform:

### 🔐 Advanced Security
- Stateless authentication using **JWT (JSON Web Tokens)**
- Custom security filters for secure API access

### 👥 Identity & Access Management
- Role-Based Access Control (**RBAC**)
- Supports **ADMIN** and **USER** roles

### 📦 Product Catalog
- Full CRUD operations for **Products** and **Categories**
- Image upload support via a dedicated `FileService`

### 🛒 Shopping Experience
- Persistent shopping carts
- Dynamic cart item management
- Multi-address support for users

### 💳 Order Orchestration
- Seamless transition from cart to order
- Order item tracking
- Payment entity integration

### 🛠️ Developer Experience
- Fully documented APIs using **Swagger / OpenAPI**
- Centralized global exception handling

### 📊 Database Flexibility
- MySQL-ready for production
- ORM powered by **JPA / Hibernate**

---

## 📚 Documentation

Detailed documentation is available in the `docs/` folder:

*   [**Architecture Overview**](docs/ARCHITECTURE.md): Explains the system design and layers.
*   [**Database Schema**](docs/DATABASE_SCHEMA.md): Entity-Relationship diagrams and table descriptions.
*   [**API Guide**](docs/API_GUIDE.md): Examples and usage instructions for key API endpoints.

---

## 🏗️ Technical Architecture

The project follows a **Clean Layered Architecture** to ensure maintainability and testability.

- **Controller Layer**  
  Handles REST requests and API versioning.

- **Service Layer**  
  Encapsulates business logic (Interfaces + Implementations).

- **Repository Layer**  
  Uses Spring Data JPA for database access.

- **Payload / DTO Layer**  
  Decouples internal entities from external API responses using **ModelMapper**.

- **Security Layer**  
  Custom JWT implementation with `AuthTokenFilter` and `WebSecurityConfig`.

---

## 🛠️ Tech Stack

| Category | Technology |
|--------|------------|
| Framework | Spring Boot 3.4.5 |
| Language | Java 21 |
| Build Tool | Maven |
| Security | Spring Security, JJWT |
| Database | MySQL, Spring Data JPA |
| Documentation | SpringDoc OpenAPI (Swagger) |
| Utilities | Lombok, ModelMapper, Validation API |

---

## 🚀 Getting Started

### Prerequisites
- JDK 21 or higher  
- Maven 3.x  
- MySQL Server  

---

### 1️⃣ Clone & Configure

```bash
git clone https://github.com/Aniruddhyagoswami/bazar.git
cd bazar/ecommerce
```

---

### 2️⃣ Database Setup

Create a database named `bazar_db` in MySQL and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bazar_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

### 3️⃣ Build & Run

```bash
# Build the application
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

---

## 📖 API Documentation

Once the server is running:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html  
- **API Base URL**: http://localhost:8080/api  

### Main Endpoint Groups

```http
POST   /api/auth/signin
GET    /api/public/products
POST   /api/admin/categories
POST   /api/carts/products/{productId}/quantity/{quantity}
```

---

## 📂 Project Structure

```plaintext
src/main/java/org/ecommerce/project/
├── config/             # Security, Swagger, and App configurations
├── controller/         # REST API Endpoints
├── exceptions/         # Global & Custom exception handlers
├── model/              # JPA Entities (User, Product, Order, etc.)
├── payload/            # DTOs and API Response wrappers
├── repository/         # Spring Data JPA Repositories
├── security/           # JWT implementation & Security filters
└── service/            # Business logic interfaces & implementations
```

---

## 🤝 Contributing

Contributions make the open-source community an amazing place!

1. Fork the project  
2. Create your feature branch  
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. Commit your changes  
   ```bash
   git commit -m "Add some AmazingFeature"
   ```
4. Push to the branch  
   ```bash
   git push origin feature/AmazingFeature
   ```
5. Open a Pull Request  

---

## 👤 Author

**Aniruddhya Goswami**  
- GitHub: https://github.com/Aniruddhyagoswami  
- LinkedIn: Aniruddhya Goswami  

---

## 📜 License

This project is licensed under the **MIT License**.  
See the `LICENSE` file for more details.
