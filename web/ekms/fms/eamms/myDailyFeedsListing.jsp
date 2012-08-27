<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<x:permission permission="com.tms.fms.permission.feed.dailyFeedsListing" module="com.tms.fms.eamms.model.EammsFeedsModule" var="hvPermission"/>
<c:if test="${!hvPermission}">
    <script>
        document.location="/ekms/cmsadmin/noPermission.jsp";
    </script>
</c:if>

<x:config>
  <page name="eamms_myDailyFeeds">
    <com.tms.fms.eamms.ui.DailyFeedsListing name="listing" width="100%"/>
  </page>
</x:config>

<c:if test="${!empty param.requestId}">
    <script> 
	    window.location='feedRequisitionForm.jsp?requestId=<c:out value="${param.requestId}"/>'
	</script>
 </c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="eamms.feed.msg.dailyFeedsListing"/>
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

        <x:display name="eamms_myDailyFeeds.listing"/>
    
    </td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15">
        </td>
    </tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

