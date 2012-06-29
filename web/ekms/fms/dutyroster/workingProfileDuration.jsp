<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_wpDurationPage">
    <com.tms.fms.facility.ui.WPDurationForm name="form" width="100%"/>
	<com.tms.fms.facility.ui.WPDurationTable name="table" width="100%"/>
  </page>
</x:config>

<c:set var="type" value="Add"/>
<x:set name="fms_wpDurationPage.form" property="type" value="${type}"/>

<c:choose>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.workingProfileDurationAdded"/>'); 
    document.location = "<c:url value="workingProfileDurationListing.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'EXISTS'}">
    <script>alert('<fmt:message key="fms.facility.msg.workingProfileDurationExists"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.workingProfileDurationNotAdded"/>');</script>
  </c:when>
</c:choose>

<c:if test="${forward.name == 'NOTDELETED'}">
  <script>alert('<fmt:message key="lms.elearning.validation.cannotBeDeleted"/>');</script>
</c:if>
<c:if test="${forward.name == 'ADD'}">
  <c:redirect url="workingProfileDurationAdd.jsp"/>
</c:if>
<c:if test="${!empty param.workingProfileDurationId}">
  <c:redirect url="workingProfileDurationEdit.jsp?workingProfileDurationId=${param.workingProfileDurationId}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.menu.workingProfileDuration"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_wpDurationPage.form"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<c:if test="${widgets['fms_wpDurationPage.form'].action =='submit'}">
	<x:set name="fms_wpDurationPage.table" property="startDate" value="${widgets['fms_wpDurationPage.form'].startDate}"/>
	<x:set name="fms_wpDurationPage.table" property="endDate" value="${widgets['fms_wpDurationPage.form'].endDate}"/>
	<x:set name="fms_wpDurationPage.table" property="manpowers" value="${widgets['fms_wpDurationPage.form'].manpowers}"/>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
	    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.searchResult'/></font></b></td>
	    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
	</tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	
	<x:display name="fms_wpDurationPage.table" ></x:display>
	
	</td></tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
	</table>
</c:if>	

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>