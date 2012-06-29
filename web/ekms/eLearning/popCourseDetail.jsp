<%@ page import="com.tms.portlet.taglibs.PortalServerUtil" %>
<%@ page import="java.util.Random" %>
<%@ include file="/common/header.jsp" %>

<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>


<x:config>
    <page name="course">

        <com.tms.elearning.course.ui.CourseViewDetail name="courseDetail" width="100%">
        </com.tms.elearning.course.ui.CourseViewDetail>
    </page>
</x:config>



<x:permission permission="com.tms.elearning.testware.View" module="com.tms.elearning.course.model.CourseModule" url="noPermission.jsp">






<c:if test="${!empty param.id}">

    <x:set name="course.courseDetail" property="courseId" value="${param.id}"></x:set>

</c:if>


<c:choose>


    <c:when test="${forward.name == 'studentsDetail'}">


        <c:forEach var="studentsId" items="${widgets['course.courseDetail'].studentsId}">


            <script>


                pausecomp(1250)
                popup()

                function pausecomp(millis)
                {
                    date = new Date();
                    var curDate = null;

                    do {
                        var curDate = new Date();
                    }
                    while (curDate - date < millis);
                }






                function popup() {
                    window.open("studentDetail.jsp?courseId=<c:out value="${widgets['course.courseDetail'].courseId}"/>&studentId=<c:out value='${studentsId}'/>", "_blank",
                            "height=400,width=400,status=yes,toolbar=no,menubar=no,location=no, scrollbars=1, resizable=1");

                }


            </script>


        </c:forEach>
        <script>
            window.location = "popCourseDetail.jsp?id=<c:out value="${widgets['course.courseDetail'].courseId}"/>";

        </script>

    </c:when>


    <c:when test="${forward.name == 'studentDetail'}">


        <script>


            window.open("studentDetail.jsp?courseName=<c:out value="${widgets['course.courseDetail'].courseNameStr}"/>&courseId=<c:out value="${widgets['course.courseDetail'].courseId}"/>&studentId=<c:out value="${widgets['course.courseDetail'].studentId}"/>", "_blank",
                    "height=250,width=400,status=yes,toolbar=no,menubar=no,location=no, scrollbars=1");

            window.location = "popCourseDetail.jsp?id=<c:out value="${widgets['course.courseDetail'].courseId}"/>";


        </script>
    </c:when>


</c:choose>


<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            Course Detail</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="course.courseDetail"/>




    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>



              </x:permission>



