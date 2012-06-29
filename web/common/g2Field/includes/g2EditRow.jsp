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
                <fmt:message key="formWizard.label.unknownColumnType"/>
            </c:otherwise>
       </c:choose>
       </td>
    </c:forEach>
    <td>
        <input class="button" type="button" value="<fmt:message key="formWizard.label.update"/>" onclick="doUpdate(this.form)">
        <input class="button" type="button" value="<fmt:message key="formWizard.label.cancel"/>" onclick="location='?cn=<c:out value="${f.absoluteName}" />&et=cancelRow&editRow=<c:out value="${rowStatus.count}" />'">
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
                var columname='<c:out value="${column.name}" />'; 
                columname=encodeURI(columname);  
                columname = columname.replace("\\+", "%20");   
                var columnamevalue=f["<c:out value="${column.name}" />"].value; 
                columnamevalue=encodeURI(columnamevalue);  
                columnamevalue = columnamevalue.replace("\\+", "%20");          
                    qs += columname+"=" + columnamevalue + "&";
                </c:when>
                <c:when test="${column.type eq 'dropDown'}">
                var columname='<c:out value="${column.name}" />'; 
                columname=encodeURI(columname);  
                columname = columname.replace("\\+", "%20");   
                
                
                    s = f["<c:out value="${column.name}" />"];
                    v = "" + s.options[s.selectedIndex].value;
                    var columnamevalue=v; 
                columnamevalue=encodeURI(columnamevalue);  
                columnamevalue = columnamevalue.replace("\\+", "%20");   
                    qs += columname + "=" + columnamevalue + "&";
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