USE [jwdb]
GO
/****** Object:  Table [dbo].[app_fd_eamms_facility]    Script Date: 08/14/2012 15:51:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[app_fd_eamms_facility](
	[id] [varchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	[dateCreated] [datetime] NULL,
	[dateModified] [datetime] NULL,
	[c_facilityName] [varchar](100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	[c_location] [varchar](10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	[c_description] [ntext] COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	[c_active] [char](1) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	[c_facilityId] [varchar](10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	[c_createdBy] [varchar](250) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
 CONSTRAINT [PK__app_fd_eamms_fac__6BAEFA67] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF

INSERT INTO app_fd_eamms_facility (id, c_facilityName, c_location, c_active, c_facilityId) 
SELECT 'fac1' ,'Facility 1','penang','1','1' UNION ALL 
SELECT 'fac2' ,'Facility 2','selangor','1','2' UNION ALL 
SELECT 'fac3' ,'Facility 3','kedah','1','3'