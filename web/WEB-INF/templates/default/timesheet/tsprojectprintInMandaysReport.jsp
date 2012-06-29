<%@ page import="java.util.Calendar"%>
<%@ include file="/common/header.jsp" %>

<style type="text/css">
.opacified {
	opacity:.5;
	-moz-opacity:.5;
	filter:alpha(opacity=50);
}
@media print {
	.inviPrint {
		display:none;
	}
}
</style>

<c:set var="w" value="${widget}" />
<c:set var="maxFractionDigits" value="2" />

<div id="printableRegion">
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
    <td class="contentTitleFont" colspan="2" style="padding: 5px 5px 5px 5px;">
	    <%
	        pageContext.setAttribute("today",Calendar.getInstance().getTime());
	    %>
    	<fmt:message key="timesheet.label.mandaysReport"/> - <fmt:formatDate value="${today}" pattern="${globalDateLong}"/>
    </td>
</tr>
<tr class="contentBgColor">
    <td style="padding: 5px 5px 5px 5px;">
    	<strong><fmt:message key="timesheet.label.project"/>:</strong> <c:out value="${w.project.projectName}"/>
    </td>
    <td align="right" style="padding: 5px 5px 5px 5px;">
    	<c:if test="${w.print==false}">
    		<input type="button" name="button" class="button inviPrint" value="<fmt:message key='timesheet.label.print'/>" onclick="javascript:window.print()" />
    	</c:if>
    </td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2">&nbsp;</td>
</tr>
<tr>
	<td class="contentBgColor" colspan="2">
        <table cellpadding="2" cellspacing="1">
            <tr>
                <td class="tableRow" nowrap><fmt:message key="project.error.startFrom"/>: </td>
                <td class="tableRow" nowrap><fmt:formatDate value="${widget.start}" pattern="${globalDateLong}" /></td>
            </tr>
            <tr>
                <td class="tableRow" nowrap><fmt:message key="project.error.endsAt"/>: </td>
                <td class="tableRow" nowrap><fmt:formatDate value="${widget.end}" pattern="${globalDateLong}" /></td>
            </tr>
        </table>
    </td>
</tr>
<c:set var="totalMandaysEstimatedThisProject" value="0" />
<c:set var="totalMandaysSpentThisProject" value="0" />

<form name="mandaysReportForm">
<tr>
	<td class="contentBgColor" colspan="2" align="right">
		<a name="status"></a><span id='updateRemarksSaveStatus' />
	</td>
