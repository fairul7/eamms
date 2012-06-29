<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ page import="kacang.Application,com.tms.fms.engineering.model.EngineeringModule"%>

<c:set var="widget" value="${widget}"/>

<jsp:include page="../../form_header.jsp" flush="true"/>

<table align="left" width="60%" cellpadding="2" cellspacing="1">
<tr>
		<td> (This part is to Fix data on redmine issue #10473)
		</td>
	</tr>
<tr>
</table>
<br>
<table align="left" width="60%" cellpadding="2" cellspacing="1" class="borderTable">

	<td ><b>Insert RequestId</b></th>
	<td ><x:display name="${widget.title.absoluteName}"/></th>
</tr>

</table>
<br>
<br>
<table align="left" width="50%" cellpadding="1" cellspacing="1" >

<tr>
	<td>
		<x:display name="${widget.btnScan.absoluteName}"/>
	</td>
	
	<td>
		<x:display name="${widget.btnSync.absoluteName}"/>
	</td>

	<td>
		<x:display name="${widget.btnClr.absoluteName}"/>
	</td>
</tr>
</table>
<br>
<br>
<table align="left" width="80%" cellpadding="2" cellspacing="1" class="borderTable">

<c:if test="${not empty widget.list}">
<tr>
	<th class="tableHeader" width="10%"><b>RequestId</b></th>
	<th class="tableHeader" width="20%"><b>Request Title</b></th>
	<th class="tableHeader" width="15%"><b>Facility (forwarded to Transport Request)</b></th>
</tr>
<c:forEach items="${widget.list}" var="listScan" varStatus="stat">
	<tr>
		<td align="center" class="classRowLabel">
			<c:out value="${listScan.engineeringRequestId}"></c:out>
		</td>

		<td align="center" class="classRowLabel">
			<c:out value="${listScan.requestTitle}"></c:out>
		</td>
		
		<td align="center" class="classRowLabel">
			<c:out value="${listScan.remarks}"></c:out>
		</td>
	</tr>
</c:forEach>
</c:if>

</table>
<tr>
<td>
&nbsp;
</td>
</tr>
<tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">Scan Defected HOU Facility Request</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<table align="left" width="60%" cellpadding="2" cellspacing="1">
<tr>
		<td> (This part is to Fix data on redmine issue #10218)
		</td>
	</tr>
</table>
<br>
<table align="left" width="60%" cellpadding="2" cellspacing="1" class="borderTable">
	
<tr>
	<td ><b>Insert RequestId</b></th>
	<td ><x:display name="${widget.title2.absoluteName}"/></th>
</tr>

</table>
<br>
<br>
<table align="left" width="50%" cellpadding="1" cellspacing="1" >

<tr>
	<td>
		<x:display name="${widget.btnScanHOURequest.absoluteName}"/>
	</td>
	
	<td>
		<x:display name="${widget.btnSyncHOURequest.absoluteName}"/>
	</td>

	<td>
		<x:display name="${widget.btnClrHOURequest.absoluteName}"/>
	</td>
</tr>
</table>
<br>
<br>
<table align="left" width="80%" cellpadding="2" cellspacing="1" class="borderTable">
<c:if test="${not empty widget.HOUDefectedList}">
<tr>
	<th class="tableHeader" width="10%"><b>RequestId</b></th>
	<th class="tableHeader" width="20%"><b>Request Title</b></th>
	<th class="tableHeader" width="15%"><b>Facility (defected Request in Unit Head View)</b></th>
</tr>
<c:forEach items="${widget.HOUDefectedList}" var="listScan2" varStatus="stat2">
	<tr>
		<td align="center" class="classRowLabel">
			<c:out value="${listScan2.requestId}"></c:out>
		</td>

		<td align="center" class="classRowLabel">
			<c:out value="${listScan2.requestTitle}"></c:out>
		</td>
		
		<td align="center" class="classRowLabel">
			<c:out value="${listScan2.name}"></c:out>
		</td>
	</tr>
</c:forEach>
</c:if>

</table>
</td>
</tr>
<jsp:include page="../../form_footer.jsp" flush="true"/>
