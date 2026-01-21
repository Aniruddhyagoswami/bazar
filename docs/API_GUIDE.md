# API Guide

This guide provides examples of how to interact with the **Bazar** API using standard tools like `curl`.

**Base URL**: `http://localhost:8080/api`

## Authentication

Most endpoints require authentication. You must first sign in to get a JWT token.

### Sign In
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "your_username",
    "password": "your_password"
  }'
```

**Response**:
```json
{
  "username": "your_username",
  "roles": ["ROLE_USER"],
  "jwtToken": "eyJhbGciOiJIUzUxMiJ9..."
}
```

**Note**: Include the returned `jwtToken` in the `Authorization` header for subsequent requests:
`Authorization: Bearer <your_token>`

---

## Products & Categories

### Get All Products
Public endpoint (no auth required).
```bash
curl -X GET "http://localhost:8080/api/public/products?pageNumber=0&pageSize=10"
```

### Create a Category (Admin Only)
```bash
curl -X POST http://localhost:8080/api/admin/categories \
  -H "Authorization: Bearer <your_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "categoryName": "Electronics"
  }'
```

---

## Cart Operations

### Add Product to Cart
```bash
# /api/carts/products/{productId}/quantity/{quantity}
curl -X POST http://localhost:8080/api/carts/products/1/quantity/2 \
  -H "Authorization: Bearer <your_token>"
```

### View Cart
```bash
curl -X GET http://localhost:8080/api/carts/users/cart \
  -H "Authorization: Bearer <your_token>"
```

---

## Orders

### Place Order
```bash
# /api/orders/users/payments/{paymentMethod}
curl -X POST http://localhost:8080/api/orders/users/payments/CreditCard \
  -H "Authorization: Bearer <your_token>"
```

---

## Error Handling

The API returns standard error responses in case of failure:

```json
{
  "message": "Resource not found",
  "status": false
}
```

Common HTTP Codes:
- `200 OK`: Success
- `201 Created`: Resource successfully created
- `400 Bad Request`: Invalid input or validation error
- `401 Unauthorized`: Missing or invalid JWT token
- `404 Not Found`: Resource does not exist
