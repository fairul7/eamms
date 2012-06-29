<%@include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.permission.rateCard" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
     <page name="rateCardCategorySetup">
        <com.tms.fms.facility.ui.RateCardCategoryEditForm name="form"/>
     </page>
</x:config>

<c:choose>
	<c:when test="${forward.name == 'EXISTS'}">
		<script>alert("<fmt:message key='fms.facility.msg.categoryIsExist'/>");</script>
	</c:when>
	<c:when test="${forward.name == 'ITEMS_COMPULSORY'}">
		<script>alert("<fmt:message key='fms.facility.msg.isCompulsory'/>");</script>
	</c:when>
	<c:when test="${forward.name == 'SAVE'}">
		<script>alert("<fmt:message key='fms.facility.msg.updateSuccess'/>");</script>
		<c:redirect url="itemCategoryList.jsp"/>	
	</c:when>
</c:choose>

<x:set name="rateCardCategorySetup.form" property="action" value="continuesetup"/>

<%@include file="/ekms/includes/header.jsp" %>
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='general.label.systemAdministration'/> > <fmt:message key='fms.label.rateCardCategorySetup'/>  
        </td>
    	<td align="right" class="contentTitleFont">&nbsp;</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
    		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
			<x:display name="rateCardCategorySetup.form" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />

<%@include file="/ekms/includes/footer.jsp" %>
