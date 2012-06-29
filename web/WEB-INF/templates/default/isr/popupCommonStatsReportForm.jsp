<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>



<jsp:include page="../form_header.jsp" flush="true"/>

<table border="1" cellpadding="0" cellspacing="0" width="50%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="1" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr class="tableHeader">
                        <td>
                        	<fmt:message key="isr.label.priority"/>
                        </td>
                        <td>
                        	<fmt:message key="isr.label.noOfReq"/>
                        </td>
                        <td>
                        	%
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    
                    <c:forEach items="${w.priorityCol}" var="row" varStatus="status">
                    <c:set var="priority" value="${row.noOfReq/w.totalPriority*100}" />
                        <tr class="tableRow">
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.priority }"/>
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.noOfReq }"/>
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            <fmt:formatNumber value="${priority}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>
<br/><br/>
<table border="1" cellpadding="0" cellspacing="0" width="50%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="1" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr class="tableHeader">
                        <td>
                        	<fmt:message key="isr.label.status"/>
                        </td>
                        <td>
                        	<fmt:message key="isr.label.noOfReq"/>
                        </td>
                        <td>
                        	%
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    
                    <c:forEach items="${w.statusCol}" var="row" varStatus="status">
                    <c:set var="status" value="${row.noOfReq/w.totalStatus*100}" />
                        <tr class="tableRow">
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.status }"/>
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.noOfReq }"/>
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            <fmt:formatNumber value="${status}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>
<br/><br/>
<table border="1" cellpadding="0" cellspacing="0" width="50%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="1" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr class="tableHeader">
                        <td>
                        	<fmt:message key="isr.label.requestType"/>
                        </td>
                        <td>
                        	<fmt:message key="isr.label.noOfReq"/>
                        </td>
                        <td>
                        	%
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    
                    <c:forEach items="${w.requestTypeCol}" var="row" varStatus="status">
                    <c:set var="request" value="${row.noOfReq/w.totalRequestType*100}" />
                        <tr class="tableRow">
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.requestType }"/>
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.noOfReq }"/>
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            <fmt:formatNumber value="${request}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>
<br/><br/>
<table border="1" cellpadding="0" cellspacing="0" width="50%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="1" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr class="tableHeader">
                        <td>
                        	<fmt:message key="isr.label.deptName"/>
                        </td>
                        <td>
                        	<fmt:message key="isr.label.noOfReqMade"/>
                        </td>
                        <td>
                        	%
                        </td>
                        <td>
                        	<fmt:message key="isr.label.noOfReqRec"/>
                        </td>
                        <td>
                        	%
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    
                    <c:forEach items="${w.deptCol}" var="row" varStatus="status">
                    	<c:set var="deptRequest" value="${row.reqDeptNoOfReq/w.totalDeptReq*100}" />
                        <c:set var="deptReceive" value="${row.recDeptNoOfReq/w.totalDeptRec*100}" />
                        <tr class="tableRow" >
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.dept }"/>
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.reqDeptNoOfReq }"/>
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            <fmt:formatNumber value="${deptRequest}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.recDeptNoOfReq }"/>
                            </td>
                            <td valign="middle" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            <fmt:formatNumber value="${deptReceive}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>