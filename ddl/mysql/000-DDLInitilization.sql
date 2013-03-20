CREATE DATABASE BigSense;
GRANT ALL ON BigSense.* TO 'db_bigsense_ddl'@'localhost' IDENTIFIED BY 'insertpassword';
GRANT SELECT,INSERT,UPDATE,DELETE ON BigSense.* TO 'db_bigsense'@'localhost' IDENTIFIED BY 'insertpassword';

USE BigSense;
CREATE TABLE ddl_info( id INT auto_increment PRIMARY KEY, version INT, installed TIMESTAMP );
