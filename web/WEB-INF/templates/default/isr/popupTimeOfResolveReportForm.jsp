<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<table border="1" cellpadding="0" cellspacing="0" width="50%">
    <tr>
        <td>
            <%-- Show Data Table --%>
            <table border="0" cellpadding="2" cellspacing="1" width="100%">
                
                <%-- Show Headers --%>
                <thead>
                    <c:set var="colspan" value="0"/>
                    <tr class="tableHeader">
                        <td>
                        	<fmt:message key="isr.label.type"/>
                        </td>
                        <td>
                        	<fmt:message key="isr.label.noOfReq"/>
                        </td>
                    </tr>
                </thead>
                <%-- Show Body --%>
                <tbody>
                    
                    <c:forEach items="${w.result}" var="row" varStatus="status">
                        <tr class="tableRow">
                            <td valign="top" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
                            	<c:out value="${row.type }"/>
                            </td>
                            <td valign="top" height="25" style="border-bottom: solid #736F6E; border-bottom-width: 0.5px ">
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