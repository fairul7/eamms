<%@ page import="com.tms.collab.formwizard.model.FormConstants"%>
<%@ include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>

<c-rt:set var="fieldTextInput" value="<%= FormConstants.FIELD_TEXT_INPUT %>"/>
<c-rt:set var="fieldTextBox" value="<%= FormConstants.FIELD_TEXT_BOX %>"/>
<c-rt:set var="fieldCheckBox" value="<%= FormConstants.FIELD_CHECK_BOX %>"/>
<c-rt:set var="fieldRadioButton" value="<%= FormConstants.FIELD_RADIO_BUTTON %>"/>
<c-rt:set var="fieldPullDownMenu" value="<%= FormConstants.FIELD_PULL_DOWN_MENU %>"/>
<c-rt:set var="fieldDateSelect" value="<%= FormConstants.FIELD_DATE_SELECT %>"/>
<c-rt:set var="fieldFileUpload" value="<%= FormConstants.FIELD_FILE_UPLOAD %>"/>

<c-rt:set var="fieldTextInputBracket" value="<%= '[' + FormConstants.FIELD_TEXT_INPUT + ']' %>"/>
<c-rt:set var="fieldTextBoxBracket" value="<%= '[' + FormConstants.FIELD_TEXT_BOX + ']' %>"/>
<c-rt:set var="fieldCheckBoxBracket" value="<%= '[' + FormConstants.FIELD_CHECK_BOX + ']' %>"/>
<c-rt:set var="fieldRadioButtonBracket" value="<%= '[' + FormConstants.FIELD_RADIO_BUTTON + ']' %>"/>
<c-rt:set var="fieldPullDownMenuBracket" value="<%= '[' + FormConstants.FIELD_PULL_DOWN_MENU + ']' %>"/>
<c-rt:set var="fieldDateSelectBracket" value="<%= '[' + FormConstants.FIELD_DATE_SELECT + ']' %>"/>
<c-rt:set var="fieldFileUploadBracket" value="<%= '[' + FormConstants.FIELD_FILE_UPLOAD + ']' %>"/>


<c:set var="hiddenField" value="none" />
<c:set var="requiredField" value="none" />
<c:set var="fieldNameField" value="none" />
<c:set var="fieldSizeField" value="none" />
<c:set var="defaultValueField" value="none" />
<c:set var="maxLengthField" value="none" />
<c:set var="optionField" value="none" />
<c:set var="maxRowsColsField" value="none" />
<c:set var="dataTypeField" value="none" />

<script>
<!--
function selectField() {
	var formName = '<c:out value="${form.absoluteName}"/>';
	var form = document.forms[formName];
	var hiddenFieldGroupName = formName + '.<c:out value="${form.hiddenNo.groupName}"/>';
	var requiredFieldGroupName = formName + '.<c:out value="${form.requiredNo.groupName}"/>';

	// set field name to blank
	form['<c:out value="${form.fieldName.absoluteName}"/>'].value = '';

	//set hidden field to no
	form[hiddenFieldGroupName][0].checked = true;


	//set required field to no
	form[requiredFieldGroupName][0].checked = true;

	//set max length to blank
	if (form['<c:out value="${form.maxCols.absoluteName}"/>'] != null)
		form['<c:out value="${form.maxCols.absoluteName}"/>'].value = '';

	//set options to blank
	if (form['<c:out value="${form.options.absoluteName}"/>'] != null)
		form['<c:out value="${form.options.absoluteName}"/>'].value = '';

	//set default value to blank
	if (form['<c:out value="${form.tfDefaultValue.absoluteName}"/>'] != null)
		form['<c:out value="${form.tfDefaultValue.absoluteName}"/>'].value = '';

	form.submit();
}
function toggleFormLayer(layerId, displayType) {
	document.getElementById(layerId).style.display = displayType;
}

function toggleLayers(field){

	if (field == 'hiddenFieldYes')
		toggleFormLayer('requiredField','none');
	else if (field == 'hiddenFieldNo')
		toggleFormLayer('requiredField','block');
	else if (field == 'requiredFieldYes')
		toggleFormLayer('hiddenField','none');
	else if (field == 'requiredFieldNo')
		toggleFormLayer('hiddenField','block');
}

