<%@ include file="/common/header.jsp" %>


<x:config>
<page name="assessmentAddOptions">

    <com.tms.elearning.testware.ui.AssessmentAddForm name="assessmentAddForm" width="100%" uid="${param.uid}">

    </com.tms.elearning.testware.ui.AssessmentAddForm>
</page>
</x:config>



 <c:if test="${!empty param.uid}">

    <x:set name="assessmentAddOptions.assessmentAddForm" property="uid" value="${param.uid}" ></x:set>

</c:if>


 <c:if test="${!empty param.uid2}">

    <x:set name="assessmentAddOptions.assessmentAddForm" property="uid2" value="${param.uid2}" ></x:set>

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



    <form id="assessmentAddForm" name="assessmentAddForm">
        <select id="assessmentAddDestination" name="assessmentAddDestination">

        <c:forEach items="${widgets['assessmentAddOptions.assessmentAddForm'].module}" var="option">
            <option value="<c:out value='${option.key}'/>"><c:out value='${option.value}'/></option>
        </c:forEach>
        </select>

        <select id="assessmentAddDestination2" name="assessmentAddDestination2">
        <c:forEach items="${widgets['assessmentAddOptions.assessmentAddForm'].lesson}" var="option">
            <option value="<c:out value='${option.key}'/>"><c:out value='${option.value}'/></option>
        </c:forEach>

        </select>
    </form>




</body>
</html>