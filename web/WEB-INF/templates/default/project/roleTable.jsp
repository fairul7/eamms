<%@ page import="org.apache.commons.lang.StringUtils,
                 com.tms.collab.forum.model.Message"%>
<%@ include file="/common/header.jsp" %>
<c:set var="widget" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<tr>
    <td colspan="2" class="wormsRow" align="left">
        <table cellpadding="4" cellspacing="1" width="100%">
            <tr>
                <td colspan="3" class="profileRow" align="left">
                    <%--<x:display name="${widget.add.absoluteName}"/>--%>
                    <c:choose>
                        <c:when test="${empty widget.projectTemplate}">
                            <input type="button" class="button" value="<fmt:message key="project.label.add"/>" onClick="window.open('<c:url value="/ekms/worms/roleAdd.jsp?projectId=${widget.projectId}&currentlyTemplate=false"/>', 'roleWindow', 'height=500,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
                        </c:when>
                        <c:otherwise>
                            <input type="button" class="button" value="<fmt:message key="project.label.add"/>" onClick="window.open('<c:url value="/ekms/worms/roleAdd.jsp?projectId=${widget.templateId}&currentlyTemplate=true"/>', 'roleWindow', 'height=500,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
                        </c:otherwise>
                    </c:choose>
                    <x:display name="${widget.delete.absoluteName}"/>
                </td>
            </tr>
            <tr>
                <td class="tableHeader" width="50%" nowrap><fmt:message key="project.label.role"/></td>
                <td class="tableHeader" width="25%" nowrap><fmt:message key="project.label.playedBy"/></td>
                <td class="tableHeader" width="25%" nowrap><fmt:message key="project.label.competenciesRequired"/></td>
            </tr>
            <c:choose>
                <c:when test="${empty widget.roles}">
                    <tr><td colspan="3" class="tableRow"><fmt:message key="project.message.noRolesFound"/></td></tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${widget.roles}" var="role">
                        <c:set var="checkbox_name" value="chk${role.roleId}"/>
                        <tr>
                            <td width="50%" valign="top" class="tableRow">
                                <x:display name="${widget.childMap[checkbox_name].absoluteName}"/>
                                <%--<x:event name="${widget.absoluteName}" type="selection" param="roleId=${role.roleId}"><b><c:out value="${widget.roleMap[checkbox_name]}"/></b></x:event>--%>
                                <a href="" onClick="window.open('<c:url value="/ekms/worms/roleOpen.jsp?roleId=${role.roleId}&currentlyTemplate=false"/>', 'roleWindow', 'height=500,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes'); return false;">
                                    <b><c:out value="${widget.roleMap[checkbox_name]}"/></b>
                                </a>
                                <br><br>
                                <c:set var="description" value="${role.roleDescription}"/>
                                <%
                                    String translated = StringUtils.replace((String)pageContext.getAttribute("description"), "\n", "<br>");
                                    pageContext.setAttribute("translated", translated);
                                %>
                                <c:out value="${translated}" escapeXml="false" />
                            </td>
                            <td width="25%" valign="top" class="tableRow">
                                <c:choose>
                                    <c:when test="${empty role.personnel}">
                                        <fmt:message key="project.message.noUsersFound"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="user" items="${role.personnel}">
                                            <li><c:out value="${user.propertyMap.firstName} ${user.propertyMap.lastName}"/></li>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td width="25%" valign="top" class="tableRow">
                                <c:choose>
                                    <c:when test="${empty role.competencies}">
                                        <fmt:message key="project.message.noCompetenciesFound"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="competency" items="${role.competencies}">
                                            <li><c:out value="${competency.competencyName}"/></li>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </table>
    </td>
</tr>
<jsp:include page="../form_footer.jsp" flush="true"/>