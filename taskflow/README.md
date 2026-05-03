# TaskFlow API

## 1. Overview

TaskFlow is a RESTful task and project management API that lets users create projects, manage tasks within them, and collaborate with assignees — all secured behind JWT-based authentication.

**Tech Stack:**
- Java 21
- Spring Boot 4.0.5
- Spring Security with JWT (stateless authentication)
- Spring Data JPA + PostgreSQL 16
- Flyway (database migrations)
- MapStruct (DTO mapping)
- Lombok
- Docker + Docker Compose

---

## 2. Architecture Decisions

**Why Spring Boot + PostgreSQL?**
Spring Boot gives a production-grade foundation with minimal boilerplate. PostgreSQL was chosen for its reliability, support for UUIDs as primary keys, and wide adoption — making it easy for reviewers to inspect data directly.

**Why JWT (stateless auth)?**
Stateless tokens avoid server-side session storage, simplify horizontal scaling, and fit naturally with REST. Tokens expire after 24 hours. Refresh token support was intentionally left out to keep scope focused — it would be the first thing I'd add.

**Why Flyway for migrations?**
Flyway ensures the schema evolves safely and predictably. `ddl-auto: validate` means Hibernate checks the schema but never modifies it — all changes go through versioned migration scripts. This prevents silent schema drift in production.

**Role-based access is lightweight by design.**
There are two implicit roles: project owner (the user who created the project) and assignee (a user assigned to a task). I didn't implement a formal roles table because the access rules are simple enough to enforce in the service layer. A more complex permission model would warrant a dedicated RBAC implementation.

**What I intentionally left out:**
- Pagination on list endpoints (would add for production)
- Email verification on registration
- Soft deletes (hard deletes kept things simple)

---

## 3. Running Locally


```bash
# 1. Clone the repository
git clone https://github.com/your-name/taskflow
cd taskflow

# 3. Start the database and application
docker compose up -d

# App is now available at http://localhost:8080
```

---

## 4. Running Migrations

Migrations run **automatically on startup** via Flyway. No manual steps are needed.

Migration scripts are located in `src/main/resources/db/migration/`.

---

## 5. Test Credentials

The database is seeded with a test user on startup. You can log in immediately without registering:

```
Email:    test@example.com
Password: password123
```

To get a JWT token:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "password123"}'
```

Use the returned token as `Authorization: Bearer <token>` on all subsequent requests.

---

## 6. API Reference

All endpoints (except `/auth/*`) require:
```
Authorization: Bearer <your_jwt_token>
```

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
Response `200`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
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

---

### Projects

#### List Projects
```http
GET /projects
```

#### Get Project
```http
GET /projects/{id}
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

#### Update Project
```http
PATCH /projects/{id}
Content-Type: application/json

{
  "name": "Updated Name",
  "description": "Updated description"
}
```

#### Delete Project
```http
DELETE /projects/{id}
```

---

### Tasks

#### List Tasks (with optional filters)
```http
GET /projects/{projectId}/tasks?status=TODO&assignee={userId}
```
Supported `status` values: `TODO`, `IN_PROGRESS`, `DONE`

#### Get Task
```http
GET /tasks/{id}
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
Supported `priority` values: `LOW`, `MEDIUM`, `HIGH`

#### Update Task
```http
PATCH /tasks/{id}
Content-Type: application/json

{
  "title": "Updated Title",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM"
}
```

#### Delete Task
```http
DELETE /tasks/{id}
```

---

### Error Responses

| Status | Meaning |
|--------|---------|
| 400 | Validation failed — check `fields` in response body |
| 401 | Missing or invalid JWT token |
| 403 | Authenticated but not authorized (e.g. not the project owner) |
| 404 | Resource not found |

Example `400`:
```json
{
  "error": "validation failed",
  "status": 400,
  "timestamp": "2024-01-01T00:00:00Z",
  "fields": {
    "email": "is required",
    "password": "is required"
  }
}
```

> A Postman collection (`taskflow.postman_collection.json`) is included in the root of the repository with all endpoints pre-configured.

---

## 7. What I'd Do With More Time

**Shortcuts taken:**
- No pagination — `GET /projects` and `GET /projects/{id}/tasks` return all results. With real data volumes this would be a problem.

**What I'd add:**
- Cursor-based pagination on list endpoints
- Maintaining Task Activity History . What Updates are done and by whom
- Allowing File Upload Feature for Project and tasks by Integrating Azure Blob 
- `GET /tasks` (global, across projects) with richer filtering — by priority, due date range, assignee
- Activity log / audit trail per task (who changed what, when)
- Email notifications on task assignment or due date approaching
- OpenAPI/Swagger docs auto-generated from the codebase rather than hand-written
- More granular roles (e.g., project member vs viewer vs admin)
- Integration tests using Testcontainers for a real PostgreSQL instance in CI
