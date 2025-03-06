-- create database --
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_database WHERE datname = 'database_lr5'
    ) THEN
        EXECUTE 'CREATE DATABASE database_lr5';
    END IF;
END $$;
-- create database -- 

-- create table --
CREATE TABLE IF NOT EXISTS clients (
    client_id INT PRIMARY KEY,
    full_name VARCHAR NOT NULL,
    phone_number VARCHAR NOT NULL,
    contact_info TEXT NOT NULL
);
-- create table --

-- add client --
CREATE OR REPLACE FUNCTION add_client(newID INT, newFullName VARCHAR, newPhone VARCHAR, newContactInfo TEXT)
RETURNS VOID AS $$
BEGIN
    INSERT INTO clients (client_id, full_name, phone_number, contact_info) 
    VALUES (newID, newFullName, newPhone, newContactInfo);
END;
$$ LANGUAGE plpgsql;
-- add client -- 

-- update client --
CREATE OR REPLACE FUNCTION update_client(newID INT, newFullName VARCHAR, newPhone VARCHAR, newContactInfo TEXT)
RETURNS VOID AS $$
BEGIN
    UPDATE clients 
    SET full_name = newFullName, 
        phone_number = newPhone, 
        contact_info = newContactInfo 
    WHERE clients.client_id = newID;
END;
$$ LANGUAGE plpgsql;
-- update client -- 

-- clear table --
CREATE OR REPLACE FUNCTION clear_clients()
RETURNS VOID AS $$ 
BEGIN 
    DELETE FROM clients;
END;
$$ LANGUAGE plpgsql;
-- clear table -- 

-- get clients --
CREATE OR REPLACE FUNCTION get_clients()
RETURNS TABLE(id INT, name VARCHAR, phone VARCHAR, contacts TEXT) AS $$
BEGIN
    RETURN QUERY SELECT * FROM clients;
END;
$$ LANGUAGE plpgsql;
-- get clients --

-- search client by ID --
CREATE OR REPLACE FUNCTION search_client_ID(desired INT)
RETURNS TABLE(id INT, name VARCHAR, phone VARCHAR, contacts TEXT) AS $$
BEGIN
    RETURN QUERY SELECT * FROM clients WHERE clients.client_id = desired;
END;
$$ LANGUAGE plpgsql;
-- search client by ID --

-- search client by name --
CREATE OR REPLACE FUNCTION search_client_name(desired VARCHAR)
RETURNS TABLE(id INT, name VARCHAR, phone VARCHAR, contacts TEXT) AS $$
BEGIN
    RETURN QUERY SELECT * FROM clients WHERE clients.full_name = desired;
END;
$$ LANGUAGE plpgsql;
-- search client by name --

-- search client by phone --
CREATE OR REPLACE FUNCTION search_client_phone(desired VARCHAR)
RETURNS TABLE(id INT, name VARCHAR, phone VARCHAR, contacts TEXT) AS $$
BEGIN
    RETURN QUERY SELECT * FROM clients WHERE clients.phone_number = desired;
END;
$$ LANGUAGE plpgsql;
-- search client by phone --

-- delete client by ID --
CREATE OR REPLACE FUNCTION delete_client_id(desired INT)
RETURNS VOID AS $$
BEGIN
    DELETE FROM clients WHERE client_id = desired;
END;
$$ LANGUAGE plpgsql;
-- delete client by ID --

-- delete client by name --
CREATE OR REPLACE FUNCTION delete_client_name(desired VARCHAR)
RETURNS VOID AS $$
BEGIN
    DELETE FROM clients WHERE full_name = desired;
END;
$$ LANGUAGE plpgsql;
-- delete client by name --

-- delete client by phone --
CREATE OR REPLACE FUNCTION delete_client_phone(desired VARCHAR)
RETURNS VOID AS $$
BEGIN
    DELETE FROM clients WHERE phone_number = desired;
END;
$$ LANGUAGE plpgsql;
-- delete client by phone --

-- create user --
CREATE OR REPLACE FUNCTION add_user(new_username VARCHAR, new_password VARCHAR)
RETURNS VOID AS $$
BEGIN
    EXECUTE format('CREATE USER %I WITH PASSWORD %L', new_username, new_password);

    EXECUTE format('GRANT SELECT ON TABLE clients TO %I', new_username);

    EXECUTE format('GRANT CONNECT ON DATABASE client_database TO %I', new_username);

END;
$$ LANGUAGE plpgsql;
-- create user --


-- create admin --
CREATE OR REPLACE FUNCTION add_admin(new_username VARCHAR, new_password VARCHAR)
RETURNS VOID AS $$
BEGIN
    EXECUTE format('CREATE ROLE %I WITH LOGIN PASSWORD %L SUPERUSER', new_username, new_password);
END;
$$ LANGUAGE plpgsql;
-- create admin --
