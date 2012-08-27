<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<x:permission permission="com.tms.fms.permission.feed.dailyFeedsLog" module="com.tms.fms.eamms.model.EammsFeedsModule" var="hvPermission"/>
<c:if test="${!hvPermission}">
    <script>
        document.location="/ekms/cmsadmin/noPermission.jsp";
    </script>
</c:if>

<x:config>
  <page name="eamms_dailyFeedsLog">
    <com.tms.fms.eamms.ui.DailyFeedsLogListing name="listing" width="100%"/>
  </page>
</x:config>

<c:if test="${!empty param.id}">
    <script> 
        window.location='dailyFeedsLogEdit.jsp?reset=1&id=<c:out value="${param.id}"/>';
    </script>
</c:if>
 
<c:if test="${forward.name == 'addNewFeedsLog'}">
    <script> 
        window.location='dailyFeedsLogAdd.jsp?reset=1';
    </script>
</c:if>
 
<c:if test="${forward.name == 'exportToCsv'}">
    <script> 
        window.open('/feedsRequisition/exportFeedsLogToCSV', 'expFeedsLogOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=50, height=50');
    </script>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="eamms.feed.msg.dailyFeedsLog"/>
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

        <x:display name="eamms_dailyFeedsLog.listing"/>
    
    </td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15">
        </td>
    </tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

