<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.model.SetupObject, com.tms.fms.transport.ui.SetupForm, com.tms.fms.transport.ui.SetupTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.admin" module="com.tms.fms.transport.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="Setup">
    	<com.tms.fms.transport.ui.SetupForm name="form"/>
        <com.tms.fms.transport.ui.SetupTable name="table" width="100%"/>
    </page>
</x:config>


<c-rt:set var="forward_succ" value="<%= SetupForm.SETUP_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= SetupForm.SETUP_ADD_FAIL %>"/>
<c-rt:set var="forward_exist" value="<%= SetupForm.SETUP_ADD_EXIST %>"/>
<c-rt:set var="forward_listing_add" value="<%= SetupTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_delete" value="<%= SetupTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_delete_fail" value="<%= SetupTable.FORWARD_LISTING_DELETE_FAIL %>"/>

<x:set name="Setup.form" property="setupType" value="<%= SetupObject.SETUP_LOCATION %>"/>
<x:set name="Setup.table" property="setupType" value="<%= SetupObject.SETUP_LOCATION %>"/>

<c:set var="formAction" value="${widgets['Setup.form'].action}"/>

<c:choose>
  <c:when test="${(param.action == 'edit' || formAction == 'setup.action.edit') && param.action != 'add'}">
  	<x:set name="Setup.form" property="action" value="<%= SetupForm.SETUP_ACTION_EDIT %>"/>
  	<c:choose>
  	  <c:when test="${not empty(param.id)}">
  	  	<c:set var="setupID" value="${param.id}"/>
      </c:when>
      <c:otherwise>
      	<c:set var="setupID" value="${widgets['Setup.form'].setupId}"/>
      </c:otherwise>
    </c:choose>
    <x:set name="Setup.form" property="setupId" value="${setupID}"/>
  	<c:choose>
		<c:when test="${forward.name == forward_succ}">
		    <x:set name="Setup.form" property="action" value="<%= SetupForm.SETUP_ACTION_ADD %>"/>
			<script>alert("<fmt:message key='fms.tran.msg.recordUpdated'/>"); document.location = "<c:url value="Location.jsp"/>";</script>
		</c:when>
		<c:when test="${forward.name == forward_fail}">
			<script>alert("<fmt:message key='fms.tran.msg.failToUpdateRecord'/>");</script>
		</c:when>
		<c:when test="${forward.name == forward_exist}">
			<script>alert("<fmt:message key='fms.tran.msg.errorRecordNameShouldBeUnique'/>");</script>
		</c:when>
	</c:choose>
  </c:when>
  <c:otherwise>
  	<x:set name="Setup.form" property="action" value="<%= SetupForm.SETUP_ACTION_ADD %>"/>
  	<c:choose>
		<c:when test="${forward.name == forward_succ}">
			<script>alert("<fmt:message key='fms.tran.msg.recordAdded'/>");</script>
		</c:when>
		<c:when test="${forward.name == forward_fail}">
			<script>alert("<fmt:message key='fms.tran.msg.failToAddRecord'/>");</script>
		</c:when>
		<c:when test="${forward.name == forward_exist}">
			<script>alert("<fmt:message key='fms.tran.msg.errorRecordNameShouldBeUnique'/>");</script>
		</c:when>
	</c:choose>
  </c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${forward.name == forward_listing_add}">
		<c:redirect url="Location.jsp?action=add"/>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete}">
		<script>alert("<fmt:message key='fms.tran.msg.recordDeleted'/>");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete_fail}">
		<script>alert("<fmt:message key='fms.tran.msg.failToDeleteOneOrMoreRecord'/>");</script>
	</c:when>
</c:choose>

<c:if test="${!empty param.setup_id}">
	<c:redirect url="Location.jsp?action=edit&id=${param.setup_id}"/>
</c:if>

<x:set name="Setup.form" property="cancelUrl" value="Location.jsp"/>
<x:set name="Setup.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.menu.storeLocationSetup'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="Setup.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.menu.storeLocationListing'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="Setup.table" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>