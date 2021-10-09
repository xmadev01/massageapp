CREATE TABLE Customer (

                          id IDENTITY PRIMARY KEY,
                          first_name VARCHAR(50) NOT NULL,
                          middle_name VARCHAR(50) NULL,
                          last_name VARCHAR(50) NULL,
                          birthday TIMESTAMP NOT NULL,
                          email VARCHAR(100) NULL,
                          phone1 VARCHAR(50) NULL,
                          phone2 VARCHAR(50) NULL,
                          address VARCHAR(200) NULL,
                          postcode VARCHAR(20) NULL,
                          health_fund INT NULL,
                          membership_num VARCHAR(50) NULL,
                          rebate_rate DOUBLE NULL,
                          medication VARCHAR(500) NULL,
                          active  BOOLEAN DEFAULT TRUE NOT NULL,
                          version INT DEFAULT 0 NOT NULL,
                          UNIQUE KEY customer_email_UNIQUE (email),
                          foreign key (health_fund) references Health_Fund(id)
);