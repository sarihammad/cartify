# Cartify

A production-ready e-commerce backend built with Spring Boot, designed for scalability and security. Provides a complete e-commerce solution with modern architecture patterns and complete features.

## Features

### Authentication & Authorization

- **JWT-based Authentication** with secure token management
- **User Registration & Login** with email/password
- **Role-based Access Control** (USER, ADMIN roles)
- **Password Encryption** using BCrypt
- **Stripe Customer Integration** for payment processing

### Product Management

- **Complete CRUD Operations** for products
- **Product Catalog** with search and filtering
- **Inventory Management** with quantity tracking
- **Product Images** support with URL storage
- **Admin-only Product Management** with proper authorization

### Shopping Cart System

- **User-specific Shopping Carts** with persistent storage
- **Add/Remove Items** with quantity management
- **Cart Item Management** with product associations
- **Real-time Cart Updates**

### Order Management

- **Order Creation** with multiple items
- **Order History** per user
- **Order Status Tracking**
- **Coupon Integration** for discounts
- **Order Items** with detailed product information

### Payment Processing

- **Stripe Integration** for secure payments
- **Payment Intent Creation** with client-side confirmation
- **Webhook Handling** for payment status updates
- **Refund Processing** with admin controls
- **Payment History** tracking

### Coupon System

- **Discount Coupons** with percentage-based discounts
- **Coupon Validation** with expiration dates
- **Active/Inactive Coupon Management**
- **Order-Coupon Integration**

### Security & Performance

- **Rate Limiting** using Bucket4j and Redis (10 requests/minute per IP)
- **API Security** with Spring Security
- **CORS Configuration** for cross-origin requests
- **Input Validation** with Bean Validation
- **SQL Injection Protection** with JPA/Hibernate

### Email Services

- **SMTP Integration** for email notifications
- **Transactional Emails** for order confirmations
- **Email Templates** support

### Event-Driven Architecture

- **Apache Kafka Integration** for event streaming
- **Asynchronous Processing** for scalability
- **Event Publishing** for system decoupling

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Load Balancer │    │   API Gateway   │
│   (Client App)  │◄──►│                 │◄──►│                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │◄──►│   Cartify API   │◄──►│     Redis       │
│   (Database)    │    │   (Spring Boot) │    │   (Cache/Limits)│
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   Apache Kafka  │
                       │   (Events)      │
                       └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   Stripe API    │
                       │   (Payments)    │
                       └─────────────────┘
```

## Tech Stack

| Component                   | Technology               | Version |
| --------------------------- | ------------------------ | ------- |
| **Language**                | Java                     | 17      |
| **Framework**               | Spring Boot              | 3.4.4   |
| **Database**                | PostgreSQL               | 15      |
| **Caching & Rate Limiting** | Redis + Bucket4j         | 8.14.0  |
| **Message Broker**          | Apache Kafka             | Latest  |
| **Payment Processing**      | Stripe                   | 24.8.0  |
| **Authentication**          | JWT + Spring Security    | -       |
| **API Documentation**       | OpenAPI 3 (Swagger)      | 2.3.0   |
| **Testing**                 | JUnit 5 + Testcontainers | 1.19.3  |
| **Build Tool**              | Maven                    | -       |
| **Containerization**        | Docker + Docker Compose  | -       |

## API Documentation

### Authentication Endpoints

- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User login

### Product Endpoints

- `GET /api/v1/products` - Get all products
- `GET /api/v1/products/{id}` - Get product by ID
- `POST /api/v1/products` - Create product (Admin only)
- `PUT /api/v1/products/{id}` - Update product (Admin only)
- `DELETE /api/v1/products/{id}` - Delete product (Admin only)

### Cart Endpoints

- `GET /api/v1/cart` - Get user's cart
- `POST /api/v1/cart/add` - Add item to cart
- `PUT /api/v1/cart/update` - Update cart item
- `DELETE /api/v1/cart/remove/{itemId}` - Remove item from cart

### Order Endpoints

- `POST /api/v1/orders` - Place order
- `GET /api/v1/orders` - Get user's orders
- `GET /api/v1/orders/{id}` - Get order details

### Payment Endpoints

- `POST /api/v1/payments/create` - Create payment intent
- `POST /api/v1/payments/refund` - Process refund
- `POST /api/v1/payments/webhook` - Stripe webhook handler

### Admin Endpoints

- `POST /api/v1/admin/refunds` - Admin refund processing
- `GET /api/v1/admin/orders` - Admin order management
