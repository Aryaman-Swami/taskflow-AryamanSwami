# TaskFlow

A full-stack task and project management application built with Spring Boot (backend API), with PostgreSQL as the database.

## Features

- User authentication (register/login) with JWT tokens
- Project management (create, read, update, delete)
- Task management with filtering options
- Role-based access control (project owners vs assignees)
- PostgreSQL database with Flyway migrations
- Docker containerization for easy deployment

## Tech Stack

### Backend
- Java 21
- Spring Boot 4.0.5
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Flyway (database migrations)
- MapStruct (DTO mapping)
- Lombok

## Quick Start with Docker

### Prerequisites

- Docker and Docker Compose installed on your system
- Git (to clone the repository)

### Running the Full Stack

1. **Clone the repository** (if you haven't already):
   ```bash
   git clone <repository-url>
   cd TaskFlow
   ```

2. **Create your environment configuration**:
   ```bash
   cp .env.example .env
   ```
   
   Edit `.env` to customize your configuration (optional - sensible defaults are provided).

3. **Start all services**:
   ```bash
   docker compose up -d
   ```

4. **Wait for the application to start** (approximately 30-60 seconds for first build).

5. **Access the application**:
   - API: http://localhost:8080

### Stopping the Application

```bash
docker compose down
```

To also remove the database volume (warning: this deletes all data):
```bash
docker compose down -v
```

## Default Test Credentials

The application comes with seed data for testing:

| Email | Password |
|-------|----------|
| test@example.com | test123 |

### Test Data Included

- **1 User**: Test User (test@example.com)
- **1 Project**: Sample Project
- **3 Tasks** with different statuses:
  - "Design Database Schema" (TODO, HIGH priority)
  - "Implement User Authentication" (IN_PROGRESS, HIGH priority)
  - "Set Up Project Structure" (DONE, MEDIUM priority)

## Database Migrations

Migrations are managed by Flyway and run automatically when the application starts.

### Migration Files Location

- `taskflow/src/main/resources/db/migration/`

### Migration Files

| File | Description |
|------|-------------|
| `V1__init_schema.sql` | Creates the initial database schema (users, projects, tasks tables) |
| `V1__init_schema__down.sql` | Down migration - drops all schema objects |
| `V2__seed_data.sql` | Inserts test/seed data |
| `V2__seed_data__down.sql` | Down migration - removes seed data |

### Running Migrations Manually

If you need to run migrations manually (e.g., for development):

```bash
# Connect to the database
docker exec -it <postgres_container_name> psql -U taskflow -d taskflow

# Flyway commands can be run through the application
# Or use the Flyway CLI tool separately
```

### Rolling Back Migrations

To roll back migrations:

```bash
# Stop the application
docker compose down

# Remove the database volume to reset
docker compose down -v

# Start fresh - migrations will run again
docker compose up -d
```

**Note**: Flyway's commercial version supports automatic down migrations. In the community version, down migrations are executed manually or by resetting the database.

## Configuration

### Environment Variables

All configuration is managed through the `.env` file at the repository root.

| Variable | Description | Default |
|----------|-------------|---------|
| `POSTGRES_DB` | Database name | `taskflow` |
| `POSTGRES_USER` | Database username | `taskflow` |
| `POSTGRES_PASSWORD` | Database password | `taskflow` |
| `POSTGRES_PORT` | Database port | `5432` |
| `SPRING_PROFILES_ACTIVE` | Spring profile | `docker` |
| `SPRING_DATASOURCE_URL` | JDBC URL | `jdbc:postgresql://postgres:5432/taskflow` |
| `SPRING_DATASOURCE_USERNAME` | Datasource username | `taskflow` |
| `SPRING_DATASOURCE_PASSWORD` | Datasource password | `taskflow` |
| `JWT_SECRET` | JWT signing secret | (secure default provided) |
| `API_PORT` | API server port | `8080` |
| `REACT_APP_PORT` | Frontend port | `3000` |

### Local Development (Without Docker)

#### Prerequisites

- Java 21+
- Maven 3.6+
- PostgreSQL 16+

#### Steps

1. **Start PostgreSQL**:
   ```bash
   # Using Docker for the database only
   docker run -d \
     -e POSTGRES_DB=taskflow \
     -e POSTGRES_USER=taskflow \
     -e POSTGRES_PASSWORD=taskflow \
     -p 5432:5432 \
     postgres:16-alpine
   ```

2. **Configure the application**:
   Update `taskflow/src/main/resources/application.yaml` with your database credentials.

3. **Build and run**:
   ```bash
   cd taskflow
   ./mvnw clean package
   java -jar target/taskflow-0.0.1-SNAPSHOT.jar
   ```

## API Documentation

### Authentication

#### Register
```http
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securepassword123"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securepassword123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

#### Refresh Token
Use the refresh token to obtain a new access token when the current one expires (valid for 14 days):
```http
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

### Projects

All project endpoints require authentication. Include the JWT token:
```
Authorization: Bearer <token>
```

#### List Projects
```http
GET /projects
```

#### Create Project
```http
POST /projects
Content-Type: application/json

{
  "name": "My Project",
  "description": "Project description"
}
```

### Tasks

#### List Tasks
```http
GET /projects/{projectId}/tasks?status=TODO&assignee={userId}
```

#### Create Task
```http
POST /projects/{projectId}/tasks
Content-Type: application/json

{
  "title": "Task Title",
  "description": "Task description",
  "status": "TODO",
  "priority": "HIGH",
  "assigneeId": "user-uuid",
  "dueDate": "2024-12-31"
}
```

## Troubleshooting

### Container Won't Start

1. Check logs:
   ```bash
   docker compose logs taskflow-api
   docker compose logs postgres
   ```

2. Ensure ports are not already in use:
   ```bash
   # Check if ports 8080 or 5432 are in use
   netstat -an | grep 8080
   netstat -an | grep 5432
   ```

### Database Connection Issues

1. Verify PostgreSQL is healthy:
   ```bash
   docker compose ps
   ```

2. Check database connectivity:
   ```bash
   docker exec -it <postgres_container> psql -U taskflow -d taskflow -c "SELECT 1;"
   ```

### Reset Everything

If you need a completely fresh start:

```bash
# Stop and remove all containers and volumes
docker compose down -v

# Remove any dangling images
docker image prune -a

# Start fresh
docker compose up -d --build
```

## License

MIT