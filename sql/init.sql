-- TODO remade this to PostgreSQL

use truckdispatch;

drop table if exists assignments;
drop table if exists shipments;
drop table if exists trucks;

create table trucks
(
    name     varchar(50) not null,
    capacity float       not null check (capacity > 0),
    primary key (name)
);

create table shipments
(
    name   varchar(50) not null,
    weight float       not null check (weight > 0),
    primary key (name)
);

create table assignments
(
    truck_name    varchar(50) not null,
    shipment_name varchar(50) not null,
    primary key (truck_name, shipment_name),
    constraint fk_truck foreign key (truck_name) references trucks (name),
    constraint fk_shipment foreign key (shipment_name) references shipments (name)
);