<%@ page import="kacang.Application,
                 org.apache.commons.collections.SequencedHashMap,
                 java.util.*,
                 kacang.ui.WidgetManager,
                 kacang.services.security.User,
                 com.tms.hr.leave.model.LeaveException,
                 com.tms.hr.leave.model.LeaveModule,
                 com.tms.hr.leave.model.LeaveEntry,
                 java.io.PrintWriter"%>
<%@ include file="/common/header.jsp" %>

<%
    // init portlet
    initPortlet(request);

    // get users on leave
    determineUsersOnLeave(request, new Date(), new Date(), "today");

    // get users on leave for next "nextDays" days
    int nextDays = 14;
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 1);
    Date startDate = cal.getTime();
    cal.add(Calendar.DAY_OF_MONTH, nextDays);
    Date endDate = cal.getTime();
    determineUsersOnLeave(request, startDate, endDate, "week");
%>

<script src="<c:url value="/common/tree/tree.js"/>"></script>

<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td class="bookmarkRow" align="center">

            <table cellpadding="2" cellspacing="1" width="95%">
                <tr>
                  <td class="bookmarkRow">

                    <p>
                    <c:if test="${approverCount > 0}">
                            <b><fmt:message key='leave.label.approvalRequired'/></b>:
                            <a href="<c:url value='/ekms/hrleave/viewApproveList.jsp'/>"><c:out value="${approverCount}"/></a>
                            <br>
                    </c:if>

<x:cache key="leaveCache" scope="application" time="300">

                    <p>
                    <b><fmt:message key='leave.label.onLeaveToday'/></b>:
                    <hr size="1">
                    <table width="100%">
                    <c:forEach items="${todayMap}" var="row" varStatus="status">
                        <c:set var="type" value="${row.key}"/>
                        <c:set var="id" value="leave_${type}"/>
                        <c:set var="icon" value="icon_${id}"/>
                        <c:set var="list" value="${row.value}"/>
                        <c:set var="count" value="${todayCountMap[type]}"/>
                        <tr>
                        <td width="20" nowrap>
                            [<a href="#" onclick="treeToggle('<c:out value="${id}"/>'); return false"><span id="<c:out value="${icon}"/>"><c:choose><c:when test="${count > 0}">+</c:when><c:otherwise>-</c:otherwise></c:choose></span></a>]
                        </td>
                        <td width="100%"><c:out value="${type}"/></td>
                        <td width="20">(<span class="articleRead"><c:out value="${count}"/></span>)</td>
                        </tr>
                        <tr>
                        <td></td>
                        <td colspan="2">
                        <div id="<c:out value="${id}"/>" style="display: none">
                            <c:if test="${count > 0}">
                                <script>
                                <!--
                                    treeLoad1('<c:out value="${id}"/>');
                                //-->
                                </script>
                                <c:forEach items="${list}" var="listRow">
                                    <c:set var="dates" value=""/>
                                    <c:set var="fullName" value="${listRow.key}"/>
                                    <c:set var="ldoMap" value="${listRow.value}"/>
                                    <c:forEach items="${ldoMap}" var="ldo" varStatus="ldoStatus">
                                        <c:if test="${ldoStatus.index > 0}"><c:set var="dates"><c:out value="${dates}"/>, </c:set></c:if>
                                        <c:set var="dates"><c:out value="${dates}"/><fmt:formatDate pattern="${globalDateLong}" value="${ldo.value.startDate}"/></c:set>
                                        <c:if test="${ldo.value.startDate != ldo.value.endDate}">
                                            <c:set var="dates"><c:out value="${dates}"/> - <fmt:formatDate pattern="${globalDateLong}" value="${ldo.value.endDate}"/></c:set>
                                        </c:if>
                                    </c:forEach>
                                    <li><a title="<c:out value="${dates}"/>" style="cursor:hand"><c:out value="${fullName}"/></a>
                                </c:forEach>
                            </c:if>
                        </div>
                        </td>
                        </tr>
                    </c:forEach>
                    </table>
                    </p>
                    <p>
                    <b><fmt:message key='leave.label.onLeaveNext'/> <%= nextDays %>  <fmt:message key='leave.label.days'/></b>:
                    <hr size="1">
                    <table width="100%">
                    <c:forEach items="${weekMap}" var="row" varStatus="status">
                        <c:set var="type" value="${row.key}"/>
                        <c:set var="id" value="wleave_${type}"/>
                        <c:set var="icon" value="icon_${id}"/>
                        <c:set var="list" value="${row.value}"/>
                        <c:set var="count" value="${weekCountMap[type]}"/>
                        <tr>
                        <td width="20" nowrap>
                            [<a href="#" onclick="treeToggle('<c:out value="${id}"/>'); return false"><span id="<c:out value="${icon}"/>"><c:choose><c:when test="${count > 0}">+</c:when><c:otherwise>-</c:otherwise></c:choose></span></a>]
                        </td>
                        <td width="100%"><c:out value="${type}"/></td>
                        <td width="20">(<span class="articleRead"><c:out value="${count}"/></span>)</td>
                        </tr>
                        <tr>
                        <td></td>
                        <td colspan="2">
                        <div id="<c:out value="${id}"/>" style="display: none">
                            <c:if test="${count > 0}">
                                <script>
                                <!--
                                    treeLoad('<c:out value="${id}"/>');
                                //-->
                                </script>
                                <c:forEach items="${list}" var="listRow">
                                    <c:set var="dates" value=""/>
                                    <c:set var="fullName" value="${listRow.key}"/>
                                    <c:set var="ldoMap" value="${listRow.value}"/>
                                    <c:forEach items="${ldoMap}" var="ldo" varStatus="ldoStatus">
                                        <c:if test="${ldoStatus.index > 0}"><c:set var="dates"><c:out value="${dates}"/>, </c:set></c:if>
                                        <c:set var="dates"><c:out value="${dates}"/><fmt:formatDate pattern="${globalDateLong}" value="${ldo.value.startDate}"/></c:set>
                                        <c:if test="${ldo.value.startDate != ldo.value.endDate}">
                                            <c:set var="dates"><c:out value="${dates}"/> - <fmt:formatDate pattern="${globalDateLong}" value="${ldo.value.endDate}"/></c:set>
                                        </c:if>
                                    </c:forEach>
                                    <li><a title="<c:out value="${dates}"/>" style="cursor:hand"><c:out value="${fullName}"/></a>
                                </c:forEach>
                            </c:if>
                        </div>
                        </td>
                        </tr>
                    </c:forEach>
                    </table>
                    </p>

