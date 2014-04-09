CREATE TABLE sites (
   id BIGINT PRIMARY KEY,
   name varchar(255),
   data_keys_pem TEXT
);
CREATE TABLE site_relays (
  site_id BIGINT,
  relay_unique_id VARCHAR(36)
);



