CREATE TABLE User (

   id IDENTITY PRIMARY KEY,
   user_code VARCHAR(20) NOT NULL,
   password VARCHAR(100) NOT NULL,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL,
   UNIQUE KEY user_user_code_UNIQUE (user_code)
)

GO

CREATE TABLE Health_Fund (

   id IDENTITY PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   description VARCHAR(100) NOT NULL,
   providerNumA VARCHAR(100) NULL,
   providerNumM VARCHAR(100) NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL,
   UNIQUE KEY health_fund_name_UNIQUE (name)
)

GO

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
   rebate_rate DOUBLE NULL,
   medication VARCHAR(500) NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL,
   UNIQUE KEY customer_email_UNIQUE (email),
   foreign key (health_fund) references Health_Fund(id)
)

GO

CREATE TABLE Practitioner (

   id IDENTITY PRIMARY KEY,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL
)

GO

CREATE TABLE Item (

   id IDENTITY PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   duration INT NOT NULL,
   type VARCHAR(100) NOT NULL,
   price DOUBLE NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL
)

GO

CREATE TABLE Treatment (

   id IDENTITY PRIMARY KEY,
   customer INT NOT NULL,
   service_date TIMESTAMP NOT NULL,
   item INT NOT NULL,
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
)

CREATE TABLE Template (

   id IDENTITY PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   content VARCHAR(5000) NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL
)



