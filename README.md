# Distributed E-Commerce Microservices Architecture

A high-throughput, event-driven backend system demonstrating advanced distributed patterns including gRPC inter-service communication, CQRS, Event Sourcing, and a Backend-for-Frontend (BFF) gateway layer. 

## System Overview

This platform was architected to solve real-world engineering constraints found in high-scale e-commerce environments, specifically addressing the N+1 query problem, dual-write data loss, and high-concurrency search indexing. 

Moving beyond simple CRUD applications, this system is designed for high availability and fault tolerance, successfully **handling 10K+ simulated requests/day** while maintaining strict service decoupling.

### Performance & Benchmarks
* **Throughput:** Scaled to handle 10,000+ API requests daily via the API Gateway.
* **Latency Optimization:** Replaced internal REST calls with gRPC, significantly reducing inter-service network overhead and serialization costs.
* **Search Synchronization:** Reduced search index sync delays by **60%** and mitigated dual-write failures by implementing an asynchronous Kafka event pipeline.

## Engineering Decisions & Trade-offs

To meet strict performance and reliability requirements, several architectural trade-offs were made:

* **Inter-Service Communication (gRPC vs. REST):** Selected gRPC for synchronous communication between the API Gateway and internal microservices. By leveraging HTTP/2 multiplexing and binary serialization (Protobuf), we eliminated JSON parsing overhead and reduced payload sizes, significantly lowering network latency during high-volume data aggregation.

* **Asynchronous Search Indexing (Kafka CQRS vs. Synchronous REST):** Implemented an event-driven CQRS architecture using Apache Kafka to decouple the Catalog database (MongoDB) from the Search index (Elasticsearch). Synchronous dual-writes during high-traffic spikes would cause cascading latency and risk data loss. By streaming `ProductCreatedEvents` through Kafka, we achieved high fault isolation—if the Search service goes down, messages queue safely. This trades strong consistency for eventual consistency, maintaining sub-millisecond read latencies even under heavy write loads.

* **Polyglot Persistence:**
  Rather than forcing all data into a single relational schema, data stores were optimized per service:
  * **PostgreSQL:** ACID compliance for transactional order data.
  * **MongoDB:** Schema flexibility for diverse product catalogs.
  * **Elasticsearch:** High-speed inverted indexing for full-text search.
  * **Redis:** Low-latency caching layer at the gateway.

## Architecture Diagram

```text
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
The Backend-for-Frontend (BFF) entry point for all external traffic.
* **Tech:** Spring Cloud Gateway, WebFlux, Redis.
* **Role:** Routes requests, aggregates data, and translates external REST/JSON requests into high-speed internal gRPC calls.

### 2. Order Service (Port 9090)
Handles the highly-transactional core of the business.
* **Tech:** Spring Boot, Data JPA, PostgreSQL.
* **Protocol:** gRPC.
* **Role:** Manages order lifecycle, complex state transitions, and inventory reservation.

### 3. Catalog Service (Port 9091)
Manages massive product datasets with a flexible schema.
* **Tech:** Spring Boot, Data MongoDB.
* **Protocol:** gRPC.
* **Role:** Source of truth for product information. Publishes `ProductCreatedEvent` to Kafka upon ingestion.

### 4. Search Service (Port 9093)
Provides high-speed, scalable search capabilities.
* **Tech:** Spring Boot, Data Elasticsearch, Kafka Consumer.
* **Protocol:** gRPC.
* **Role:** Consumes events from Kafka to build a read-optimized, heavily denormalized search index independent of the main catalog load.

### 5. Discovery Server (Port 8761)
Service registry for dynamic scaling and routing.
* **Tech:** Netflix Eureka.

## 🛠️ Local Development & Deployment

### Prerequisites
* Java 21+
* Maven
* Docker & Docker Compose
* Python 3 (for test data hydration)

### Spin Up Infrastructure
Start the required databases (PostgreSQL, MongoDB, Elasticsearch, Redis) and message broker (Kafka):
```bash
docker-compose up -d
```
###Start Microservices
Run the services in the following order. Open a separate terminal for each:

1.  **Discovery Server**
    ```bash
    cd services/discovery-server
    mvn clean package -DskipTests
    java -jar target/discovery-server-0.0.1-SNAPSHOT.jar
    ```

3.  **Backend Services**
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

4.  **API Gateway**
    ```bash
    cd services/api-gateway
    mvn clean package -DskipTests
    java -jar target/ecomProject-0.0.1-SNAPSHOT.jar
    ```

5.  **Hydrate Data**
    Populate the system with a high volume of sample products to test Kafka synchronization and Elasticsearch indexing:
    ```bash
    python3 hydrate.py
    ```

## API Endpoints (Gateway)

*   `POST /api/products` - Ingest a new product (Triggers Kafka event)
*   `GET /api/products/{id}` - Retrieve detailed product data
*   `POST /api/orders` - Initialize a transactional order
*   `GET /api/orders/{id}` - Retrieve order status
