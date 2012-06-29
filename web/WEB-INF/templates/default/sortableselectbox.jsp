<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="selectbox" value="${widget}"/>

<script>
<!--
    function <c:out value="${selectbox.absoluteNameForJavaScript}"/>sortItems(obj, upDown) {
      if (obj == null)
        return;

      o = obj.options;

      if (upDown > 0)
        for(c=obj.length-1; c>=0; c--) {
          dest = c + 1;
          if(o[c].selected && dest<obj.length) {
            destOp = new Option(o[dest].text, o[dest].value, o[dest].defaultSelected, o[dest].selected);
            o[dest] = new Option(o[c].text, o[c].value, o[c].defaultSelected, o[c].selected);
            o[c] = destOp;
          }
        }
      else
        for(c=0; c<obj.length; c++) {
          dest = c - 1;
          if(o[c].selected && dest>=0) {
            destOp = new Option(o[dest].text, o[dest].value, o[dest].defaultSelected, o[dest].selected);
            o[dest] = new Option(o[c].text, o[c].value, o[c].defaultSelected, o[c].selected);
            o[c] = destOp;
          }
        }
    }

    function <c:out value="${selectbox.absoluteNameForJavaScript}"/>submit() {
        members = document.forms['<c:out value="${selectbox.rootForm.absoluteName}"/>'].elements['<c:out value="${selectbox.absoluteName}"/>'];
        for(c=0; c < members.length; c++) members.options[c].selected = true;
        return true;
    }
//-->
</script>

<table border="0" cellspacing="0" cellpadding="1">
<tr>
<td>
<c:if test="${selectbox.invalid}">
  !<span style="border:1 solid #de123e">
</c:if>

<select
    name="<c:out value="${selectbox.absoluteName}"/>"
    size="<c:out value="${selectbox.rows}"/>"
    onBlur="<c:out value="${selectbox.onBlur}"/>"
    onChange="<c:out value="${selectbox.onChange}"/>"
    onFocus="<c:out value="${selectbox.onFocus}"/>"
    <c:if test="${selectbox.multiple}"> multiple</c:if>
>
<c:forEach items="${selectbox.optionMap}" var="option">
    <option value="<c:out value="${option.key}"/>"<c:if test="${selectbox.selectedOptions[option.key]}"> selected</c:if>>
        <c:out value="${option.value}"/></option>
</c:forEach>
</select>

<c:if test="${selectbox.invalid}">
  </span>
</c:if>
</td>
<td>
<input type="button" class="button" onclick="<c:out value="${selectbox.absoluteNameForJavaScript}"/>sortItems(document.forms['<c:out value="${selectbox.rootForm.absoluteName}"/>']['<c:out value="${selectbox.absoluteName}"/>'], -1)" value="^">
<br>
<input type="button" class="button" onclick="<c:out value="${selectbox.absoluteNameForJavaScript}"/>sortItems(document.forms['<c:out value="${selectbox.rootForm.absoluteName}"/>']['<c:out value="${selectbox.absoluteName}"/>'], 1)" value="v">
<%--<a href="javascript:sortItems(document.forms['<c:out value="${selectbox.rootForm.absoluteName}"/>']['<c:out value="${selectbox.absoluteName}"/>'], 1)">v</a>--%>
</td>
</tr>
</table>



