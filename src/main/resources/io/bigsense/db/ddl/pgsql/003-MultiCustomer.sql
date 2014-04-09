CREATE TABLE sites (
   id BIGINT PRIMARY KEY,
   name varchar(255),
   data_keys_pem TEXT
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sites TO db_bigsense;


CREATE TABLE site_relays (
  site_id BIGINT,
  relay_unique_id VARCHAR(36)
);


GRANT SELECT,INSERT,UPDATE,DELETE ON site_relays TO db_bigsense;