</tr>
<tr class="contentBgColor">
	<td colspan="2" class="tableBackground">
		<table width="100%" cellpadding="2" cellspacing="1">
			<tr>
				<td class="tableHeader" valign="top"><strong><fmt:message key="timesheet.label.taskName"/></strong></td>
				<td class="tableHeader" valign="top"><strong><fmt:message key="timesheet.label.estimatedMandays"/></strong></td>
				<td class="tableHeader" valign="top"><strong><fmt:message key="timesheet.label.actualMandaysSpent" /></strong></td>
				<td class="tableHeader" valign="top"><strong><fmt:message key="timesheet.label.variance" /></strong></td>
				<!-- <td class="tableHeader" valign="top"><strong><fmt:message key="timesheet.label.balance" /></strong></td>   -->
				<td class="tableHeader" valign="top"><strong><fmt:message key="timesheet.label.remarks" /></strong></td>
			</tr>

			<!-- Tasks with Timesheet -->
			<c:forEach items="${w.task}" var="task">
				<c:if test="${!empty task}">
				<c:set var="totalMandaysEstimatedThisTask" value="${task.estimationMandays*task.totalAssignee}" />
				<c:set var="totalMandaysEstimatedEachUserThisTask" value="${task.estimationMandays}" />
				<c:set var="totalMandaysEstimatedThisProject" value="${totalMandaysEstimatedThisProject + totalMandaysEstimatedThisTask}" />
				<c:set var="totalMandaysSpentThisProject" value="${totalMandaysSpentThisProject + task.totalMandaysSpent}" />

				<tr>
					<!-- Tasks with Timesheet -->
					<td class="tableRow" valign="top" style="font-weight:bold">


						<c:out value="${task.title}"/>
					</td>
					<!-- estimated manday -->
					<td class="tableRow" align="right">
						<fmt:formatNumber value="${totalMandaysEstimatedThisTask}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
					</td>
					<!-- manday spent -->
					<td class="tableRow" align="right">
						<fmt:formatNumber value="${task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
					</td>
					<!-- variance -->
					<td class="tableRow" align="right">
					<c:if test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent < 0}">
								<span style="color:#CC0000">
							</c:if>
							<fmt:formatNumber value="${totalMandaysEstimatedThisTask - task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
							<c:if test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent < 0}">
								</span>
							</c:if>
						
					</td>
					<!-- balance -->
					<!--
					<td class="tableRow" align="right">
						<c:choose>
						<c:when test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent <= 0}">
							0.0
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${totalMandaysEstimatedThisTask - task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
						</c:otherwise>
						</c:choose>
					</td>
					-->
					<!-- remarks -->
					<td class="tableRow">
						<span id="remarksViewText_<c:out value="${task.id}"/>"><c:out value="${task.remarks}"/></span>
					</td>
				</tr>
				<c:forEach begin="0" end="${task.totalAssignee - 1}" var="userCount">
					<c:if test="${!empty task.userList[userCount][0]}">

					<tr>
						<!-- assignee name -->
						<td class="tableRow" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" style="padding-left:15px;" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:out value="${task.userList[userCount][1]}"/>
						</td>
						<!-- estimated manday -->
						<td class="tableRow" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<fmt:formatNumber value="${totalMandaysEstimatedEachUserThisTask}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
						</td>
						<!-- manday spent -->
						<td class="tableRow" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<fmt:formatNumber value="${task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
						</td>
						<!-- variance -->
						<td class="tableRow" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:if test="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount] < 0}">
								<span style="color:#CC0000">
							</c:if>
							<fmt:formatNumber value="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
							<c:if test="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount] < 0}">
								</span>
							</c:if>
						</td>
						<!-- balance -->
						<!--
						<td class="tableRow" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:choose>
							<c:when test="${task.totalMandaysEstimated - task.totalMandaysSpentEachUser[userCount] <= 0}">
								0.0
							</c:when>
							<c:otherwise>
								<fmt:formatNumber value="${task.totalMandaysEstimated - task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
							</c:otherwise>
							</c:choose>
						</td>
						-->
						<!-- remarks -->
						<td class="tableRow" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<span id="remarksViewText_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"><c:out value="${task.userSpecificRemarks[userCount]}"/></span>
						</td>
					</tr>
					</c:if>
				</c:forEach>
				</c:if>
			</c:forEach>

			<!-- Tasks without Timesheet -->
			<c:forEach items="${w.noTSTask}" var="task">
				<c:if test="${!empty task}">

	
				<c:set var="totalMandaysEstimatedThisTask" value="${task.estimationMandays*task.totalAssignee}" />
				<c:set var="totalMandaysEstimatedEachUserThisTask" value="${task.estimationMandays}" />
				<c:set var="totalMandaysEstimatedThisProject" value="${totalMandaysEstimatedThisProject + totalMandaysEstimatedThisTask}" />
				<c:set var="totalMandaysSpentThisProject" value="${totalMandaysSpentThisProject + task.totalMandaysSpent}" />

				<tr>
					<!-- task name -->
					<td class="tableRow" valign="top" style="font-weight:bold">
						<c:out value="${task.title}"/>
					</td>
					<!-- estimated manday -->
					<td class="tableRow" align="right">
					<fmt:formatNumber value="${totalMandaysEstimatedThisTask}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
					</td>


					<!-- manday spent -->
					<!--
					<td class="tableRow" align="right">
						<fmt:formatNumber value="0" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
					</td>

					-->

					<!-- manday spent -->
					<td class="tableRow" align="right">
						<fmt:formatNumber value="${task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />

					</td>

					<!-- variance -->
					<td class="tableRow" align="right">
					<c:if test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent < 0}">
								<span style="color:#CC0000">
							</c:if>
							<fmt:formatNumber value="${totalMandaysEstimatedThisTask - task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
							<c:if test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent < 0}">
								</span>
							</c:if>						
					</td>
					<!-- balance -->
					<!--
					<td class="tableRow" align="right">
						<c:choose>
						<c:when test="${totalMandaysEstimatedThisTask - task.totalMandaysSpent <= 0}">
							0.0
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${totalMandaysEstimatedThisTask - task.totalMandaysSpent}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
						</c:otherwise>
						</c:choose>
					</td>
					-->
					<!-- remarks -->
					<td class="tableRow">
						<span id="remarksViewText_<c:out value="${task.id}"/>"><c:out value="${task.remarks}"/></span>
					</td>
				</tr>
				<c:forEach begin="0" end="${task.totalAssignee - 1}" var="userCount">
					<c:if test="${!empty task.userList[userCount][0]}">

					<tr>
						<!-- assignee name -->
						<td class="tableRow" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" style="padding-left:15px;" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:out value="${task.userList[userCount][1]}"/>
						</td>
						<!-- estimated manday -->
						<td class="tableRow" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<fmt:formatNumber value="${totalMandaysEstimatedEachUserThisTask}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
						</td>
						<!-- manday spent -->
						<td class="tableRow" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<fmt:formatNumber value="${task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
						</td>
						<!-- variance -->
						<td class="tableRow" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:if test="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount] < 0}">
								<span style="color:#CC0000">
							</c:if>
							<fmt:formatNumber value="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
							<c:if test="${totalMandaysEstimatedEachUserThisTask - task.totalMandaysSpentEachUser[userCount] < 0}">
								</span>
							</c:if>
						</td>
						<!-- balance -->
						<!--
						<td class="tableRow" align="right" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<c:choose>
							<c:when test="${task.totalMandaysEstimated - task.totalMandaysSpentEachUser[userCount] <= 0}">
								0.0
							</c:when>
							<c:otherwise>
								<fmt:formatNumber value="${task.totalMandaysEstimated - task.totalMandaysSpentEachUser[userCount]}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
							</c:otherwise>
							</c:choose>
						</td>
						-->

						<!-- remarks -->
						<td class="tableRow" name="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>" id="td_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>">
							<span id="remarksViewText_<c:out value="${task.id}"/>_<c:out value="${task.userList[userCount][0]}"/>"><c:out value="${task.userSpecificRemarks[userCount]}"/></span>
						</td>
					</tr>
					</c:if>
				</c:forEach>
				</c:if>
			</c:forEach>

			<!-- Total -->
			<tr>
				<td class="tableRow" style="font-weight:bold">
					<fmt:message key="timesheet.label.total"/>
				</td>
				<td class="tableRow" align="right" style="font-weight:bold">
					<fmt:formatNumber value="${totalMandaysEstimatedThisProject}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
				</td>
				<td class="tableRow" align="right" style="font-weight:bold">
					<fmt:formatNumber value="${totalMandaysSpentThisProject}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
				</td>
				<td class="tableRow" align="right" style="font-weight:bold">
					<c:if test="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject < 0}">
						<span style="color:#CC0000">
					</c:if>
					<fmt:formatNumber value="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject}" maxFractionDigits="${maxFractionDigits}" pattern="#0.00" />
					<c:if test="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject < 0}">
						</span>
					</c:if>
				</td>
				<!--
				<td class="tableRow" align="right" style="font-weight:bold">
					<c:choose>
						<c:when test="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject <= 0}">
							0.0
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${totalMandaysEstimatedThisProject - totalMandaysSpentThisProject}" maxFractionDigits="${maxFractionDigits}" pattern="#0.0" />
						</c:otherwise>
					</c:choose>
				</td>
				-->
				<td class="tableRow">&nbsp;</td>
			</tr>
		</table>
	</td>
</tr>
</form>
</table>
</div>