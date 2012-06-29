<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" scope="request" value="${widget}"/>

<script>
<!--
function loadUsers() {

	var openerFormName = '<c:out value="${param.formcn}"/>';

	if (openerFormName != '') {
		var formName = '<c:out value="${form.absoluteName}"/>';
		var usersListBoxName = '<c:out value="${form.childMap.usersListTable.childMap.selectBox.absoluteName}"/>';
		var popupField = window.opener.getPopupField();

		sourceBox = window.opener.document.forms[openerFormName].elements[openerFormName + '.' + popupField];
		targetBox = document.forms[formName].elements[usersListBoxName];
		
		// remove target options
		for (i = targetBox.length - 1; i >= 0; i--) 
			targetBox.options[i] = null;


		

		//copy source to target
		var values = '';
		var names = '';
		for (i=0; i < sourceBox.length; i++) {
			if (sourceBox.options[i].value == '') 
				break;
			values += sourceBox.options[i].value + "|";
			names += sourceBox.options[i].text + "|";		
		}

		
		populate(targetBox, values, names);	
	}
	this.focus();
}

function populate(targetBox, values, names) {
	valuesArray = values.split("|");
	namesArray = names.split("|");
	for (i=0; i < namesArray.length; i++) {
		if (valuesArray[i] != '' && namesArray[i] != '') {
			targetBox.options[i] = new Option(namesArray[i], valuesArray[i], false, true);
		}
		
	}
}
//-->
</script>

<jsp:include page="../form_header.jsp" flush="true"/>

<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
  <tbody>

    <tr>
      <td style="vertical-align: top;">Selected<br>
      </td>
      <td style="vertical-align: top;">
        <x:display name="${form.childMap.usersListTable.childMap.selectBox.absoluteName}"/>
        <br>
        <x:display name="${form.childMap.usersListTable.absoluteName}"/>
      </td>
    </tr>

  </tbody>
</table>


<jsp:include page="../form_footer.jsp" flush="true"/>