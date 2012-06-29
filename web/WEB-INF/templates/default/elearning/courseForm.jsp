
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>



<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr><td colspan="2">
        <jsp:include page="../form_header.jsp" flush="true"/>
        <table width="100%" cellpadding="3" cellspacing="1">
        
        
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.course.label.courseName"/>*</td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.course.absoluteName}"/></td>
        </tr>
<%--
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.course.label.instructor"/></td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.instructor.absoluteName}"/></td>
        </tr>
--%>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.course.label.synopsis"/></td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.synopsis.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.course.label.category"/></td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.category.absoluteName}"/></td>
        </tr>

        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.course.label.author"/></td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.author.absoluteName}"/></td>
        </tr>


        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key='eLearning.element.active'/></td>
            <td class="classRow"><x:display name="${widget.childMap.ispublic.absoluteName}"/></td>
        </tr>

        <tr><td class="classRowLabel" valign="top" align="right">&nbsp;</td><td class="classRowLabel" valign="top" align="right"></td></tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
            <td class="classRow"><x:display name="${widget.childMap.submit.absoluteName}"/> <input type="button" class="button" value="Cancel" onclick="self.location='courses.jsp'"></td>
        </tr>
        
    	</table>
    	<jsp:include page="../form_footer.jsp" flush="true"/>
    </td>
    </tr>
</table>