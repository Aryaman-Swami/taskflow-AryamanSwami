-- Down migration for V3__add_task_history.sql
-- Drop indexes first
DROP INDEX IF EXISTS idx_task_history_change_type;
DROP INDEX IF EXISTS idx_task_history_updated_at;
DROP INDEX IF EXISTS idx_task_history_updated_by;
DROP INDEX IF EXISTS idx_task_history_task_id;

-- Drop table
DROP TABLE IF EXISTS task_history CASCADE;