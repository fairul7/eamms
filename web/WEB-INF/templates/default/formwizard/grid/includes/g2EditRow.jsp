<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<tr>
    <td align="right"><c:out value="${rowStatus.count}" />*</td>
    <c:forEach var="colList" items="${rowList}" varStatus="colStatus">
        <c:set var="column" value="${f.columnList[colStatus.index]}" />
        <c:set var="val" value="${f.dataList[rowStatus.index][colStatus.index]}" />
        <td>
        <c:choose>
            <c:when test="${column.type eq 'text'}">
                <input type="text" size="3" name="<c:out value="${column.name}" />" value="<c:out value="${val}" />" onfocus="doFocus(this)" onblur="doBlur(this)">
            </c:when>
            <c:when test="${column.type eq 'dropDown'}">
                <select name="<c:out value="${column.name}" />">
                    <c:forEach var="item" items="${column.itemMap}">
                        <option value="<c:out value="${item.key}" />"
                        <c:if test="${val eq item.key}">selected</c:if>
                        ><c:out value="${item.value}" /></option>
                    </c:forEach>
                </select>
            </c:when>
            <c:when test="${column.type eq 'formula'}">
                <%--do not show formula--%>
                &nbsp;
            </c:when>
            <c:otherwise>
                -unknown column type-
            </c:otherwise>
       </c:choose>
       </td>
    </c:forEach>
    <td>
        <input type="button" value="Update" onclick="doUpdate(this.form)">
        <input type="button" value="Cancel" onclick="location='?cn=<c:out value="${f.absoluteName}" />&et=cancelRow&editRow=<c:out value="${rowStatus.count}" />'">
    </td>
</tr>
<script>
<!--
    function doUpdate(f) {
        qs = "&";
        <c:forEach var="colList" items="${rowList}" varStatus="colStatus">
            <c:set var="column" value="${f.columnList[colStatus.index]}" />

            <c:choose>
                <c:when test="${column.type eq 'text'}">
                    qs += "<c:out value="${column.name}" />=" + escape(f["<c:out value="${column.name}" />"].value) + "&";
                </c:when>
                <c:when test="${column.type eq 'dropDown'}">
                    s = f["<c:out value="${column.name}" />"];
                    v = "" + s.options[s.selectedIndex].value;
                    qs += "<c:out value="${column.name}" />=" + escape(v) + "&";
                </c:when>
                <c:otherwise>
                    <%--ignore--%>
                </c:otherwise>
           </c:choose>
        </c:forEach>
        <%--alert(qs);--%>
        location = "?cn=<c:out value="${f.absoluteName}" />&et=updateRow&editRow=<c:out value="${rowStatus.count}" />" + qs;
    }
// -->
</script>