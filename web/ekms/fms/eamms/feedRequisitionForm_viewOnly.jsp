<%@ page import="com.tms.fms.eamms.ui.FeedRequisitionForm"%>
<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<%
//TODO : permission
%>

<x:config>
  <page name="eamms_feedRequisition">
    <com.tms.fms.eamms.ui.FeedRequisitionForm name="view" width="100%"/>
  </page>
</x:config>

<c:if test="${!empty param.requestId}">
   <x:set name="eamms_feedRequisition.view" property="requestId" value="${param.requestId}"/>
</c:if>
<x:set name="eamms_feedRequisition.view" property="mode" value="<%=FeedRequisitionForm.READ_ONLY%>"/>

<c:if test="${forward.name=='cancel_form_action'}" >
    <c:choose>
	    <c:when test="${param.history eq '1'}">
		    <script>
		        document.location='feedHistory.jsp'
		    </script>
	    </c:when>
	    <c:otherwise>
	        <script>
                document.location='allFeedsListing.jsp'
            </script>
	    </c:otherwise>
    </c:choose>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="eamms.feed.list.msg.incomingVisualFeedReqForm"/>
            </font></b>
        </td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">
        </td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="eamms_feedRequisition.view"/>
    
    </td></tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15">
        </td>
    </tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>

