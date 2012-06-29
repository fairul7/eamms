<%@include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"  />
<jsp:include page="../form_header.jsp" flush="true"/>
<table>
    <tr>
        <td align="right" valign="top" class="blogLabel"><fmt:message key='weblog.label.blogName'/></td>
        <td>
            <x:display name="${form.nameTF.absoluteName}" />
            <br>
            <c:choose>
                <c:when test="${form.nameTF.invalid}" ><font color="#de123e"><fmt:message key='weblog.label.nameused'/></font></c:when>
                <c:otherwise>
                    <fmt:message key='weblog.label.weblogurl'/><%= request.getServerName()+":"+request.getServerPort()%>/blog/
					<c:choose>
						<c:when test="${!empty form.nameTF.value}" ><c:out value="${form.nameTF.value}" /></c:when>
						<c:otherwise><strong><fmt:message key='weblog.label.blogname'/></strong></c:otherwise>
                	</c:choose>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <td align="right" valign="top" class="blogLabel"><fmt:message key='weblog.label.title'/></td>
        <td><x:display name="${form.title.absoluteName}" /></td>
    </tr>
    <tr>
        <td align="right" valign="top" class="blogLabel"><fmt:message key='weblog.label.description'/></td>
        <td><x:display name="${form.description.absoluteName}" /></td>
    </tr>
    <tr>
        <td align="right"></td>
        <td><x:display name="${form.addButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}"/></td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>



