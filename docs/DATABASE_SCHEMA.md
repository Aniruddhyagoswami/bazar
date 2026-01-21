# Database Schema

The **Bazar** database is designed to support a full-featured e-commerce application. Below is the Entity-Relationship (ER) diagram and description of the key tables.

## ER Diagram

```mermaid
erDiagram
    USERS ||--o{ ROLES : "has"
    USERS ||--o{ ADDRESSES : "has"
    USERS ||--o{ PRODUCTS : "sells (as seller)"
    USERS ||--|| CART : "has"

    CART ||--o{ CART_ITEMS : "contains"

    CATEGORIES ||--o{ PRODUCTS : "contains"

    PRODUCTS ||--o{ CART_ITEMS : "is in"
    PRODUCTS ||--o{ ORDER_ITEMS : "is in"

    ORDERS ||--o{ ORDER_ITEMS : "contains"
    ORDERS ||--|| PAYMENT : "has"
    ORDERS ||--|| ADDRESSES : "shipped to"

    USERS {
        long userId PK
        string username
        string email
        string password
    }

    ROLES {
        int roleId PK
        string roleName
    }

    ADDRESSES {
        long addressId PK
        string street
        string buildingName
        string city
        string state
        string country
        string pincode
    }

    CATEGORIES {
        long categoryId PK
        string categoryName
    }

    PRODUCTS {
        long productId PK
        string productName
        string description
        int quantity
        double price
        double discount
        double specialPrice
        string image
    }

    CART {
        long cartId PK
        double totalPrice
    }

    CART_ITEMS {
        long cartItemId PK
        int quantity
        double productPrice
        double discount
    }

    ORDERS {
        long orderId PK
        string email
        date orderDate
        double totalAmount
        string orderStatus
    }

    ORDER_ITEMS {
        long orderItemId PK
        int quantity
        double orderedProductPrice
        double discount
    }

    PAYMENT {
        long paymentId PK
        string paymentMethod
    }
```

## Entity Descriptions

### User Management
- **USERS**: Stores user credentials and profile information.
- **ROLES**: Defines user roles (e.g., `ROLE_USER`, `ROLE_ADMIN`).
- **ADDRESSES**: Stores shipping/billing addresses associated with users.

### Product Catalog
- **CATEGORIES**: Hierarchical organization of products (e.g., Electronics, Books).
- **PRODUCTS**: The core items for sale. Contains pricing, stock, and descriptions. Linked to a Category and a Seller (User).

### Shopping & Orders
- **CART**: Represents a user's shopping cart. One-to-one relationship with User.
- **CART_ITEMS**: Represents individual items within a cart, linking a Product to a Cart with a specific quantity.
- **ORDERS**: Represents a finalized purchase. Linked to a shipping Address and Payment.
- **ORDER_ITEMS**: A snapshot of the products at the time of purchase (price, discount, quantity).
- **PAYMENT**: Stores payment method details for an order.
