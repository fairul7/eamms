<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="textfield" value="${widget}"/>

<c:set var="size" value="${textfield.size}"/>
<c:if test="${empty size}">
    <c:set var="size" value="20"/>
</c:if>

<c:choose>
    <c:when test="${!textfield.invalid}">
    <input
       type="text" name="<c:out value="${textfield.absoluteName}"/>"
       value="<c:out value="${textfield.value}"/>"
       onBlur="<c:out value="${textfield.onBlur}"/>"
       onChange="<c:out value="${textfield.onChange}"/>"
       onFocus="<c:out value="${textfield.onFocus}"/>"
       onSelect="<c:out value="${textfield.onSelect}"/>"
       maxlength="<c:out value="${textfield.maxlength}"/>"
       size="<c:out value="${size}"/>"
    >
    </c:when>
    <c:otherwise>
    !<input
       style="border:1px solid #de123e"
       type="text" name="<c:out value="${textfield.absoluteName}"/>"
       value="<c:out value="${textfield.value}"/>"
       onBlur="<c:out value="${textfield.onBlur}"/>"
       onChange="<c:out value="${textfield.onChange}"/>"
       onFocus="<c:out value="${textfield.onFocus}"/>"
       onSelect="<c:out value="${textfield.onSelect}"/>"
       maxlength="<c:out value="${textfield.maxlength}"/>"
       size="<c:out value="${size}"/>"
    >
    </c:otherwise>
</c:choose>

<input type="button" class="button" name="<c:out value='${textfield.absoluteNameForJavaScript}'/>button1" value="v" onclick="<c:out value='${textfield.absoluteNameForJavaScript}'/>cal.toggle(document.forms[<c:out value='${textfield.rootForm.absoluteName}'/>].elements['button1'])">

<%-- paul: conditional include of script file to improve loading time in browser --%>
<c:if test="${empty(requestScope.datePickerLoaded)}">
	<script language="javascript" src="<c:out value='${pageContext.request.contextPath}'/>/common/datePopupField/DatePicker.jsp"></script>
	<c:set var="datePickerLoaded" value="1" scope="request"/>
</c:if>
<script language="javascript">
		var <c:out value='${textfield.absoluteNameForJavaScript}'/>cal;

		function <c:out value='${textfield.absoluteNameForJavaScript}'/>onclick() {
            <c:out value='${textfield.absoluteNameForJavaScript}'/>init();
            <c:out value='${textfield.absoluteNameForJavaScript}'/>cal.toggle(document.forms["<c:out value='${textfield.rootForm.absoluteName}'/>"].elements['<c:out value='${textfield.absoluteNameForJavaScript}'/>button1']);
        }

		function <c:out value='${textfield.absoluteNameForJavaScript}'/>init() {
            <c:if test="${empty textfield.date}">
    			<c:out value='${textfield.absoluteNameForJavaScript}'/>cal = new Calendar();
            </c:if>
            <c:if test="${!empty textfield.date}">
    			<c:out value='${textfield.absoluteNameForJavaScript}'/>cal = new Calendar(new Date(<c:out value='${textfield.date.time}'/>));
            </c:if>
			<c:out value='${textfield.absoluteNameForJavaScript}'/>cal.setIncludeWeek(true);
			<c:out value='${textfield.absoluteNameForJavaScript}'/>cal.setFormat("<c:out value='${textfield.format}'/>");
			<c:out value='${textfield.absoluteNameForJavaScript}'/>cal.create();

			document.forms["<c:out value='${textfield.rootForm.absoluteName}'/>"].elements['<c:out value='${textfield.absoluteNameForJavaScript}'/>button1'].onclick = function() {
				<c:out value='${textfield.absoluteNameForJavaScript}'/>cal.toggle(document.forms["<c:out value='${textfield.rootForm.absoluteName}'/>"].elements['<c:out value='${textfield.absoluteNameForJavaScript}'/>button1']);
			}
			<c:out value='${textfield.absoluteNameForJavaScript}'/>cal.onchange = function() {
				document.forms["<c:out value='${textfield.rootForm.absoluteName}'/>"].elements["<c:out value='${textfield.absoluteName}'/>"].value  = <c:out value='${textfield.absoluteNameForJavaScript}'/>cal.formatDate();
			}
		}

        document.forms["<c:out value='${textfield.rootForm.absoluteName}'/>"].elements['<c:out value='${textfield.absoluteNameForJavaScript}'/>button1'].onclick=<c:out value='${textfield.absoluteNameForJavaScript}'/>onclick;

</script>

<c:forEach var="child" items="${textfield.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>


