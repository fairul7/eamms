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
                
                <%--<tr><td class="classRow" colspan=2 align='center'><font color='#FF0000'><x:display name="${form.errorMsg.absoluteName}"/></font></td></tr> --%>
               <%-- <tr>
                    <td class="classRowLabel" valign="top" align="right">Course</td>
                    <td class="classRow"><x:display name="${form.childMap.course.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Module</td>
                    <td class="classRow"><x:display name="${form.childMap.module.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Lesson</td>
                    <td class="classRow"><x:display name="${form.childMap.lesson.absoluteName}"/></td>
                </tr>--%>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Question *</td>
                    <td class="classRow"><x:display name="${form.childMap.question.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Answer A *</td>
                    <td class="classRow"><x:display name="${form.childMap.answer_a.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Answer B *</td>
                    <td class="classRow"><x:display name="${form.childMap.answer_b.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Answer C *</td>
                    <td class="classRow"><x:display name="${form.childMap.answer_c.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Answer D *</td>
                    <td class="classRow"><x:display name="${form.childMap.answer_d.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">Correct Answer</td>
                    <td class="classRowLabel" valign="center">A<x:display name="${form.childMap.a.absoluteName}"/> &nbsp;&nbsp;B<x:display name="${form.childMap.b.absoluteName}"/> &nbsp;&nbsp;C<x:display name="${form.childMap.c.absoluteName}"/> &nbsp;&nbsp;D<x:display name="${form.childMap.d.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
                    <td class="classRow">
                        <x:display name="${form.childMap.submit.absoluteName}"/>
                        <input type="button" class="button" value="Cancel" onclick="self.location='question.jsp'">
                    </td>
                </tr>
                
            </table>
            <jsp:include page="../form_footer.jsp" flush="true"/>
        </td>
    </tr>
</table>



