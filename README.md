# BigSense

Full Documentation: [BigSense.io](http://bigsense.io)

<img align="right" width="200px" src="http://bigsense.github.io/bigsense-logo-only-cropped.png" alt="BigSense Logo">

BigSense is a web services designed to ingest and aggregate data from sensor networks. It can be used with its companion client [LtSense](https://github.com/bigsense/ltsense) to gather and store data from temperature, humidity and other environmental sensors.

## Installation / Quick Start Guide

### Database

You'll need to setup a database. BigSense supports most recently versions of mysql, postgres and mssql. Refer to your Linux distributions documentation for creating a database and create a database, user and password to be used later. Alternatively, you can run both your database and BigSense from a docker container which is covered below.

### Debian, Ubuntu, Devuan, etc.

Start by importing the BigSense repository key

```
curl https://repo.bigsense.io/bigsense.io.key | sudo apt-key add -
```

Then add the appropriate repository based on the init system used in your distribution:

```
# Upstart (Ubuntu 14.04 Trusty, Ubuntu 14.10 Utopic, etc.)

sudo add-apt-repository 'deb https://repo.bigsense.io/debs upstart nightly'

# systemv (Debian 7 Wheezy, Devuan, etc.)

sudo add-apt-repository 'deb https://repo.bigsense.io/debs systemv nightly'

# systemd (Debian 8+, Ubuntu 15+, most current distributions)

sudo add-apt-repository 'deb https://repo.bigsense.io/debs systemd nightly'

```

Then install OpenJDK and BigSense:

```
sudo apt-get update
sudo apt-get install openjdk-7-jre-headless bigsense
```

### openSUSE, CentOS, and other RPM based distributions

Start by importing the BigSense repository key

```
sudo curl https://repo.bigsense.io/bigsense.io.key --create-dirs -o /etc/pki/rpm-gpg/RPM-GPG-KEY-BigSense
sudo rpm --import /etc/pki/rpm-gpg/RPM-GPG-KEY-BigSense
```

Next, create the *BigSense.repo* file

* CentOS: `/etc/yum.repos.d/BigSense.repo`
* OpenSuse: `/etc/zypp/repos.d/BigSense.repo`

```
[BigSense]
name=BigSense Repository for RPM
baseurl=https://repo.bigsense.io/rpms/stable
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-BigSense
```

Then install OpenJDK and BigSense:

```
# CentOS

sudo yum install java-1.8.0-openjdk-headless bigsense

# OpenSUSE

sudo zypper install java-1_8_0-openjdk-headless bigsense

```

### Docker

You'll want to pair BigSense with a database container. The following commands will create a network, mysql container and BigSense container.

```
docker network create -d bridge bigsense

docker run -d --name bigsense-mysql --network bigsense \
  -v bigsense-mysql-data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=mysq1adm1n \
  -e MYSQL_DATABASE=bigsense mysql:5

docker run -d --name bigsense-web --network bigsense -p 8080:8080 \
   -e DB_HOSTNAME=bigsense-mysql \
   -e DB_PORT=3306 \
   -e DB_DATABASE=bigsense \
   -e DB_USER=bigsense \
   -e DB_PASS=s0mePa33w0rd \
   -e DBO_USER=root \
   -e DBO_PASS=mysq1adm1n \
   -e SECURITY_MANAGER=Disabled \
   -e DBMS=mysql \
   -e HTTP_PORT=8080 bigsense/bigsense:0.4.0

```

### Docker Environment Variables

| DB_HOSTNAME | hostname or ip address for database server |
| DB_DATABASE | database name |
| DB_PORT | database port |
| DB_USER | user for non-DDL operations |
| DB_PASS | password for standard db user |
| DBO_USER | user for DDL/schema operations |
| DBO_PASS | password for dbo user |
| SECURITY_MANAGER | Can be Signature or Disabled |
| DBMS | Can be pgsql, mysql or mssql for Postgres, MySQL and MS Sql respectively |
| HTTP_PORT | post for BigSense to listen on for http requests |


## Configuration (non-docker)

Create `/etc/bigsense/bigsense.conf` with the following:

```
dbms=pgsql
dbHostname=localhost
dbPort=3456
dbDatabase=bigsense
dbUser=db_bigsense
dbPass=bigsense
dboUser=db_bigsense_ddl
dboPass=bigsenseDDL
securityManager=Disabled
httpPort=8080
server=tomcat
```

* _dbms_ can be either pgsql, mysql or mssql for Postgres, MySQL and Microsoft SQL respectively
* _dbHostname_, _dbPort_ and _dbDatabase_ are database connection parameters
* _dbUser/dbPass_ and _dboUser/dboPass_ specify the two database users
    * The DBO user can update the schema and is only used upon startup of BigSense
    * The non-DBO user is used for all further connections when reading from/writing to the database
* _securityManager_ can be used to verify all incoming sensor data. Currently can be set to Disabled or Signature.
* _httpPort_ the port the web service will run on
* _server_ can be either jetty or tomcat

Once configured, restart the service using `systemctl restart bigsense.service` on a systemd distribution or `/etc/init.d/bigsense restart` on others.

## Sending Sensor Data

You can test out sending data to the webservice using curl:

```
curl -X POST localhost:8080/Sensor.sense.json --data '[
  {"id": "MyRelayID", "timestamp": "1549670382708",
  "gps": {"location": {"longitude": 13.0, "latitude": -12.0, "altitude": 5.0}},
  "sensors": [{"id": "Temp001", "type": "Temperature", "units": "C", "data": "34"},
  {"id": "Humidity001", "type": "Humdity", "units": "%", "data": "30"}]}]'

```

## Pulling Data

You can then pull that data using the same webservice API:

`curl -X POSTlocalhost:8080/Query/Latest/100.txt`

