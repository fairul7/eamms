<%@ page import="com.tms.collab.formwizard.grid.G2Column"%>
<%@ include file="/common/header.jsp" %>

<c:set var="form" value="${widget}"/>

<c:if test="${!empty param.absoluteName}">
    <c:set var="absoluteName" value="${param.absoluteName}" scope="session" />
</c:if>



<c-rt:set var="typeTextBracket" value="<%= '[' + G2Column.TYPE_TEXT + ']'%>"/>
<c-rt:set var="typeFormulaBracket" value="<%= '[' + G2Column.TYPE_FORMULA + ']'%>"/>
<c-rt:set var="typeDropDownBracket" value="<%= '[' + G2Column.TYPE_DROP_DOWN + ']'%>"/>

<c-rt:set var="typeText" value="<%= G2Column.TYPE_TEXT %>"/>
<c-rt:set var="typeFormula" value="<%= G2Column.TYPE_FORMULA %>"/>
<c-rt:set var="typeDropDown" value="<%= G2Column.TYPE_DROP_DOWN %>"/>



<c:set var="gridNameField" value="block" />
<c:set var="gridNumberField" value="block" />

<c:set var="gridFormulaField" value="none" />
<c:set var="gridItemField" value="none" />


<c:choose>
    <c:when test="${form.sbType.value == typeTextBracket}">
        <c:set var="gridNumberField" value="block" />
    </c:when>

    <c:when test="${form.sbType.value == typeFormulaBracket}">
        <c:set var="gridFormulaField" value="block" />
        <c:set var="gridNumberField" value="block" />
    </c:when>

    <c:when test="${form.sbType.value == typeDropDownBracket}">
        <c:set var="gridItemField" value="block" />
        <c:set var="gridNumberField" value="block" />
    </c:when>
</c:choose>


<jsp:include page="../form_header.jsp" flush="true"/>

<script>
<!--

function selectType() {
    var formName = '<c:out value="${form.absoluteName}"/>';
    var form = document.forms[formName];
    var type = form['<c:out value="${form.sbType.absoluteName}" />'].value;

    if (type == '<c:out value="${typeFormula}"/>') {
        toggleFormLayer('gridNumberField','block');
        toggleFormLayer('gridFormulaField','block');
        toggleFormLayer('gridItemField','none');
    }
    else if (type == '<c:out value="${typeDropDown}"/>') {
        toggleFormLayer('gridNumberField','block');
        toggleFormLayer('gridFormulaField','none');
        toggleFormLayer('gridItemField','block');
    }
    else {
        toggleFormLayer('gridNumberField','block');
        toggleFormLayer('gridFormulaField','none');
        toggleFormLayer('gridItemField','none');
    }
}

function toggleFormLayer(layerId, displayType) {
	document.getElementById(layerId).style.display = displayType;
}

function doDone() {
     try {
        maxRows = 10000;
        for(i=0; i < maxRows; i++) {
            document.getElementById("tableAction" + i).style.display = "none";
        }
     } catch(e) {
        // done
    }
    opener.document.getElementById("<c:out value="${absoluteName}.div" />").innerHTML = document.getElementById("htmlTable").innerHTML;
    window.close();
}

//-->
</script>

<table width="100%">

<tr>
    <td colspan = "2">
        <div id = "gridNameField" style='display:<c:out value="${gridNameField}"/>'>
  		    <table width = "100%">
               <%--<tr>
                    <td valign="top" align="right" width = "30%"><B>Title</B></td>
                    <td>
                        <x:display name="${form.tfTitle.absoluteName}" />
                        <c:out value = "${form.tfTitle.message}" />
                    </td>
                </tr>--%>

                <tr>
                    <td valign="top" align="center" colspan="2">&nbsp;</td>
                </tr>

                <tr>
                    <td valign="top" align="center" colspan="2"><x:display name="${form.lbColumnLabel.absoluteName}" /></td>
                </tr>

                <tr>
                <td colspan="2">
