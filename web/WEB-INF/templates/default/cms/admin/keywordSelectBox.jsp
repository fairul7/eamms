<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<c:set var="comboSelectBox" value="${widget}"/>
<script language="javascript">
    //Adding Members
    function addMember(strForm, strNonMembers, strMembers)
    {
        members = document.forms[strForm].elements[strMembers];
        nonMembers = document.forms[strForm].elements[strNonMembers];
        if(nonMembers.length>0 && nonMembers.options[0].value==-1) return;
        for(c=0; c<nonMembers.length; c++)
        {
            if(nonMembers.options[c].selected)
            {
                if(members.length>0 && members.options[0].value==-1) members.options[0] = null;
                members.options[members.length] = new Option();
                for(c2=members.length-1; c2>0; c2--)
                {
                    members.options[c2].text = members.options[c2-1].text;
                    members.options[c2].value = members.options[c2-1].value;
                }
                o = new Option(nonMembers.options[c].text, nonMembers.options[c].value, false, true);
                members.options[0] = o;
                nonMembers.options[c--] = null;
            }
        }
        /**
        if(nonMembers.length==0)
        {
            nonMembers.options[0] = new Option();
            nonMembers.options[0].text = '-NA-';
            nonMembers.options[0].value= '-1';
        }
        */
    }
    //Remove Members
    function removeMember(strForm, strNonMembers, strMembers)
    {
        members = document.forms[strForm].elements[strMembers];
        nonMembers = document.forms[strForm].elements[strNonMembers];
        if(members.length>0 && members.options[0].value==-1) return;
        for(c=0; c<members.length; c++)
        {
            if(members.options[c].selected)
            {
                if(nonMembers.length>0 && nonMembers.options[0].value==-1) nonMembers.options[0] = null;
                nonMembers.options[nonMembers.length] = new Option();
                for(c2=nonMembers.length-1; c2>0; c2--)
                {
                    nonMembers.options[c2].text = nonMembers.options[c2-1].text;
                    nonMembers.options[c2].value = nonMembers.options[c2-1].value;
                }
                o = new Option(members.options[c].text, members.options[c].value, false, true);
                nonMembers.options[0] = o;
                members.options[c--] = null;
            }
        }
        /**
        if(members.length==0)
        {
            members.options[0] = new Option();
            members.options[0].text = '-NA-';
            members.options[0].value= '-1';
        }
        */
    }
    //Select Members
    function selectMembers(strForm, strNonMembers, strMembers)
    {
        members = document.forms[strForm].elements[strMembers];
//        nonMembers = document.forms[strForm].elements[strNonMembers];

//        for(i=0; i < nonMembers.length; i++) nonMembers.options[i].selected = true;
        for(c=0; c < members.length; c++) members.options[c].selected = true;

        return true;
    }
</script>
<table>
    <tr>
        <td width="45%" align="center">
            <fmt:message key='general.label.available'/><br>
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
        </td>
        <td width="10%" align="center">
            <input
                style="background: lightblue; border:1px embossed blue; cursor:hand"
                type="button" class="button"
                name="<%= kacang.stdui.Button.PREFIX_BUTTON %><c:out value="${comboSelectBox.leftButton.absoluteName}"/>"
                value="<c:out value="${comboSelectBox.leftButton.text}"/>"
                onClick="<c:out value="${comboSelectBox.leftButton.onClick}"/>"
            >
            <br>
            <input
                style="background: lightblue; border:1px embossed blue; cursor:hand"
                type="button" class="button"
                name="<%= kacang.stdui.Button.PREFIX_BUTTON %><c:out value="${comboSelectBox.rightButton.absoluteName}"/>"
                value="<c:out value="${comboSelectBox.rightButton.text}"/>"
                onClick="<c:out value="${comboSelectBox.rightButton.onClick}"/>"
            >
        </td>
        <td width="45%" align="center">
            <fmt:message key='general.label.selected'/><br>
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
        </td>
    </tr>
</table>
