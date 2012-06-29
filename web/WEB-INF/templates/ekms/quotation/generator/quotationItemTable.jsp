<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="itemlist" value="${widget.quotationItems}" />
<c:set var="columnList" value="${widget.tableColumns}" />
<c:set var="tableType" value="${widget.tableId }" />

<div>
<table border="1">

<thead>
	<tr>
	<th>No.</th>
	<c:forEach var="column" items="${columnList}">
	  <th><c:out value="${column.header}"/></th>
	</c:forEach>
<%--
		<th></th>
		<th>Description</th>
		<th>Units</th>
		<th>QTY</th>
		<th>Cost(MYR)</th>
--%>
	</tr>
</thead>		

<tbody>
	<c:forEach var="item" items="${itemlist}" varStatus="status" >
	<tr>
		<th>
		  <c:out value="${status.index+1}"/>
			<c:if test="${'1' eq widget.canEdit}">
			<br/>
			<a href="
			  <c:url value='editQtnItem.jsp'>
                <c:param name='itemId' value='${item.itemId}'/>
<%--            <c:param name='tableId' value='${tableType }' /> --%>
              </c:url>">Edit
            </a>
            </c:if>
		</th>
		<c:out value="${item.itemTableRow }" escapeXml="false"/>
<%-- 
		<td><c:out value="${item.itemDescription }" escapeXml="false"/></td>
		<td class="midcol"><c:out value="${item.itemUnit}"/></td>
		<td class="midcol"><c:out value="${item.itemQuantity}"/></td>
		<td class="rightcol"><c:out value="${item.itemCost}"/></td>	
--%>
	</tr>	
	</c:forEach>
</tbody>
</table>
</div>
