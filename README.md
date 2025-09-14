## Trucks Distribution project, based on Guice, Mybatis and Netty

### Purpose
This project supports the simplest logistic system, which accepts shipments and trucks
and finds the heaviest shipment for new truck or the lightest truck for new shipment.
I have implemented this task using "non-standard" technologies:
* Netty web server (no servlets support)
* Guice dependency injection framework
* MyBatis ORM

Additional technologies:
* Liquibase
* JaCoCo