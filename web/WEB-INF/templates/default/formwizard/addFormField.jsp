<%@ page import="kacang.Application,
                 com.tms.collab.formwizard.model.FormModule,
                 kacang.services.security.SecurityService,
                 kacang.services.security.User,
                 com.tms.collab.formwizard.model.FormConstants,
                 com.tms.collab.formwizard.grid.G2Column,
                 java.util.Calendar,
                 kacang.util.UuidGenerator,
                 kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddFormField,
                 com.tms.collab.formwizard.ui.AddG2FieldForm"%>
<%@ include file="/common/header.jsp" %>



<c:set var="form" value="${widget}"/>




<c-rt:set var="fieldTextBlock" value="<%= FormConstants.FIELD_TEXT_BLOCK %>"/>
<c-rt:set var="fieldTextInput" value="<%= FormConstants.FIELD_TEXT_INPUT %>"/>
<c-rt:set var="fieldTextBox" value="<%= FormConstants.FIELD_TEXT_BOX %>"/>
<c-rt:set var="fieldCheckBox" value="<%= FormConstants.FIELD_CHECK_BOX %>"/>
<c-rt:set var="fieldRadioButton" value="<%= FormConstants.FIELD_RADIO_BUTTON %>"/>
<c-rt:set var="fieldPullDownMenu" value="<%= FormConstants.FIELD_PULL_DOWN_MENU %>"/>
<c-rt:set var="fieldDateSelect" value="<%= FormConstants.FIELD_DATE_SELECT %>"/>
<c-rt:set var="fieldFileUpload" value="<%= FormConstants.FIELD_FILE_UPLOAD %>"/>
<c-rt:set var="fieldTableGrid" value="<%= FormConstants.FIELD_TABLE_GRID %>"/>
<c-rt:set var="fieldTemplateDevider" value="<%= FormConstants.FIELD_TEMPLATE_DIVIDER %>"/>
<c-rt:set var="fieldSelect" value="<%= FormConstants.FIELD_SELECT %>"/>

<c-rt:set var="typeText" value="<%= G2Column.TYPE_TEXT %>"/>
<c-rt:set var="typeFormula" value="<%= G2Column.TYPE_FORMULA %>"/>
<c-rt:set var="typeDropDown" value="<%= G2Column.TYPE_DROP_DOWN %>"/>


<c-rt:set var="fieldTextBlockBracket" value="<%= '[' + FormConstants.FIELD_TEXT_BLOCK + ']' %>"/>
<c-rt:set var="fieldTextInputBracket" value="<%= '[' + FormConstants.FIELD_TEXT_INPUT + ']' %>"/>
<c-rt:set var="fieldTextBoxBracket" value="<%= '[' + FormConstants.FIELD_TEXT_BOX + ']' %>"/>
<c-rt:set var="fieldCheckBoxBracket" value="<%= '[' + FormConstants.FIELD_CHECK_BOX + ']' %>"/>
<c-rt:set var="fieldRadioButtonBracket" value="<%= '[' + FormConstants.FIELD_RADIO_BUTTON + ']' %>"/>
<c-rt:set var="fieldPullDownMenuBracket" value="<%= '[' + FormConstants.FIELD_PULL_DOWN_MENU + ']' %>"/>
<c-rt:set var="fieldDateSelectBracket" value="<%= '[' + FormConstants.FIELD_DATE_SELECT + ']' %>"/>
<c-rt:set var="fieldFileUploadBracket" value="<%= '[' + FormConstants.FIELD_FILE_UPLOAD + ']' %>"/>
<c-rt:set var="fieldTableGridBracket" value="<%= '[' + FormConstants.FIELD_TABLE_GRID + ']' %>"/>
<c-rt:set var="fieldTemplateDeviderBracket" value="<%= '[' + FormConstants.FIELD_TEMPLATE_DIVIDER + ']' %>"/>
<c-rt:set var="fieldSelectBracket" value="<%= '[' + FormConstants.FIELD_SELECT + ']' %>"/>

<c-rt:set var="typeTextBracket" value="<%= '[' + G2Column.TYPE_TEXT + ']'%>"/>
<c-rt:set var="typeFormulaBracket" value="<%= '[' + G2Column.TYPE_FORMULA + ']'%>"/>
<c-rt:set var="typeDropDownBracket" value="<%= '[' + G2Column.TYPE_DROP_DOWN + ']'%>"/>




