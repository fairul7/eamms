<%@ page import="kacang.Application,
                 kacang.services.security.SecurityService"%>
<%@ include file="/common/header.jsp"%>

<%
    Application app = Application.getInstance();
    SecurityService service = (SecurityService) app.getService(SecurityService.class);
    String userId = service.getCurrentUser(request).getId();
    String workflowURL = app.getInstance().getProperty("com.tms.workflow.host");
    
    boolean wfAdmin = service.hasPermission(userId, "com.tms.workflow.WorkflowAdministrator", null, null);
    boolean wfUsr = service.hasPermission(userId, "com.tms.workflow.WorkflowUser", null, null);
    boolean staffWorkload = service.hasPermission(userId, "com.tms.workflow.permission.staffWorkload", null, null);
    
    boolean woNew = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.newWo", null, null);
    boolean woAssigned = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woAssigned", null, null);
    boolean woMy = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woMy", null, null);
    boolean woAll = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woAll", null, null);
    
    boolean facilityList = service.hasPermission(userId, "com.tms.workflow.permission.viewFacilitySetup", null, null);
    boolean facilityManage = service.hasPermission(userId, "com.tms.workflow.permission.manageFacility", null, null);
    
    boolean hwViewList = service.hasPermission(userId, "com.tms.workflow.permission.viewHardwareListing", null, null);
    boolean hwManage = service.hasPermission(userId, "com.tms.workflow.permission.manageHardware", null, null); 
    boolean swViewList = service.hasPermission(userId, "com.tms.workflow.permission.viewSoftwareListing", null, null);
    boolean swManage = service.hasPermission(userId, "com.tms.workflow.permission.manageSoftware", null, null); 
    boolean supplierViewList = service.hasPermission(userId, "com.tms.workflow.permission.viewSupplier", null, null);
    boolean supplierManage = service.hasPermission(userId, "com.tms.workflow.permission.manageSupplier", null, null);   
    boolean sparePartViewList = service.hasPermission(userId, "com.tms.workflow.permission.viewSparePart", null, null);
    boolean sparePartManage = service.hasPermission(userId, "com.tms.workflow.permission.manageSparePart", null, null);
    
    /*boolean rentalSubmit = service.hasPermission(userId, "com.tms.workflow.permission.submitRentalRequest", null, null);
    boolean rentalVerifyApprove = service.hasPermission(userId, "com.tms.workflow.permission.verifyApproveAssign", null, null);
    boolean rentalViewUpdate = service.hasPermission(userId, "com.tms.workflow.permission.viewUpdate", null, null);
    boolean rentalUpdateRental = service.hasPermission(userId, "com.tms.workflow.permission.updateRentalRequest", null, null);*/
    boolean rentalViewRequest = service.hasPermission(userId, "com.tms.workflow.permission.viewRentalRequest", null, null);
    boolean rentalReassign = service.hasPermission(userId, "com.tms.workflow.permission.reassignEngineer", null, null);
    
    boolean pmNew = service.hasPermission(userId, "com.tms.workflow.permission.newPM", null, null);
    boolean pmListAssigned = service.hasPermission(userId, "com.tms.workflow.permission.pmAssignedToMe", null, null);
    boolean pmListMy = service.hasPermission(userId, "com.tms.workflow.permission.myPMListing", null, null);
    boolean pmListAll = service.hasPermission(userId, "com.tms.workflow.permission.allPMListing", null, null);
%>

<script>
function noPermission1()
{
    alert('<fmt:message key="com.tms.workflow.noPermission"/>');
    window.close();
}
</script>

<c-rt:set var="wurl" value="<%= workflowURL %>"/>
<c:choose>
    <c:when test="${param.addr eq 'myTask'}">
        <c:set var="address1" value="${wurl}/jw/web/console/home"/>
        <c:if test="<%=!wfAdmin && !wfUsr%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'workLoad'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/work_order_userview//staff_workload_list"/>
        <c:if test="<%=!staffWorkload%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'wo01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/work_order_userview//wo_new"/>
        <c:if test="<%=!woNew%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'wo02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/work_order_userview//wo_list_assigned"/>
        <c:if test="<%=!woAssigned%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'wo03'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/work_order_userview//wo_list_my"/>
        <c:if test="<%=!woMy%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'wo04'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/work_order_userview//wo_list_all"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sw01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//listingSoftware_read_only"/>
        <c:if test="<%=!swViewList%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sw02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//listingSoftware"/>
        <c:if test="<%=!swManage%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>   
   <c:when test="${param.addr eq 'hw01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//hardwareViewList"/>
        <c:if test="<%=!hwViewList%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'hw02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//hardware"/>
        <c:if test="<%=!hwManage%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'fs01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//facility_list_view_only"/>
        <c:if test="<%=!facilityList%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'fs02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//facility_list"/>
        <c:if test="<%=!facilityManage%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'ss01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/supplierSetup//supplierList_read_only"/>
        <c:if test="<%=!supplierViewList%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'ss02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/supplierSetup//supplierList"/>
        <c:if test="<%=!supplierManage%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    
    <c:when test="${param.addr eq 'sp01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spList"/>
        <c:if test="<%=!sparePartManage%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spListLow"/>
        <c:if test="<%=!sparePartManage%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp03'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spListHigh"/>
        <c:if test="<%=!sparePartManage%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp04'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spInventoryHistoryList"/>
        <c:if test="<%=!sparePartManage%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp05'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//sparePart_readonly"/>
        <c:if test="<%=!sparePartViewList%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp06'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spListLow_readonly"/>
        <c:if test="<%=!sparePartViewList%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp07'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spListHigh_readonly"/>
        <c:if test="<%=!sparePartViewList%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp08'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spInventoryHistoryList"/>
        <c:if test="<%=!sparePartViewList%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'rs01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//"/>
        <c:if test="<%=!rentalViewRequest%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'rs02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//"/>
        <c:if test="<%=!rentalReassign%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'pm01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/preventiveMaintenance//pmForm"/>
        <c:if test="<%=!pmNew%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'pm02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/preventiveMaintenance//assignedPMList"/>
        <c:if test="<%=!pmListAssigned%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'pm03'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/preventiveMaintenance//myPMList"/>
        <c:if test="<%=!pmListMy%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'pm04'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/preventiveMaintenance//allPMList"/>
        <c:if test="<%=!pmListAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    
    <c:otherwise>
        <script>
            window.close();
        </script>
    </c:otherwise>
</c:choose>

<html>
    <head>
        <script language="JavaScript" type="text/javascript" src="/ekms/includes/jquery-1.4.2.min.js"></script>
        <script>
            $(function() {
                var widthRatio = $('#ifrm').width() / $(window).width();
                $(window).resize(function() {
                    $('#ifrm').css({width: $(window).width() * widthRatio});
                }); 
            });

            $(function() {
                var heightRatio = $('#ifrm').height() / $(window).height();
                $(window).resize(function() {
                    $('#ifrm').css({height: $(window).height() * heightRatio});
                }); 
            });
        </script>
    </head>
    <body onload=reSize()>
       <iframe id=ifrm name="inlineframe" src="${address1}" frameborder="0" scrolling="auto" width="995" height="595" marginwidth="5" marginheight="5" ></iframe>
    </body>
</html>


