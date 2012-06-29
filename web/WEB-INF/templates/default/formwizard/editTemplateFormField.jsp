<%@ page import="com.tms.collab.formwizard.model.FormConstants,
                 com.tms.collab.formwizard.grid.G2Column,
                 kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddG2FieldForm,
                 com.tms.collab.formwizard.grid.G2Field"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
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



<c-rt:set var="typeTextBracket" value="<%= '[' + G2Column.TYPE_TEXT + ']'%>"/>
<c-rt:set var="typeFormulaBracket" value="<%= '[' + G2Column.TYPE_FORMULA + ']'%>"/>
<c-rt:set var="typeDropDownBracket" value="<%= '[' + G2Column.TYPE_DROP_DOWN + ']'%>"/>
<c-rt:set var="typeText" value="<%= G2Column.TYPE_TEXT %>"/>
<c-rt:set var="typeFormula" value="<%= G2Column.TYPE_FORMULA %>"/>
<c-rt:set var="typeDropDown" value="<%= G2Column.TYPE_DROP_DOWN %>"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<script>
<!--
function toggleFormLayer(layerId, displayType) {
	document.getElementById(layerId).style.display = displayType;
}
function toggleLayers(field){
    if ('<c:out value="${form.fieldTypeValue.text}"/>' == '<c:out value="${fieldTextBlock}"/>')
        toggleFormLayer('requiredField','none');
	else if (field == 'hiddenFieldYes')
		toggleFormLayer('requiredField','none');
	else if (field == 'hiddenFieldNo')
		toggleFormLayer('requiredField','block');
	else if (field == 'requiredFieldYes')
		toggleFormLayer('hiddenField','none');
	else if (field == 'requiredFieldNo')
		toggleFormLayer('hiddenField','block');
}
function doEdit() {
    u = "<c:url value="frwAddG2FieldForm.jsp?absoluteName=${form.absoluteName}&widgetName=${form.absoluteName}.${form.nodeName}&formTemplateId=${form.formTemplateId}&formUid=${form.formUid}"/>";
    var g2FieldPopup = window.open(u, "g2FieldPopup");
    if (g2FieldPopup != null)
        g2FieldPopup.focus();
}
//-->
</script>
<c:set var="requiredField" value = "block"/>
<c:set var="hiddenField" value = "block"/>
<c:set var="gridNameField" value="none" />
<c:set var="gridNumberField" value="none" />
<c:set var="gridFormulaField" value="none" />
<c:set var="gridItemField" value="none" />

<c:if test = "${form.childMap.requiredYes.checked}">
	<c:set var="requiredField" value = "block"/>
	<c:set var="hiddenField" value = "none"/>
</c:if>
<c:if test = "${form.childMap.hiddenYes.checked}">
	<c:set var="requiredField" value = "none"/>
	<c:set var="hiddenField" value = "block"/>
</c:if>
<c:if test="${form.fieldTypeValue.text == fieldTextBlock}">
    <c:set var="requiredField" value="none" />
</c:if>

<c:if test="${form.fieldTypeValue.text == fieldTableGrid || (form.fieldTypeValue.text != fieldTextBlock
                                                             && form.fieldTypeValue.text != fieldTextInput
                                                             && form.fieldTypeValue.text != fieldTextBox
                                                             && form.fieldTypeValue.text != fieldCheckBox
                                                             && form.fieldTypeValue.text != fieldRadioButton
                                                             && form.fieldTypeValue.text != fieldPullDownMenu
                                                             && form.fieldTypeValue.text != fieldDateSelect
                                                             && form.fieldTypeValue.text != fieldFileUpload
                                                             )}">
    <c:set var="requiredField" value="none" />
    <c:set var="hiddenField" value = "none"/>
</c:if>
<table width="100%">
    <tr><td><b><x:display name="${form.fieldTypeLabel.absoluteName}" /></b></td></tr>
    <tr>
        <td colspan = "2">
            <div id = "spanAlign">
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
	<td>
		<div id = "hiddenField" style='display:<c:out value="${hiddenField}"/>'>
			<table width = "100%">
				<tr>
					<td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.hiddenField'/></B></td>
					<td>
						<x:display name="${form.hiddenNo.absoluteName}"/> <fmt:message key='formWizard.label.no'/> &nbsp;&nbsp;
						<x:display name="${form.hiddenYes.absoluteName}"/> <fmt:message key='formWizard.label.yes'/>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>


<tr>
	<td>
		<div id = "requiredField" style='display:<c:out value="${requiredField}"/>'>
			<table width = "100%">
				<tr>
					<td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.requiredField'/></B></td>
					<td>
						<x:display name="${form.requiredNo.absoluteName}"/> <fmt:message key='formWizard.label.requiredField.no'/> &nbsp;&nbsp;
						<x:display name="${form.requiredYes.absoluteName}"/> <fmt:message key='formWizard.label.requiredField.yes'/>
					</td>
				</tr>
			</table>
		</div>
	</td>
</tr>



<c:if test="${!form.fieldName.hidden}">
<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.fieldName'/></B></td>
                <td>
    	            <x:display name="${form.fieldName.absoluteName}"/>
		            <c:out value = "${form.fieldName.message}" />
                </td>
            </tr>
        </table>
     </td>
</tr>
</c:if>