<c:set var="hiddenField" value="none" />
<c:set var="requiredField" value="none" />
<c:set var="fieldNameField" value="none" />
<c:set var="fieldSizeField" value="none" />
<c:set var="defaultValueField" value="none" />
<c:set var="maxLengthField" value="none" />
<c:set var="optionField" value="none" />
<c:set var="maxRowsColsField" value="none" />
<c:set var="dataTypeField" value="none" />
<c:set var="labelValueField" value="none" />
<c:set var="independentField" value="none" />
<c:set var="gridNameField" value="none" />
<c:set var="gridNumberField" value="none" />
<c:set var="gridFormulaField" value="none" />
<c:set var="gridItemField" value="none" />
<c:set var="spanAlign" value="none" />
<c:set var="resetLayout" value="none" />
<c:set var="tableGridColumns" value="none" />
<c:set var="tableGridColumnsList" value="none" />
<c:set var="note" value="none" />



<c:choose>
    <c:when test="${form.fieldType.value == fieldTextBlockBracket}">
        <c:set var="hiddenField" value="block" />
        <c:set var="requiredField" value="none" />
        <c:set var="fieldNameField" value="none" />
        <c:set var="fieldSizeField" value="none" />
        <c:set var="defaultValueField" value="none" />
        <c:set var="maxLengthField" value="none" />
        <c:set var="optionField" value="none" />
        <c:set var="maxRowsColsField" value="none" />
        <c:set var="dataTypeField" value="none" />
        <c:set var="labelValueField" value="block" />
        <c:set var="independentField" value="none" />
        <c:set var="gridNameField" value="none" />
        <c:set var="gridNumberField" value="none" />
        <c:set var="gridFormulaField" value="none" />
        <c:set var="gridItemField" value="none" />
        <c:set var="spanAlign" value="block" />
        <c:set var="resetLayout" value="none" />
        <c:set var="tableGridColumns" value="none" />
        <c:set var="tableGridColumnsList" value="none" />
        <c:set var="note" value="block" />
    </c:when>

    <c:when test="${form.fieldType.value == fieldTextInputBracket}">
        <c:set var="hiddenField" value="block" />
        <c:set var="requiredField" value="block" />
        <c:set var="fieldNameField" value="block" />
        <c:set var="fieldSizeField" value="block" />
        <c:set var="defaultValueField" value="block" />
        <c:set var="maxLengthField" value="block" />
        <c:set var="optionField" value="none" />
        <c:set var="maxRowsColsField" value="none" />
        <c:set var="dataTypeField" value="block" />
        <c:set var="labelValueField" value="none" />
        <c:set var="independentField" value="none" />
        <c:set var="gridNameField" value="none" />
        <c:set var="gridNumberField" value="none" />
        <c:set var="gridFormulaField" value="none" />
        <c:set var="gridItemField" value="none" />
        <c:set var="spanAlign" value="block" />
        <c:set var="resetLayout" value="none" />
        <c:set var="tableGridColumns" value="none" />
        <c:set var="tableGridColumnsList" value="none" />
        <c:set var="note" value="block" />
    </c:when>
    
    <c:when test="${form.fieldType.value == fieldTextBoxBracket}">
        <c:set var="hiddenField" value="block" />
        <c:set var="requiredField" value="block" />
        <c:set var="fieldNameField" value="block" />
        <c:set var="fieldSizeField" value="none" />
        <c:set var="defaultValueField" value="block" />
        <c:set var="maxLengthField" value="none" />
        <c:set var="optionField" value="none" />
        <c:set var="maxRowsColsField" value="block" />
        <c:set var="dataTypeField" value="none" />
        <c:set var="labelValueField" value="none" />
        <c:set var="independentField" value="none" />
        <c:set var="gridNameField" value="none" />
        <c:set var="gridNumberField" value="none" />
        <c:set var="gridFormulaField" value="none" />
        <c:set var="gridItemField" value="none" />
        <c:set var="spanAlign" value="block" />
        <c:set var="resetLayout" value="none" />
        <c:set var="tableGridColumns" value="none" />
        <c:set var="tableGridColumnsList" value="none" />
        <c:set var="note" value="block" />
    </c:when>

    <c:when test="${form.fieldType.value == fieldCheckBoxBracket || form.fieldType.value == fieldRadioButtonBracket || form.fieldType.value == fieldPullDownMenuBracket}">
        <c:set var="hiddenField" value="block" />
        <c:set var="requiredField" value="block" />
        <c:set var="fieldNameField" value="block" />
        <c:set var="fieldSizeField" value="none" />
        <c:set var="defaultValueField" value="none" />
        <c:set var="maxLengthField" value="none" />
        <c:set var="optionField" value="block" />
        <c:set var="maxRowsColsField" value="none" />
        <c:set var="dataTypeField" value="none" />
        <c:set var="labelValueField" value="none" />
        <c:set var="independentField" value="none" />
        <c:set var="gridNameField" value="none" />
        <c:set var="gridNumberField" value="none" />
        <c:set var="gridFormulaField" value="none" />
        <c:set var="gridItemField" value="none" />
        <c:set var="spanAlign" value="block" />
        <c:set var="resetLayout" value="none" />
        <c:set var="tableGridColumns" value="none" />
        <c:set var="tableGridColumnsList" value="none" />
        <c:set var="note" value="block" />
    </c:when>

    <c:when test="${form.fieldType.value == fieldDateSelectBracket || form.fieldType.value == fieldFileUploadBracket}">
        <c:set var="hiddenField" value="block" />
        <c:set var="requiredField" value="block" />
        <c:set var="fieldNameField" value="block" />
        <c:set var="fieldSizeField" value="none" />
        <c:set var="defaultValueField" value="none" />
        <c:set var="maxLengthField" value="none" />
        <c:set var="optionField" value="none" />
        <c:set var="maxRowsColsField" value="none" />
        <c:set var="dataTypeField" value="none" />
        <c:set var="labelValueField" value="none" />
        <c:set var="independentField" value="none" />
        <c:set var="gridNameField" value="none" />
        <c:set var="gridNumberField" value="none" />
        <c:set var="gridFormulaField" value="none" />
        <c:set var="gridItemField" value="none" />
        <c:set var="spanAlign" value="block" />
        <c:set var="resetLayout" value="none" />
        <c:set var="tableGridColumns" value="none" />
        <c:set var="tableGridColumnsList" value="none" />
        <c:set var="note" value="block" />
    </c:when>
    
    <c:when test="${form.fieldType.value == fieldTemplateDeviderBracket || form.fieldType.value == fieldSelectBracket}">
        <c:set var="independentField" value="none" />
        <c:set var="spanAlign" value="none" />
        <c:set var="resetLayout" value="none" />
        <c:set var="tableGridColumns" value="none" />
        <c:set var="tableGridColumnsList" value="none" />
    </c:when>
    
    <c:when test="${form.fieldType.value == fieldTableGridBracket}">
        <c:set var="hiddenField" value="none" />
        <c:set var="requiredField" value="none" />
        <c:set var="fieldNameField" value="none" />
        <c:set var="fieldSizeField" value="none" />
        <c:set var="defaultValueField" value="none" />
        <c:set var="maxLengthField" value="none" />
        <c:set var="optionField" value="none" />
        <c:set var="maxRowsColsField" value="none" />
        <c:set var="dataTypeField" value="none" />
        <c:set var="labelValueField" value="none" />
        <c:set var="independentField" value="none" />

        <c:set var="gridNameField" value="none" />
        <c:set var="gridNumberField" value="none" />

        <c:set var="gridFormulaField" value="none" />
        <c:set var="gridItemField" value="none" />
        <c:set var="spanAlign" value="block" />
        <c:set var="resetLayout" value="none" />
        <c:set var="tableGridColumns" value="block" />
        <c:set var="tableGridColumnsList" value="block" />
        <c:set var="note" value="block" />
    </c:when>

    <c:otherwise>
        <c:set var="hiddenField" value="none" />
        <c:set var="requiredField" value="none" />
        <c:set var="fieldNameField" value="none" />
        <c:set var="fieldSizeField" value="none" />
        <c:set var="defaultValueField" value="none" />
        <c:set var="maxLengthField" value="none" />
        <c:set var="optionField" value="none" />
        <c:set var="maxRowsColsField" value="none" />
        <c:set var="dataTypeField" value="none" />
        <c:set var="labelValueField" value="none" />
        <c:set var="independentField" value="block" />
        <c:set var="gridNameField" value="none" />
        <c:set var="gridNumberField" value="none" />
        <c:set var="gridFormulaField" value="none" />
        <c:set var="spanAlign" value="block" />
        <c:set var="resetLayout" value="none" />
        <c:set var="tableGridColumns" value="none" />
        <c:set var="tableGridColumnsList" value="none" />        
    </c:otherwise>
