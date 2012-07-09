alter table app_fd_eamms_asset_hw alter column c_purchaseDate datetime null;
alter table app_fd_eamms_asset_hw alter column c_purchasePrice decimal(10,2)  null;
-------------- can not change -------------------------------

alter table app_fd_eamms_asset_hw alter column c_hwId varchar(30) not null;
alter table app_fd_eamms_asset_hw alter column c_barcode varchar(30) not null;
alter table app_fd_eamms_asset_hw alter column c_serialNo varchar(30)  null;
alter table app_fd_eamms_asset_hw alter column c_abbreviation varchar(30) null;
alter table app_fd_eamms_asset_hw alter column c_description varchar(255)  null;
alter table app_fd_eamms_asset_hw alter column c_make varchar(100)  null;
alter table app_fd_eamms_asset_hw alter column c_model varchar(100)  null;
alter table app_fd_eamms_asset_hw alter column c_supplierId varchar(10) not null;
alter table app_fd_eamms_asset_hw alter column c_po varchar(30)  null;
alter table app_fd_eamms_asset_hw alter column c_relatedHwId varchar(255) null;
alter table app_fd_eamms_asset_hw alter column c_category varchar(30) not null;
alter table app_fd_eamms_asset_hw alter column c_facilityId varchar(30) not null;
alter table app_fd_eamms_asset_hw alter column c_condition varchar(30) not null;
alter table app_fd_eamms_asset_hw alter column c_status varchar(30) not null;
alter table app_fd_eamms_asset_hw alter column c_attachment varchar(100)  null;
alter table app_fd_eamms_asset_hw alter column c_notes varchar(255)  null;
alter table app_fd_eamms_asset_hw alter column c_forRent varchar(3) not null;
alter table app_fd_eamms_asset_hw alter column c_createdBy varchar(250) null;


alter table app_fd_eamms_asset_hw_transfer alter column c_hwId varchar(255) not null;
alter table app_fd_eamms_asset_hw_transfer alter column c_createdBy varchar(250) null;
alter table app_fd_eamms_asset_hw_transfer alter column c_fromFacilityId varchar(10) not null;
alter table app_fd_eamms_asset_hw_transfer alter column c_toFacilityId varchar(30) not null;
alter table app_fd_eamms_asset_hw_transfer alter column c_reason varchar(255)  null;


