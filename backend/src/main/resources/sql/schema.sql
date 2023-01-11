-- create database billing_system;
\connect billing_system;

-- All services
CREATE TABLE services
(
    Service_Id UUID PRIMARY KEY,
    Case_Id  VARCHAR(40) NOT NULL,
    Client_Id   VARCHAR(40) NOT NULL,
    Service   VARCHAR(10000) NOT NULL,
    Date    DATE NOT NULL,
    Attorneys   JSONB NOT NULL,
    Amount  FLOAT NOT NULL
);

INSERT INTO services(Service_Id, Case_Id, Client_Id, Service, Date, Attorneys, Amount)
VALUES ('ecceccc5-d4be-4dd5-87ac-cb375c8f5ea5', '0b32333f-4d31-4d3b-89c3-b2824f8794ba', 'bc37c7ca-0175-4fdb-8b3e-a1952a271c98', 'Meeting to discuss patent for new AI technology', '2022-06-04',
        '[{"id": "bc37c7ca-0175-4fdb-8b3e-a1952a271a98", "minutes": 60}]', 300);

INSERT INTO services(Service_Id, Case_Id, Client_Id, Service, Date, Attorneys, Amount)
VALUES ('a644294e-601d-401b-b713-a6bd061497f5', 'f35fe8ae-aa46-4305-bb3f-21fc45c8888c', 'c59df7c9-183a-422d-9113-5a9ffd4fd4ca', 'Conference to discuss new case', '2022-06-05',
        '[{"id": "bc37c7ca-0175-4fdb-8b3e-a1952a271a98", "minutes": 45},{"id":"bc37c7ca-0175-4fdb-8b3e-a1952a271b98", "minutes":75}]', 400);


-- All clients
CREATE TABLE clients
(
    Client_Id  VARCHAR(40) PRIMARY KEY,
    Client_Name   VARCHAR(500) NOT NULL,
    Currency_Code   VARCHAR(3) NOT NULL,
    Amount  FLOAT NOT NULL
);

INSERT INTO clients(Client_Id, Client_Name, Currency_Code, Amount)
VALUES ('bc37c7ca-0175-4fdb-8b3e-a1952a271c98', 'Microsoft', 'USD', 100000);

INSERT INTO clients(Client_Id, Client_Name, Currency_Code, Amount)
VALUES ('c59df7c9-183a-422d-9113-5a9ffd4fd4ca', 'Bohringer', 'GBP', 354600);

-- All attorneys
CREATE TABLE attorneys
(
    Attorney_Id  VARCHAR(40) PRIMARY KEY,
    First_Name   VARCHAR(500),
    Last_Name   VARCHAR(500),
    Service_Pricing     JSONB NOT NULL
);

INSERT INTO attorneys(Attorney_Id, First_Name, Last_Name, Service_Pricing)
VALUES ('bc37c7ca-0175-4fdb-8b3e-a1952a271a98', 'Sanjay', 'Kumar', '[{"clientId": "bc37c7ca-0175-4fdb-8b3e-a1952a271c98", "price": 45}, {"clientId": "c59df7c9-183a-422d-9113-5a9ffd4fd4ca", "price": 100}]');

INSERT INTO attorneys(Attorney_Id, First_Name, Last_Name, Service_Pricing)
VALUES ('bc37c7ca-0175-4fdb-8b3e-a1952a271b98', 'Arpita', 'Sawhney', '[{"clientId": "bc37c7ca-0175-4fdb-8b3e-a1952a271c98", "price": 25}, {"clientId": "c59df7c9-183a-422d-9113-5a9ffd4fd4ca", "price": 60}]');


-- All cases
CREATE TABLE cases
(
    Case_Id  VARCHAR(40) PRIMARY KEY,
    Case_Name   VARCHAR(500),
    Currency_Code   VARCHAR(3) NOT NULL,
    Amount  FLOAT NOT NULL
);

INSERT INTO cases(Case_Id, Case_Name, Currency_Code, Amount)
VALUES ('0b32333f-4d31-4d3b-89c3-b2824f8794ba', 'AI Case', 'USD', 30000);

INSERT INTO cases(Case_Id, Case_Name, Currency_Code, Amount)
VALUES ('f35fe8ae-aa46-4305-bb3f-21fc45c8888c', 'Pharma case', 'GBP', 20000);


-- All disbursements
CREATE TABLE disbursements
(
    Disbursement_Id   UUID PRIMARY KEY,
    Case_Id     VARCHAR(40) NOT NULL,
    Client_Id   VARCHAR(40) NOT NULL,
    Disbursement VARCHAR(10000) NOT NULL,
    Date    DATE NOT NULL,
    Currency_Code   VARCHAR(3) NOT NULL,
    Conversion_Rate     FLOAT NOT NULL,
    Inr_Amount     FLOAT NOT NULL,
    Conversion_Amount   FLOAT NOT NULL
);

INSERT INTO disbursements(Disbursement_Id, Case_Id, Client_Id, Disbursement, Date, Currency_Code, Conversion_Rate, Inr_Amount, Conversion_Amount)
VALUES ('f35fe8ae-aa46-4305-bb3f-21fc45c8888c', '0b32333f-4d31-4d3b-89c3-b2824f8794ba', 'bc37c7ca-0175-4fdb-8b3e-a1952a271c98', 'Flight tickets', '2022/06/05', 'GBP', 96.93, 969.3, 10.0);

INSERT INTO disbursements(Disbursement_Id, Case_Id, Client_Id, Disbursement, Date, Currency_Code, Conversion_Rate, Inr_Amount, Conversion_Amount)
VALUES ('ec02e254-894d-4600-b319-274679d0bd28', 'f35fe8ae-aa46-4305-bb3f-21fc45c8888c', 'c59df7c9-183a-422d-9113-5a9ffd4fd4ca', 'Hotel booking', '2022/06/05', 'USD', 70.73, 7073.0, 100.0);


