CREATE DATABASE IF NOT EXISTS moodssalon

USE moodssalon

CREATE TABLE user(
    userId VARCHAR(15) PRIMARY KEY,
    fullName VARCHAR(50) NOT NULL,
    userName VARCHAR(30) NOT NULL,
    type VARCHAR(20) NOT NULL,
    gmail VARCHAR(30) NOT NULL ,
    password VARCHAR(20) NOT NULL,
    image longblob null,
    receipt_folder varchar(300) null
)

CREATE TABLE customer(
    customerId VARCHAR(15) PRIMARY KEY ,
    name VARCHAR(50) NOT NULL ,
    gmail VARCHAR(30) UNIQUE NOT NULL ,
    contact VARCHAR(15) NOT NULL ,
    address VARCHAR(100) NOT NULL
)

CREATE TABLE appointment(
    appointmentId VARCHAR(15) PRIMARY KEY ,
    date DATE NOT NULL ,
    time VARCHAR(15) NOT NULL ,
    status VARCHAR(20) NOT NULL ,
    customerId VARCHAR(15) NOT NULL ,
    CONSTRAINT FOREIGN KEY (customerId) REFERENCES customer(customerId) on update cascade on delete cascade
)

CREATE TABLE payment(
    paymentId VARCHAR(15) PRIMARY KEY ,
    amount DECIMAL(10,2) NOT NULL ,
    date DATE NOT NULL ,
    userId VARCHAR(15) NOT NULL ,
    appointmentId VARCHAR(15) NOT NULL ,
    CONSTRAINT FOREIGN KEY (userId) REFERENCES user(userId),
    CONSTRAINT FOREIGN KEY (appointmentId) REFERENCES appointment(appointmentId) on update cascade on delete cascade
)

CREATE TABLE employee(
    employeeId VARCHAR(15) PRIMARY KEY ,
    name VARCHAR(50) NOT NULL ,
    address VARCHAR(200) NOT NULL ,
    contact VARCHAR(15) NOT NULL ,
    jobRole VARCHAR(30) NOT NULL ,
    salary DECIMAL(10,2) NOT NULL
)

CREATE TABLE employee_details(
    appointmentId VARCHAR(15) ,
    employeeId VARCHAR(15) ,
    CONSTRAINT FOREIGN KEY (appointmentId) REFERENCES appointment(appointmentId) on update cascade on delete cascade,
    CONSTRAINT FOREIGN KEY (employeeId) REFERENCES employee(employeeId) on update cascade on delete cascade,
    CONSTRAINT PRIMARY KEY (appointmentId,employeeId)
)

CREATE TABLE attendance(
    attendanceId VARCHAR(15) PRIMARY KEY ,
    date DATE NOT NULL ,
    status VARCHAR(20) NOT NULL ,
    employeeId VARCHAR(15) NOT NULL ,
    CONSTRAINT FOREIGN KEY (employeeId) REFERENCES employee(employeeId) on update cascade on delete cascade
)

CREATE TABLE service(
    serviceId VARCHAR(15) PRIMARY KEY ,
    description VARCHAR(100) NOT NULL ,
    price DECIMAL(10,2) NOT NULL
)

CREATE TABLE service_details(
    appointmentId VARCHAR(15) ,
    serviceId VARCHAR(15) ,
    CONSTRAINT FOREIGN KEY (appointmentId) REFERENCES appointment(appointmentId) on update cascade on delete cascade,
    CONSTRAINT FOREIGN KEY (serviceId) REFERENCES service(serviceId) on update cascade on delete cascade,
    CONSTRAINT PRIMARY KEY (appointmentId,serviceId)
)

CREATE TABLE supplier(
    supplierId VARCHAR(15) PRIMARY KEY ,
    name VARCHAR(100) NOT NULL ,
    contact VARCHAR(15) NOT NULL ,
    address VARCHAR(200) NOT NULL
)

CREATE TABLE inventory(
    inventoryId VARCHAR(15) PRIMARY KEY ,
    name VARCHAR(100) NOT NULL ,
    qtyOnHand INT NOT NULL ,
    unitPrice DECIMAL(10,2) NOT NULL
)

CREATE TABLE inventory_details(
    appointmentId VARCHAR(15) ,
    inventoryId VARCHAR(15) ,
    usedQty INT NOT NULL ,
    CONSTRAINT FOREIGN KEY (appointmentId) REFERENCES appointment(appointmentId) on update cascade on delete cascade,
    CONSTRAINT FOREIGN KEY (inventoryId) REFERENCES inventory(inventoryId) on update cascade on delete cascade,
    CONSTRAINT PRIMARY KEY (appointmentId,inventoryId)
)

CREATE TABLE inventory_Order(
    orderId VARCHAR(15) PRIMARY KEY ,
    date DATE NOT NULL ,
    supplierId VARCHAR(15) NOT NULL ,
    userId VARCHAR(15) NOT NULL ,
    CONSTRAINT FOREIGN KEY (supplierId) REFERENCES supplier(supplierId) on update cascade on delete cascade,
    CONSTRAINT FOREIGN KEY (userId) REFERENCES user(userId) on update cascade on delete cascade
)

CREATE TABLE inventory_order_details(
    orderId VARCHAR(15) ,
    inventoryId VARCHAR(15) ,
    usedQty INT NOT NULL ,
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES inventory_order(orderId) on update cascade on delete cascade,
    CONSTRAINT FOREIGN KEY (inventoryId) REFERENCES inventory(inventoryId) on update cascade on delete cascade,
    CONSTRAINT PRIMARY KEY (orderId,inventoryId)
)






