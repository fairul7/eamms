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
		        <td class="fieldTitle"><fmt:message key="isr.label.status"/></td>
		        <td >
		        	<x:display name="${w.sbStatus.absoluteName }"/>
		        </td>
		    </tr>
		    <tr>
		        <td class="fieldTitle"><fmt:message key="isr.label.dept"/></td>
		        <td >
		        	<c:out value="${w.departmentName }"/>
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

<table border="0" cellpadding="4" cellspacing="4" width="100%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="2" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr height="25">
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.type"/>
                        </td>
                        <td class="tableHeader" width="30%">
                        	<fmt:message key="isr.label.noOfReq"/>
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    
                    <c:forEach items="${w.result}" var="row" varStatus="status">
                        <tr class="tableRow" height="25">
                            <td valign="top">
                            	<a href="#" onclick="javascript:window.open('popupTimeOfResolveReportDetail.jsp?type=<c:out value='${row.reportLink}'/>&statusId=<c:out value='${w.statusId}'/>')">
                            	<c:out value="${row.type }"/>
                            	</a>
                            </td>
                            <td valign="top">
                            	<c:out value="${row.noOfReq }"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>