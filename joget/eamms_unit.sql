
CREATE TABLE app_fd_eamms_unit_owner(
id VARCHAR(30) NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL,
description VARCHAR(255) NULL,
active CHAR(1) NOT NULL,
createdBy VARCHAR(250) NULL,
dateCreated DATETIME
);

INSERT INTO app_fd_eamms_unit_owner(
id, name, description, active, createdBy, dateCreated)
VALUES('1', 'Broadcast Operation (BO)', '', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

INSERT INTO app_fd_eamms_unit_owner(
id, name, description, active, createdBy, dateCreated)
VALUES('2', 'Broadcast System Unit (BSU)', '', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

INSERT INTO app_fd_eamms_unit_owner(
id, name, description, active, createdBy, dateCreated)
VALUES('3', 'Transmission Development (TRD)', '', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

INSERT INTO app_fd_eamms_unit_owner(
id, name, description, active, createdBy, dateCreated)
VALUES('4', 'Mechanical & Electrical (M&E)', '', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

INSERT INTO app_fd_eamms_unit_owner(
id, name, description, active, createdBy, dateCreated)
VALUES('5', 'Light & Sound', '', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

INSERT INTO app_fd_eamms_unit_owner(
id, name, description, active, createdBy, dateCreated)
VALUES('6', 'Outside Broadcasting (OB)', '', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

INSERT INTO app_fd_eamms_unit_owner(
id, name, description, active, createdBy, dateCreated)
VALUES('7', 'Camera', '', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

INSERT INTO app_fd_eamms_unit_owner(
id, name, description, active, createdBy, dateCreated)
VALUES('8', 'Engineering Admin & Facilities', '', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());

INSERT INTO app_fd_eamms_unit_owner(
id, name, description, active, createdBy, dateCreated)
VALUES('9', 'Post Production & Project', '', '1', 'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', GETDATE());
