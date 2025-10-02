#!/bin/bash

# Aurora Marketplace - Start All Services Script

echo "Starting Aurora Marketplace Services..."

# Function to start a service
start_service() {
    local service_name=$1
    local service_path=$2
    local port=$3
    
    echo "Starting $service_name on port $port..."
    cd "$service_path"
    
    if [ -f "mvnw" ]; then
        ./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=$port" &
    else
        echo "ERROR: Maven wrapper not found in $service_path"
        return 1
    fi
    
    cd - > /dev/null
    echo "SUCCESS: $service_name started"
}

# Start services (ordered by importance)
start_service "API Gateway" "services/api-gateway" "8081"
start_service "Search Service" "services/search-service" "8082"
start_service "Order Service" "services/order-service" "8083"
start_service "User Service" "services/user-service" "8084"
start_service "Merchant Service" "services/merchant-service" "8086"
start_service "Cart Service" "services/cart-service" "8087"

# Start frontend
echo "Starting Frontend..."
cd frontend
npm run serve &
cd - > /dev/null

echo ""
echo "All services are starting up!"
echo ""
echo "Frontend: http://localhost:8080"
echo "API Gateway: http://localhost:8081"
echo "Search Service: http://localhost:8082"
echo "Order Service: http://localhost:8083"
echo "User Service: http://localhost:8084"
echo "Merchant Service: http://localhost:8086"
echo "Cart Service: http://localhost:8087"
echo ""
echo "API Documentation:"
echo "   - API Gateway: http://localhost:8081/swagger-ui.html"
echo "   - Search: http://localhost:8082/swagger-ui.html"
echo "   - Orders: http://localhost:8083/swagger-ui.html"
echo "   - Users: http://localhost:8084/swagger-ui.html"
echo "   - Merchants: http://localhost:8086/swagger-ui.html"
echo "   - Cart: http://localhost:8087/swagger-ui.html"
echo ""
echo "To stop all services: pkill -f spring-boot:run && pkill -f npm"
