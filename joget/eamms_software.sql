--CREATE REFERENCE TABLE FOR SOFTWARE TABLE
CREATE TABLE app_fd_eamms_category_sw(
id VARCHAR(30) NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL,
description VARCHAR(255) NULL,
active CHAR(1) NOT NULL,
createdBy VARCHAR(250) NULL,
dateCreated DATETIME
);

INSERT INTO app_fd_eamms_category_sw(
id, name, description, active, createdBy, dateCreated)
VALUES('1', 'Operating System', 'Operating System', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());
INSERT INTO app_fd_eamms_category_sw(
id, name, description, active, createdBy, dateCreated)
VALUES('2', 'Utility', 'Utility', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());
INSERT INTO app_fd_eamms_category_sw(
id, name, description, active, createdBy, dateCreated)
VALUES('3', 'Video Editing', 'Video Editing', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());
INSERT INTO app_fd_eamms_category_sw(
id, name, description, active, createdBy, dateCreated)
VALUES('4', 'Graphics', 'Graphics', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());
INSERT INTO app_fd_eamms_category_sw(
id, name, description, active, createdBy, dateCreated)
VALUES('5', 'Broadcast System', 'Broadcast System', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

CREATE TABLE app_fd_eamms_status_sw(
id VARCHAR(30) NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL,
description VARCHAR(255) NULL,
active CHAR(1) NOT NULL,
createdBy VARCHAR(250) NULL,
dateCreated DATETIME);

INSERT INTO app_fd_eamms_status_sw(
id, name, description, active, createdBy, dateCreated)
VALUES('1', 'In Use', 'In Use', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());
INSERT INTO app_fd_eamms_status_sw(
id, name, description, active, createdBy, dateCreated)
VALUES('2', 'Inactive', 'Inactive', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());
INSERT INTO app_fd_eamms_status_sw(
id, name, description, active, createdBy, dateCreated)
VALUES('3', 'Expired', 'Expired', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

--ALTER TABLE FROM TEXT TO CORRESPONDED DATA TYPE IN FUNCTIONAL SPEC
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_licenseExpiryDate VARCHAR(20);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_licenseExpiryDate VARCHAR(10);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_supplierId VARCHAR(10);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_status VARCHAR(20);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_actKey VARCHAR(20);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_attachment VARCHAR(100);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_version VARCHAR(20);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_serialNo VARCHAR(30);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_author VARCHAR(100);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_category VARCHAR(30);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_relatedHwId VARCHAR(30);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_description VARCHAR(255);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_purchaseDate VARCHAR(20);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_purchaseDate VARCHAR(10);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_swId VARCHAR(30);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_po VARCHAR(100);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_notes VARCHAR(255);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_license VARCHAR(40);
ALTER TABLE app_fd_eamms_asset_sw DROP COLUMN c_purchasePrice;
ALTER TABLE app_fd_eamms_asset_sw ADD c_purchasePrice NUMERIC(10,2);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_purchasePrice VARCHAR(13);
ALTER TABLE app_fd_eamms_asset_sw ALTER COLUMN c_unitId VARCHAR(40);


--CREATE VIEW FOR fms_user_details FROM FMS SECURITY TABLE DB
CREATE VIEW fms_user_details
AS
SELECT * FROM fmsdb.dbo.security_user;

--CREATE SECURITY CONSTRAINT
--TO BE DONE LATER ONCE ALL THE TABLES ARE SET