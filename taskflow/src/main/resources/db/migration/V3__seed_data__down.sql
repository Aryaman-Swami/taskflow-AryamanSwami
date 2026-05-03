-- Down migration for V2__seed_data.sql
-- This removes the seed data inserted by the up migration

-- Delete tasks first (they have foreign key references)
DELETE FROM task WHERE id IN (
    'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33',
    'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44',
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55'
);

-- Delete project (references user)
DELETE FROM project WHERE id = 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22';

-- Delete user
DELETE FROM "user" WHERE id = 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11';