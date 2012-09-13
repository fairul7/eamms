


SELECT * FROM app_fd_eamms_error_txm 

insert into app_fd_eamms_error_txm (id,name,description,active)
select 'F','Fault','Fault','1' union all 
select 'N','Normal','Normal','1' union all 
select 'TF','TxFault','TxFault','1' union all 
select 'O','Others','Others','1' 


delete from app_fd_eamms_error_txm where id='123'