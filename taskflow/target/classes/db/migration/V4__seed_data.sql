-- Seed data for development/testing
-- This file populates the database with initial test data

-- Insert a test user with a known password
-- Password: "test123" - BCrypt hashed with strength 12
INSERT INTO "user" (id, name, email, password, created_at) 
VALUES (
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    'Test User',
    'test@example.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj8YKFqMqK2G',
    CURRENT_TIMESTAMP
)
ON CONFLICT (id) DO NOTHING;

-- Insert a test project owned by the test user
INSERT INTO project (id, name, description, owner_id, created_at)
VALUES (
    'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
    'Sample Project',
    'A sample project for testing the TaskFlow application',
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    CURRENT_TIMESTAMP
)
ON CONFLICT (id) DO NOTHING;

-- Insert test tasks with different statuses
-- Task 1: TODO status
INSERT INTO task (id, title, description, status, priority, project_id, assignee_id, due_date, created_at, updated_at)
VALUES (
    'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33',
    'Design Database Schema',
    'Create the initial database schema with users, projects, and tasks tables',
    'TODO',
    'HIGH',
    'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    CURRENT_DATE + INTERVAL '7 days',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (id) DO NOTHING;

-- Task 2: IN_PROGRESS status
INSERT INTO task (id, title, description, status, priority, project_id, assignee_id, due_date, created_at, updated_at)
VALUES (
    'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44',
    'Implement User Authentication',
    'Set up JWT-based authentication with Spring Security',
    'IN_PROGRESS',
    'HIGH',
    'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    CURRENT_DATE + INTERVAL '3 days',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (id) DO NOTHING;

-- Task 3: DONE status
INSERT INTO task (id, title, description, status, priority, project_id, assignee_id, due_date, created_at, updated_at)
VALUES (
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55',
    'Set Up Project Structure',
    'Initialize the Spring Boot project with all necessary dependencies',
    'DONE',
    'MEDIUM',
    'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    CURRENT_DATE - INTERVAL '2 days',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (id) DO NOTHING;