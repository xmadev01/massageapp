CREATE TABLE Health_Fund (

                             id IDENTITY PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             description VARCHAR(100) NOT NULL,
                             provider_numa VARCHAR(100) NULL,
                             provider_numm VARCHAR(100) NULL,
                             active  BOOLEAN DEFAULT TRUE NOT NULL,
                             version INT DEFAULT 0 NOT NULL,
                             UNIQUE KEY health_fund_name_UNIQUE (name)
);