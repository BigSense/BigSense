CREATE USER db_bigsense_ddl CREATEDB PASSWORD 'bigsenseDDL';
CREATE USER db_bigsense PASSWORD 'bigsense';

CREATE DATABASE bigsense
  WITH OWNER = db_bigsense_ddl
       ENCODING = 'UTF8';


GRANT ALL ON DATABASE bigsense to db_bigsense;


\connect bigsense;

CREATE EXTENSION postgis;
CREATE TABLE ddl_info( id SERIAL, version INT, installed TIMESTAMP );

GRANT SELECT,INSERT,UPDATE,DELETE ON ddl_info TO db_bigsense_ddl;
GRANT USAGE ON SEQUENCE ddl_info_id_seq TO db_bigsense_ddl;

