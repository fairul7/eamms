<%@include file="/common/header.jsp"%>

<c:set var="f" value="${widget}" />
<c:set var="an" value="${f.absoluteName}" />
<%
    String an = (String) pageContext.getAttribute("an");
    pageContext.setAttribute("an", an.replace('.', '_'));
%>

<c:if test="${f.editButtonVisible}">
<input class="button" type="button" value="Edit '<c:out value="${f.title}" />'" onclick="doEdit<c:out value="${an}" />()">
</c:if>
<script>
<!--
function doEdit<c:out value="${an}" />() {
    u = "<c:url value="/common/g2Field/g2Field.jsp?absoluteName=${f.absoluteName}" />";
    window.open(u, "g2FieldPopup");
}

function doFocus(obj) {
  obj.size = "40";
}
function doBlur(obj) {
  obj.size = "3";
}
//-->
</script>

<div id="<c:out value="${widget.absoluteName}.div" />">
&nbsp;
<table border="1" style="border: 1px ridge silver"  bordercolor="gray" cellspacing="0" cellpadding="3" width="100%">


    <c:if test="${empty f.dataList}">
        <%-- no data in field--%>
        <tr><td>
            -No Data-
        </td></tr>
    </c:if>

    <c:if test="${!empty f.dataList}">
        <%@include file="includes/g2Header.jsp" %>
        <%-- view mode --%>
        <c:set var="noButtons" value="true" />
        <c:forEach var="rowList" items="${f.dataList}" varStatus="rowStatus">
            <%@include file="includes/g2ViewRow.jsp" %>
        </c:forEach>
        <%@include file="includes/g2TotalRow.jsp" %>
    </c:if>
</table>
<c:if test="${f.invalid}"><div><font color="red"><fmt:message key="formWizard.label.validateGridField"/></font></div></c:if>

</div>