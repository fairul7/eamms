<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<table cellpadding="2" cellspacing="0" width="100%">
<tr>
    <td>
		<table width="100%" cellpadding="4" cellspacing="1" align=center>
		    <tr>
		        <td class="fieldTitle"><fmt:message key="isr.label.dateRange"/></td>
		        <td >
		        	<x:display name="${w.fromDate.absoluteName }"/>
		        	&nbsp;&nbsp;<b><fmt:message key="general.to" /></b>&nbsp;&nbsp;
		        	<x:display name="${w.toDate.absoluteName }"/>
		        </td>
		    </tr>
		    <tr>
		        <td></td>
		        <td >
		        	<x:display name="${w.btnSubmit.absoluteName }"/>
		        	<x:display name="${w.btnPrint.absoluteName }"/>
		        	<input type="button" class="button" name="exportCsv" value="<fmt:message key="isr.label.exportToCsv"/>" onclick="javascript:generateCsv()" />
		        </td>
		    </tr>
		</table>
    </td>
</tr>
</table>

<fmt:message key="isr.label.printReportNote"/><br/>
		* <fmt:message key="isr.label.reportStarNote"/><br/>

<!-- Priority Stats -->
<table border="0" cellpadding="4" cellspacing="4" width="100%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="2" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr class="tableHeader" height="25">
                        <td class="tableHeader" width="20%">
                        	<fmt:message key="isr.label.priority"/>
                        </td>
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.noOfReq"/>
                        </td>                       
                        <td class="tableHeader">
                        	%
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    <c:forEach items="${w.priorityCol}" var="row" varStatus="status">
                    <c:set var="priority" value="${row.noOfReq/w.totalPriority*100}" />
                        <tr class="tableRow" height="25">
                            <td valign="top">
                            	<c:out value="${row.priority }"/>
                            </td>
                            <td valign="top">
                            	<c:out value="${row.noOfReq }"/>
                            </td>
                            <td valign="top">
                            <fmt:formatNumber value="${priority}" maxFractionDigits="2" pattern="#0.00" />%
     
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>

<!-- Status Stats -->
<table border="0" cellpadding="4" cellspacing="4" width="100%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="2" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr class="tableHeader" height="25">
                        <td class="tableHeader" width="20%">
                        	<fmt:message key="isr.label.status"/>
                        </td>
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.noOfReq"/>
                        </td>
                        <td class="tableHeader">
                        	%
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    
                    <c:forEach items="${w.statusCol}" var="row" varStatus="status">
                        <tr class="tableRow" height="25">
                        <c:set var="status" value="${row.noOfReq/w.totalStatus*100}" />
                            <td valign="top">
                            	<c:out value="${row.status }"/>
                            </td>
                            <td valign="top">
                            	<c:out value="${row.noOfReq }"/>
                            </td>
                            <td valign="top">
                            <fmt:formatNumber value="${status}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>

<!-- Request Type Stats -->
<table border="0" cellpadding="4" cellspacing="4" width="100%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="2" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr class="tableHeader" height="25">
                        <td class="tableHeader" width="20%">
                        	<fmt:message key="isr.label.requestType"/>
                        </td>
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.noOfReq"/>
                        </td>
                        <td class="tableHeader">
                        	%
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    <c:forEach items="${w.requestTypeCol}" var="row" varStatus="status">
                        <tr class="tableRow" height="25">
                        <c:set var="request" value="${row.noOfReq/w.totalRequestType*100}" />
                            <td valign="top">
                            	<c:out value="${row.requestType }"/>
                            </td>
                            <td valign="top">
                            	<c:out value="${row.noOfReq }"/>
                            </td>
                            <td valign="top">
                            <fmt:formatNumber value="${request}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>

<!-- Dept Stats -->
<table border="0" cellpadding="4" cellspacing="4" width="100%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="2" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr class="tableHeader" height="25">
                        <td class="tableHeader" width="40%">
                        	<fmt:message key="isr.label.deptName"/>
                        </td>
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.noOfReqMade"/>
                        </td>
                        <td class="tableHeader">
                        	%
                        </td>
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.noOfReqRec"/>
                        </td>
                        <td class="tableHeader">
                        	%
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>   
                    <c:forEach items="${w.deptCol}" var="row" varStatus="status">
                        <tr class="tableRow" height="25">
                        <c:set var="deptRequest" value="${row.reqDeptNoOfReq/w.totalDeptReq*100}" />
                        <c:set var="deptReceive" value="${row.recDeptNoOfReq/w.totalDeptRec*100}" />
                            <td valign="top">
                            	<c:out value="${row.dept }"/>
                            </td>
                            <td valign="top">
                            	<c:out value="${row.reqDeptNoOfReq }"/>
                            </td>
                            <td valign="top">
                            <fmt:formatNumber value="${deptRequest}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                            <td valign="top">
                            	<c:out value="${row.recDeptNoOfReq }"/>
                            </td>
                            <td valign="top">
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