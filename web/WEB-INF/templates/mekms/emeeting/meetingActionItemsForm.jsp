<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<table align="center" cellpadding="0" cellspacing="1" width="100%">
    <tr>
        <td class="emeetingBackground">
            <table width="100%" align="center" cellpadding="3" cellspacing="1">
                <tr>
                    <td class="emeetingRowLabel" valign="top" align="right" width="20%" nowrap><fmt:message key='emeeting.label.title'/>&nbsp;</td>
                    <td class="emeetingRowLabel" valign="top" width="80%">
                        <x:display name="${widget.title.absoluteName}"/>
                        <c:if test="${widget.title.invalid}"><c:out value="${widget.title.childMap.validTitle.message}"/></c:if>
                    </td>
                </tr>
                <c:if test="${(empty widget.item) || (empty widget.item.children)}">
                    <tr>
                        <td class="emeetingRowLabel" valign="top" align="right" width="20%" nowrap><fmt:message key='emeeting.label.parent'/>&nbsp;</td>
                        <td class="emeetingRowLabel" valign="top" width="80%"><x:display name="${widget.itemParent.absoluteName}"/></td>
                    </tr>
                </c:if>
                <tr>
                    <td class="emeetingRowLabel" valign="top" align="right" width="20%" nowrap><fmt:message key='emeeting.label.minutes'/>&nbsp;</td>
                    <td class="emeetingRowLabel" valign="top" width="80%"><x:display name="${widget.notes.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="emeetingRowLabel" valign="top" align="right" width="20%" nowrap><fmt:message key='emeeting.label.action'/>&nbsp;</td>
                    <td class="emeetingRowLabel" valign="top" width="80%"><x:display name="${widget.itemAction.absoluteName}"/></td>
                </tr>
<%--
                <c:if test="${! empty widget.item.tasks}">
                    <tr>
                        <td class="emeetingRowLabel" valign="top" align="right" width="20%" nowrap><fmt:message key='emeeting.label.task'/>&nbsp;</td>
                        <td class="emeetingRowLabel" valign="top" width="80%">
                    <c:forEach var="task" items="${widget.item.tasks}" >
                            <table width="95%" cellpadding="2" cellspacing="1" align="center">
                                <tr>
                                <tr>
                                    <td class="emeetingRowLabel" valign="top" align="right" width="20%" nowrap>
                                        <fmt:message key='emeeting.label.title'/>&nbsp;</td>
                                    <td class="emeetingRow">
                                         <c:out value="${task.title}" />
                                    </td>
                                </tr>

                                <tr>
                                    <td class="emeetingRowLabel" valign="top" align="right" width="20%" nowrap>
                                        <fmt:message key='emeeting.label.assignedTo'/>&nbsp;</td>
                                    <td class="emeetingRow">
                                        <c:forEach items="${task.attendees}" var="assignee">
                                            <c:out value="${assignee.propertyMap.firstName}"/> <c:out value="${assignee.propertyMap.lastName}"/><br>
                                        </c:forEach>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="emeetingRowLabel" valign="top" align="right" width="20%" nowrap><fmt:message key='emeeting.label.dueDate'/>&nbsp;</td>
                                    <td class="emeetingRow"><fmt:formatDate value="${task.dueDate}" pattern="EEE d MMM yyyy, h:mma"/></td>
                                </tr>
                            </table>
                    </c:forEach>
                        </td>
                    </tr>
                </c:if>
--%>
                <tr>
                    <td class="emeetingRowLabel" valign="top" align="right" width="20%" nowrap>&nbsp;</td>
                    <td class="emeetingRowLabel" valign="top" width="80%">
                        <c:choose>
                            <c:when test="${empty widget.item}">
                                <x:display name="${widget.buttonAdd.absoluteName}"/>
                            </c:when>
                            <c:otherwise>
                                <x:display name="${widget.buttonEdit.absoluteName}"/>
                                <x:display name="${widget.buttonDelete.absoluteName}"/>
                                <x:display name="${widget.buttonAssignTask.absoluteName}"/>
<%--
                                <x:display name="${widget.buttonEditTask.absoluteName}"/>
                                <x:display name="${widget.buttonDeleteTask.absoluteName}"/>
--%>
                                <x:display name="${widget.buttonCancel.absoluteName}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
