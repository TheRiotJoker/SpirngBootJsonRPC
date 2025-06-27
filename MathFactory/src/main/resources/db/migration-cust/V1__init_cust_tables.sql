CREATE TABLE customer_usage_data (
                                           uuid UUID PRIMARY KEY,
                                           customer_uuid UUID NOT NULL,
                                           function_name VARCHAR(50) NOT NULL,
                                           counter INTEGER NOT NULL,
                                           total_cost INTEGER NOT NULL,

                                           CONSTRAINT uq_customer_function UNIQUE (customer_uuid, function_name)
);

CREATE TABLE customer_threshold_data (
                                            uuid UUID PRIMARY KEY,
                                            customer_uuid UUID NOT NULL UNIQUE,
                                            threshold INTEGER NOT NULL
                                     )