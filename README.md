# Aurora Marketplace

A comprehensive marketplace platform built with microservices architecture, featuring Vue.js frontend and Spring Boot backend services.

## Overview

Aurora Marketplace is a project built to demonstrate and understand modern software architecture patterns, particularly microservices, REST APIs, and enterprise search capabilities. This project serves as a learning platform for understanding how different technologies work together in a real-world application.

### Learning Objectives

This project was created to explore and understand:

- **Microservices Architecture**: How to design, implement, and orchestrate independent services
- **REST API Development**: Building robust, scalable APIs with Spring Boot
- **Service Communication**: Inter-service communication patterns using Feign clients
- **Enterprise Search**: Implementing advanced search capabilities with Apache Solr
- **API Gateway Pattern**: Centralized routing, load balancing, and service orchestration
- **Database Integration**: Working with MongoDB for data persistence
- **Frontend-Backend Integration**: Connecting Vue.js frontend with microservices backend

### Key Features

- **Modern Frontend**: Vue.js 2.7.14 with Vue Router and Vuex
- **Microservices Backend**: Spring Boot services with independent scaling
- **Advanced Search**: Apache Solr-powered product search
- **User Management**: Complete authentication and profile management
- **Order Processing**: Full order lifecycle management
- **Shopping Cart**: Persistent cart functionality
- **Merchant Management**: Multi-merchant support
- **API Gateway**: Centralized routing and orchestration

## Architecture

The platform follows a microservices architecture pattern with the following components:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │   Search Service │
│   (Vue.js)      │◄──►│   (Port 8081)   │◄──►│   (Port 8082)   │
│   (Port 8080)   │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                    ┌─────────────────────────┐
                    │     Microservices       │
                    │                         │
                    │  ┌─────────┐ ┌─────────┐│
                    │  │  Users  │ │  Cart   ││
                    │  │ (8084)  │ │ (8087)  ││
                    │  └─────────┘ └─────────┘│
                    │                         │
                    │  ┌─────────┐ ┌─────────┐│
                    │  │ Orders  │ │Merchants││
                    │  │ (8083)  │ │ (8086)  ││
                    │  └─────────┘ └─────────┘│
                    └─────────────────────────┘
                                │
                                ▼
                    ┌─────────────────────────┐
                    │      Databases          │
                    │                         │
                    │  ┌─────────┐ ┌─────────┐│
                    │  │MongoDB  │ │  Solr   ││
                    │  │         │ │         ││
                    │  └─────────┘ └─────────┘│
                    └─────────────────────────┘
