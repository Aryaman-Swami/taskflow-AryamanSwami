-- Add task_history table for tracking task changes
CREATE TABLE task_history (
    id BIGSERIAL PRIMARY KEY,
    task_id UUID NOT NULL REFERENCES task(id) ON DELETE CASCADE,
    task_data JSONB NOT NULL,
    updated_by UUID NOT NULL REFERENCES "user"(id),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    change_type VARCHAR(20) NOT NULL CHECK (change_type IN ('CREATED', 'UPDATED', 'DELETED'))
);

-- Create index for faster queries by task_id
CREATE INDEX idx_task_history_task_id ON task_history(task_id);

-- Create index for faster queries by updated_by
CREATE INDEX idx_task_history_updated_by ON task_history(updated_by);

-- Create index for faster queries by updated_at
CREATE INDEX idx_task_history_updated_at ON task_history(updated_at);

-- Create index for faster queries by change_type
CREATE INDEX idx_task_history_change_type ON task_history(change_type);