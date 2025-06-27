CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE available_functions (
                                     id UUID PRIMARY KEY,
                                     function_name VARCHAR(50) UNIQUE NOT NULL,
                                     cost INTEGER NOT NULL,
                                     enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE instance_calculation_data (
                                           uuid UUID PRIMARY KEY,
                                           instance_uuid UUID NOT NULL,
                                           function_name VARCHAR(50) NOT NULL,
                                           counter INTEGER NOT NULL,
                                           total_cost INTEGER NOT NULL,

                                           CONSTRAINT uq_instance_function UNIQUE (instance_uuid, function_name)
);

INSERT INTO available_functions (id, function_name, cost) VALUES
                                                              (gen_random_uuid(), 'ADDITION', 2),
                                                              (gen_random_uuid(), 'SUBTRACTION', 3),
                                                              (gen_random_uuid(), 'MULTIPLICATION', 25),
                                                              (gen_random_uuid(), 'DIVISION', 50),
                                                              (gen_random_uuid(), 'FACTORIAL', 100),
                                                              (gen_random_uuid(), 'POWER', 1150);