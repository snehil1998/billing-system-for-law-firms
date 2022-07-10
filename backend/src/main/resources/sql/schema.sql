-- create database billing_system;
\connect billing_system;

-- All bills
-- CREATE TABLE services
-- (
--     Service_Id UUID PRIMARY KEY,
--     Case_Id  UUID NOT NULL,
--     Client_Id   UUID NOT NULL,
--     Service   VARCHAR(500) NOT NULL,
--     Description    VARCHAR(10000),
--     Date    DATE NOT NULL,
--     Attorney_Ids   VARCHAR(10000),
--     Minutes     INTEGER NOT NULL,
--     Amount  FLOAT NOT NULL
-- );
--
-- INSERT INTO services(Service_Id, Case_Id, Client_Id, Service, Description, Date, Attorney_Ids, Minutes, Amount)
-- VALUES ('ecceccc5-d4be-4dd5-87ac-cb375c8f5ea5', '0b32333f-4d31-4d3b-89c3-b2824f8794ba', 'bc37c7ca-0175-4fdb-8b3e-a1952a271c98', 'Meeting', 'discuss patent for new AI technology', '2022-06-04', 'bc37c7ca-0175-4fdb-8b3e-a1952a271a98', 60, 300);
--
-- INSERT INTO services(Service_Id, Case_Id, Client_Id, Service, Description, Date, Attorney_Ids, Minutes, Amount)
-- VALUES ('a644294e-601d-401b-b713-a6bd061497f5', 'f35fe8ae-aa46-4305-bb3f-21fc45c8888c', 'c59df7c9-183a-422d-9113-5a9ffd4fd4ca', 'Conference', 'discuss new case', '2022-06-05', 'bc37c7ca-0175-4fdb-8b3e-a1952a271a98,bc37c7ca-0175-4fdb-8b3e-a1952a271b98', 75, 400);

CREATE TABLE services
(
    Service_Id UUID PRIMARY KEY,
    Case_Id  UUID NOT NULL,
    Client_Id   UUID NOT NULL,
    Service   VARCHAR(500) NOT NULL,
    Description    VARCHAR(10000),
    Date    DATE NOT NULL,
    Attorneys   JSONB NOT NULL,
    Amount  FLOAT NOT NULL
);

INSERT INTO services(Service_Id, Case_Id, Client_Id, Service, Description, Date, Attorneys, Amount)
VALUES ('ecceccc5-d4be-4dd5-87ac-cb375c8f5ea5', '0b32333f-4d31-4d3b-89c3-b2824f8794ba', 'bc37c7ca-0175-4fdb-8b3e-a1952a271c98', 'Meeting', 'discuss patent for new AI technology', '2022-06-04',
        '[{"id": "bc37c7ca-0175-4fdb-8b3e-a1952a271a98", "minutes": 60}]', 300);

INSERT INTO services(Service_Id, Case_Id, Client_Id, Service, Description, Date, Attorneys, Amount)
VALUES ('a644294e-601d-401b-b713-a6bd061497f5', 'f35fe8ae-aa46-4305-bb3f-21fc45c8888c', 'c59df7c9-183a-422d-9113-5a9ffd4fd4ca', 'Conference', 'discuss new case', '2022-06-05',
        '[{"id": "bc37c7ca-0175-4fdb-8b3e-a1952a271a98", "minutes": 45},{"id":"bc37c7ca-0175-4fdb-8b3e-a1952a271b98", "minutes":75}]', 400);


-- All clients
CREATE TABLE clients
(
    Client_Id  UUID PRIMARY KEY,
    Client_Name   VARCHAR(500) NOT NULL,
    Currency_Code   VARCHAR(3) NOT NULL,
    Service_Pricing FLOAT NOT NULL,
    Amount  FLOAT NOT NULL
);

INSERT INTO clients(Client_Id, Client_Name, Currency_Code, Service_Pricing, Amount)
VALUES ('bc37c7ca-0175-4fdb-8b3e-a1952a271c98', 'Microsoft', 'USD', 300, 100000);

INSERT INTO clients(Client_Id, Client_Name, Currency_Code, Service_Pricing, Amount)
VALUES ('c59df7c9-183a-422d-9113-5a9ffd4fd4ca', 'Bohringer', 'GBP', 200, 354600);

-- All attorneys
CREATE TABLE attorneys
(
    Attorney_Id  UUID PRIMARY KEY,
    First_Name   VARCHAR(500),
    Last_Name   VARCHAR(500)
);

INSERT INTO attorneys(Attorney_Id, First_Name, Last_Name)
VALUES ('bc37c7ca-0175-4fdb-8b3e-a1952a271a98', 'Sanjay', 'Kumar');

INSERT INTO attorneys(Attorney_Id, First_Name, Last_Name)
VALUES ('bc37c7ca-0175-4fdb-8b3e-a1952a271b98', 'Arpita', 'Sawhney');


-- All cases
CREATE TABLE cases
(
    Case_Id  UUID PRIMARY KEY,
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
    Case_Id     UUID NOT NULL,
    Client_Id   UUID NOT NULL,
    Date    DATE NOT NULL,
    Currency_Code   VARCHAR(3) NOT NULL,
    Conversion_Rate     FLOAT NOT NULL,
    Inr_Amount     FLOAT NOT NULL,
    Conversion_Amount   FLOAT NOT NULL
);

INSERT INTO disbursements(Disbursement_Id, Case_Id, Client_Id, Date, Currency_Code, Conversion_Rate, Inr_Amount, Conversion_Amount)
VALUES ('f35fe8ae-aa46-4305-bb3f-21fc45c8888c', '0b32333f-4d31-4d3b-89c3-b2824f8794ba', 'bc37c7ca-0175-4fdb-8b3e-a1952a271c98', '2022/06/05', 'GBP', 96.93, 969.3, 10.0);

INSERT INTO disbursements(Disbursement_Id, Case_Id, Client_Id, Date, Currency_Code, Conversion_Rate, Inr_Amount, Conversion_Amount)
VALUES ('ec02e254-894d-4600-b319-274679d0bd28', 'f35fe8ae-aa46-4305-bb3f-21fc45c8888c', 'c59df7c9-183a-422d-9113-5a9ffd4fd4ca', '2022/06/05', 'USD', 70.73, 7073.0, 100.0);