</x:cache>
                    
                  </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td class="portletFooter">&nbsp;
    <input class="button" value="<fmt:message key='leave.label.applyLeave'/>" type="button" onClick="location.href='<c:out value="/ekms/hrleave/applyLeave.jsp"/>'"/>
    </td></tr>
</table>


<%!
    void initPortlet(HttpServletRequest request) throws LeaveException {

        // get users on leave
        LeaveModule lm = (LeaveModule)Application.getInstance().getModule(LeaveModule.class);
        User user = WidgetManager.getWidgetManager(request).getUser();

        // set variables in scope
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        Calendar start = Calendar.getInstance();
        start.set(2000, Calendar.JANUARY, 1);
        Calendar end = Calendar.getInstance();
        end.set(year+1, Calendar.DECEMBER, 31);
        int approverCount = lm.countLeaveToApproveList(start.getTime(), end.getTime(), user);
        approverCount += lm.countCreditLeaveToApproveList(start.getTime(), end.getTime(), user);
        request.setAttribute("approverCount", new Integer(approverCount));
    }

    void determineUsersOnLeave(HttpServletRequest request, Date startDate, Date endDate, String attributeName) throws Exception {
        Map typeMap = new TreeMap();
        Map typeCountMap = new TreeMap();

        // get users on leave
        LeaveModule lm = (LeaveModule)Application.getInstance().getModule(LeaveModule.class);
        Collection list = lm.viewLeaveList(startDate, endDate, null, null, null, new String[] { LeaveModule.STATUS_APPROVED, LeaveModule.STATUS_CANCEL_SUBMITTED, LeaveModule.STATUS_CANCEL_REJECTED }, null, null, false, 0, -1);

        // get users
        Collection userIdList = new HashSet();
        for (Iterator i=list.iterator(); i.hasNext();) {
            LeaveEntry entry = (LeaveEntry)i.next();
            userIdList.add(entry.getUserId());
        }
        String[] userIdArray = (String[])userIdList.toArray(new String[0]);
        Map employeeMap = lm.viewEmployeeMap(userIdArray);

        // organize objects
        for (Iterator i=list.iterator(); i.hasNext();) {
            LeaveEntry entry = (LeaveEntry)i.next();
            String label = entry.getLeaveTypeName();
            if (label == null) {
                label = entry.getLeaveType();
            }

            Map userList = (Map)typeMap.get(label);
            if (userList == null) {
                userList = new TreeMap();
            }
            String userId = entry.getUserId();
            User user = (User)employeeMap.get(userId);
            if (user != null) {
                String employeeName = user.getName();
                Map leaveMap = (Map)userList.get(employeeName);
                if (leaveMap == null) {
                    leaveMap = new TreeMap();
                }
                leaveMap.put(entry.getStartDate(), entry);
                userList.put(employeeName, leaveMap);
                typeMap.put(label, userList);
                typeCountMap.put(label, new Integer(userList.size()));
            }
        }

        // set variables in scope
        request.setAttribute(attributeName + "Map", typeMap);
        request.setAttribute(attributeName + "CountMap", typeCountMap);
    }
%>

