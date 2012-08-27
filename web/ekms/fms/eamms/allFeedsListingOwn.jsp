<%@ page import="com.tms.fms.eamms.ui.DailyFeedsListingWithExport"%>
<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<x:permission permission="com.tms.fms.permission.feed.allFeedsListingOwn" module="com.tms.fms.eamms.model.EammsFeedsModule" var="hvPermission"/>
<c:if test="${!hvPermission}">
    <script>
        document.location="/ekms/cmsadmin/noPermission.jsp";
    </script>
</c:if>

<x:config>
  <page name="eamms_allOwnFeeds">
    <com.tms.fms.eamms.ui.DailyFeedsListingWithExport name="listing" width="100%"/>
  </page>
</x:config>

<x:set name="eamms_allOwnFeeds.listing" property="mode" value="<%=DailyFeedsListingWithExport.VIEW_ALL_OWN%>"/>

<c:if test="${!empty param.requestId}">
    <script> 
        window.location='feedRequisitionForm_viewOnly.jsp?requestId=<c:out value="${param.requestId}"/>'
    </script>
</c:if>
 
<c:if test="${forward.name == 'no_key_selected'}">
    <script> 
        alert('<fmt:message key="eamms.feed.list.alert.msg.no_key_selected"/>');
    </script>
</c:if>
 
<c:if test="${forward.name == 'exportToExcel'}">
    <script> 
        window.open('/feedsRequisition/exportFeedsToExcel', 'expFeedsOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=50, height=50');
    </script>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="eamms.feed.msg.allFeedsListingOwn"/>
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

        <x:display name="eamms_allOwnFeeds.listing"/>
    
    </td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15">
        </td>
    </tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

