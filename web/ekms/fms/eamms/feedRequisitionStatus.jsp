<%@ page import="kacang.Application,
                 com.tms.fms.eamms.model.EammsFeedsModule"%>
                 
<%@ include file="/common/header.jsp" %>
<%
    EammsFeedsModule mod = (EammsFeedsModule)Application.getInstance().getModule(EammsFeedsModule.class);

    String requestId = request.getParameter("requestId");
    String requestedDateStr = "";
    if(requestId != null && !requestId.equals(""))
    {
    	requestedDateStr = mod.getFeedsRequestedDate(requestId);
    }
    
    request.setAttribute("requestedDateStr", requestedDateStr);
%>

<c:if test="${!empty requestedDateStr && requestedDateStr ne ''}">
<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="100%">
    <tr>
        <td colspan="2" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="eamms.feed.msg.feedRequisitionStatus"/></font></b></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right">&nbsp;</td>
        <td width="70%" class="profileRow">
            <table>
		        <tr>
		          <td>
		            <fmt:message key="eamms.feed.msg.feedRequisitionStatusMsg"/> <%=requestedDateStr%>
		          </td>
		        </tr>
            </table>
        </td>
    </tr>
</table><br>
</c:if>