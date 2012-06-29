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
		        	<fmt:message key="isr.label.to"/>
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
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                        <td align="right" valign="top" class="tableHeader">
                        <fmt:message key="isr.label.status"/>
                        </td>
                        </tr>
                        <tr>
                        <td align="left" valign="bottom" class="tableHeader">
                        <fmt:message key="isr.label.priority"/>
                        </td>
                        </tr>
                        </table>                       	
                        </td>
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.statusNew"/>
                        </td>                       
                        <td class="tableHeader" align="right">
                        	%
                        </td>
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.statusInProgress"/>
                        </td>                       
                        <td class="tableHeader" align="right">
                        	%
                        </td><td class="tableHeader">
                        	<fmt:message key="isr.label.statusClarification"/>
                        </td>                       
                        <td class="tableHeader" align="right">
                        	%
                        </td>
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.statusClosed"/>
                        </td>                       
                        <td class="tableHeader" align="right">
                        	%
                        </td>
                        <td class="tableHeader">
                        	<fmt:message key="isr.label.statusResolved"/>
                        </td>                       
                        <td class="tableHeader" align="right">
                        	%
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    <c:forEach items="${w.priority}" var="row" varStatus="status">               
                    <c:set var="totalNew" value="${row.newTotalReq/w.newNo*100}" />
                    <c:if test="${w.newNo==0}">
                    <c:set var="totalNew" value="${row.newTotalReq/1*100}" />
                    </c:if>
                    <c:set var="totalProgress" value="${row.progressTotalReq/w.progressNo*100}" />
                    <c:if test="${w.progressNo==0}">
                    <c:set var="totalProgress" value="${row.progressTotalReq/1*100}" />
                    </c:if>
                    <c:set var="totalClass" value="${row.clarificationTotalReq/w.classificationNo*100}" />
                    <c:if test="${w.classificationNo==0}">
                    <c:set var="totalClass" value="${row.clarificationTotalReq/1*100}" />
                    </c:if>
                    <c:set var="totalresolved" value="${row.resolvedTotalReq/w.resolvedNo*100}" />
                    <c:if test="${w.resolvedNo==0}">
                    <c:set var="totalresolved" value="${row.resolvedTotalReq/1*100}" />
                    </c:if>
                    <c:set var="totalclosed" value="${row.closedTotalReq/w.closedNo*100}" />
                    <c:if test="${w.closedNo==0}">
                    <c:set var="totalclosed" value="${row.closedTotalReq/1*100}" />
                    </c:if>
                        <tr class="tableRow" height="25">
                            <td valign="top">
                            	<c:out value="${row.configDetailName }"/>
                            </td>
                            <td valign="top">
                            	<c:out value="${row.newTotalReq }"/>
                            </td>
                            <td valign="top" align="right">
                            <fmt:formatNumber value="${totalNew}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                            <td valign="top">
                            	<c:out value="${row.progressTotalReq }"/>
                            </td>
                            <td valign="top" align="right">
                            <fmt:formatNumber value="${totalProgress}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                            
                            <td valign="top">
                            	<c:out value="${row.clarificationTotalReq }"/>
                            </td>
                            <td valign="top" align="right">
                            <fmt:formatNumber value="${totalClass}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                            <td valign="top">
                            	<c:out value="${row.closedTotalReq }"/>
                            </td>
                            <td valign="top" align="right">
                            <fmt:formatNumber value="${totalclosed}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                            <td valign="top">
                            	<c:out value="${row.resolvedTotalReq }"/>
                            </td>
                            <td valign="top" align="right">
                            <fmt:formatNumber value="${totalresolved}" maxFractionDigits="2" pattern="#0.00" />%    
                            </td>
                            
                        </tr>
                    </c:forEach>
                    <tr class="tableRow" height="25">
                            <td valign="top">
                            	<b><fmt:message key="isr.label.total"/></b>
                            </td>
                            <td valign="top">
                            	<b><c:out value="${w.newNo }"/></b>
                            </td>
                            <td valign="top" align="right">
                            <b>100.00%</b>      
                            </td>
                            <td valign="top">
                            	<b><c:out value="${w.progressNo }"/></b>
                            </td>
                            <td valign="top" align="right">
                            <b>100.00%</b>      
                            </td>
                            
                            <td valign="top">
                            	<b><c:out value="${w.classificationNo }"/></b>
                            </td>
                            <td valign="top" align="right">
                            <b>100.00%</b>     
                            </td>
                            <td valign="top">
                            	<b><c:out value="${w.closedNo }"/></b>
                            </td>
                            <td valign="top" align="right">
                            <b>100.00%</b>    
                            </td>
                            <td valign="top">
                            	<b><c:out value="${w.resolvedNo }"/></b>
                            </td>
                            <td valign="top" align="right">
                            <b>100.00%</b>   
                            </td>
                            
                        </tr>
                </tbody>
            </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>