</c:choose>





<jsp:include page="../form_header.jsp" flush="true"/>

<script>
<!--

function selectField() {


    var formName = '<c:out value="${form.absoluteName}"/>';
    var form = document.forms[formName];

    var fieldType = form['<c:out value="${form.fieldType.absoluteName}"/>'].value;
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


    if (form['<c:out value="${form.maxLength.absoluteName}"/>'] != null)
		form['<c:out value="${form.maxLength.absoluteName}"/>'].value = '';


    if (form['<c:out value="${form.rtbLabel.absoluteName}"/>'] != null)
		form['<c:out value="${form.rtbLabel.absoluteName}"/>'].value = '';

    if (form['<c:out value="${form.maxRows.absoluteName}"/>'] != null)
		form['<c:out value="${form.maxRows.absoluteName}"/>'].value = '';

    if (form['<c:out value="${form.fieldSize.absoluteName}"/>'] != null)
		form['<c:out value="${form.fieldSize.absoluteName}"/>'].value = '';


    if (form['<c:out value="${form.tfColspan.absoluteName}" />'] != null) {
        form['<c:out value="${form.tfColspan.absoluteName}"/>'].value = '1';
        if (fieldType == '<c:out value="${fieldTextBlock}"/>' || fieldType == '<c:out value="${fieldTableGrid}"/>' ||
           (fieldType != '<c:out value="${fieldTextBlock}"/>'
            && fieldType != '<c:out value="${fieldTextInput}"/>'
            && fieldType != '<c:out value="${fieldTextBox}"/>'
            && fieldType != '<c:out value="${fieldCheckBox}"/>'
            && fieldType != '<c:out value="${fieldRadioButton}"/>'
            && fieldType != '<c:out value="${fieldPullDownMenu}"/>'
            && fieldType != '<c:out value="${fieldDateSelect}"/>'
            && fieldType != '<c:out value="${fieldFileUpload}"/>'
            && fieldType != '<c:out value="${fieldTableGrid}"/>'))           
            form['<c:out value="${form.tfColspan.absoluteName}"/>'].value = '2';
    }

    if (form['<c:out value="${form.tfRowspan.absoluteName}" />'] != null)
        form['<c:out value="${form.tfRowspan.absoluteName}"/>'].value = '1';

    if (form['<c:out value="${form.independentField.absoluteName}"/>'] != null)
        form['<c:out value="${form.independentField.absoluteName}"/>'].checked = false;

    if (form['<c:out value="${form.cbResetLayout.absoluteName}"/>'] != null)
        form['<c:out value="${form.cbResetLayout.absoluteName}"/>'].checked = true;

    if (form['<c:out value="${form.sbAlign.absoluteName}"/>'] != null)
        form['<c:out value="${form.sbAlign.absoluteName}"/>'].options[0].selected = true;

    if (form['<c:out value="${form.sbValign.absoluteName}"/>'] != null)
        form['<c:out value="${form.sbValign.absoluteName}"/>'].options[0].selected = true;

    if (form['<c:out value="${form.tfTitle.absoluteName}"/>'] != null)
        form['<c:out value="${form.tfTitle.absoluteName}"/>'].value = '';





    form.submit();

}
function toggleFormLayer(layerId, displayType) {
	document.getElementById(layerId).style.display = displayType;
}

