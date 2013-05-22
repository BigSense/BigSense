CREATE USER db_bigsense_ddl CREATEDB WITH PASSWORD 'insertpassowrd';
CREATE USER db_bigsense WITH PASSWORD 'inserpassword'

CREATE DATABASE BigSennse
  WITH OWNER = db_bigsense_ddl
       ENCODING = 'UTF8'

GRANT SELECT, UPDATE, INSERT ON DATABASE BigSense TO db_bigsense;

USE BigSense;
CREATE TABLE ddl_info( id INT auto_increment PRIMARY KEY, version INT, installed TIMESTAMP );