<div id="htmlTable">
    <table width="100%">
                <tr>
                    <td valign="top" align="center" colspan="2"><B><fmt:message key='formWizard.label.listOfColumns'/></B></td>
                </tr>

                <tr>
                    <td valign="top" align="center" colspan="2">
                        <table cellpadding="4" cellspacing="4">

                            <c:choose>
                                <c:when test="${empty form.columnList}">
                                    <tr>
                                        <td valign="top" align="center"><B><fmt:message key='formWizard.label.addG2FieldForm.noColumnsFound'/></B></td>
                                    </tr>
                                </c:when>

                                <c:otherwise>
                                    <tr style="background: #4779AB">
                                        <td><span style="color:white; font-weight:bold"><fmt:message key='formWizard.label.addG2FieldForm.id'/> *</span></td>
                                        <td><span style="color:white; font-weight:bold"><fmt:message key='formWizard.label.addG2FieldForm.header'/> *</span></td>
                                        <td><span style="color:white; font-weight:bold"><fmt:message key='formWizard.label.addG2FieldForm.type'/></span></td>
                                        <td id="tableAction0"><span style="color:white; font-weight:bold">&nbsp;</span></td>
                                    </tr>
                                    <c:forEach items="${form.columnList}" var="list" varStatus="status">
                                        <tr>
                                            <td valign="top" align="left"><c:out value="${list.name}"/></td>
                                            <td valign="top" align="left"><c:out value="${list.header}"/></td>
                                            <td valign="top" align="left"><c:out value="${list.type}"/></td>
                                            <td valign="top" align="left" id="tableAction<c:out value="${status.count}" />">
                                                [ <a href="?cn=<c:out value="${form.absoluteName}"/>&et=editColumn&columnId=<c:out value="${list.name}"/>"><fmt:message key='formWizard.label.edit'/></a>
                                                <c:if test="${form.tableGridDataEmpty}" >
                                                | <a href="?cn=<c:out value="${form.absoluteName}"/>&et=deleteColumn&columnId=<c:out value="${list.name}"/>"><fmt:message key='formWizard.label.delete'/></a>
                                                </c:if>
                                                ]
                                            </td>
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



                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.addG2FieldForm.id'/></B></td>
                    <td>
                        <x:display name="${form.tfId.absoluteName}" />
                        <c:out value = "${form.tfId.message}" />
                    </td>
                </tr>


                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.addG2FieldForm.header'/></B></td>
                    <td>
                        <x:display name="${form.tfHeader.absoluteName}" />
                        <c:out value = "${form.tfHeader.message}" />
                    </td>
                </tr>

                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.addG2FieldForm.type'/></B></td>
                    <td>
                        <x:display name="${form.sbType.absoluteName}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>

<tr>
    <td colspan = "2">
        <div id = "gridFormulaField" style='display:<c:out value="${gridFormulaField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.addG2FieldForm.formula'/></B></td>
                    <td>
                        <x:display name="${form.tfFormula.absoluteName}" />
                        <c:out value = "${form.tfFormula.message}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>

<tr>
    <td colspan = "2">
        <div id = "gridItemField" style='display:<c:out value="${gridItemField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.addG2FieldForm.options'/></B></td>
                    <td>
                        <x:display name="${form.tbItems.absoluteName}" />
                        <c:out value = "${form.tbItems.message}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>

<tr>
    <td colspan = "2">
        <div id = "gridNumberField" style='display:<c:out value="${gridNumberField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.addG2FieldForm.CalculateTotal'/></B></td>
                    <td>
                        <x:display name="${form.cbTotal.absoluteName}" />
                    </td>
                </tr>
            </table>
        </div>
    </td>
</tr>

<tr>
    <td colspan = "2">
        <div id = "gridNameField" style='display:<c:out value="${gridNameField}"/>'>
  		    <table width = "100%">
                <tr>
                    <td valign="top" align="right" width = "30%"><B><fmt:message key='formWizard.label.addG2FieldForm.validationAs'/></B></td>
                    <td>
                        <x:display name="${form.sbValidation.absoluteName}" />
                    </td>
                </tr>

                <tr>
                    <td valign="top" align="center" colspan="2">                    
                        <c:if test="${form.tableGridDataEmpty}" >
                            <x:display name="${form.btAddColumn.absoluteName}" />
                        </c:if>

                        <c:if test="${!empty param.columnId && param.et == 'editColumn'}">
                            <x:display name="${form.btUpdateColumn.absoluteName}" />
                        </c:if>

                        <x:display name="${form.btReturnToMainForm.absoluteName}" />
                    </td>
                </tr>



                 <tr>
                    <td valign="top" align="center" colspan="2">&nbsp;</td>
                </tr>

            </table>
        </div>
    </td>
</tr>

</table>

<c:if test="${!empty param.columnId && param.et == 'editColumn'}">
    <input type = "hidden" name="columnId" value="<c:out value="${param.columnId}"/>">
</c:if>

<jsp:include page="../form_footer.jsp" flush="true"/>