function toggleLayers(field){
    var formName = '<c:out value="${form.absoluteName}"/>';
    var form = document.forms[formName];

    var fieldType = form['<c:out value="${form.fieldType.absoluteName}"/>'].value;       
    if (fieldType == '<c:out value="${fieldTextBlock}"/>') {
        toggleFormLayer('requiredField','none');
    }
	else if (field == 'hiddenFieldYes') {
		toggleFormLayer('requiredField','none');
    }
	else if (field == 'hiddenFieldNo') {
		toggleFormLayer('requiredField','block');
    }
	else if (field == 'requiredFieldYes') {
		toggleFormLayer('hiddenField','none');
    }
	else if (field == 'requiredFieldNo')  {
		toggleFormLayer('hiddenField','block');
    }
    else if (field == 'independentField')
    {
        if(document.getElementById('resetLayout').style.display == 'block') {
           toggleFormLayer('resetLayout','none');
        }
        else {
           toggleFormLayer('resetLayout','block');
        }

        if(document.getElementById('spanAlign').style.display == 'block') {
           toggleFormLayer('spanAlign','none');
        }
        else {
           toggleFormLayer('spanAlign','block');
        }
    }
}


function doEdit() {
    u = "<c:url value="frwAddG2FieldForm.jsp?absoluteName=${form.absoluteName}&formId=${form.formId}" />"
    var g2FieldPopup = window.open(u, "g2FieldPopup");
    if (g2FieldPopup != null)
        g2FieldPopup.focus();
}

