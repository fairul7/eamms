<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
    <tr>
        <td>
        	<jsp:include page="../form_header.jsp" flush="true"/>
            <table width="100%" cellpadding="3" cellspacing="1">
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Assessment*</td>
                    <td class="classRow"><x:display name="${form.childMap.name.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Course*</td>
                    <td class="classRow"><x:display name="${form.childMap.course.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Module*</td>
                    <td class="classRow"><x:display name="${form.childMap.module.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Lesson*</td>
                    <td class="classRow"><x:display name="${form.childMap.lesson.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Start Date</td>
                    <td class="classRow"><x:display name="${form.childMap.stdate.absoluteName}"/><x:display name="${form.childMap.stmonth.absoluteName}"/><x:display name="${form.childMap.styear.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">End Date</td>
                    <td class="classRow"><x:display name="${form.childMap.endate.absoluteName}"/><x:display name="${form.childMap.enmonth.absoluteName}"/><x:display name="${form.childMap.enyear.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Time Limit</td>
                    <td class="classRow"><x:display name="${form.childMap.timelimit.absoluteName}"/>Hrs</td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Level</td>
                    <td class="classRow"><x:display name="${form.childMap.level.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='eLearning.element.active'/></td>
                    <td class="classRow"><x:display name="${form.childMap.ispublic.absoluteName}"/></td>
                </tr>



                <tr>
                    <td class="classRowLabel" valign="top" align="right">&nbsp;</td>

                    <td class="classRow">
                        <x:display name="${form.childMap.submit.absoluteName}"/>
                        <input type="button" class="button" value="Cancel" onclick="self.location='assessment.jsp'">
                    </td>

                </tr>
            </table>
            <jsp:include page="../form_footer.jsp" flush="true"/>
        </td>
    </tr>
</table>



