<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="comboSelectBox" value="${widget}"/>
<script language="javascript">
    //Adding Members
    function addMember(strForm, strNonMembers, strMembers)
    {
		moveMember(strForm, strNonMembers, strMembers);
        setHiddenValue(strForm,strMembers);
    }
    //Remove Members
    function removeMember(strForm, strNonMembers, strMembers)
    {
		moveMember(strForm, strMembers, strNonMembers);
        setHiddenValue(strForm,strMembers);
    }

    //Select Members
    function selectMembers(strForm, strNonMembers, strMembers)
    {
        members = document.forms[strForm].elements[strMembers];
        nonMembers = document.forms[strForm].elements[strNonMembers];

        if (nonMembers != null) {
            for(i=0; i < nonMembers.length; i++) nonMembers.options[i].selected = true;
            for(c=0; c < members.length; c++) members.options[c].selected = true;
        }

        return true;
    }

	function moveMember(strForm, strSrc, strDest) {
		var selSrc  = document.forms[strForm].elements[strSrc];
		var selDest = document.forms[strForm].elements[strDest];

		if (selSrc.length>0 && selSrc.options[0].value==-1) return;
		if (selDest.length>0 && selDest.options[0].value==-1) selDest.options[0] = null;

		<%-- // get number selected --%>
		var numSelected = 0;
		for (var c=0; c<selSrc.length; c++) {
			if (selSrc.options[c].selected) {
				numSelected++;
			}
		}

		if (numSelected > 0) {
			<%-- // make way for new entries --%>
			for (var c=0; c<numSelected; c++) {
				selDest.options[selDest.length] = new Option();
			}

			<%-- // move entries down --%>
			for (var c=selDest.length-numSelected-1; c>=0; c--) {
				selDest.options[c+numSelected].text = selDest.options[c].text;
				selDest.options[c+numSelected].value = selDest.options[c].value;
			}

			<%-- // move entries over --%>
			var newIndex = 0;
			for (var c=0; c<selSrc.length; c++) {
				if (selSrc.options[c].selected) {
					var o = new Option(selSrc.options[c].text, selSrc.options[c].value, false, true);
					selDest.options[newIndex++] = o;
					selSrc.options[c--] = null;
				}
			}
		}

	}

    function setHiddenValue(strForm,strSel) {
        var sHidden = strForm+".resourceshidden";
         var sel = document.forms[strForm].elements[strSel];
         var hidden = document.forms[strForm].elements[sHidden];
         if (hidden!=undefined) {
            var s="";
            for(var c=0;c<sel.length; c++){
                if (c==0)
                    s = sel.options[c].value;
                else
                    s += ","+sel.options[c].value;
            }
            hidden.value=s;
            //alert(hidden.value);
         }
    }

</script>
<table>
    <tr>
        <td width="45%" align="center">
            <b><!--Available--><fmt:message key="comboSelectBox.available"/></b><br>
            <select
                name="<c:out value="${comboSelectBox.leftSelect.absoluteName}"/>"
                size="<c:out value="${comboSelectBox.leftSelect.rows}"/>"
                multiple
            >
            <c:forEach items="${comboSelectBox.leftSelect.optionMap}" var="option">
                <option value="<c:out value="${option.key}"/>"<c:if test="${comboSelectBox.leftSelect.selectedOptions[option.key]}"> selected</c:if>>
                    <c:out value="${option.value}"/>
                </option>
            </c:forEach>
            </select>
            <c:forEach var="child" items="${comboSelectBox.leftSelect.children}">
                <br><span style="color:red"><x:display name="${child.absoluteName}"/></span>
            </c:forEach>
        </td>
        <td width="10%" align="center">
            <input
                type="button" class="button"
                name="<%= kacang.stdui.Button.PREFIX_BUTTON %><c:out value="${comboSelectBox.leftButton.absoluteName}"/>"
                value="<c:out value="${comboSelectBox.leftButton.text}"/>"
                onClick="<c:out value="${comboSelectBox.leftButton.onClick}"/>"
            >
            <br>
            <input
                type="button" class="button"
                name="<%= kacang.stdui.Button.PREFIX_BUTTON %><c:out value="${comboSelectBox.rightButton.absoluteName}"/>"
                value="<c:out value="${comboSelectBox.rightButton.text}"/>"
                onClick="<c:out value="${comboSelectBox.rightButton.onClick}"/>"
            >
        </td>
        <td width="45%" align="center">
            <b><!--Selected--><fmt:message key="comboSelectBox.selected"/></b><br>
            <select
                name="<c:out value="${comboSelectBox.rightSelect.absoluteName}"/>"
                size="<c:out value="${comboSelectBox.rightSelect.rows}"/>"
                multiple
            >
            <c:forEach items="${comboSelectBox.rightSelect.optionMap}" var="option">
                <option value="<c:out value="${option.key}"/>"<c:if test="${comboSelectBox.rightSelect.selectedOptions[option.key]}"> selected</c:if>>
                    <c:out value="${option.value}"/>
                </option>
            </c:forEach>
            </select>
            <c:forEach var="child" items="${comboSelectBox.rightSelect.children}">
                <br><span style="color:red"><x:display name="${child.absoluteName}"/></span>
            </c:forEach>
        </td>
    </tr>
</table>
