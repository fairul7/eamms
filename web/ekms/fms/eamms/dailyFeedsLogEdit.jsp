<%@ page import="com.tms.fms.eamms.ui.FeedsLogForm"%>
<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<x:permission permission="com.tms.fms.permission.feed.dailyFeedsLog" module="com.tms.fms.eamms.model.EammsFeedsModule" var="hvPermission"/>
<c:if test="${!hvPermission}">
    <script>
        document.location="/ekms/cmsadmin/noPermission.jsp";
    </script>
</c:if>

<x:config>
  <page name="eamms_editFeedLog">
    <com.tms.fms.eamms.ui.FeedsLogForm name="form" width="100%"/>
  </page>
</x:config>

<c:if test="${param.reset == '1'}" >
    <x:set name="eamms_editFeedLog.form" property="runInit" value="<%=true%>"/>
</c:if>

<c:if test="${!empty param.id}">
   <x:set name="eamms_editFeedLog.form" property="feedLogId" value="${param.id}"/>
</c:if>
<x:set name="eamms_editFeedLog.form" property="mode" value="<%=FeedsLogForm.EDIT%>"/>

<c:if test="${forward.name=='continue' || forward.name=='back'}" >
    <script>
        document.location='dailyFeedsLogEdit.jsp';
    </script>
</c:if>

<c:if test="${forward.name=='submit_continue'}" >
    <script>
        alert('<fmt:message key="eamms.feed.list.alert.msg.submitted"/>');
    </script>
</c:if>

<c:if test="${forward.name=='submitted'}" >
    <script>
        alert('<fmt:message key="eamms.feed.list.alert.msg.submitted"/>');
        document.location='dailyFeedsLogListing.jsp'
    </script>
</c:if>

<c:if test="${forward.name=='cancel_form_action'}" >
    <script>
        document.location='dailyFeedsLogListing.jsp'
    </script>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="eamms.feed.log.msg.editLog"/>
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

        <x:display name="eamms_editFeedLog.form"/>
    
    </td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15">
        </td>
    </tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

