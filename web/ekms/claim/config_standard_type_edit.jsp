<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_standard_type_edit">
          <com.tms.hr.claim.ui.ClaimStandardTypeEditForm name="form"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<c:if test="${forward.name == 'claimStandardTypeEdit'}">
	<script>
		alert('Sucessfully updated the DB');
	</script>
   <c:redirect url="config_standard_type.jsp"/>
</c:if>
<c:choose>
   <c:when test="${not empty(param.claimStandardTypeID)}">
      <c:set var="claimStandardTypeID" value="${param.claimStandardTypeID}" />
      <x:set name="jsp_standard_type_edit.form" property="claimStandardTypeID" value="${param.claimStandardTypeID}"/>
   </c:when>
   <c:otherwise>
      <c:set var="claimStandardTypeID" value="${widgets['jsp_standard_type_edit.form'].claimStandardTypeID}" />
      <x:set name="jsp_standard_type_edit.form" property="claimStandardTypeID" value="${widgets['jsp_standard_type_edit.form'].claimStandardTypeID}" />
   </c:otherwise>
</c:choose>



<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" /> 

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">

			Edit Predefined Expenses Item

			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">



<x:display name="jsp_standard_type_edit.form"/>







	</td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

 </x:permission>   


<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
