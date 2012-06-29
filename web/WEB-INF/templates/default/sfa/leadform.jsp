<%@include file="/common/header.jsp"%>

<c:set var="form" value="${widget}"  ></c:set>

    <jsp:include page="../form_header.jsp" flush="true"/>


<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
    <tr>
        <td valign="center"><B>Select Existing Company &nbsp; *
            <br>OR<BR>
            New Company
        </B></td>
        <td colspan="3"> <x:display name="${form.sel_Companies.absoluteName}" /><br><BR>
            <B><x:display name="${form.newCompany.absoluteName}" />
            </td>
    </tr>

    <tr>
        <td valign="top">
            <B>Contact No.</B>
        </td>
        <td valign="top"colspan="3">
            <x:display name="${form.contactNo.absoluteName}" />
        </td>

    </tr>
    <tr>
            <td valign="top"><B>Contact Person</B></td>
            <td valign="top"colspan="3"><x:display name="${form.contactName.absoluteName}" /></td>
    </tr>


    <tr>
        <td valign="top"><B>Remarks</B></td>
        <td colspan ="3" valign="top">
            <x:display name="${form.remarks.absoluteName}" />
        </td>

    </tr>

    <tr>
        <td valign="top"><B>Source</B> </td>
        <td valign="top" colspan="3">
        <x:display name="${form.sel_OpportunitySource.absoluteName}" />
        </td>
    </tr>

    <tr>
        <td>
            &nbsp;
        </td>
        <td colspan="3">
            <x:display name="${form.submit.absoluteName}" />
            <x:display name="${form.cancel.absoluteName}" />

        </td>
    </tr>

</table>
<jsp:include page="../form_footer.jsp" flush="true"/>