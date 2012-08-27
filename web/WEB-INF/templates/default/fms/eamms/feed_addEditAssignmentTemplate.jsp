<%@ page import="com.tms.fms.eamms.ui.Feed_AddEditAssignmentDetails"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath}/jQuery/jquery-1.6.4.min.js'/>"></script>

<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>

<c:set var="mode_add" value="<%=Feed_AddEditAssignmentDetails.MODE_ADD%>"/>
<c:set var="mode_edit" value="<%=Feed_AddEditAssignmentDetails.MODE_EDIT%>"/>
<c:set var="mode_network" value="<%=Feed_AddEditAssignmentDetails.MODE_NETWORK%>"/>

<table>
    <c:if test="${mode_edit eq form.mode || mode_network eq form.mode}">
        <tr>
	        <td><x:display name="${form.childMap.pane.absoluteName}.assignmentIdLab"/></td>
	        <td><x:display name="${form.childMap.pane.absoluteName}.assignmentId"/></td>
	    </tr>
    </c:if>
    <c:if test="${mode_add eq form.mode || mode_edit eq form.mode}">
	    <tr>
	        <td><x:display name="${form.childMap.pane.absoluteName}.feedTitleLab"/></td>
	        <td><x:display name="${form.childMap.pane.absoluteName}.feedTitleSb"/></td>
	    </tr>
	    <tr>
	        <td><div id="d_fr_lab"><x:display name="${form.childMap.pane.absoluteName}.requiredDateFrLab"/></div></td>
	        <td><div id="d_fr"><x:display name="${form.childMap.pane.absoluteName}.requiredDateFr"/></div></td>
	    </tr>
	    <tr>
	        <td><div id="d_to_lab"><x:display name="${form.childMap.pane.absoluteName}.requiredDateToLab"/></div></td>
	        <td><div id="d_to"><x:display name="${form.childMap.pane.absoluteName}.requiredDateTo"/></div></td>
	    </tr>
	    <tr>
	        <td><div id="d_req_lab"><x:display name="${form.childMap.pane.absoluteName}.requiredDateLab"/></div></td>
	        <td><div id="d_req"><x:display name="${form.childMap.pane.absoluteName}.requiredDate"/></div></td>
	    </tr>
	    <tr>
	        <td><x:display name="${form.childMap.pane.absoluteName}.requiredTimeLab"/></td>
	        <td><x:display name="${form.childMap.pane.absoluteName}.requiredTimePane"/></td>
	    </tr>
	    <tr>
	        <td><x:display name="${form.childMap.pane.absoluteName}.timeZoneSbLab"/></td>
	        <td><x:display name="${form.childMap.pane.absoluteName}.timeZoneSb"/></td>
	    </tr>
	    <tr>
	        <td><x:display name="${form.childMap.pane.absoluteName}.totalReqTimeLab"/></td>
	        <td><x:display name="${form.childMap.pane.absoluteName}.totalReqTimeSbPane"/></td>
	    </tr>
	    <tr>
	        <td><x:display name="${form.childMap.pane.absoluteName}.remarksLab"/></td>
	        <td><x:display name="${form.childMap.pane.absoluteName}.remarks"/></td>
	    </tr>
	    <tr>
	        <td><x:display name="${form.childMap.pane.absoluteName}.dummyLab1"/></td>
	        <td><x:display name="${form.childMap.pane.absoluteName}.buttonPane"/></td>
		</tr>
    </c:if>
    <c:if test="${mode_network eq form.mode}">
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.feedTitleLab"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.feedTitle_nw"/></td>
        </tr>
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.requiredDateLab"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.requiredDate_nw"/></td>
        </tr>
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.requiredTimeLab"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.requiredTime_nw"/></td>
        </tr>
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.totalReqTimeLab"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.totalTimeReq_nw"/></td>
        </tr>
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.remarksLab"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.remarks_nw"/></td>
        </tr>
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.bookingStatusLab"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.bookingStatusSb"/></td>
        </tr>
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.networkRemarksLab"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.networkRemarks"/></td>
        </tr>
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.attchmentLab"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.attchPane"/></td>
        </tr>
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.statusSbLab"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.statusSb"/></td>
        </tr>
        <tr>
            <td><x:display name="${form.childMap.pane.absoluteName}.dummyLab1"/></td>
            <td><x:display name="${form.childMap.pane.absoluteName}.update"/></td>
        </tr>
     </c:if>
</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>

<script>
    function changeDateInput(obj)
    {
        var d_fr_lab = document.getElementById('d_fr_lab');
        var d_fr = document.getElementById('d_fr');
        var d_to_lab = document.getElementById('d_to_lab');
        var d_to = document.getElementById('d_to');
        var d_req_lab = document.getElementById('d_req_lab');
        var d_req = document.getElementById('d_req');

         $.ajax({
                url:"/tvro/blockbooking?tvroServiceId=" + $(obj).val(),
                success: function(response)
                {
                    if(response == '1')
                    {
                    	d_req_lab.style.display = 'none';
                    	d_req.style.display = 'none';
                    	
                    	d_fr_lab.style.display = 'block';
                    	d_fr.style.display = 'block';
                    	
                    	d_to_lab.style.display = 'block';
                    	d_to.style.display = 'block';
                    }
                    else
                    {
                    	d_req_lab.style.display = 'block';
                        d_req.style.display = 'block';
                        
                        d_fr_lab.style.display = 'none';
                        d_fr.style.display = 'none';
                        
                        d_to_lab.style.display = 'none';
                        d_to.style.display = 'none';
                    }
                }
         });
    }

    $(document).ready(function() 
    {       
        var tvroSb = document.getElementsByName('<c:out value="${form.childMap.pane.absoluteName}"/>.feedTitleSb')[0];
    	changeDateInput(tvroSb);
	});
</script>