//-->
</script>


<jsp:include page="../form_header.jsp" flush="true"/>

<table>

<tr>
    <td valign="top" align="right" width = "20%"><B><fmt:message key='formWizard.label.fieldType'/></B></td>
    <td>
    	<x:display name="${form.fieldType.absoluteName}"/>
		<c:out value = "${form.fieldType.message}" />
    </td>
</tr>


<tr>
	<td colspan = "2">
		<div id = "hiddenField" style='display:block'>
			<table width = "100%">
				<tr>
					<td valign="top" align="right" width = "30%"><B>Hidden Field</B></td>
					<td>
						<x:display name="${form.hiddenNo.absoluteName}"/> No<br>
						<x:display name="${form.hiddenYes.absoluteName}"/> Yes
						[Hidden fields only show up during query]
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>

<tr>
	<td colspan = "2">
		<div id = "requiredField" style='display:block'>
			<table width = "100%">
				<tr>
					<td valign="top" align="right" width = "30%"><B>Required Field</B></td>
					<td>
						<x:display name="${form.requiredNo.absoluteName}"/> No<br>
						<x:display name="${form.requiredYes.absoluteName}"/> Yes
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>

<tr>
    <td valign="top" align="right" width = "30%"><B>Field Name</B></td>
    <td align="left">
    	<x:display name="${form.fieldName.absoluteName}"/>
		<c:out value = "${form.fieldName.message}" />
    </td>
</tr>

<c:if test="${!form.fieldSize.hidden}">
<tr>
    <td valign="top" align="right" width = "30%"><B>Field Size</B></td>
    <td align="left">
	    <x:display name="${form.fieldSize.absoluteName}"/>
		<c:out value = "${form.fieldSize.message}" />
    </td>
</tr>
</c:if>

<c:if test="${!form.tfDefaultValue.hidden}">
<tr>
    <td valign="top" align="right" width = "30%"><B>Default Value</B></td>
    <td align="left">
    	<x:display name="${form.tfDefaultValue.absoluteName}"/>
    </td>
</tr>
</c:if>

<c:if test="${!form.fieldSize.hidden}">
<tr>
    <td valign="top" align="right" width = "30%"><B>Max Length</B></td>
</c:if>

<c:if test="${!form.options.hidden}">
<tr>
    <td valign="top" align="right">&nbsp;</td>
    <td align="left">
   	Please separate each option by a <b>blank/new line</b>.
    </td>
</tr>

<tr>
    <td valign="top" align="right" width = "30%"><B>Options</B></td>
    <td align="left">
    <x:display name="${form.options.absoluteName}"/><br>
	<c:out value = "${form.options.message}" />
    </td>
</tr>
</c:if>

<c:if test="${!form.maxRows.hidden}">
<tr>
    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.maxRows'/></B></td>
    <td align="left">
    <x:display name="${form.maxRows.absoluteName}"/>
	<c:out value = "${form.maxRows.message}" />
    </td>
</tr>
<tr>
    <td valign="top" align="right" width = "30%"><B>Max Cols.</B></td>
</c:if>

<c:if test="${!form.maxCols.hidden}">
    <td align="left">
	    <x:display name="${form.maxCols.absoluteName}"/>
		<c:out value = "${form.maxCols.message}" />
    </td>
</tr>
</c:if>

<c:if test="${!form.tfDataType.hidden}">
<tr>
    <td valign="top" align="right" width = "30%"><B>Data Type</B></td>
    <td align="left">
    <x:display name="${form.tfDataType.absoluteName}" />
    </td>
</tr>
</c:if>

<tr>
    <td valign="top"  align="center" colspan = "2">
        <x:display name="${form.add.absoluteName}"/>
        <x:display name="${form.finish.absoluteName}"/>
    </td>
</tr>


</table>


<jsp:include page="../form_footer.jsp" flush="true"/>
