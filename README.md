# User Address Directory API

A Spring Boot microservice for managing user address directory with full CRUD operations and search functionality.

## Features

- ✅ Create, Read, Update, Delete user addresses
- ✅ Search addresses by name, phone, email, city, or keyword
- ✅ RESTful API with OpenAPI/Swagger documentation
- ✅ H2 in-memory database
- ✅ Health check endpoints via Actuator
- ✅ Code coverage with JaCoCo (80%+ threshold)
- ✅ Docker containerization
- ✅ Helm chart for Kubernetes deployment
- ✅ Azure DevOps CI/CD pipeline

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.2**
- **Maven**
- **H2 Database**
- **Spring Data JPA**
- **SpringDoc OpenAPI**
- **JaCoCo**
- **Docker**

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+

### Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd backend-api

# Build the application
mvn clean package

# Run tests with coverage
mvn clean verify

# Run the application
mvn spring-boot:run
```

The application will start at `http://localhost:8080`

### Using Docker

```bash
# Build Docker image
docker build -t addressbook-api .

# Run container
docker run -p 8080:8080 addressbook-api
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/addresses` | Create a new address |
| GET | `/api/addresses` | Get all addresses |
| GET | `/api/addresses/{id}` | Get address by ID |
| PUT | `/api/addresses/{id}` | Update address |
| DELETE | `/api/addresses/{id}` | Delete address |
| GET | `/api/addresses/search?q={keyword}` | Search addresses |
| GET | `/api/addresses/search/name?name={name}` | Search by name |
| GET | `/api/addresses/search/city?city={city}` | Search by city |

## API Usage Examples (curl)

### Create a new address

```bash
curl -X POST http://localhost:8080/api/addresses \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "phone": "1234567890",
    "email": "john@example.com",
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  }'
```

### Get all addresses

```bash
curl http://localhost:8080/api/addresses
```

### Get address by ID

```bash
curl http://localhost:8080/api/addresses/1
```

### Update an address

```bash
curl -X PUT http://localhost:8080/api/addresses/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "phone": "9876543210",
    "email": "john.smith@example.com",
    "street": "456 Oak Ave",
    "city": "Los Angeles",
    "state": "CA",
    "zipCode": "90001",
    "country": "USA"
  }'
```

### Delete an address

```bash
curl -X DELETE http://localhost:8080/api/addresses/1
```

### Search addresses by keyword

```bash
# Search across all fields (name, phone, email, address, city, etc.)
curl "http://localhost:8080/api/addresses/search?q=John"
```

### Search by name

```bash
curl "http://localhost:8080/api/addresses/search/name?name=John"
```

### Search by city

```bash
curl "http://localhost:8080/api/addresses/search/city?city=New%20York"
```

### Check health status

```bash
curl http://localhost:8080/actuator/health
```

## Swagger UI

Access the interactive API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Health Check

Access health endpoints at:
- Health: http://localhost:8080/actuator/health
- Info: http://localhost:8080/actuator/info

## H2 Database Console

Access the H2 console at http://localhost:8080/h2-console with:
- JDBC URL: `jdbc:h2:mem:addressdb`
- Username: `sa`
- Password: (empty)

## Testing

```bash
# Run all tests
mvn test

# Run integration tests
mvn verify

# Generate coverage report
mvn verify
open target/site/jacoco/index.html
```

## Project Structure

```
backend-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/addressbook/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── exception/        # Exception handling
│   │   │   ├── mapper/           # Entity-DTO mappers
│   │   │   ├── repository/       # JPA repositories
│   │   │   ├── service/          # Business logic
│   │   │   └── AddressBookApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/                      # Unit & Integration tests
├── helm/addressbook/              # Helm chart
├── Dockerfile
├── azure-pipelines.yml            # CI/CD pipeline
└── pom.xml
```

## Build for Release

To build with a specific version:

```bash
# Maven
mvn clean package -Drevision=1.0.0

# Docker
docker build --build-arg VERSION=1.0.0 -t addressbook-api:1.0.0 .
```

## Deployment

### Azure Container Instances

The Azure DevOps pipeline (`azure-pipelines.yml`) handles:
1. Build and test with code coverage
2. Docker image build and push to ACR
3. Deploy to Azure Container Instances

### Kubernetes (Helm)

```bash
# Install
helm install addressbook ./helm/addressbook

# Upgrade
helm upgrade addressbook ./helm/addressbook

# Uninstall
helm uninstall addressbook
```

## Configuration

Environment variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | default |
| `SERVER_PORT` | Application port | 8080 |

## License

Apache 2.0
