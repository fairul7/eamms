<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.ItemForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<c-rt:set var="forward_succ" value="<%= ItemForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= ItemForm.FORWARD_ADD_FAIL %>"/>
<c-rt:set var="forward_cont" value="<%= ItemForm.FORWARD_ADD_SUCCESS_CONT %>"/>
<c-rt:set var="forward_exist" value="<%= ItemForm.FORWARD_ADD_EXIST %>"/>

<x:config>
    <page name="AddItem">
    	<com.tms.fms.facility.ui.ItemForm name="form"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${not empty(param.fid)}">
		<c:set var="facilityID" value="${param.fid}"/>
	</c:when>
	<c:otherwise>
		<c:set var="facilityID" value="${widgets['AddItem.form'].facilityId}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("Item Added!");document.location = "<c:url value="ViewFacility.jsp?fid=${facilityID}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("Error! Fail to add item.");</script>
	</c:when>
	<c:when test="${forward.name == forward_cont}">
		<script>alert("Item Added!");document.location = "<c:url value="AddItem.jsp?fid=${facilityID}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_exist}">
		<script>alert("Sorry, Same item code existed.");</script>
	</c:when>
</c:choose>

<x:set name="AddItem.form" property="facilityId" value="${facilityID}"/>
<x:set name="AddItem.form" property="action" value="<%= ItemForm.FORM_ACTION_ADD %>"/>
<x:set name="AddItem.form" property="cancelUrl" value="ViewFacility.jsp?fid=${facilityID}"/>
<x:set name="AddItem.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<script language="JavaScript">
	function checkLength(){
		var textBox = document.forms['AddItem.form'].elements['AddItem.form.tfItemCode'];
		if(textBox.value.length > 16){
			alert("<fmt:message key='fms.facility.msg.itemBarcodeAlreadyMoreThan16Charaters'/>");
		}
	}
</script>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.addNewItem'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="AddItem.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>