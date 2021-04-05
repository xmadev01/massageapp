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


CREATE TABLE Health_Fund (

   id IDENTITY PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   description VARCHAR(100) NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL,
   UNIQUE KEY health_fund_name_UNIQUE (name)
)

CREATE TABLE Customer (

   id IDENTITY PRIMARY KEY,
   first_name VARCHAR(50) NOT NULL,
   middle_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   birthday TIMESTAMP NOT NULL,
   email VARCHAR(100) NULL,
   phone1 VARCHAR(50) NULL,
   phone2 VARCHAR(50) NULL,
   address VARCHAR(200) NULL,
   postcode VARCHAR(20) NULL,
   health_fund INT NOT NULL,
   membership_num VARCHAR(50) NULL,
   rebate_rate DOUBLE NULL,
   medication VARCHAR(500) NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL,
   UNIQUE KEY customer_email_UNIQUE (email),
   UNIQUE KEY customer_membership_num_UNIQUE (membership_num),
   foreign key (health_fund) references Health_Fund(id)
)


CREATE TABLE Treatment (

   id IDENTITY PRIMARY KEY,
   customer INT NOT NULL,
   service_date TIMESTAMP NOT NULL,
   expense_amt DECIMAL NOT NULL,
   claimed_amt DECIMAL NOT NULL,
   venue VARCHAR(200) NULL,
   created_date TIMESTAMP NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL,
   foreign key (customer) references Customer(id)
)

CREATE TABLE Practitioner (

   id IDENTITY PRIMARY KEY,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL
)

CREATE TABLE Item (

   id IDENTITY PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   duration INT NOT NULL,
   price DOUBLE NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL
)

CREATE TABLE Treatment_Item (

   id IDENTITY PRIMARY KEY,
   treatment_id INT NOT NULL,
   item_id INT NOT NULL,
   active  BOOLEAN DEFAULT TRUE NOT NULL,
   version INT DEFAULT 0 NOT NULL
)



