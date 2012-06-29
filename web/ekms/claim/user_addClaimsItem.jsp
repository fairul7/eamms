<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
   <page name="jsp_addClaimsItem">
      <com.tms.hr.claim.ui.ClaimFormItemForm name="addFormItem" width="100%" />
		<tabbedpanel name="tab1" width="100%">
			<panel name="general" text="General">
      		<com.tms.hr.claim.ui.ClaimFormItemGeneralForm name="addFormItemGeneral" width="100%" />
			</panel>
			<panel name="travel" text="Travel">
      <com.tms.hr.claim.ui.ClaimFormItemTravelForm name="addFormItemTravel" width="100%" />
			</panel>
			<panel name="standard" text="Predefined">
      <com.tms.hr.claim.ui.ClaimFormItemStandardForm name="addFormItemStandard" width="100%" />
			</panel>
		</tabbedpanel>
   </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<c:if test="${forward.name=='success'}">
    <script>
        alert("<fmt:message key='claims.label.added'/>");

        window.opener.location="user_editClaim.jsp";
         // window.opener.location.reload();
         // window.close();
    </script>


</c:if>




<c:if test="${forward.name=='categoryemtpy'}">
    <script>

      alert("<fmt:message key='claims.label.categoryempty'/>");
    </script>


</c:if>

<c:if test="${forward.name=='fail'}">
    <script>

      alert("<fmt:message key='claims.label.value.toobig'/>");
    </script>


</c:if>

<c:if test="${forward.name=='sameNameExist'}">
    <script>

      alert("<fmt:message key='claims.label.purpose.same'/>");
    </script>


</c:if>


<c:if test="${forward.name == 'notype'}">
    <script>


        alert("<fmt:message key="claims.label.note.typedept.notype"/>");
    </script>
</c:if>







<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <title>Add Expenses Item</title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>

<%
	try
	{
%>


<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
			<fmt:message key="claims.label.claimListing"/> > <fmt:message key="claims.label.addExpensesItem"/>
			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<table width="100%" bgcolor="#FFFFFF" cellpadding="0" cellspacing="0"><tr><td>

<table width="100%" cellspacing="1" cellpadding="5">
	<!-- <tr valign="top" bgcolor="#cccccc"> <td><spacer type="block" height="1"></td> </tr>-->
	<tr valign="top">
		<td bgcolor="#EFEFEF" class="contentBgColor">
  <c:if test="${not empty(param.formId)}">
      <x:set name="jsp_editClaim.tab1.general.addFormItemGeneral"
                     property="formId" value="${param.formId}"/>
      <x:set name="jsp_editClaim.tab1.travel.addFormItemTravel"
                     property="formId" value="${param.formId}"/>
      <%--<x:set name="jsp_editClaim.tab1.standard.addFormItemStandard"
                     property="formId" value="${param.formId}"/>--%>
			<x:display name="jsp_editClaim.tab1"/>
	</c:if>
  <c:if test="${empty(param.formId) and not empty(widgets['jsp_editClaim.tab1.general.addFormItemGeneral'].formId)}">
			<x:display name="jsp_editClaim.tab1"/>
   </c:if>

	</td>
	</tr>


</table>

</td></tr></table>

	</td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<%
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}

%>


</body>
</html>


</x:permission>