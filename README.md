
Billing Software POC (JavaFX frontend + Spring Boot backend)
============================================================

What's included
- backend/  -> Spring Boot minimal API (products)
- frontend/ -> JavaFX minimal app (login, dashboard, inventory POC)

Prerequisites
- Java 17 JDK
- Maven
- MySQL (or change application.properties to use another DB)

Quick start (backend)
1. Edit backend/src/main/resources/application.properties and set your MySQL username/password.
2. Start MySQL and create database 'billing_system' (or let Hibernate create it).
   - mysql -u root -p
   - CREATE DATABASE billing_system;
3. Run backend:
   cd backend
   mvn spring-boot:run

Quick start (frontend)
1. Open the frontend folder in your IDE (IntelliJ recommended)
2. Run the JavaFX app:
   mvn javafx:run

POC features
- Login screen (local check: admin/1234)
- Dashboard with navigation to Inventory, Billing, Reports
- Inventory screen can create a sample product by hitting backend /api/products

Notes
- This is a minimal POC to get you started. Expand models, add JWT auth, invoice generation,
  payment integration, and reporting as next steps.
