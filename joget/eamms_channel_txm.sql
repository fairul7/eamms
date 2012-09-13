

select * from app_fd_eamms_channel_txm

insert into app_fd_eamms_channel_txm (id,name,description,active) 
values (
'NTV7',
'NTV7',
'NTV7',
'1'
)

update app_fd_eamms_channel_txm set 
id = '8TV',
name = '8TV',
description = '8TV',
active = '1'
where id='TV8'