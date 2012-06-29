<%@ include file="/common/header.jsp" %>

<c:set var="selectBox" value="${widget}"/>
<input type="hidden" id="<c:out value="selectedValue"/>">
<input type="hidden" id="<c:out value="selectedName"/>">
<input type="button" class="button" name="selectButton" onclick="return popupSelectBoxOpen('<c:out value="${selectBox.absoluteName}"/>')" value='<fmt:message key="popupSelectBox.select"/>'>
<input type="button" class="button" name="removeButton" onclick="popupSelectBoxRemove()" value="<fmt:message key="popupSelectBox.unselect"/>">
<script>
<!--   

    function <c:out value="${selectBox.absoluteNameForJavaScript}"/>submit() {
        members = document.forms['<c:out value="${selectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${selectBox.absoluteName}"/>'];
        for(c=0; c < members.length; c++) members.options[c].selected = true;
        return true;
    }
//-->
</script>

<table border="0" cellspacing="0" cellpadding="1">
<tr>
<td>
<c:if test="${selectBox.invalid}">
  !<span style="border:1 solid #de123e">
</c:if>
<select
    name="<c:out value="${selectBox.absoluteName}"/>"
    size="<c:out value="${selectBox.rows}"/>"
    onBlur="<c:out value="${selectBox.onBlur}"/>"
    onChange="<c:out value="${selectBox.onChange}"/>"
    onFocus="<c:out value="${selectBox.onFocus}"/>"
    multiple
>
<c:forEach items="${selectBox.optionMap}" var="option">
    <option value="<c:out value="${option.key}"/>"<c:if test="${selectBox.selectedOptions[option.key]}"> selected</c:if>>
        <c:out value="${option.value}"/></option>
</c:forEach>
</select>

<c:if test="${selectBox.invalid}">
  </span>
</c:if>
</td>
<c:if test="${selectBox.sortable}">
<td>
    <table cellspacing="1" cellpadding="0">
    <tr>
    <td>
    <input type="button" class="button" style="width:20px; height:20px;" onclick="<c:out value="${selectBox.absoluteNameForJavaScript}"/>sortItems(document.forms['<c:out value="${selectBox.rootForm.absoluteName}"/>']['<c:out value="${selectBox.absoluteName}"/>'], -1)" value="^">
    </td>
    </tr>
    <tr>
    <td>
    <input type="button" class="button" style="width:20px; height:20px;" onclick="<c:out value="${selectBox.absoluteNameForJavaScript}"/>sortItems(document.forms['<c:out value="${selectBox.rootForm.absoluteName}"/>']['<c:out value="${selectBox.absoluteName}"/>'], 1)" value="v">
    </td>
    </tr>
    </table>
</td>
</c:if>
</tr>
</table>

<script>
<!--
    function popupSelectBoxOpen(absoluteName) {
        window.open('<c:url value="/ekms/popupSelectBox/addressBookSelectBox.jsp"/>?popupSelectBoxCn=' + absoluteName , "popupSelectBoxWindow", "height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
        return false;
    }

    function popupSelectBoxPopulate(v, n) {
    	
        targetBox = document.forms['<c:out value="${selectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${selectBox.absoluteName}"/>'];

        // get values and names
        //v = document.getElementById('selectedValue').value;
        //n = document.getElementById('selectedName').value;
        values = v.split("|||");
        names = n.split("|||");

        for (i = 0; i < names.length; i++) {
            if (values[i] != '' && names[i] != '') {
                // remove duplicate options
                for(c=0; c<targetBox.length; c++) {
                    if (targetBox.options[c].value == values[i]) {
                        targetBox.options[c] = null;
                        break;
                    }
                }
                // add option
                targetBox.options[targetBox.options.length] = new Option(names[i], values[i], true, true);
            }
        }
    }

    function popupSelectBoxRemove() {
		//alert("Hello");
        var targetBox = document.forms['<c:out value="${selectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${selectBox.absoluteName}"/>'];

        // remove target options
        for(c=targetBox.length-1; c>=0; c--) {
            if (targetBox.options[c].selected) {
                targetBox.options[c] = null;
            }
        }

        return false;
    }


//-->
</script>
