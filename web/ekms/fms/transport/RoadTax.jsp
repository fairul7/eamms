<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.ui.InsuranceForm, com.tms.fms.transport.ui.InsuranceTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="Insurance">
    	<com.tms.fms.transport.ui.InsuranceForm name="form"/>
        <com.tms.fms.transport.ui.InsuranceTable name="table" width="100%"/>
    </page>
</x:config>

<c-rt:set var="forward_succ" value="<%= InsuranceForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= InsuranceForm.FORWARD_ADD_FAIL %>"/>
<c-rt:set var="forward_edit_succ" value="<%= InsuranceForm.FORWARD_EDIT_SUCCESS %>"/>
<c-rt:set var="forward_edit_fail" value="<%= InsuranceForm.FORWARD_EDIT_FAIL %>"/>
<c-rt:set var="forward_listing_add" value="<%= InsuranceTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_delete" value="<%= InsuranceTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_delete_fail" value="<%= InsuranceTable.FORWARD_LISTING_DELETE_FAIL %>"/>

<c:choose>
  <c:when test="${not empty(param.vid)}">
  	<c:set var="vehicleID" value="${param.vid}"/>
  </c:when>
  <c:otherwise>
	<c:set var="vehicleID" value="${widgets['Insurance.form'].vehicle_num}"/>
  </c:otherwise>
</c:choose>

<x:set name="Insurance.form" property="vehicle_num" value="${vehicleID}"/>
<x:set name="Insurance.table" property="vehicle_num" value="${vehicleID}"/>

<c:set var="formAction" value="${widgets['Insurance.form'].action}"/>
<x:set name="Insurance.form" property="cancelUrl" value="ViewVehicleDetail.jsp?id=${vehicleID}"/>
<c:choose>
  <c:when test="${(param.action == 'edit' || formAction == 'form.action.edit') && param.action != 'add'}">
  	<x:set name="Insurance.form" property="action" value="<%= InsuranceForm.FORM_ACTION_EDIT %>"/>
  	<x:set name="Insurance.form" property="cancelUrl" value="RoadTax.jsp?action=add&vid=${vehicleID}"/>
  	<c:choose>
  	  <c:when test="${not empty(param.rid)}">
  	  	<c:set var="recordID" value="${param.rid}"/>
      </c:when>
      <c:otherwise>
      	<c:set var="recordID" value="${widgets['Insurance.form'].id}"/>
      </c:otherwise>
    </c:choose>
    <x:set name="Insurance.form" property="id" value="${recordID}"/>
  </c:when>
  <c:otherwise>
  	<x:set name="Insurance.form" property="action" value="<%= InsuranceForm.FORM_ACTION_ADD %>"/>
  </c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${forward.name == forward_edit_succ}">
		<x:set name="Insurance.form" property="action" value="<%= InsuranceForm.FORM_ACTION_ADD %>"/>
		<script>alert("Record updated!"); document.location = "<c:url value="RoadTax.jsp?action=add&vid=${vehicleID}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_edit_fail}">
		<script>alert("Error! Fail to update record.");</script>
	</c:when>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("Record added!"); document.location = "<c:url value="RoadTax.jsp?action=add&vid=${vehicleID}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("Error! Fail to add record.");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_add}">
		<c:redirect url="RoadTax.jsp?action=add&vid=${vehicleID}"/>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete}">
		<script>alert("Delete successfully");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete_fail}">
		<script>alert("Error! Fail to delete one or more record.");</script>
	</c:when>
</c:choose>

<c:if test="${!empty param.id}">
	<c:redirect url="RoadTax.jsp?action=edit&vid=${vehicleID}&rid=${param.id}"/>
</c:if>

<x:set name="Insurance.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.roadTaxInsuranceTracking'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="Insurance.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.roadTaxInsuranceListing'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="Insurance.table" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>