

CREATE TABLE app_fd_eamms_pm_request_status
(id VARCHAR(255) NOT NULL, 
pmRequestId VARCHAR(30) NOT NULL, -- constraint pmRequestId foreign key references app_fd_eamms_pm_request(c_pmRequestId),
status VARCHAR(30) NOT NULL,
createdBy VARCHAR(250),
dateCreated DATETIME,
PRIMARY KEY (id)
)