<c:if test="${!form.fieldSize.hidden}">
<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.fieldSize'/></B></td>
                <td align="left">
    	            <x:display name="${form.fieldSize.absoluteName}"/>
		            <c:out value = "${form.fieldSize.message}" />
                </td>
            </tr>
        </table>
     </td>
</tr>
</c:if>

<c:if test="${!form.tfDefaultValue.hidden}">
<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.defaultValue'/></B></td>
                <td align="left">
    	            <x:display name="${form.tfDefaultValue.absoluteName}"/>
                </td>
            </tr>
        </table>
     </td>
</tr>
</c:if>

<c:if test="${!form.maxLength.hidden}">
<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.maxLength'/></B></td>
                <td align="left">
    	            <x:display name="${form.maxLength.absoluteName}"/>
                    <c:out value = "${form.maxLength.message}" />
                </td>
            </tr>
        </table>
     </td>
</tr>
</c:if>

<c:if test="${!form.options.hidden}">
<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%">&nbsp;</td>
                <td align="left">
   	                <fmt:message key='formWizard.label.separateOption'/> <b><fmt:message key='formWizard.label.blankLine'/></b>.
                </td>
            </tr>
        </table>
    </td>
</tr>


<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.options'/></B></td>
                <td align="left">
    	            <x:display name="${form.options.absoluteName}"/>
		            <c:out value = "${form.options.message}" />
                </td>
            </tr>
        </table>
    </td>
</tr>
</c:if>

<c:if test="${!form.maxRows.hidden}">
<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.maxRows'/></B></td>
                <td align="left">
	                <x:display name="${form.maxRows.absoluteName}"/>
		            <c:out value = "${form.maxRows.message}" />
                </td>
            </tr>
        </table>
    </td>
</tr>
</c:if>

<c:if test="${!form.maxCols.hidden}">
<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.maxCols'/></B></td>
                <td align="left">
            	    <x:display name="${form.maxCols.absoluteName}"/>
		            <c:out value = "${form.maxCols.message}" />
                </td>
            </tr>
        </table>
    </td>
</tr>
</c:if>

<c:if test="${!form.tfDataType.hidden}">
<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.dataType'/></B></td>
                <td align="left">
                    <x:display name="${form.tfDataType.absoluteName}" />
                </td>
            </tr>
        </table>
    </td>
</tr>
</c:if>

<c:if test="${!form.rtbLabel.hidden}">
<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top" align="right" width="30%"><B><fmt:message key='formWizard.label.label'/></B></td>
                <td align="left">
                    <x:display name="${form.rtbLabel.absoluteName}" />
                    <c:out value = "${form.rtbLabel.message}" />
                </td>
            </tr>
        </table>
    </td>
</tr>
</c:if>
<c:if test="${!form.tfTitle.hidden}">
    <tr>
        <td colspan = "2">
            <div id = "tableGridColumns">
                <table width = "100%">
                    <tr>
                        <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.title'/></B></td>
                        <td align = "left">
                            <x:display name="${form.tfTitle.absoluteName}" />
                            <c:out value = "${form.tfTitle.message}" />
                        </td>
                    </tr>
                    <tr><td colspan="2" align="center"><a href="javascript:doEdit()"><fmt:message key='formWizard.label.addTableGrid'/></a></td></tr>
                    <tr><td colspan="2" align="center"><x:display name="${form.lbColumnLabel.absoluteName}" /></td></tr>
                </table>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan = "2" >
            <div id = "<c:out value="${widget.absoluteName}.div"/>">
                <table width="100%">
                    <tr><td valign="top" align="center" colspan="2"><B><fmt:message key='formWizard.label.listOfColumns'/></B></td></tr>
                    <tr>
                        <td valign="top" align="center" colspan="2">
                            <table cellpadding="4" cellspacing="4">
                                <c:set var="columnList" value="${widget.childMap[form.nodeName].columnList}"/>
                                <c:choose>
                                    <c:when test="${empty columnList}">
                                        <tr><td valign="top" align="center"><B><fmt:message key='formWizard.label.noColumnsFound'/></B></td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <tr style="background: #4779AB">
                                            <td><span style="color:white; font-weight:bold"><fmt:message key='formWizard.label.id'/></span></td>
                                            <td><span style="color:white; font-weight:bold"><fmt:message key='formWizard.label.header'/></span></td>
                                            <td><span style="color:white; font-weight:bold"><fmt:message key='formWizard.label.type'/></span></td>
                                        </tr>
                                        <c:forEach items="${columnList}" var="column">
                                            <tr>
                                                <td valign="top" align="left"><c:out value="${column.name}"/></td>
                                                <td valign="top" align="left"><c:out value="${column.header}"/></td>
                                                <td valign="top" align="left"><c:out value="${column.type}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
</c:if>

<tr>
    <td>
        <table width = "100%">
            <tr>
                <td valign="top"  align="center">
                    <x:display name="${form.btMoveUp.absoluteName}"/>
                    <x:display name="${form.btMoveDown.absoluteName}"/>
                    <x:display name="${form.save.absoluteName}"/>
                    <x:display name="${form.btRemove.absoluteName}"/>
                </td>
            </tr>
        </table>
    </td>
</tr>

<c:if test="${!empty param.columnId && param.et == 'editColumn'}">
    <input type = "hidden" name="columnId" value="<c:out value="${param.columnId}"/>">
</c:if>

</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
