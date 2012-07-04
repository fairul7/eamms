
Steps to setup SSO in local:

1) Unzip workflowClasses.zip into FMS folder

2) Unzip plugins.zip into Joget wflow folder

3) Upload ekms-directory-plugin-0.9.jar

4) Go to Directory Manager Setting > Plugin Configuration 
	EKP JDBC Driver: SQLServerDriver
	EKP JDBC URL: jdbc:sqlserver://localhost:1433;DatabaseName=fmsdb;SelectMethod=cursor	
	Admin Permission: com.tms.workflow.WorkflowAdministrator
	User Permission: com.tms.workflow.WorkflowUser
	(see kacang-config-workflow.xml)
	
5) Run FMS tomcat - go to System Administration > System Administrator Group Permission > check the Workflow Administrator checkbox

6) EAMMS tab will appear after permission granted
