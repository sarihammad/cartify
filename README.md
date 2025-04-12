# Cartify

**Cartify** is a production-ready e-commerce backend built with **Spring Boot**, supporting secure authentication, role-based access, product management, shopping cart functionality, Stripe payments, and Redis-based rate limiting. Designed for scalability, resilience, and real-world system design readiness.

## Live Deployment

http://ec2-18-222-124-255.us-east-2.compute.amazonaws.com/api/v1

---

## Features

- JWT Authentication (Signup, Login, Role-based access)  
- Product Management (CRUD + pagination, filtering)  
- Shopping Cart per User
- Coupon Management & Discounts
- Order Placement & Payment via Stripe
- Refund Handling via Stripe
- Stripe Webhook Listener with Signature Verification  
- Redis Rate Limiting with Bucket4j  
- PostgreSQL for persistent storage  
- Integration & Unit Tests using Testcontainers  
- Dockerized for easy deployment  

---

## Tech Stack

| Layer              | Tech                                |
|-------------------|-------------------------------------|
| Language           | Java 17                             |
| Framework          | Spring Boot 3                       |
| Database           | PostgreSQL                          |
| Caching & Limits   | Redis + Bucket4j                    |
| Payments           | Stripe + Webhooks                   |
| Auth               | JWT + Spring Security               |
| Testing            | JUnit 5 + Testcontainers            |
| Deployment         | Docker Compose   
