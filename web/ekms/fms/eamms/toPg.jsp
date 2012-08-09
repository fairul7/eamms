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
    
    boolean woNew = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.newWo", null, null);
    boolean woAssigned = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woAssigned", null, null);
    boolean woMy = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woMy", null, null);
    boolean woAll = service.hasPermission(userId, "com.tms.workflow.workOrder.permission.woAll", null, null);
    
    boolean facilityList = service.hasPermission(userId, "com.tms.workflow.permission.viewFacilitySetup", null, null);
    boolean facilityManage = service.hasPermission(userId, "com.tms.workflow.permission.manageFacility", null, null);
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
        <c:set var="address1" value="${wurl}/jw/web/console/home"/>
        <c:if test="<%=!wfAdmin && !wfUsr%>">
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
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//listingSoftware"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sw02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//listingSoftware"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>   
   <c:when test="${param.addr eq 'hw01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//hardwareViewList"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'hw02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//hardware"/>
        <c:if test="<%=!woAll%>">
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
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/supplierSetup//supplierList"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'ss02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/supplierSetup//supplierList"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    
    <c:when test="${param.addr eq 'sp01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spList"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spListLow"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp03'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spListHigh"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'sp04'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/sparePart//spInventoryHistoryList"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'rs01'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//RentalRequest"/>
        <c:if test="<%=!woAll%>">
            <script>
                noPermission1();
            </script>
        </c:if>
    </c:when>
    <c:when test="${param.addr eq 'rs02'}">
        <c:set var="address1" value="${wurl}/jw/web/userview/EAMMS/EAMMS//RentalListing"/>
        <c:if test="<%=!woAll%>">
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
       <iframe id=ifrm name="inlineframe" src="${address1}" frameborder="0" scrolling="auto" width="795" height="595" marginwidth="5" marginheight="5" ></iframe>
    </body>
</html>


