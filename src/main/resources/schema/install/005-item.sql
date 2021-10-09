CREATE TABLE Item (

                      id IDENTITY PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      duration INT NULL,
                      type VARCHAR(100) NOT NULL,
                      price DOUBLE NOT NULL,
                      active  BOOLEAN DEFAULT TRUE NOT NULL,
                      version INT DEFAULT 0 NOT NULL
);