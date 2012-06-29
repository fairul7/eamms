<%@ include file="/common/header.jsp" %>

<c:if test="${!empty param.absoluteName}">
    <c:set var="absoluteName" value="${param.absoluteName}" scope="session" />
</c:if>

<x:display name="${absoluteName}" body="custom">
<c:set var="f" value="${widget}" />



<html>
<head>
<title>Editing <c:out value="${f.title}" /></title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/cmsadmin/styles/style.css">

<script>
<!--
function doFocus(obj) {
  obj.size = "40";
}
function doBlur(obj) {
  obj.size = "3";
}//-->
</script>

</head>
<body onload="focus()">
<form>
<div id="htmlTable">
&nbsp;
<table border="1" bordercolor="gray" style="border: 1px ridge silver" cellspacing="0" cellpadding="3" width="100%" >
    <c:if test="${empty f.dataList}">
        <%-- no data in field--%>
        <tr><td>
            -No Data-
        </td></tr>
    </c:if>

    <c:if test="${!empty f.dataList}">
        <c:if test="${f.editRow eq 0}">
            <%@include file="includes/g2Header.jsp" %>
            <%-- view mode --%>
            <c:forEach var="rowList" items="${f.dataList}" varStatus="rowStatus">
                <%@include file="includes/g2ViewRow.jsp" %>
            </c:forEach>
            <%@include file="includes/g2TotalRow.jsp" %>
        </c:if>

        <c:if test="${f.editRow gt 0}">
            <%@include file="includes/g2Header.jsp" %>
            <c:forEach var="rowList" items="${f.dataList}" varStatus="rowStatus">
                <c:if test="${f.editRow-1 ne rowStatus.index}">
                    <%-- view mode --%>
                    <%@include file="includes/g2ViewRow.jsp" %>
                </c:if>
                <c:if test="${f.editRow-1 eq rowStatus.index}">
                    <%-- edit mode --%>
                    <c:set var="editMode" value="true" />
                    <%@include file="includes/g2EditRow.jsp" %>
                </c:if>
            </c:forEach>
            <%@include file="includes/g2TotalRow.jsp" %>
         </c:if>
    </c:if>
</table>
<c:if test="${f.invalid}"><div><font color="red"><fmt:message key="formWizard.label.validateGridField"/></font></div></c:if>
</div></form>

<input class="button" type="button" value="<fmt:message key="formWizard.label.newRow"/>" onclick="location='<c:url value="/common/g2Field/g2Field.jsp?et=newRow&cn=${f.absoluteName}" />'">
<c:if test="${!editMode}">
    <input class="button" type="button" value="<fmt:message key="formWizard.label.returnToMain"/>" onclick="doDone()">
    <script>
    function doDone() {
        try {
            maxRows = 10000;
            for(i=0; i < maxRows; i++) {
                document.getElementById("tableAction" + i).style.display = "none";
            }
        } catch(e) {
            // done
        }
        opener.document.getElementById("<c:out value="${absoluteName}.div" />").innerHTML = document.getElementById("htmlTable").innerHTML;
        window.close();
    }
    </script>
</c:if>
</body>
</html>
</x:display>
