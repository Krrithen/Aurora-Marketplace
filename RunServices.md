### 1. Start Infrastructure (Docker)
This spins up PostgreSQL, MongoDB, Kafka, Zookeeper, and Elasticsearch.

```bash
# Terminal 1
docker-compose up -d
```

### 2. Start Discovery Server (Port 8761)
This must be running before the other services so they can register.

```bash
# Terminal 2
cd services/discovery-server
mvn clean package -DskipTests
java -jar target/discovery-server-0.0.1-SNAPSHOT.jar
```

### 3. Start Backend Services
Run these in separate terminals.

**Catalog Service (Port 9091)**
```bash
# Terminal 3
cd services/catalog-service
mvn clean package -DskipTests
java -jar target/Catalog-0.0.1-SNAPSHOT.jar
```

**Search Service (Port 9093)**
```bash
# Terminal 4
cd services/search-service
mvn clean package -DskipTests
java -jar target/Search-0.0.1-SNAPSHOT.jar
```

**Order Service (Port 9090)**
```bash
# Terminal 5
cd services/order-service
mvn clean package -DskipTests
java -jar target/Orders-0.0.1-SNAPSHOT.jar
```

### 4. Start API Gateway (Port 8081)
This is the entry point for your tests.

```bash
# Terminal 6
cd services/api-gateway
mvn clean package -DskipTests
java -jar target/ecomProject-0.0.1-SNAPSHOT.jar
```

### 5. Hydrate & Test
Once everything is up (check the logs for "Started Application"), run the script.

```bash
# Terminal 7 (Root folder)
python3 hydrate.py
```

You can check the data directly inside the Docker containers using these commands:

### 1. Check MongoDB (Catalog Data)
```bash
# Enter the MongoDB container
docker exec -it aurora-mongo-catalog mongosh -u admin -p password

# Inside the Mongo shell:
use catalog_db
db.products.countDocuments()
db.products.findOne()
exit
```

### 2. Check Elasticsearch (Search Index)
You can query Elasticsearch directly via `curl` (it exposes a REST API on port 9200).

```bash
# Check if the 'products' index exists and has documents
curl "http://localhost:9200/products/_count?pretty"

# Search for everything (match_all)
curl -X GET "http://localhost:9200/products/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match_all": {}
  }
}
'
```


### 3. Test Search**
```bash
curl "http://localhost:8081/api/search?q=laptop"
```