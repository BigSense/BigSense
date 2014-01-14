CREATE DATABASE BigSense;
GRANT ALL ON BigSense.* TO 'db_bigsense_ddl'@'localhost' IDENTIFIED BY 'insertpassword';
GRANT SELECT,INSERT,UPDATE,DELETE,EXECUTE ON BigSense.* TO 'db_bigsense'@'localhost' IDENTIFIED BY 'insertpassword';

USE BigSense;
CREATE TABLE ddl_info( id INT auto_increment PRIMARY KEY, version INT, installed TIMESTAMP );

delimiter $$
drop function if exists `isnumeric` $$
create function `isnumeric` (s varchar(255)) returns int deterministic
begin
set @match =
   '^(([0-9+-.$]{1})|([+-]?[$]?[0-9]*(([.]{1}[0-9]*)|([.]?[0-9]+))))$';

return if(s regexp @match, 1, 0);
end $$

delimiter ;

GRANT EXECUTE ON PROCEDURE BigSense.isnumeric TO 'db_bigsense'@'localhost'