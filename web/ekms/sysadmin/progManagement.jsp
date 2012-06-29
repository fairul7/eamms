<%@include file="/common/header.jsp"%>
<x:config>
    <page name="programSetup">
    	<com.tms.fms.setup.ui.ProgramManagement name="form"/>
    </page>
</x:config>

<x:set name="programSetup.form" property="cancelUrl" value="programListing.jsp"/>

<%-- Event Handling --%>
<c:if test="${forward.name=='save'}" >
    <script>
    	alert("<fmt:message key='fms.setup.succesfulAdd'/>");   	
        document.location = "programListing.jsp";
    </script>
</c:if>

<c:if test="${forward.name=='edit'}" >
    <script>
    	alert("<fmt:message key='fms.setup.succesfulEdit'/>");    	
        document.location = "programListing.jsp";
    </script>
</c:if>
<c:if test="${forward.name=='duplicatePfeCode'}" >
    <script>
    	alert("<fmt:message key='fms.setup.duplicatePFECode'/>");    	
    </script>
</c:if>

<x:set name="programSetup.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contentBgColor">
  <tr valign="MIDDLE">
    	<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
    		<fmt:message key='fms.setup.form.programManagement'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
 <x:display name="programSetup.form" ></x:display>
 </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
