<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="assessment">
        <com.tms.elearning.testware.ui.AssessmentTable name="assessmentTable" width="100%">

        </com.tms.elearning.testware.ui.AssessmentTable>
    </page>
</x:config>

<x:permission permission="com.tms.elearning.testware.View" module="com.tms.elearning.coursecategory.model.CategoryModule">

    <c:if test="${!empty param.id}">
        <%--     user for edit --%>
        <x:set name="assessment.assessmentTable" property="id" value="${param.id}" ></x:set>
    </c:if>

    <c:choose>
        <c:when test="${forward.name == 'add'}" >
            <c:redirect url="assessmentAdd.jsp" />
        </c:when>
        <c:when test="${forward.name == 'deleted'}" >
            <script>
                alert("<fmt:message key='eLearning.alert.label.assessmentdeleted'/>");
                window.location="assessment.jsp";
            </script>
        </c:when>
        <c:when test="${forward.name == 'notSelected'}" >
            <script>
                alert("<fmt:message key='eLearning.alert.label.assessmentselected'/>");
            </script>
        </c:when>
        <c:when test="${forward.name == 'activate'}" >
            <script>
                alert("<fmt:message key='eLearning.general.message.activated'/>");
            </script>
        </c:when>
        <c:when test="${forward.name == 'deactivate'}" >
            <script>
                alert("<fmt:message key='eLearning.general.message.activated'/>");
            </script>
        </c:when>
    </c:choose>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>
    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.Instructor_Menu'/> > <A HREF="assessment.jsp"><fmt:message key='eLearning.menu.label.registerAssessment'/></A></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:display name="assessment.assessmentTable"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>
</x:permission>

<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>