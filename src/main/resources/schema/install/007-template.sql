CREATE TABLE Template (

                          id IDENTITY PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          content VARCHAR(5000) NOT NULL,
                          active  BOOLEAN DEFAULT TRUE NOT NULL,
                          version INT DEFAULT 0 NOT NULL
);