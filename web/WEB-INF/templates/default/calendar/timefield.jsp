<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="timefield" value="${widget}"/>

<c:if test="${timefield.invalid}">
  !<span style="border:1 solid #de123e">
</c:if>

<select name="<c:out value="${timefield.hourName}"/>" onChange="javascript:change()" class="WCHhider" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
<c:forEach begin="0" end="23" var="h">
    <option value="<c:out value="${h}"/>" <c:if test="${h eq timefield.hour}">selected</c:if>>
        <fmt:formatNumber pattern="00" value="${h}"/></option>
</c:forEach>
</select>

<select name="<c:out value="${timefield.minuteName}"/>" onChange="javascript:change2()" class="WCHhider" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
<c:forEach begin="0" end="45" step="15" var="m">
    <option value="<c:out value="${m}"/>" <c:if test="${m eq timefield.minute}">selected</c:if>>
        <fmt:formatNumber pattern="00" value="${m}"/></option>
</c:forEach>
</select>

<c:if test="${timefield.invalid}">
  </span>
</c:if>

<script>
function change() {
var hour=document.forms["<c:out value='${timefield.rootForm.absoluteName}'/>"].elements['<c:out value="${timefield.hourName}"/>'].value;
var minutes=document.forms["<c:out value='${timefield.rootForm.absoluteName}'/>"].elements['<c:out value="${timefield.minuteName}"/>'].value;
hour=hour*1+1;
if(hour==24){
hour=0;}
document.forms["<c:out value='${timefield.rootForm.absoluteName}'/>"].elements['<c:out value='${timefield.rootForm.absoluteName}'/>.endTime*hour'].value=hour;
document.forms["<c:out value='${timefield.rootForm.absoluteName}'/>"].elements['<c:out value='${timefield.rootForm.absoluteName}'/>.endTime*minute'].value=minutes;
}
function change2() {
var minutes=document.forms["<c:out value='${timefield.rootForm.absoluteName}'/>"].elements['<c:out value="${timefield.minuteName}"/>'].value;
document.forms["<c:out value='${timefield.rootForm.absoluteName}'/>"].elements['<c:out value='${timefield.rootForm.absoluteName}'/>.endTime*minute'].value=minutes;
}
</script>