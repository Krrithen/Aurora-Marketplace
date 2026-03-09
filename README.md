# Aurora Marketplace

A high-performance, event-driven e-commerce platform built with modern microservices architecture. It demonstrates advanced patterns like gRPC, CQRS, Event Sourcing, and Backend-for-Frontend (BFF).

## Overview

Aurora Marketplace is designed to solve real-world engineering challenges such as the N+1 query problem, dual-write data loss, and high-concurrency search. It moves beyond simple CRUD to showcase a production-grade system design.

### Key Architecture Features

- **Microservices Architecture**: 4 focused services (Gateway, Order, Catalog, Search).
- **gRPC Communication**: High-performance, strongly-typed internal communication using Protobuf.
- **Event-Driven Design**: Asynchronous data synchronization using Apache Kafka (CQRS pattern).
- **Backend-for-Frontend (BFF)**: The API Gateway acts as a smart orchestration layer, aggregating data for the client.
- **Polyglot Persistence**: Using the right database for the job:
    - **PostgreSQL**: Transactional data (Orders).
    - **MongoDB**: Flexible product catalog (Catalog).
    - **Elasticsearch**: Full-text search engine (Search).
    - **Redis**: Caching and rate limiting (Gateway).

## Architecture Diagram

```
┌──────────────┐       HTTP/JSON        ┌───────────────────┐
│              │ ─────────────────────► │                   │
│   Client     │                        │    API Gateway    │
│ (Web/Mobile) │ ◄───────────────────── │      (BFF)        │
│              │                        │    (Port 8081)    │
└──────────────┘                        └─────────┬─────────┘
                                                  │
                                         gRPC     │
          ┌───────────────────────────────────────┼──────────────────────────────────────┐
          │                                       │                                      │
          ▼                                       ▼                                      ▼
┌───────────────────┐                   ┌───────────────────┐                  ┌───────────────────┐
│                   │                   │                   │                  │                   │
│   Order Service   │                   │  Catalog Service  │                  │   Search Service  │
│    (Port 9090)    │                   │    (Port 9091)    │                  │    (Port 9093)    │
│                   │                   │                   │                  │                   │
└─────────┬─────────┘                   └─────────┬─────────┘                  └─────────┬─────────┘
          │                                       │                                      │
          │ JDBC                                  │ Mongo Driver                         │ REST/Native
          ▼                                       ▼                                      ▼
    ┌────────────┐                          ┌────────────┐                         ┌─────────────┐
    │ PostgreSQL │                          │  MongoDB   │                         │Elasticsearch│
    └────────────┘                          └─────┬──────┘                         └──────▲──────┘
                                                  │                                       │
                                                  │         ProductCreatedEvent           │
                                                  └───────────────────────────────────────┘
                                                                 (via Kafka)
```

## Service Details

### 1. API Gateway (Port 8081)
The entry point for all external traffic.
*   **Tech**: Spring Cloud Gateway, WebFlux.
*   **Role**: Routes requests, handles authentication (future), and aggregates data.
*   **Key Feature**: Translates external REST/JSON requests into internal gRPC calls.

### 2. Order Service (Port 9090)
Handles the transactional core of the business.
*   **Tech**: Spring Boot, Data JPA, PostgreSQL.
*   **Protocol**: gRPC.
*   **Role**: Manages order lifecycle and inventory reservation.

### 3. Catalog Service (Port 9091)
Manages product data with a flexible schema.
*   **Tech**: Spring Boot, Data MongoDB.
*   **Protocol**: gRPC.
*   **Role**: Source of truth for product information.
*   **Key Feature**: Publishes `ProductCreatedEvent` to Kafka whenever a product is added.

### 4. Search Service (Port 9093)
Provides high-speed search capabilities.
*   **Tech**: Spring Boot, Data Elasticsearch, Kafka Consumer.
*   **Protocol**: gRPC.
*   **Role**: Consumes events from Kafka to build a read-optimized search index.

### 5. Discovery Server (Port 8761)
Service registry for dynamic service discovery.
*   **Tech**: Netflix Eureka.

## Getting Started

### Prerequisites
*   **Java 21**
*   **Maven**
*   **Docker & Docker Compose**
*   **Python 3** (for hydration script)

### Installation & Run

1.  **Start Infrastructure**
    Spin up the databases and message brokers:
    ```bash
    docker-compose up -d
    ```

2.  **Start Discovery Server**
    ```bash
    cd services/discovery-server
    mvn clean package -DskipTests
    java -jar target/discovery-server-0.0.1-SNAPSHOT.jar
    ```

3.  **Start Backend Services**
    Open separate terminals for each:
    ```bash
    # Catalog Service
    cd services/catalog-service
    mvn clean package -DskipTests
    java -jar target/Catalog-0.0.1-SNAPSHOT.jar

    # Search Service
    cd services/search-service
    mvn clean package -DskipTests
    java -jar target/Search-0.0.1-SNAPSHOT.jar

    # Order Service
    cd services/order-service
    mvn clean package -DskipTests
    java -jar target/Orders-0.0.1-SNAPSHOT.jar
    ```

4.  **Start API Gateway**
    ```bash
    cd services/api-gateway
    mvn clean package -DskipTests
    java -jar target/ecomProject-0.0.1-SNAPSHOT.jar
    ```

5.  **Hydrate Data**
    Populate the system with sample data from the `dataset/` folder:
    ```bash
    python3 hydrate.py
    ```

## API Endpoints (Gateway)

*   `POST /api/products` - Create a product (Hydration)
*   `GET /api/products/{id}` - Get product details
*   `POST /api/orders` - Create an order
*   `GET /api/orders/{id}` - Get order details
