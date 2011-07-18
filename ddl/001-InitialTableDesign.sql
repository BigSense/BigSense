CREATE TABLE relays (
  id
  unique_id
)

CREATE TABLE sensors (
  id
  unique_id
  relay_id
  sensor_type
  units
)

CREATE TABLE sensor_types (
  id
  name
)

CREATE TABLE data_package (
  id
  timestamp
  relay_id
)

CREATE TABLE sensor_data (
  id
  package_id
  data
)

CREATE TABLE relay_meta_data (
  relay_id
  var
  value
)

CREATE TABLE sensor_meta_data (
  sensor_id
  var
  value
)
