CREATE TABLE Worker ( ID integer NOT NULL PRIMARY KEY AUTO_INCREMENT, PESEL numeric NOT NULL, WorkerType tinyint NOT NULL, Name character(40) NOT NULL, Surname character(40) NOT NULL, BusinessPhone varchar(20) NOT NULL, Salary decimal(10,2) NOT NULL )

CREATE TABLE Trader ( ID integer NOT NULL PRIMARY KEY AUTO_INCREMENT, WorkerID integer NOT NULL, Commision double NOT NULL, CommisionLimit double NOT NULL, CONSTRAINT Worker_Trader FOREIGN KEY (WorkerID) REFERENCES Worker (ID) )

CREATE TABLE Manager ( ID integer NOT NULL PRIMARY KEY AUTO_INCREMENT, WorkerID integer NOT NULL, BusinessAllowance integer NOT NULL, CostLimit integer NOT NULL, ServiceCardNumber integer NOT NULL, CONSTRAINT Worker_Manager FOREIGN KEY (WorkerID) REFERENCES Worker (ID) )