--liquibase formatted sql

--changeset opavloshchuk:1

DROP TABLE IF EXISTS trucks;
CREATE TABLE trucks
(
    name     VARCHAR(50) NOT NULL PRIMARY KEY,
    capacity FLOAT       NOT NULL CHECK (capacity > 0)
);

DROP TABLE IF EXISTS shipments;
CREATE TABLE shipments
(
    name   VARCHAR(50) NOT NULL PRIMARY KEY,
    weight FLOAT       NOT NULL CHECK (weight > 0)
);

DROP TABLE IF EXISTS assignments;
CREATE TABLE assignments
(
    truck_name    VARCHAR(50) NOT NULL,
    shipment_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (truck_name, shipment_name),
    CONSTRAINT fk_truck FOREIGN KEY (truck_name) REFERENCES trucks (name),
    CONSTRAINT fk_shipment FOREIGN KEY (shipment_name) REFERENCES shipments (name)
);