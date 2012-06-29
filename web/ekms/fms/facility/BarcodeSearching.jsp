<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.ItemTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<c-rt:set var="forward_listing_add" value="<%= ItemTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_writeoff" value="<%= ItemTable.FORWARD_LISTING_WRITEOFF %>"/>
<c-rt:set var="forward_listing_missing" value="<%= ItemTable.FORWARD_LISTING_MISSING %>"/>
<c-rt:set var="forward_listing_inactive" value="<%= ItemTable.FORWARD_LISTING_INACTIVE %>"/>
<c-rt:set var="forward_listing_error" value="<%= ItemTable.FORWARD_LISTING_ERROR %>"/>
<c-rt:set var="forward_listing_undo_succ" value="<%= ItemTable.FORWARD_LISTING_UNDO_SUCC %>"/>

<x:config>
    <page name="Search">
    	<com.tms.fms.facility.ui.SearchForm name="form"/>
    	<com.tms.fms.facility.ui.ItemTable name="table"/>
	</page>
</x:config>

<x:permission permission="com.tms.fms.facility.undoCheckOut" module="com.tms.fms.facility.model.FacilityModule">
	<x:set name="Search.table" property="undoCheckOut" value="${true}"/>
</x:permission>

<c:if test="${!empty param.barcode}">
	<c:redirect url="ViewItem.jsp?rid=${param.barcode}&mode=search"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<c:choose>
	<c:when test="${forward.name == forward_listing_error}">
		<script type="text/javascript">
			alert("<c:out value='${errorMsg}'/>");
		</script>	
	</c:when>
	<c:when test="${forward.name == forward_listing_writeoff}">
		<form name="writeoff" action="WriteOff.jsp?mode=search" method="post">
			<c:forEach var="key" items="${selectedKeys}" varStatus="i">
				<input type="hidden" name="selectedKeys" value="${key}">
			</c:forEach>
		</form>
		
		<script type="text/javascript">
			document.forms['writeoff'].submit();
		</script>	
	</c:when>
	<c:when test="${forward.name == forward_listing_missing}">
		<form name="missing" action="Missing.jsp?mode=search" method="post">
			<c:forEach var="key" items="${selectedKeys}" varStatus="i">
				<input type="hidden" name="selectedKeys" value="${key}">
			</c:forEach>
		</form>
		
		<script type="text/javascript">
			document.forms['missing'].submit();
		</script>	
	</c:when>
	<c:when test="${forward.name == forward_listing_inactive}">
		<form name="inactive" action="Inactive.jsp?mode=search" method="post">
			<c:forEach var="key" items="${selectedKeys}" varStatus="i">
				<input type="hidden" name="selectedKeys" value="${key}">
			</c:forEach>
		</form>
		
		<script type="text/javascript">
			document.forms['inactive'].submit();
		</script>	
	</c:when>
	<c:when test="${forward.name == forward_listing_undo_succ}">
		<script type="text/javascript">
			alert("Undo check out successful");
		</script>	
	</c:when>
</c:choose>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.barcodeSearch'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="Search.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<c:if test="${widgets['Search.form'].itemCode !=''}">
	<x:set name="Search.table" property="searchModeText" value="${widgets['Search.form'].itemCode}"/>
	<x:set name="Search.table" property="searchMode" value="${true}"/>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
	    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.searchResult'/></font></b></td>
	    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
	</tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	
	<x:display name="Search.table" ></x:display>
	
	</td></tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
	</table>
</c:if>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>