//-->
</script>

<table width="100%">

<tr>
	<td colspan = "2">
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.fieldType'/></B></td>
                <td>
    	            <x:display name="${form.fieldType.absoluteName}"/>
		            <c:out value = "${form.fieldType.message}" />
                </td>
            </tr>
        </table>
    </td>
</tr>

<tr>
    <td colspan = "2">
        <div id = "independentField" style='display:<c:out value="${independentField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B>Independent Field</B></td>
                    <td align="left">
                        <x:display name="${form.independentField.absoluteName}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>



<tr>
    <td colspan = "2">
        <div id = "note" style='display:<c:out value="${note}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" colspan="2"><font color="red"><fmt:message key='formWizard.message.field.note'/></font></td>
                </tr>

                <tr>
                    <td valign="top" align="right" colspan="2">&nbsp;</td>
                </tr>
            </table>
        </div>
    </td>
</tr>

<tr>
    <td colspan = "2">
        <div id = "spanAlign" style='display:<c:out value="${spanAlign}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.columnSpan'/></B></td>
                    <td align="left">
                        <x:display name="${form.tfColspan.absoluteName}" />
                        <c:out value = "${form.tfColspan.message}" />
                    </td>
                </tr>

                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.rowSpan'/></B></td>
                    <td align="left">
                        <x:display name="${form.tfRowspan.absoluteName}" />
                        <c:out value = "${form.tfRowspan.message}" />
                    </td>
                </tr>

                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.alignment'/></B></td>
                    <td align="left">
                        <x:display name="${form.sbAlign.absoluteName}" />
                    </td>
                </tr>

                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.verticalAlignment'/></B></td>
                    <td align="left">
                        <x:display name="${form.sbValign.absoluteName}" />
                    </td>
                </tr>


            </table>
         </div>
    </td>
</tr>


<tr>
	<td colspan = "2">
		<div id = "hiddenField" style='display:<c:out value="${hiddenField}"/>'>
			<table width = "100%">
				<tr>
					<td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.hiddenField'/></B></td>
					<td>
						<x:display name="${form.hiddenNo.absoluteName}"/> <fmt:message key='formWizard.label.no'/><br>
						<x:display name="${form.hiddenYes.absoluteName}"/> <fmt:message key='formWizard.label.yes'/>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>




<tr>
	<td colspan = "2">
		<div id = "requiredField" style='display:<c:out value="${requiredField}"/>'>
			<table width = "100%">
				<tr>
					<td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.requiredField'/></B></td>
					<td>
						<x:display name="${form.requiredNo.absoluteName}"/> <fmt:message key='formWizard.label.requiredField.no'/> <br>
						<x:display name="${form.requiredYes.absoluteName}"/> <fmt:message key='formWizard.label.requiredField.yes'/>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>




<tr>
    <td colspan = "2">
    	<div id = "fieldNameField" style='display:<c:out value="${fieldNameField}"/>'>
			<table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.fieldName'/></B></td>
                    <td align="left">
    	                <x:display name="${form.fieldName.absoluteName}"/>
		                <c:out value = "${form.fieldName.message}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>



<tr>
    <td colspan = "2">
        <div id = "fieldSizeField" style='display:<c:out value="${fieldSizeField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.fieldSize'/></B></td>
                    <td align="left">
	                    <x:display name="${form.fieldSize.absoluteName}"/>
		                <c:out value = "${form.fieldSize.message}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>


<tr>
    <td colspan = "2">
        <div id = "defaultValueField" style='display:<c:out value="${defaultValueField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.defaultValue'/></B></td>
                    <td align="left">
    	                <x:display name="${form.tfDefaultValue.absoluteName}"/>
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>

<tr>
    <td colspan = "2">
        <div id = "labelValueField" style='display:<c:out value="${labelValueField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.label'/></B></td>
                    <td align="left">
    	                <x:display name="${form.rtbLabel.absoluteName}"/>
                        <c:out value = "${form.rtbLabel.message}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>

<tr>
    <td colspan = "2">
        <div id = "maxLengthField" style='display:<c:out value="${maxLengthField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.maxLength'/></B></td>
                    <td align="left">
    	                <x:display name="${form.maxLength.absoluteName}"/>
		                <c:out value = "${form.maxLength.message}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>



