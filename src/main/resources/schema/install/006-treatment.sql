CREATE TABLE Treatment (

                           id IDENTITY PRIMARY KEY,
                           customer INT NOT NULL,
                           service_date TIMESTAMP NOT NULL,
                           item INT NOT NULL,
                           duration INT NULL,
                           practitioner INT NOT NULL,
                           expense_amt DECIMAL NOT NULL,
                           claimed_amt DECIMAL NOT NULL,
                           venue VARCHAR(200) NULL,
                           medical_case_record VARCHAR(5000) NULL,
                           created_date TIMESTAMP NOT NULL,
                           active  BOOLEAN DEFAULT TRUE NOT NULL,
                           version INT DEFAULT 0 NOT NULL,
                           foreign key (customer) references Customer(id),
                           foreign key (practitioner) references Practitioner(id),
                           foreign key (item) references Item(id)
);