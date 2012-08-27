<%@ page import="com.tms.fms.eamms.ui.Feed_AddEditAssignmentDetails, com.tms.fms.eamms.ui.FeedRequisitionForm"%>
<%@include file="/common/header.jsp" %>

<c:set var="feedReqMode" value="${widgets['eamms_feedRequisition.form'].mode}"/>
<c:set var="eng_coor" value="<%=FeedRequisitionForm.ENGINEERING_COORDINATOR_VIEW%>"/>
<c:set var="network_Head" value="<%=FeedRequisitionForm.UNIT_HEAD_NET_VEIW%>"/>

<c:if test="${feedReqMode ne eng_coor && feedReqMode ne network_Head }">
    <script>
        window.close();
        document.location="/ekms/cmsadmin/noPermission.jsp";
    </script>
</c:if>

<x:config>
     <page name="eamms_add_assignment_details">
        <com.tms.fms.eamms.ui.Feed_AddEditAssignmentDetails name="form" width="100%"/>
     </page>
</x:config>
<x:set name="eamms_add_assignment_details.form" property="assignId" value="<%=null%>"/>
<x:set name="eamms_add_assignment_details.form" property="feedsDetailsId" value="${param.feedsDetailsId}"/>
<x:set name="eamms_add_assignment_details.form" property="mode" value="<%=Feed_AddEditAssignmentDetails.MODE_ADD%>"/>
<x:set name="eamms_add_assignment_details.form" property="blockBooking" value="<%=null%>"/>

<c:if test="${forward.name=='outOfrequiredDateRange'}" >
    <script>
        alert('<fmt:message key="eamms.feed.list.alert.msg.outOfrequiredDateRange"/>');
    </script>
</c:if>

<c:if test="${forward.name=='continue'}" >
    <script>
        window.opener.location='feedRequisitionForm.jsp?requestId=${widgets['eamms_feedRequisition.form'].requestId}'
        alert('<fmt:message key="eamms.feed.list.alert.msg.added"/>');
    </script>
</c:if>

<c:if test="${forward.name=='submitted'}" >
    <script>
        window.opener.location='feedRequisitionForm.jsp?requestId=${widgets['eamms_feedRequisition.form'].requestId}'
        alert('<fmt:message key="eamms.feed.list.alert.msg.added"/>');
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='cancel_form_action'}" >
    <script>
        window.close();
    </script>
</c:if>

<%@include file="/ekms/includes/linkCSS.jsp" %>
  
<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
    <tr valign="MIDDLE">
        <td height="22" class="contentTitleFont">
            &nbsp;<fmt:message key='eamms.feed.list.msg.addAssignmentDetails'/>  
        </td>
        <td align="right" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" class="contentBgColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">
        </td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" class="contentBgColor">
            <x:display name="eamms_add_assignment_details.form" ></x:display>
        </td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" class="contentBgColor">
            <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
    </tr>
</table>

<script>
	/*function changeDateInput(obj)
	{
	    //alert(obj.value);
	    var tr = document.getElementById('tr_0');
	    alert(tr.innerHTML);
	    
		var date = document.getElementsByName('eamms_add_assignment_details.form.pane.requiredDate')[0];
		var dateFr = document.getElementsByName('eamms_add_assignment_details.form.pane.requiredDateFr')[0];
		var dateTo = document.getElementsByName('eamms_add_assignment_details.form.pane.requiredDateTo')[0];

		 $.ajax({
	            url:"/tvro/blockbooking?tvroServiceId=" + $(obj).val(),
	            success: function(response)
	            {
		            if(response == '1')
		            {
		            	date.style.display = 'none';
		            	dateFr.style.display = 'block';
		            	dateTo.style.display = 'block';
		            }
		            else
		            {
		            	date.style.display = 'block';
                        dateFr.style.display = 'none';
                        dateTo.style.display = 'none';
		            }
	            }
	     });
	}*/
</script>

