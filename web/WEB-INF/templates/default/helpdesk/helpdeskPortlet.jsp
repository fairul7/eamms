<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="hd" value="${widget}"/>

<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td class="bookmarkRow" align="center">

            <table cellpadding="2" cellspacing="1" width="95%">
                <tr>
                  <td class="bookmarkRow">
                    
                    <b>Number Of Incidents Reported Today</b>: <a href="/ekms/helpdesk/incidentList.jsp"><c:out value="${hd.numOfIncidentToday}"/></a>
                    <hr size="1">
                    <c:set var="icon" value="icon_mri"/>
                    [<a href="#" onclick="treeToggle('<c:out value="mri"/>'); return false"><span id="<c:out value="${icon}"/>">+</span></a>]
                    
                    <b>Most Recent Incidents</b>:
                    
                    <hr size="1">
                    <div id="<c:out value="mri"/>" style="display: none">
                    <script>
                    <!--
                        treeLoad('<c:out value="mri"/>');
                    //-->
                    </script>
                    <table width="100%">
                        <c:forEach items="${hd.recentIncident}" var="inc">
                            <c:set var="type" value="${row.key}"/>
                            <c:set var="count" value="${todayCountMap[type]}"/>
                            <tr>
                                <td>
                                    <fmt:formatDate pattern="${globalDatetimeLong}" value="${inc.created}" />
                                </td>
                                <td>
                                    <c:choose>
                                    	<c:when test="${inc.resolved}">
                                    		R
                                    	</c:when>
                                    	<c:otherwise>
                                    		U
                                    	</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    #<fmt:formatNumber pattern="####" maxIntegerDigits="4" minIntegerDigits="4" value="${inc.incidentCode}" />
                                </td>
                                <td>
                                    <a href="helpdesk/incidentOpen.jsp?incidentId=<c:out value='${inc.incidentId}'/>" title="<c:out value='${inc.companyName}'/>">
                                        <b><c:out value="${inc.subject}"/></b>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                        	<td align="left" colspan="4"><i>U - unsolved, R - resolved</i></td>
                        </tr>
                    </table>
                    </div>
                   </td>
               </tr>
               
               <tr>
                  <td class="bookmarkRow">
                    <c:set var="icon" value="icon_oui"/>
                    [<a href="#" onclick="treeToggle('<c:out value="oui"/>'); return false"><span id="<c:out value="${icon}"/>">+</span></a>]
                    
                    <b>Oldest Unresolved Incidents</b>:
                    <hr size="1">
                    <div id="<c:out value="oui"/>" style="display: none">
                    <script>
                    <!--
                        treeLoad('<c:out value="oui"/>');
                    //-->
                    </script>
                    <table width="100%">
                        <c:forEach items="${hd.oldestUnresolvedIncident}" var="inc">
                            <tr>
                                <td>
                                    <fmt:formatDate pattern="${globalDatetimeLong}" value="${inc.created}" />
                                </td>
                                <td>
                                    #<fmt:formatNumber pattern="####" maxIntegerDigits="4" minIntegerDigits="4" value="${inc.incidentCode}" />
                                </td>
                                <td>
                                    <a href="helpdesk/incidentOpen.jsp?incidentId=<c:out value='${inc.incidentId}'/>" title="<c:out value='${inc.companyName}'/>">
                                        <b><c:out value="${inc.subject}"/></b>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    </div>
                </td>
               </tr>
               
               <tr>
                  <td class="bookmarkRow">
                    <c:set var="icon" value="icon_lui"/>
                    [<a href="#" onclick="treeToggle('<c:out value="lui"/>'); return false"><span id="<c:out value="${icon}"/>">+</span></a>]
                    
                    <b>Latest Unresolved Incidents</b>:
                    <hr size="1">
                    <div id="<c:out value="lui"/>" style="display: none">
                    <script>
                    <!--
                        treeLoad('<c:out value="lui"/>');
                    //-->
                    </script>
                    <table width="100%">
                        <c:forEach items="${hd.latestUnresolvedIncident}" var="inc">
                            <tr>
                                <td>
                                    <fmt:formatDate pattern="${globalDatetimeLong}" value="${inc.created}" />
                                </td>
                                <td>
                                    #<fmt:formatNumber pattern="####" maxIntegerDigits="4" minIntegerDigits="4" value="${inc.incidentCode}" />
                                </td>
                                <td>
                                    <a href="helpdesk/incidentOpen.jsp?incidentId=<c:out value='${inc.incidentId}'/>" title="<c:out value='${inc.companyName}'/>">
                                        <b><c:out value="${inc.subject}"/></b>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    </div>
                </td>
               </tr>
               
               <tr>
                  <td class="bookmarkRow">
                    <c:set var="icon" value="icon_ouirbm"/>
                    [<a href="#" onclick="treeToggle('<c:out value="ouirbm"/>'); return false"><span id="<c:out value="${icon}"/>">+</span></a>]
                                        
                    <b>Oldest Unresolved Incidents Reported By Me</b>:
                    <hr size="1">
                    <div id="<c:out value="ouirbm"/>" style="display: none">
                    <script>
                    <!--
                        treeLoad('<c:out value="ouirbm"/>');
                    //-->
                    </script>
                    <table width="100%">
                        <c:forEach items="${hd.oldestUnresolvedIncidentByMe}" var="inc">
                            <tr>
                                <td>
                                    <fmt:formatDate pattern="${globalDatetimeLong}" value="${inc.created}" />
                                </td>
                                <td>
                                    #<fmt:formatNumber pattern="####" maxIntegerDigits="4" minIntegerDigits="4" value="${inc.incidentCode}" />
                                </td>
                                <td>
                                    <a href="helpdesk/incidentOpen.jsp?incidentId=<c:out value='${inc.incidentId}'/>" title="<c:out value='${inc.companyName}'/>">
                                        <b><c:out value="${inc.subject}"/></b>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    </div>

                  </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td class="portletFooter">&nbsp;
    <input class="button" value="New Incident" type="button" onClick="location.href='<c:out value="/ekms/helpdesk/incidentCompany.jsp"/>'"/>
    </td></tr>
</table>