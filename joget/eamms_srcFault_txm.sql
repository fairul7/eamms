


SELECT * FROM app_fd_eamms_srcFault_txm


insert into app_fd_eamms_srcFault_txm (id,name,description,active)
select 'TM','TM','TM','1' union all 
select 'Celcom','Celcom','Celcom','1' union all 
select 'Conty','Conty','Conty','1' union all 
select 'Studio','Studio','Studio','1' union all 
select 'OB','OB','StOBudio','1' union all 
select 'MCAR','MCAR','MCAR','1' union all 
select 'STL','STL','STL','1' 