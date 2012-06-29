<%@include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.permission.rateCard" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
     <page name="rateCardSetRate">
        <com.tms.fms.facility.ui.RateCardSetRateForm name="form"/>
		<com.tms.fms.facility.ui.RateCardSetRateTable name="table"/>
     </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.id)}">
    <c:set var="id" value="${param.id}"/>
  </c:when>
  <c:otherwise>
    <c:set var="id" value="${widgets['rateCardSetRate.form'].id}"/>
  </c:otherwise>
</c:choose>
<x:set name="rateCardSetRate.form" property="id" value="${id}"/>
<x:set name="rateCardSetRate.table" property="id" value="${id}"/>

<c:if test="${forward.name == 'continue'}" >
	<c:choose>
		<c:when test="${not empty param.id}">
	  		<c:redirect url="rateCardSetRate.jsp?id=${param.id}"/> 
		</c:when>
		<c:otherwise>
			<c:redirect url="rateCardList.jsp"/>
		</c:otherwise>
	</c:choose>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='general.label.systemAdministration'/> > <fmt:message key='fms.label.rateCardSetRate'/>  
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
			<x:display name="rateCardSetRate.form" ></x:display>
		</td>
  	</tr>
	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
			<x:display name="rateCardSetRate.table" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />

<%@include file="/ekms/includes/footer.jsp" %>
