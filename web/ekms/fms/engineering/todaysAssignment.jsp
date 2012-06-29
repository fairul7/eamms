<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_todaysAssignmentHOUListingPage">
    <com.tms.fms.engineering.ui.TodaysAssignmentHOUTable name="listing" width="100%"/>
  </page>
</x:config>

<c:if test="${!empty param.groupId}">
	<script>	
		var nX = (screen.availWidth - 700)/2;
		var nY = (screen.availHeight - 500)/2;
		
		window.open('<c:url value="completeAssignmentHOU.jsp?page=today&groupId=${param.groupId}"/>', 'print', 'height=500, width=700, screenX='+ nX +',left='+ nX+ ',screenY='+nY+',top='+nY +', menubar=0, statusbar=0, resizeable=yes, scrollbars=yes');
	</script>		
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.menu.todaysAssignment"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_todaysAssignmentHOUListingPage.listing"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>