<%@ page import="java.util.Calendar,
                 java.util.Date,
                 kacang.Application"%>
<%@ include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<table cellpadding="2" cellspacing="1">
    <tr>
        <td class="classRowLabel" align="right" valign="top"><fmt:message key='project.error.users'/>&nbsp;<c:out value="*"/></td>
        <td class="classRow"><x:display name="${form.users.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" align="right"><fmt:message key='project.error.startFrom'/></td>
        <td class="classRow"><x:display name="${form.startDate.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" align="right"><fmt:message key='project.error.endsAt'/></td>
        <td class="classRow"><x:display name="${form.endDate.absoluteName}"/></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <x:display name="${form.submit.absoluteName}"/>
            <x:display name="${form.reset.absoluteName}"/>
        </td>
    </tr>
</table>
<c:if test="${!form.invalid && !empty form.report}">
    <br>
    <table cellpadding="0" cellspacing="0" width="95%" align="center" border="0">
        <tr>
            <td class="tableRow">
                <c:set var="reportStarts" value="${widget.startDate.date}" scope="page"/>
                <c:set var="reportEnds" value="${widget.endDate.date}" scope="page"/>
                <%
                    Calendar reportStarts = Calendar.getInstance();
                    Calendar reportEnds = Calendar.getInstance();
                    Date dateReportStart = (Date)pageContext.getAttribute("reportStarts");
                    Date dateReportEnd = (Date)pageContext.getAttribute("reportEnds");
                    reportStarts.setTime(dateReportStart);
                    reportEnds.setTime(dateReportEnd);
                    int reportDuration = reportEnds.get(Calendar.DAY_OF_YEAR)-reportStarts.get(Calendar.DAY_OF_YEAR)+1+((reportEnds.get(Calendar.YEAR)-reportStarts.get(Calendar.YEAR))*365);

                    Calendar taskStarts = Calendar.getInstance();
                    Calendar taskEnds = Calendar.getInstance();
                %>
                <table cellpadding="4" cellspacing="1" width="100%">
                    <tr><td colspan="6" class="tableRow"><b><fmt:message key='project.label.resourcemonitor'/></b></td></tr>
                    <tr>
                        <td colspan="5" class="classRow">&nbsp;</td>
                        <td width="58%" class="classRow" valign="top" nowrap colspan="<%= reportDuration %>">
                            <table cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td class="classTextSmall">&darr;&nbsp;<fmt:formatDate value="${form.startDate.date}" pattern="${globalDateShort}"/></td>
                                    <td class="classTextSmall" align="right"><fmt:formatDate value="${form.endDate.date}" pattern="${globalDateShort}"/>&nbsp;&darr;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
					<%-- Generating days --%>
                    <tr>
						<td colspan="5" class="classRow">&nbsp;</td>
                        <c-rt:set var="currentDate" value="<%= new java.util.Date() %>"/>
                        <c:set var="today"><fmt:formatDate pattern="d MMM" value="${currentDate}" /></c:set>
                        <%
							Calendar tempStart = Calendar.getInstance();
                            int dayOfWeek = reportStarts.get(Calendar.DAY_OF_WEEK);
                            tempStart.setTime(reportStarts.getTime());
                            String strDay = "&nbsp;";
							boolean sunday = false;
                            for(int i=0; i<reportDuration; i++)
                            {
								switch(dayOfWeek)
								{
									case 2: strDay = Application.getInstance().getMessage("general.label.m"); sunday = false; break;
                                        case 3: strDay = Application.getInstance().getMessage("general.label.t"); sunday = false; break;
                                        case 4: strDay = Application.getInstance().getMessage("general.label.w"); sunday = false; break;
                                        case 5: strDay = Application.getInstance().getMessage("general.label.t"); sunday = false; break;
                                        case 6: strDay = Application.getInstance().getMessage("general.label.f"); sunday = false; break;
                                        case 7: strDay = Application.getInstance().getMessage("general.label.s"); sunday = false; break;
                                        default: strDay = Application.getInstance().getMessage("general.label.s"); sunday = true; break;
								}
                                pageContext.setAttribute("tempDate", tempStart.getTime());
                        %>
                            <c:set var="fDate"><fmt:formatDate pattern="d MMM" value="${tempDate}" /></c:set>
                            <td class="<% if(sunday) {%>tableRow<%}else{%>classRow<%}%>" align="center" style="cursor:hand" title="<c:out value="${fDate}"/>">
                                <span<c:if test="${fDate == today}"> class="highlight"</c:if>><%= strDay %></span>
                            </td>
                        <%
                                if(dayOfWeek == 8)
                                    dayOfWeek = 2;
                                else
                                    dayOfWeek++;

                                tempStart.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        %>
                    </tr>
                    <%-- END: Generating days --%>
                    <c:forEach items="${form.report}" var="record">
                        <tr><td colspan="<%= (reportDuration+5) %>" class="classRowLabel"><b><c:out value="${record.key.name}"/></b></td></tr>
                        <c:choose>
                            <c:when test="${!empty record.value}">
                                <c:forEach items="${record.value}" var="task">
                                    <c:set var="taskStart" value="${task.startDate}" scope="page"/>
                                    <c:set var="taskEnd" value="${task.endDate}" scope="page"/>
                                    <%
                                        Date dateTaskStart = (Date)pageContext.getAttribute("taskStart");
                                        Date dateTaskEnd = (Date)pageContext.getAttribute("taskEnd");
                                        Calendar actualStart = Calendar.getInstance();
                                        Calendar actualEnd = Calendar.getInstance();
                                        actualStart.setTime(dateTaskStart);
                                        actualEnd.setTime(dateTaskEnd);
                                        if(dateTaskStart.before(dateReportStart))
                                            taskStarts.setTime(dateReportStart);
                                        else
                                            taskStarts.setTime(dateTaskStart);
                                        if(dateTaskEnd.after(dateReportEnd))
                                            taskEnds.setTime(dateReportEnd);
                                        else
                                            taskEnds.setTime(dateTaskEnd);

                                        int projectDuration = (taskEnds.get(Calendar.DAY_OF_YEAR)-taskStarts.get(Calendar.DAY_OF_YEAR))+1+((taskEnds.get(Calendar.YEAR)-taskStarts.get(Calendar.YEAR))*365);
                                        int leftOffset = 0;
                                        if(!((taskStarts.get(Calendar.DAY_OF_YEAR)-reportStarts.get(Calendar.DAY_OF_YEAR)+((taskStarts.get(Calendar.YEAR)-reportStarts.get(Calendar.YEAR))*365)) == 0))
                                            leftOffset = taskStarts.get(Calendar.DAY_OF_YEAR)-reportStarts.get(Calendar.DAY_OF_YEAR)+((taskStarts.get(Calendar.YEAR)-reportStarts.get(Calendar.YEAR))*365);
                                        int rightFiller = 0;
                                        if(!((taskEnds.get(Calendar.DAY_OF_YEAR)-reportEnds.get(Calendar.DAY_OF_YEAR)+((taskEnds.get(Calendar.YEAR)-reportEnds.get(Calendar.YEAR))*365)) == 0))
                                            rightFiller = reportDuration-(leftOffset + projectDuration);
                                        int actualDuration = (actualEnd.get(Calendar.DAY_OF_YEAR)-actualStart.get(Calendar.DAY_OF_YEAR))+1+((actualEnd.get(Calendar.YEAR)-actualStart.get(Calendar.YEAR))*365);
                                    %>
                                    <tr>
                                        <td class="classRowLabel" width="2%" nowrap>&nbsp;&nbsp;</td>
                                        <td class="classRow" width="25%" nowrap>[<c:out value="${task.category}"/>] <c:out value="${task.title}"/></td>
                                        <td class="classRow" width="5%" nowrap align="center"><%= actualDuration %> Days</td>
                                        <td class="classRow" width="5%" nowrap align="center"><fmt:formatDate value="${task.startDate}" pattern="${globalDateShort}"/></td>
                                        <td class="classRow" width="5%" nowrap align="center"><fmt:formatDate value="${task.endDate}" pattern="${globalDateShort}"/></td>
                                        <% for(int i = 0; i < leftOffset; i++) { %>
                                            <td class="classRow"><img src="<c:url value='/ekms/images/clear.gif'/>" width="1" height="1"></td>
                                        <% } %>
                                        <td class="classRow" colspan="<%= projectDuration %>">
                                            <table cellpadding="1" cellspacing="0" border="0" width="100%" height="15">
                                                <tr><td class="tableHeader"><img src="<c:url value='/ekms/images/clear.gif'/>" width="1" height="1"></td></tr>
                                            </table>
                                        </td>
                                        <% for(int i = 0; i < rightFiller; i++) { %>
                                            <td class="classRow"><img src="<c:url value='/ekms/images/clear.gif'/>" width="1" height="1"></td>
                                        <% } %>
                                    </tr>
                                </c:forEach>
                                <c:if test="${!empty form.freeTime[record.key]}">
                                    <tr>
                                        <td colspan="5" class="classRow" align="right">Free Time </td>
                                        <%
                                            /* Calculating free time matrix */
                                            Calendar lastDate = Calendar.getInstance();
                                            Calendar currentStart = Calendar.getInstance();
                                            Calendar currentEnd = Calendar.getInstance();
                                            Calendar freeStart = Calendar.getInstance();
                                            Calendar freeEnd = Calendar.getInstance();
                                            int blockDuration = 0;
                                            int totalCoverage = 0;
                                            lastDate.setTime((Date)pageContext.getAttribute("reportStarts"));
                                        %>
                                        <c:forEach items="${form.freeTime[record.key]}" var="block">
                                            <c:set var="currentStart" value="${block.key}" scope="page"/>
                                            <c:set var="currentEnd" value="${block.value}" scope="page"/>
                                            <%
                                                currentStart.setTime((Date)pageContext.getAttribute("currentStart"));
                                                currentEnd.setTime((Date)pageContext.getAttribute("currentEnd"));
												int difference = (lastDate.get(Calendar.DAY_OF_YEAR)-currentStart.get(Calendar.DAY_OF_YEAR)+((lastDate.get(Calendar.YEAR)-currentStart.get(Calendar.YEAR))*365));
                                                if(!(difference == 0 || difference == -1))
                                                {
                                                    freeStart.setTime(lastDate.getTime());
                                                    if(!((freeStart.get(Calendar.DAY_OF_YEAR)-reportStarts.get(Calendar.DAY_OF_YEAR)+((freeStart.get(Calendar.YEAR)-reportStarts.get(Calendar.YEAR))*365))==0))
                                                        freeStart.add(Calendar.DAY_OF_YEAR, 1);
                                                    freeEnd.setTime(currentStart.getTime());
                                                    freeEnd.add(Calendar.DAY_OF_YEAR, -1);
                                                    blockDuration = (freeEnd.get(Calendar.DAY_OF_YEAR)-freeStart.get(Calendar.DAY_OF_YEAR))+1+((freeEnd.get(Calendar.YEAR)-freeStart.get(Calendar.YEAR))*365);
                                                    totalCoverage += blockDuration;

                                                    pageContext.setAttribute("freeStart", freeStart.getTime());
                                                    pageContext.setAttribute("freeEnd", freeEnd.getTime());
                                            %>
                                                <td colspan="<%= blockDuration %>" class="classRow">
                                                    <table cellpadding="1" cellspacing="0" border="0" width="100%" height="15">
                                                        <tr>
                                                            <td class="tableHeader">
                                                                <table cellpadding="1" cellspacing="0" border="0" width="100%" height="15">
                                                                    <tr><th title="<fmt:formatDate value="${freeStart}" pattern="${globalDateShort}"/> - <fmt:formatDate value="${freeEnd}" pattern="d MMM yy"/>" class="tableRow"><img src="<c:url value='/ekms/images/clear.gif'/>" width="1" height="1"></th></tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            <%
                                                }
                                                /* Calculating filler widths */
                                                blockDuration = (currentEnd.get(Calendar.DAY_OF_YEAR)-currentStart.get(Calendar.DAY_OF_YEAR))+1+((currentEnd.get(Calendar.YEAR)-currentStart.get(Calendar.YEAR))*365);
                                                lastDate.setTime(currentEnd.getTime());
                                                totalCoverage += blockDuration;
                                                for(int i =0; i<blockDuration; i++)
                                                {
                                            %>
                                                    <td class="classRow"><img src="<c:url value='/ekms/images/clear.gif'/>" width="1" height="1"></td>
                                            <% } %>
                                        </c:forEach>
                                        <%
                                            if(totalCoverage < reportDuration)
                                            {
                                                freeStart.setTime(lastDate.getTime());
                                                if(!((freeStart.get(Calendar.DAY_OF_YEAR)-reportStarts.get(Calendar.DAY_OF_YEAR)+((freeStart.get(Calendar.YEAR)-reportStarts.get(Calendar.YEAR))*365))==0))
                                                    freeStart.add(Calendar.DAY_OF_YEAR, 1);
                                                freeEnd.setTime(reportEnds.getTime());
                                                pageContext.setAttribute("freeStart", freeStart.getTime());
                                                pageContext.setAttribute("freeEnd", freeEnd.getTime());
                                        %>
                                            <td colspan="<%= (reportDuration-totalCoverage) %>%" class="classRow">
                                                <table cellpadding="1" cellspacing="0" border="0" width="100%" height="15">
                                                    <tr>
                                                        <td class="tableHeader">
                                                            <table cellpadding="1" cellspacing="0" border="0" width="100%" height="100%">
                                                                <tr><th title="<fmt:formatDate value="${freeStart}" pattern="${globalDateShort}"/> - <fmt:formatDate value="${freeEnd}" pattern="d MMM yy"/>" class="tableRow"><img src="<c:url value='/ekms/images/clear.gif'/>" width="1" height="1"></th></tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        <% } %>
                                    </tr>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="<%= (reportDuration+5) %>" class="classRow"><fmt:message key='project.error.noTaskFound'/></td></tr>
                            </c:otherwise>
                        </c:choose>
						<%-- TODO: Remove diagnostic and debugging block --%>
						<%--<c:forEach items="${form.freeTime[record.key]}" var="block">
							<tr><td colspan="<%= reportDuration+5 %>"><c:out value="${block.key} [TO] ${block.value}"/></td></tr>
						</c:forEach>--%>
                    </c:forEach>
					<%-- Generating days --%>
                    <tr>
						<td colspan="5" class="classRow">&nbsp;</td>
                        <c-rt:set var="currentDate" value="<%= new java.util.Date() %>"/>
                        <c:set var="today"><fmt:formatDate pattern="d MMM" value="${currentDate}" /></c:set>
                        <%
							tempStart = Calendar.getInstance();
                            dayOfWeek = reportStarts.get(Calendar.DAY_OF_WEEK);
                            tempStart.setTime(reportStarts.getTime());
                            strDay = "&nbsp;";
							sunday = false;
                            for(int i=0; i<reportDuration; i++)
                            {
								switch(dayOfWeek)
								{
									case 2: strDay = Application.getInstance().getMessage("general.label.m"); sunday = false; break;
                                        case 3: strDay = Application.getInstance().getMessage("general.label.t"); sunday = false; break;
                                        case 4: strDay = Application.getInstance().getMessage("general.label.w"); sunday = false; break;
                                        case 5: strDay = Application.getInstance().getMessage("general.label.t"); sunday = false; break;
                                        case 6: strDay = Application.getInstance().getMessage("general.label.f"); sunday = false; break;
                                        case 7: strDay = Application.getInstance().getMessage("general.label.s"); sunday = false; break;
                                        default: strDay = Application.getInstance().getMessage("general.label.s"); sunday = true; break;
								}
                                pageContext.setAttribute("tempDate", tempStart.getTime());
                        %>
                            <c:set var="fDate"><fmt:formatDate pattern="d MMM" value="${tempDate}" /></c:set>
                            <td class="<% if(sunday) {%>tableRow<%}else{%>classRow<%}%>" align="center" style="cursor:hand" title="<c:out value="${fDate}"/>">
                                <span<c:if test="${fDate == today}"> class="highlight"</c:if>><%= strDay %></span>
                            </td>
                        <%
                                if(dayOfWeek == 8)
                                    dayOfWeek = 2;
                                else
                                    dayOfWeek++;

                                tempStart.add(Calendar.DAY_OF_MONTH, 1);
                            }
                        %>
                    </tr>
                    <%-- END: Generating days --%>
                    <tr>
                        <td colspan="5" class="classRow">&nbsp;</td>
                        <td width="58%" class="classRow" valign="top" nowrap colspan="<%= reportDuration %>">
                            <table cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td class="classTextSmall">&uarr;&nbsp;<fmt:formatDate value="${form.startDate.date}" pattern="${globalDateShort}"/></td>
                                    <td class="classTextSmall" align="right"><fmt:formatDate value="${form.endDate.date}" pattern="${globalDateShort}"/>&nbsp;&uarr;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</c:if>
<jsp:include page="../../form_footer.jsp" flush="true"/>
