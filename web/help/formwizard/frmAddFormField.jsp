<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Type of Fields</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Text Block</td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			Information that will appear in the form. This field does not involve data entry. Option available in this field <br>
			<li><em>Label</em> indicates the information to be displayed in the form.</li>
		</td>
	</tr>
    <tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Text Input</td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			Single row of field for data input. Options available in this field: <br>
			<li><em>Field Name</em> indicates the name of the field.</li>
			<li><em>Field Size</em> determines the size of the field.</li>
			<li><em>Default Value</em> indicates the default valie of the field.</li>
			<li><em>Max Value </em>determines the maximum number of text allowable in the text area.</li>
			<li><em>Data Type</em> specifies the rtpe of validation that the system will perform before user submitting the form.</li>
		</td>
	</tr>
    <tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Text Box</td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			Two dimension field type that made up of rows and columns. Options available in this field: <br>
			<li><em>Field Name</em> indicates the name of the field.</li>
			<li><em>Field Size</em> determines the size of the field.</li>
			<li><em>Default Value</em> indicates the default value of the field.</li>
			<li><em>Max Rows</em> indicates the the horizontal display size of the field.</li>
			<li><em>Max Cols</em> indicates the vertical display size of the field.</li>
		</td>
	</tr>
    <tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Check Box</td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			Selection field type that only allows single selection. Options available in this field: <br>
			<li><em>Field Name</em> indicates the name of the field.</li>
			<li><em>Options</em> represents a selection of all available values.</li>
		</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Radio Button</td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			Selection field type that allows multiple selections. Options available in this field: <br>
			<li><em>Field Name</em> indicates the name of the field.</li>
			<li><em>Options </em>represents a selection of all available values.</li>
		</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Pull Down Menu</td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			Selection is from a pull-down menu. Options available in this field:<br>
			<li><em>Field Name</em> indicates the name of the field.</li>
			<li><em>Options</em> represents a selection of all available values.</li>
		</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Date Select</td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			Field type that accepts date entry. Option available in this field: <br>
			<li><em>Field Name</em> indicates the name of the field.</li>
		</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">File Upload</td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			Field type that accepst file upload. Option available in this field: <br>
  			<li><em>Field Name </em>indicates the name of the field.</li>
		</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Table Grid</td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			Table Grid is a HTML table that allows the creator to define number of fixed columns (items). Simple formula can be applied to each cell (such as Plus"+", Minus"-", Multiply"*" and divide"/"). Options available in this field: <br>
			<li><em>Title</em> indicates the name of the field.</li>
		</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Adding Form Fields</td></tr>
    <tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">Select a field type. The screen will refreshed.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">
			Using the advance formatting options: <br>
			<li><em>Column Span</em> indicates the number of columns to be occupied by the field.</li>
			<li><em>Row Span</em> indicates the number of rows to be occupied by the field.</li>
			<li><em>Alignment</em> indicates whether the field will appear on left, center or right side of the cell.</li>
			<li>Vertical Alignment indicates whether the field will appear on top, middle or bottom of the cell.</li>
			<em>NOTE: These options will affect the form if the form Table Column is more than 2.</em>
		</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Selecting <strong>No</strong> for <em>Hidden Field</em> option indicates that the field will not be hidden when user submits the form.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">4.</td>
		<td valign=top align=left class="text">Select <strong>Yes</strong> for <em>Required Field </em>option indicates that the field is required to be inserted/selected by the user when submitting the form.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">5.</td>
		<td valign=top align=left class="text">Click on the <strong>Add </strong>button.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">6.</td>
		<td valign=top align=left class="text">Repeat step 1 to 5 if needed. Click on <strong>Done</strong> button to <em>Activate </em>the form.</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Creating A Table Grid Field</td></tr>
    <tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">Select <strong>Table Grid </strong>from the menu.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">Insert the title.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Click on the <strong>Add Table Grid Columns</strong> link. A new window will appear.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">4.</td>
		<td valign=top align=left class="text">Insert the ID and Header.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">5.</td>
		<td valign=top align=left class="text">
			Select a type of field to be added into the table: <br>
			<li><em>Text</em> indicates normal text.</li>
			<li><em>Formula </em>indicates the field is a formula.</li>
			<li><em>Select box </em>indicates the field is a selection menu.</li>
		</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">6.</td>
		<td valign=top align=left class="text"><em>Calculate Total</em> option indicate the field will display total amount entered. This denotes that the field is a number.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">7.</td>
		<td valign=top align=left class="text">
			Select an option from the Validating field: <br>
			<li><em>Required Column </em>indicates the field is a compulsory field.</li>
			<li><em>Number</em> indicates the field is a numeric type.</li>
		</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">8.</td>
		<td valign=top align=left class="text">Click on the <strong>Add Column</strong> button.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">9.</td>
		<td valign=top align=left class="text">Repeat steps 4 to 7 to add new columns. Click on <strong>Return To Main Form</strong> link when finish.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">10.</td>
		<td valign=top align=left class="text">Click on <strong>Add </strong>button to complete the table grid field.</td>
	</tr>
</table>
<%@ include file="../includes/footer.jsp" %>