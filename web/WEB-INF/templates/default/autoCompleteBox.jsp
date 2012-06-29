<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="textbox" value="${widget}"/>
<c:set var="optionsVarName" value="${textbox.optionsVarName}"/>

<script language="javascript" type="text/javascript" src="<c:url value='/common/autoCompleteBox/actb.js'/>"></script>
<c:if test="${empty textbox.sharedOptionsVarName}">
<script language="javascript" type="text/javascript" src="<c:url value='/common/autoCompleteBox/autoCompleteOptions.jsp?widget=${textbox.absoluteName}'/>"></script>
<%--
<script>
<!--
<c:set var="optionList" value=""/>
<c:forEach var="option" items="${textbox.options}" varStatus="stat" >
    <%
        String option = (String)pageContext.getAttribute("option");
        option = StringUtils.replace(option, "'", "\\'");
        pageContext.setAttribute("option", option);
    %>
    <c:if test="${stat.index>0}"><c:set var="optionList" value="${optionList},"/></c:if>
    <c:set var="optionList"><c:out value="${optionList}" escapeXml="false" />'<c:out value="${option}" escapeXml="false"/>'</c:set>
</c:forEach>
var <c:out value="${textbox.optionsVarName}"/>=new Array(<c:out value="${optionList}" escapeXml="false" />);
//-->
</script>
--%>
</c:if>

<c:if test="${!empty textbox.sharedOptionsVarName}">
    <c:set var="optionsVarName" value="${textbox.sharedOptionsVarName}"/>
</c:if>

<c:choose>
<c:when test="${textbox.rows == '1'}" >

<c:set var="size" value="${textfield.size}"/>
<c:if test="${empty size}">
    <c:set var="size" value="50"/>
</c:if>

<c:choose>
    <c:when test="${!textfield.invalid}">
    <input
       onkeypress='return actb_checkkey_moz(event,this,<c:out value="${optionsVarName}"/>)' onblur='actb_removedisp(this)'
       onkeydown='return actb_checkkey(event,this,<c:out value="${optionsVarName}"/>)'
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
       onkeypress='return actb_checkkey_moz(event,this,<c:out value="${optionsVarName}"/>)' onblur='actb_removedisp(this)'
       onkeydown='return actb_checkkey(event,this,<c:out value="${optionsVarName}"/>)'
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
<c:forEach var="child" items="${textfield.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>


</c:when>
<c:otherwise>

<c:choose>
<c:when test="${!textbox.invalid}" >
<textarea
    onkeypress='return actb_checkkey_moz(event,this,<c:out value="${optionsVarName}"/>)' onblur='actb_removedisp(this)'
    onkeydown='return actb_checkkey(event,this,<c:out value="${optionsVarName}"/>)'
    name="<c:out value="${textbox.absoluteName}"/>"
    onBlur="<c:out value="${textbox.onBlur}"/>"
    onChange="<c:out value="${textbox.onChange}"/>"
    onFocus="<c:out value="${textbox.onFocus}"/>"
    onSelect="<c:out value="${textbox.onSelect}"/>"
    rows="<c:out value="${textbox.rows}"/>"
    cols="<c:out value="${textbox.cols}"/>"
><c:out value="${textbox.value}"/></textarea>
    </c:when>
    <c:otherwise>
!<textarea
    onkeypress='return actb_checkkey_moz(event,this,<c:out value="${optionsVarName}"/>)' onblur='actb_removedisp(this)'
    onkeydown='return actb_checkkey(event,this,<c:out value="${optionsVarName}"/>)'
    name="<c:out value="${textbox.absoluteName}"/>"
    style="border:1 solid #de123e"
    name="<c:out value="${textbox.absoluteName}"/>"
    onBlur="<c:out value="${textbox.onBlur}"/>"
    onChange="<c:out value="${textbox.onChange}"/>"
    onFocus="<c:out value="${textbox.onFocus}"/>"
    onSelect="<c:out value="${textbox.onSelect}"/>"
    rows="<c:out value="${textbox.rows}"/>"
    cols="<c:out value="${textbox.cols}"/>"
><c:out value="${textbox.value}"/></textarea>
    </c:otherwise>
</c:choose>
<c:forEach var="child" items="${textbox.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>

</c:otherwise>
</c:choose>


