CREATE TABLE Practitioner (

                              id IDENTITY PRIMARY KEY,
                              first_name VARCHAR(50) NOT NULL,
                              last_name VARCHAR(50) NOT NULL,
                              association_num VARCHAR(100) NULL,
                              arhg_num VARCHAR(100) NULL,
                              active  BOOLEAN DEFAULT TRUE NOT NULL,
                              version INT DEFAULT 0 NOT NULL
);