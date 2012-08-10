


CREATE TABLE app_fd_eamms_pm_request_dates 
(id Varchar(255) NOT NULL,
pmRequestId Varchar(30) NOT NULL, --FK to app_fd_eamms_pm_request.pmRequestId,
pmDate Varchar(30) NOT NULL,
serviceStatus Varchar(30) NOT NULL,
engineerAssigned Varchar(100), 
createdBy Varchar(250),-- FK to fms_user_details.userId,
dateCreated Varchar(30),
PRIMARY KEY (id));





