<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<jsp:include page="../../form_header.jsp" flush="true"/>
<div><b><u><fmt:message key='fms.facility.label.globalAssignmentListingPageSetup'/></u></b></div><br/>
<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="15%">
            <fmt:message key='fms.facility.form.refreshRate'/>&nbsp;*</td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.tfRefreshRate"/> (<fmt:message key='fms.facility.form.inSeconds'/>)</td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right">
            <fmt:message key='fms.facility.form.noOfDays'/></td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.tfNoOfDays"/> (<fmt:message key='fms.facility.form.max3Days'/>)</td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right">
            <fmt:message key='fms.facility.form.footerMessage'/></td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.tbFooterMessage"/></td>
    </tr>   
    <tr>
        <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
        <td class="classRow" valign="top" width="40%">  
            <x:display name="${form.absoluteName}.btnSubmit"/></td>
    </tr>
</table>
<br/>

<%-- Auto Assignment --%>
<div><b><u><fmt:message key='fms.facility.label.autoAssignmentSetup'/></u></b></div><br/>
<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>">
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.facility.label.daysForAutoAssignment'/>
        </td>
        <td class="classRow" valign="top" >
            <c:if test="${! empty form.daysAutoSetting}">
                <c:set var="rowz" value=""></c:set>
                <c:forEach items="${form.daysAutoSetting}" var="autoSetting">
                    <x:display name="${autoSetting.absoluteName}"/>
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right">
            &nbsp;
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.btnSubmitAutoAssignment"/>
        </td>
    </tr>
</table>
<%-- //// Disable Auto Assignment 
--%>

<div><b><u><fmt:message key='fms.facility.label.todayAssignmentSetup'/></u></b></div><br/>
<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>">
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.facility.label.showAssignmentsFor'/>
        </td>
        <td class="classRow" valign="top">
            <c:if test="${! empty form.daysSetting}">
                <c:forEach items="${form.daysSetting}" var="setting">
                    <x:display name="${setting.absoluteName}"/>
                </c:forEach>
            </c:if>
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right">
            &nbsp;
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.btnSubmitAssignment"/>
        </td>
    </tr>
</table>

<div><b><u><fmt:message key='fms.facility.label.syncProgramCode'/></u></b></div><br/>
<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>">
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.facility.label.1stScheduledTime'/>
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.scheduleTime1Label"/> ( <fmt:message key='fms.facility.label.updatedOn'/> <x:display name="${form.absoluteName}.scheduleTime1ModifiedDate"/> )
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.facility.label.newScheduledTime'/>
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.SyncHour1"/> <x:display name="${form.absoluteName}.SyncMinute1"/> 
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.facility.label.2ndScheduledTime'/>
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.scheduleTime2Label"/> ( <fmt:message key='fms.facility.label.updatedOn'/> <x:display name="${form.absoluteName}.scheduleTime2ModifiedDate"/> )
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.facility.label.newScheduledTime'/>
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.SyncHour2"/> <x:display name="${form.absoluteName}.SyncMinute2"/>  
        </td>
    </tr>
    
    <tr>
        <td class="classRowLabel" valign="top" align="right">
            &nbsp;
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.btnSyncScheduleTime"/>
        </td>
    </tr>
</table>

<div><b><u><fmt:message key='fms.tran.setup.abwScheduler'/>:</u></b></div><br/>
<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>">
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.tran.setup.abwCurrentSchedTime'/>:
        </td>
        <td class="classRow" valign="top">
            <c:out value="${form.abwTransSched}"/> ( <fmt:message key='fms.facility.label.updatedOn'/> <c:out value="${form.abwTransSchedModDate}"/> )
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.tran.setup.abwNewSchedule'/>:
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.abwSchedHour"/> <x:display name="${form.absoluteName}.abwSchedMins"/> 
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right">
            &nbsp;
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.childMap.btnAbwSchedTime.absoluteName}"/>
        </td>
    </tr>
</table>

<div><b><u><fmt:message key='fms.tran.setup.abwEngScheduler'/>:</u></b></div><br/>
<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>">
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.tran.setup.abwCurrentSchedTime'/>:
        </td>
        <td class="classRow" valign="top">
            <c:out value="${form.abwEngTransSched}"/> ( <fmt:message key='fms.facility.label.updatedOn'/> <c:out value="${form.abwEngTransSchedModDate}"/> )
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right" width="27%">
            <fmt:message key='fms.tran.setup.abwNewSchedule'/>:
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.absoluteName}.abwEngSchedHour"/> <x:display name="${form.absoluteName}.abwEngSchedMins"/> 
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" valign="top" align="right">
            &nbsp;
        </td>
        <td class="classRow" valign="top">
            <x:display name="${form.childMap.btnAbwEngTransferCostSched.absoluteName}"/>
        </td>
    </tr>
</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>