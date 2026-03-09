# flash-sale-system

A high-concurrency, real-time distributed system designed to handle flash sale scenarios with thousands of simultaneous users.

# Tech Stack
Android: Jetpack Compose, Retrofit, OkHttp (WebSockets), Hilt (Dependency Injection), Coroutines.

Backend: Java, Spring Boot, Spring Data JPA.

Performance & Scaling: Redis (Atomic Inventory Management), Apache Kafka (Async Order Processing).

Real-time: WebSockets for live stock broadcasting.

Database: PostgreSQL.

# Architecture
The project follows a Microservices-ready architecture focusing on high availability and eventual consistency.

Fast Inventory Check: Stock is managed in Redis using atomic decrements to prevent overselling.

Asynchronous Persistence: Orders are pushed to Kafka, allowing the main service to remain responsive while the database updates in the background.

Live Updates: Every stock change is broadcasted via WebSockets, ensuring all users see real-time "Unit Left" counts without refreshing.

# Key Features
Admin Panel: Real-time stock injection via a secure dialog.

Concurrency Handling: Atomic operations in Redis to handle race conditions.

Self-Healing Cache: Automatically populates Redis from PostgreSQL if the cache is empty.

Modern UI: Built with Jetpack Compose using Material 3.
# How to Run
Infrastructure: Run docker-compose up to start PostgreSQL, Kafka, and Redis.

Backend: Run the lightning-order-service Spring Boot application.

Android: Open the project in Android Studio and update the BASE_URL in RetrofitClient to your local IP.

# Reference Video
[Reference.webm.zip](https://github.com/user-attachments/files/25838989/Reference.webm.zip)