```

## Project Structure

```
Aurora-Marketplace/
├── frontend/                           # Vue.js Frontend Application
│   ├── src/
│   │   ├── components/                 # Reusable Vue components
│   │   ├── views/                     # Page components
│   │   │   ├── HomeView.vue           # Homepage
│   │   │   ├── CartView.vue           # Shopping cart page
│   │   │   ├── AccountView.vue        # User account page
│   │   │   ├── MyLogin.vue            # Login page
│   │   │   └── MyRegister.vue         # Registration page
│   │   ├── router/                    # Vue Router configuration
│   │   ├── store/                     # Vuex store modules
│   │   ├── config/                    # Configuration files
│   │   │   └── api.js                 # API configuration
│   │   └── assets/                    # Static assets
│   ├── package.json                   # Frontend dependencies
│   ├── vue.config.js                  # Vue CLI configuration
│   └── public/                        # Public assets
├── services/                          # Backend Microservices
│   ├── api-gateway/                   # API Gateway Service (Port 8081)
│   │   ├── src/main/java/com/example/auroramarketplace/
│   │   │   ├── controller/            # REST controllers
│   │   │   ├── services/              # Business logic
│   │   │   ├── entities/              # Data models
│   │   │   ├── dto/                   # Data transfer objects
│   │   │   ├── config/                # Configuration classes
│   │   │   └── feignclients/          # Service clients
│   │   ├── src/main/resources/
│   │   │   └── application.properties # Service configuration
│   │   └── pom.xml                    # Maven dependencies
│   ├── search-service/                # Search Service (Port 8082)
│   │   ├── src/main/java/com/example/Search/
│   │   │   ├── controller/            # Search endpoints
│   │   │   ├── config/                # Solr configuration
│   │   │   └── dto/                   # Search DTOs
│   │   └── pom.xml
│   ├── order-service/                 # Order Processing Service (Port 8083)
│   ├── user-service/                  # User Management Service (Port 8084)
│   ├── merchant-service/              # Merchant Management Service (Port 8086)
│   └── cart-service/                  # Shopping Cart Service (Port 8087)
├── scripts/                           # Build and Deployment Scripts
│   ├── start-all-services.sh          # Start all services script
│   └── build-all.sh                   # Build all services script
└── README.md                          # This file
```

## Service Details

### Frontend Service (Port 8080)

**Technology Stack:**
- Vue.js 2.7.14
- Vue Router 3.6.5
- Vuex 3.6.2
- Axios 1.6.0

**Key Components:**
- `DemoProducts.vue`: Product listing with search and filtering
- `Navbar.vue`: Navigation with user authentication
- `CartView.vue`: Shopping cart interface
- `AccountView.vue`: User account management

### API Gateway Service (Port 8081)

**Technology Stack:**
- Spring Boot 2.7.18
- Spring Cloud Gateway
- Spring Security
- MongoDB

**Key Endpoints:**
- `/products/*`: Product management
- `/users/*`: User operations
- `/cart/*`: Cart operations
- `/orders/*`: Order processing
- `/merchants/*`: Merchant management

### Search Service (Port 8082)

**Technology Stack:**
- Spring Boot 2.7.18
- Apache Solr 8.11.2
- Spring Data Solr

**Key Endpoints:**
- `GET /search/{query}`: Search products
- `POST /search/add`: Add product to search index
- `GET /search/suggestions`: Get search suggestions

### Order Service (Port 8083)

**Technology Stack:**
- Spring Boot 2.7.18
- MongoDB
- Spring Mail

**Key Endpoints:**
- `POST /orders`: Create new order
- `GET /orders/{id}`: Get order details
- `PUT /orders/{id}/status`: Update order status
- `GET /orders/user/{userId}`: Get user orders

### User Service (Port 8084)

**Technology Stack:**
- Spring Boot 2.7.18
- MongoDB
- Spring Security

**Key Endpoints:**
- `POST /users/register`: User registration
- `POST /users/login`: User authentication
- `GET /users/{id}`: Get user profile
- `PUT /users/{id}`: Update user profile

### Merchant Service (Port 8086)

**Technology Stack:**
- Spring Boot 2.7.18
- MongoDB
- Spring Data MongoDB

**Key Endpoints:**
- `POST /merchants`: Register merchant
- `GET /merchants/{id}`: Get merchant details
- `POST /merchants/{id}/products`: Add products
- `GET /merchants/{id}/products`: Get merchant products

### Cart Service (Port 8087)

**Technology Stack:**
- Spring Boot 2.7.18
- MongoDB
- Spring Data MongoDB

**Key Endpoints:**
- `POST /cart/add`: Add item to cart
- `GET /cart/{userId}`: Get user cart
- `PUT /cart/{userId}/item/{productId}`: Update cart item
- `DELETE /cart/{userId}/item/{productId}`: Remove cart item

## Search Features

1. **Full-Text Search**
   - Product name, description, and category search
   - Fuzzy matching for typos
   - Synonym support

2. **Faceted Search**
   - Filter by category
   - Price range filtering
   - Brand filtering
   - Availability filtering

3. **Auto-Complete**
   - Real-time search suggestions
   - Popular search terms
   - Typo correction

4. **Search Analytics**
   - Search query tracking
   - Popular products
   - Search performance metrics
