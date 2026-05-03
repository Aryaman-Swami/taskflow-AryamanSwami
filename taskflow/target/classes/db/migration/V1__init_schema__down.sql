-- Down migration for V1__init_schema.sql
-- This drops all objects created in the up migration in the correct order
-- (respecting foreign key constraints)

-- Drop trigger first
DROP TRIGGER IF EXISTS update_task_updated_at ON task;

-- Drop function
DROP FUNCTION IF EXISTS update_updated_at_column();

-- Drop tables in order of dependencies
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;

-- Drop extension (only if no other objects depend on it)
DROP EXTENSION IF EXISTS "uuid-ossp";