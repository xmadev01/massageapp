CREATE TABLE User (

                      id IDENTITY PRIMARY KEY,
                      user_code VARCHAR(20) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      first_name VARCHAR(50) NOT NULL,
                      last_name VARCHAR(50) NOT NULL,
                      active  BOOLEAN DEFAULT TRUE NOT NULL,
                      version INT DEFAULT 0 NOT NULL,
                      UNIQUE KEY user_user_code_UNIQUE (user_code)
);