<%@ include file="/common/header.jsp" %>
<x:config>
<page name="assessment">
    <com.tms.elearning.testware.ui.AssessmentTableByModule name="assessmentTableByModule" width="100%">

        <listener_script>
            String id = event.getRequest().getParameter("id");

        </listener_script>
    </com.tms.elearning.testware.ui.AssessmentTableByModule>
</page>
</x:config>
<c:if test="${!empty param.id}">

    <x:set name="assessment.assessmentTableByModule" property="moduleId" value="${param.id}" ></x:set>

</c:if>



<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr valign="middle">
                    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.maintitle'/> > <A HREF="regCourses.jsp"><fmt:message key='eLearning.menu.label.RegisteredCourses'/></A> > <a href="javascript:history.back()"><fmt:message key='eLearning.menu.label.Modules'/></A> > <fmt:message key='eLearning.menu.label.Assessments'/></font></b></td>
                    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
                </tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<x:display name="assessment.assessmentTableByModule"/>
     Note: <fmt:message key='eLearning.assessment.easy'/>=1, <fmt:message key='eLearning.assessment.intermediate'/>=2, <fmt:message key='eLearning.assessment.difficult'/>=3.


      </td></tr>
                    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
                </table>

<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>