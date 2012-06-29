<%@ include file="/common/header.jsp" %>


<x:config>
<page name="assessmentEditOptions">

    <com.tms.elearning.testware.ui.AssessmentEditForm name="assessmentEditForm" width="100%" >

    </com.tms.elearning.testware.ui.AssessmentEditForm>
</page>
</x:config>



 <c:if test="${!empty param.uid}">

    <x:set name="assessmentEditOptions.assessmentEditForm" property="uid" value="${param.uid}" ></x:set>

</c:if>


 <c:if test="${!empty param.uid2}">

    <x:set name="assessmentEditOptions.assessmentEditForm" property="uid2" value="${param.uid2}" ></x:set>

</c:if>


<html>
<head>
<script language="Javascript">
<!--
    function frameOnload() {
        try {
            parent.loadObjects();
        }
        catch(e) {
        }
    }
//-->
</script>
</head>
<body onload="frameOnload()">



    <form id="assessmentEditForm" name="assessmentEditForm">
        <select id="assessmentEditDestination" name="assessmentEditDestination">

        <c:forEach items="${widgets['assessmentEditOptions.assessmentEditForm'].module}" var="option">
            <option value="<c:out value='${option.key}'/>"><c:out value='${option.value}'/></option>
        </c:forEach>
       </select>
          <select id="assessmentEditDestination2" name="assessmentEditDestination2">
        <c:forEach items="${widgets['assessmentEditOptions.assessmentEditForm'].lesson}" var="option">
            <option value="<c:out value='${option.key}'/>"><c:out value='${option.value}'/></option>
        </c:forEach>

        </select>
    </form>




</body>
</html>