<tr>
    <td colspan = "2">
        <div id = "optionField" style='display:<c:out value="${optionField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right">&nbsp;</td>
                    <td align="left">
   	                    <fmt:message key='formWizard.label.separateOption'/> <b><fmt:message key='formWizard.label.blankLine'/></b>.
                    </td>
                </tr>
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.options'/></B></td>
                    <td align="left">
                        <x:display name="${form.options.absoluteName}"/><br>
	                    <c:out value = "${form.options.message}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>


<tr>
    <td colspan = "2">
        <div id = "maxRowsColsField" style='display:<c:out value="${maxRowsColsField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.maxRows'/></B></td>
                    <td align="left">
                        <x:display name="${form.maxRows.absoluteName}"/>
	                    <c:out value = "${form.maxRows.message}" />
                    </td>
                </tr>
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.maxCols'/></B></td>
                    <td align="left">
	                    <x:display name="${form.maxCols.absoluteName}"/>
		                <c:out value = "${form.maxCols.message}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>


<tr>
    <td colspan = "2">
        <div id = "dataTypeField" style='display:<c:out value="${dataTypeField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.dataType'/></B></td>
                    <td align="left">
                        <x:display name="${form.tfDataType.absoluteName}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>





<tr>
    <td colspan = "2">
        <div id = "tableGridColumns" style='display:<c:out value="${tableGridColumns}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.title'/></B></td>
                    <td align = "left">
                        <x:display name="${form.tfTitle.absoluteName}" />
                        <c:out value = "${form.tfTitle.message}" />
                    </td>
                </tr>

                <tr>                
                    <td colspan="2" align="center"><a href="javascript:doEdit()"><fmt:message key='formWizard.label.addTableGrid'/></a></td>
                </tr>

                <tr>
                    <td colspan="2" align="center"><x:display name="${form.lbColumnLabel.absoluteName}" /></td>
                </tr>


            </table>
        </div>
    </td>
</tr>

<tr>
    <td colspan = "2" >
        <div id = "<c:out value="${widget.absoluteName}.div"/>" style='display:<c:out value="${tableGridColumnsList}"/>'>

<%
    WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");

    if(wm!=null) {
	    AddG2FieldForm form = (AddG2FieldForm)wm.getWidget("addG2FieldFormPage.addG2FieldForm");
        if (form != null) {
%>
    <c-rt:set var="columnList" value="<%= form.getColumnList() %>"/>
    <table width="100%">
               <tr>
                    <td valign="top" align="center" colspan="2"><B><fmt:message key='formWizard.label.listOfColumns'/></B></td>
                </tr>

                <tr>
                    <td valign="top" align="center" colspan="2">
                        <table cellpadding="4" cellspacing="4">
                            <c:choose>
                                <c:when test="${empty columnList}">
                                    <tr>
                                        <td valign="top" align="center"><B><fmt:message key='formWizard.label.noColumnsFound'/></B></td>
                                    </tr>
                                </c:when>

                                <c:otherwise>
                                    <tr style="background: #4779AB">
                                        <td><span style="color:white; font-weight:bold"><fmt:message key='formWizard.label.id'/></span></td>
                                        <td><span style="color:white; font-weight:bold"><fmt:message key='formWizard.label.header'/></span></td>
                                        <td><span style="color:white; font-weight:bold"><fmt:message key='formWizard.label.type'/></span></td>
                                    </tr>
                                    <c:forEach items="${columnList}" var="list">
                                        <tr>
                                            <td valign="top" align="left"><c:out value="${list.name}"/></td>
                                            <td valign="top" align="left"><c:out value="${list.header}"/></td>
                                            <td valign="top" align="left"><c:out value="${list.type}"/></td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </table>
                    </td>
                </tr>
                </table>
<%
        }
    }
%>

        </div>
    </td>
</tr>


<tr>
    <td valign="top"  align="center" colspan = "2">
        <c:if test="${form.fieldType.value != fieldSelectBracket && form.fieldType.value != fieldTemplateDeviderBracket}">
            <x:display name="${form.add.absoluteName}"/>
        </c:if>
        <x:display name="${form.finish.absoluteName}"/>
    </td>
</tr>



<c:if test="${!empty param.columnId && param.et == 'editColumn'}">
    <input type = "hidden" name="columnId" value="<c:out value="${param.columnId}"/>">
</c:if>


</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
