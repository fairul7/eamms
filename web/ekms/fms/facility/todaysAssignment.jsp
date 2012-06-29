<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_todaysAssignmentListingPage">
    <!--  <com.tms.fms.facility.ui.TodaysAssignmentTable name="listing" width="100%"/>-->
    <com.tms.fms.facility.ui.TodaysAssignmentListingTable name="listing" width="100%"/> 
  </page>
</x:config>


<c:if test="${!empty param.groupId}">
  <c:redirect url="checkOutDetails.jsp?groupId=${param.groupId}"/>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.menu.todaysAssignment"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
    	<td bgcolor="#EFEFEF" class="contentBgColor">
    		<table>
    			<tr>
    				<td><fmt:message key='fms.facility.note'/>:</td>
    				<td align="left"><img align="center" src="/ekms/images/icn_check.png"> <fmt:message key='fms.facility.assignmentUtilized'/></td>
    			</tr>
    			<tr>
    				<td>&nbsp;</td>
    				<td align="left"><img align="center" src="/ekms/images/icn_delete.png"> <fmt:message key='fms.facility.assignmentNotUtilized'/></td>
    			</tr>
    		</table>
		</td>
		<td bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_todaysAssignmentListingPage.listing"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>