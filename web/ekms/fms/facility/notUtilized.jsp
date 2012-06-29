<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="assignmentNotUtilized">
    <com.tms.fms.engineering.ui.NotUtilizedForm name="form" width="100%"/>
  </page>
</x:config>

<c:if test="${not empty(param.requestId)}">
	<x:set name="assignmentNotUtilized.form" property="requestId" value="${param.requestId}"/>
</c:if>
<c:set var="requestId" value="${widgets['assignmentNotUtilized.form'].requestId}" />


<c:if test="${forward.name=='save'}" >
    <script>
    alert('Assignment is updated!');       
    window.opener.location = 'requestDetails.jsp?page=all&cn=fms_AssignmentListingPage.listing&et=sel&requestId=<c:out value="${requestId}" />'
    window.close();
    </script>
</c:if>

<c:if test="${forward.name=='error'}" >
    <script>
    	alert('Fail to update!');
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <script>
        window.close();
    </script>
</c:if>

<%@include file="/ekms/includes/linkCSS.jsp" %>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
        	<fmt:message key="fms.facility.notUtilizedReason"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="assignmentNotUtilized.form"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>