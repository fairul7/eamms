<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<html style="width:300px; Height: 60px;">
 <head>
  <title><fmt:message key='richTextBox.selectPhrase'/></title>
<script language="javascript">

var myTitle = window.dialogArguments;
document.title = myTitle;


function returnSelected() {
  var idx  = document.all.textPulldown.selectedIndex;
  var text = document.all.textPulldown[idx].text;

  window.returnValue = text;          // set return value
  window.close();                     // close dialog
}

</script>
</head>
<body bgcolor="#FFFFFF" topmargin=15 leftmargin=0>

<form method=get onSubmit="Set(document.all.ColorHex.value); return false;">
<div align=center>

<select name="textPulldown">
<option><fmt:message key='richTextBox.phrase1'/></option>
<option><fmt:message key='richTextBox.phrase2'/></option>
<option><fmt:message key='richTextBox.phrase3'/></option>
</select>

<input type="button" class="button" value=" <fmt:message key='richTextBox.go'/> " onClick="returnSelected()">

</div>
</form>
</body></html>