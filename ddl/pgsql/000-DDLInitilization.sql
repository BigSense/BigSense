CREATE USER db_bigsense_ddl CREATEDB PASSWORD 'insertpassowrd';
CREATE USER db_bigsense PASSWORD 'inserpassword';

CREATE DATABASE bigsense
  WITH OWNER = db_bigsense_ddl
       ENCODING = 'UTF8';


GRANT ALL PRIVILEGES ON DATABASE bigsense to db_bigsense;


\connect bigsense;

CREATE EXTENSION postgis;
CREATE TABLE ddl_info( id SERIAL, version INT, installed TIMESTAMP );
