#!/bin/bash

# Aurora Marketplace - Build All Services Script

echo "Building Aurora Marketplace Services..."

# Function to build a service
build_service() {
    local service_name=$1
    local service_path=$2
    
    echo "Building $service_name..."
    cd "$service_path"
    
    if [ -f "mvnw" ]; then
        ./mvnw clean package -DskipTests
        if [ $? -eq 0 ]; then
            echo "SUCCESS: $service_name built successfully"
        else
            echo "ERROR: Failed to build $service_name"
            return 1
        fi
    else
        echo "ERROR: Maven wrapper not found in $service_path"
        return 1
    fi
    
    cd - > /dev/null
}

# Build services
build_service "Order Service" "services/order-service"
build_service "Search Service" "services/search-service"
build_service "API Gateway" "services/api-gateway"

# Build frontend
echo "Building Frontend..."
cd frontend
npm install
npm run build
if [ $? -eq 0 ]; then
    echo "SUCCESS: Frontend built successfully"
else
    echo "ERROR: Failed to build Frontend"
fi
cd - > /dev/null

echo ""
echo "Build process completed!"
echo "JAR files are in each service's target/ directory"
echo "Frontend build is in frontend/dist/ directory"
