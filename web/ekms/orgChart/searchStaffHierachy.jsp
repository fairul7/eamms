<%@ page import="com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject,
				com.tms.hr.orgChart.model.OrgChartHandler,
				com.tms.hr.orgChart.ui.StaffHierachyForm,
				java.util.Collection,
				java.util.Iterator,
				kacang.Application"%>
<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="orgChart">
		<com.tms.hr.orgChart.ui.SearchHierachyForm name="searchstaffForm"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${!empty param.userId}">
		<x:set name="orgChart.searchstaffForm" property="userId" value="${param.userId }" />
	</c:when>
	<c:otherwise>
		<x:set name="orgChart.searchstaffForm" property="userId" value="" />
	</c:otherwise>
</c:choose>

<c-rt:set var="forward_saved" value="<%=StaffHierachyForm.FORWARD_SAVED%>"/>
<c-rt:set var="forward_exist" value="<%=StaffHierachyForm.FORWARD_EXIST%>"/>
<c-rt:set var="forward_updated" value="<%=StaffHierachyForm.FORWARD_UPDATED%>"/>
<c-rt:set var="forward_recovered" value="<%=StaffHierachyForm.FORWARD_RECOVERED%>"/>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="searchHierachy.jsp"/>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<table cellpadding="0" cellspacing="0" width="100%">
	<tr valign="MIDDLE">
		<td height="22" bgcolor="#003366" class="contentTitleFont">
        <b>
            <font color="#FFCF63" class="contentTitleFont">
            &nbsp;
            <c:choose>
            <c:when test="${!empty param.userId}">
                <fmt:message key='orgChart.hierachy.label.staffHierachy'/>
            </c:when>
			</c:choose>
			</font>
		</b>
		</td>
		<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  	</tr>
  	
  	<tr>  		
  		<td class="contentBgColor">
  			<span id="hierachyInitializationMsg" style="margin:5px; font-weight:bold; display:none;"></span>
  		</td>
  		
  	</tr>
  	
	<tr>
        <td>
            <x:display name="orgChart.searchstaffForm"/>
        </td>
    </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>