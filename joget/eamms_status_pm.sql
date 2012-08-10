

CREATE TABLE app_fd_eamms_status_pm  
(id VARCHAR(30) NOT NULL,
name VARCHAR(50) NOT NULL,
description VARCHAR(255),
active CHAR(1) NOT NULL,
createdBy VARCHAR(250),
dateCreated VARCHAR(30),
PRIMARY KEY (id)
-- ,FOREIGN KEY (createdBy) references fms_user_details(userId),
)

INSERT INTO app_fd_eamms_status_pm (id, name, active) 
SELECT 'N' ,'New','1' UNION ALL 
SELECT 'P' ,'In Progress','1' UNION ALL 
SELECT 'O' ,'Overdue','1' UNION ALL 
SELECT 'C' ,'Completed','1' UNION ALL 
SELECT 'E' ,'Expired','1'
SELECT 'NC' ,'Not Completed','1'


select * from app_fd_eamms_status_pm