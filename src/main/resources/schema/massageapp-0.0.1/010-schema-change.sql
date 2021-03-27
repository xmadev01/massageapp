CREATE TABLE User (

   id IDENTITY PRIMARY KEY,
   user_code VARCHAR(20) NOT NULL,
   password VARCHAR(100) NOT NULL,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL
)



CREATE TABLE Customer (

   id IDENTITY PRIMARY KEY,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   email VARCHAR(100) NULL,
   phone VARCHAR(50) NULL,
   mobile VARCHAR(50) NULL,
   address VARCHAR(200) NULL,
   health_fund INT NULL,
   membership_num VARCHAR(50) NULL,
   rebate_rate DOUBLE NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL
)



CREATE TABLE Service (

   id IDENTITY PRIMARY KEY,
   customer INT NOT NULL,
   service_date_time TIMESTAMP NOT NULL,
   expense_amt DECIMAL NOT NULL,
   service_type INT NOT NULL,
   claimed_amt DECIMAL NOT NULL,
   duration INT NOT NULL,
   venue VARCHAR(200) NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL,
   foreign key (customer) references Customer(id)
)

