<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<script>
    function selectAll(obj,i) {
        if (obj.checked) {
            for (var a=0;a<i;a++) {
                var s= "<c:out value="${w.rootForm.absoluteName}"/>.cbNode"+a;
                var cbObj = document.forms['<c:out value="${w.rootForm.absoluteName}"/>'][s] ;
                cbObj.checked=true;
            }
        }
        else {
            for (var a=0;a<i;a++) {
                var s= "<c:out value="${w.rootForm.absoluteName}"/>.cbNode"+a;
                var cbObj = document.forms['<c:out value="${w.rootForm.absoluteName}"/>'][s];
                cbObj.checked=false;
            }
        }
    }
</script>

<table width="100%" cellpadding="0" cellspacing="0">
<tr>
    <td colspan="2"><b><fmt:message key="txy.label.mappedNode"/></b></td>
</tr>
<% int i=0; %>
<c:forEach var="selectedNode" items="${w.list}">
    <% String s=""+i; %>
    <c-rt:set var="i" value="<%=s%>"/>
    <tr>
        <td width="10%" align="right">
        &nbsp;
        </td>
        <td width="90%">
        <x:display name="${w.cbNodes[i].absoluteName}"/>
        <c:forEach var="child" items="${w.mappedList[i]}">
            <c:if test="${child.taxonomyId!=selectedNode.taxonomyId}">
            <c:out value="${child.taxonomyName}"/>&nbsp;&gt;&nbsp;
            </c:if>
            <c:if test="${child.taxonomyId==selectedNode.taxonomyId}">
            <b><c:out value="${child.taxonomyName}"/></b>
            </c:if>
        </c:forEach>
        </td>
    </tr>
    <% i++; %>
</c:forEach>
<% if (i>0) { %>
<tr>
    <td>&nbsp;</td>
    <td>
        <br>
        <input type="checkBox" onClick="selectAll(this,<%=i%>)">Select All
        <br>
        <x:display name="${w.btnRemove.absoluteName}"/>
    </td>
</tr>
<% } %>
<tr>
    <td colspan="2">&nbsp;</td>
</tr>
<tr>
    <td colspan="2"><hr></td>
</tr>
<tr>
    <td colspan="2">&nbsp;</td>
</tr>
<tr>
    <td>&nbsp;</td>
    <td>
		<input type="button" value="Add New Mapping" class="button" onClick="window.open('contentMappingAdd.jsp?id=<c:out value="${w.contentId}"/>','','menubar=no,resizable=yes,width=404,height=400,scrollbars=yes')">
    </td>
</tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>