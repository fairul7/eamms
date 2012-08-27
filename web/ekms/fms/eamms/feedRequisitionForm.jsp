<%@ page import="java.util.Collection,
                 java.util.ArrayList,
                 kacang.services.security.SecurityService,
                 kacang.services.security.Group,
                 kacang.util.Log,
                 com.tms.fms.eamms.ui.FeedRequisitionForm"%>
                 
<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<x:permission permission="com.tms.fms.permission.feed.dailyFeedsListing" module="com.tms.fms.eamms.model.EammsFeedsModule" var="hvPermission"/>
<c:if test="${!hvPermission}">
    <script>
        document.location="/ekms/cmsadmin/noPermission.jsp";
    </script>
</c:if>

<%
	String c_view = FeedRequisitionForm.ENGINEERING_COORDINATOR_VIEW;

	SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
	String currentUserId =  Application.getInstance().getCurrentUser().getId();
	
	Collection<Group> groups = null;
	try
	{
	    groups = ss.getUserGroups(currentUserId);
	}
	catch(Exception e)
	{
	    Log.getLog(getClass()).error(e, e);
	}
	
	ArrayList<String> arr = new ArrayList();
	if(groups != null && !groups.isEmpty())
	{
	    for(Group grp : groups)
	    {
	        String groupId = grp.getId();
	        if(Application.getInstance().getProperty("UnitHeadEngineering").equals(groupId))
	        {
	        	c_view = FeedRequisitionForm.UNIT_HEAD_ENG_VIEW;
                break;
	        }
	        else if(Application.getInstance().getProperty("UnitHeadNetwork").equals(groupId))
	        {
	        	c_view = FeedRequisitionForm.UNIT_HEAD_NET_VEIW;
                break;
	        }
	        else if(Application.getInstance().getProperty("NetworkEngineer").equals(groupId))
	        {
	        	c_view = FeedRequisitionForm.NETWORK_ENGINEER_VIEW;
	        	break;
	        }
	    }
	}
%>

<x:config>
  <page name="eamms_feedRequisition">
    <com.tms.fms.eamms.ui.FeedRequisitionForm name="form" width="100%"/>
  </page>
</x:config>

<c:if test="${!empty param.requestId}">
   <x:set name="eamms_feedRequisition.form" property="requestId" value="${param.requestId}"/>
</c:if>
<x:set name="eamms_feedRequisition.form" property="mode" value="<%=c_view%>"/>

<c:if test="${forward.name=='submitted'}" >
    <script>
        alert('<fmt:message key="eamms.feed.list.alert.msg.submitted"/>');
        document.location='myDailyFeedsListing.jsp'
    </script>
</c:if>

<c:if test="${forward.name=='verified'}" >
    <script>
        //alert('<fmt:message key="eamms.feed.list.alert.msg.verified"/>');
        document.location='myDailyFeedsListing.jsp'
    </script>
</c:if>

<c:if test="${forward.name=='approved'}" >
    <script>
        //alert('<fmt:message key="eamms.feed.list.alert.msg.approved"/>');
        document.location='myDailyFeedsListing.jsp'
    </script>
</c:if>

<c:if test="${forward.name=='rejected'}" >
    <script>
        //alert('<fmt:message key="eamms.feed.list.alert.msg.rejected"/>');
        document.location='myDailyFeedsListing.jsp'
    </script>
</c:if>

<c:if test="${forward.name=='DELETED'}" >
    <script>
        alert('<fmt:message key="eamms.feed.list.alert.msg.deleted"/>');
        document.location='feedRequisitionForm.jsp?requestId=${widgets['eamms_feedRequisition.form'].requestId}'
    </script>
</c:if>

<c:if test="${forward.name=='cancel_form_action'}" >
    <script>
        document.location='myDailyFeedsListing.jsp'
    </script>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="eamms.feed.list.msg.incomingVisualFeedReqForm"/>
            </font></b>
        </td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">
        </td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="eamms_feedRequisition.form"/>
    
    </td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15">
        </td>
    </tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

