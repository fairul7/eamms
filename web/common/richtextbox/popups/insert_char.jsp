<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html style="width: 624px; height: 173px;"><head><!--
  //======= Insert_Char.html ===========================================================================
  //  htmlArea 2.03
  //  http://www.interactivetools.com
  //
  //------- Author ---------------------------------------------------------------------------------------
  //  Original Author: Mark Benson
  //  Released: 09/29/03
  //  Description: Insert Special Characters
  //
  //------- Notes -----------------------------------------------------------------------------------------
  //    Based loosely off ISO entities name codes.
  //		If you have trouble with the main table overlaping adjust font size in styles
  //		If you DON'T want the mouseover hover comment out the tree lines bellow
  //------- Install ---------------------------------------------------------------------------------------
  //    // special characters
  //				else if (cmdID == 'SpecChar') {
  //					var newchar = showModalDialog(_editor_url + "popups/insert_char.html", '', "resizable: no; help: no; status: no; scroll: no;");
  //					if (newchar == '') {return;} 
  //				else {editor_insertHTML(objname,newchar);}
  //				}
  //
  //=======================================================================================================
--><title><fmt:message key='richTextBox.specialCharacter'/></title>

<meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type">
<meta content="no" http-equiv="MSThemeCompatible">
<style type="text/css">
<!--
html,body,input,span,div{background:#ccc;color:ButtonText;margin:0px;position:absolute;}
.Preview{padding-top:20px;border:#000 1px solid;background:#ececed;text-align:center;font:25pt Arial,Helvetica,sans-serif;}
.Spwr{width:20px;height:20;border:none;background:#fff;text-align:center;font:10pt Arial,Helvetica,sans-serif;}
input{width:67px;height:22px;border:#000 1px solid;text-align:center;background:#ececed;font-size:9pt;}
.btn{border:#000 1px solid;background:#ececed;text-align:center;}
.btnov{border:#000 1px outset;background:#e2e2e3;}
#MainTable{border:#000 1px solid;cursor:hand;}
-->
</style>
<script language="javascript">
<!--
function _CloseOnEsc() {
	if (event.keyCode == 27) { window.close(); return;}
}

function CloseWindow() {
	window.returnValue = null;
	window.close();
}

var ActiveHover = null;

function Init() {
	document.body.onkeypress = _CloseOnEsc;
	var TdCells = document.getElementById('MainTable').getElementsByTagName('TD');
	for (var i=0; i < TdCells.length; i++) {
	if (TdCells[i].className == "Spwr") {
		  TdCells[i].onmouseover = over; //// Remove to kill hover 1 of 3 ////
		  TdCells[i].onmouseout = out;
		  TdCells[i].onclick = InsertCode;
		}
	}
}

function over() {
	if (ActiveHover == this) return
	this.style.backgroundColor = "highlight" //// Remove to kill hover 2 of 3 ////
	this.style.color = "highlighttext" //// Remove to kill hover 3 of 3 ////
	document.getElementById('insert').value = this.title;	// Preview unescaped
	document.getElementById('previewchar').innerHTML = document.getElementById('insert').value;	// Preview escaped
}

function out() {
	if (ActiveHover == this) return
	this.style.backgroundColor = "FFFFFF"
	this.style.color = "#000000"
}

function InsertCode() {
	var Code = document.getElementById('insert').value = this.title;	// select character
	window.returnValue = Code;	// set return value
	window.close();
}
//-->
</script></head>


<body onload="Init()">

<div style="left: 5px; top: 5px; width: 532px; height: 136px;" id="container">
<table align="center" cellspacing="2" cellpadding="1" id="MainTable">
<tbody><tr>
<td title="&amp;lsquo;" class="Spwr">&#8216;</td>
<td title="&amp;rsquo;" class="Spwr">&#8217;</td>
<td title="&amp;ldquo;" class="Spwr">&#8220;</td>
<td title="&amp;rdquo;" class="Spwr">&#8221;</td>
<td title="&amp;quot;" class="Spwr">"</td>
<td title="&amp;bdquo;" class="Spwr">&#8222;</td>
<td title="&amp;sbquo;" class="Spwr">&#8218;</td>
<td title="&amp;uml;" class="Spwr">¨</td>
<td title="&amp;#732;" class="Spwr">&#732;</td>
<td title="&amp;dagger;" class="Spwr">&#8224;</td>
<td title="&amp;Dagger;" class="Spwr">&#8225;</td>
<td title="&amp;lsaquo;" class="Spwr">&#8249;</td>
<td title="&amp;rsaquo;" class="Spwr">&#8250;</td>
<td title="&amp;lt;" class="Spwr">&lt;</td>
<td title="=" class="Spwr">=</td>
<td title="&amp;gt;" class="Spwr">&gt;</td>
<td title="&amp;laquo;" class="Spwr">«</td>
<td title="&amp;raquo;" class="Spwr">»</td>
<td title="&amp;#710;" class="Spwr">&#710;</td>
<td title="&amp;uarr;" class="Spwr">&#8593;</td>
<td title="&amp;frasl;" class="Spwr">&#8260;</td>
<td title="&amp;rarr;" class="Spwr">&#8594;</td>
</tr><tr>
<td title="&amp;brvbar;" class="Spwr">¦</td>
<td title="&amp;ndash;" class="Spwr">&#8211;</td>
<td title="&amp;mdash;" class="Spwr">&#8212;</td>
<td title="&amp;iexcl;" class="Spwr">¡</td>
<td title="&amp;times;" class="Spwr">×</td>
<td title="&amp;trade;" class="Spwr">&#8482;</td>
<td title="&amp;copy;" class="Spwr">©</td>
<td title="&amp;reg;" class="Spwr">®</td>
<td title="&amp;darr;" class="Spwr">&#8595;</td>
<td title="&amp;divide;" class="Spwr">÷</td>
<td title="&amp;frac14;" class="Spwr">¼</td>
<td title="&amp;frac12;" class="Spwr">½</td>
<td title="&amp;frac34;" class="Spwr">¾</td>
<td title="&amp;sect;" class="Spwr">§</td>
<td title="&amp;curren;" class="Spwr">¤</td>
<td title="&amp;yen;" class="Spwr">¥</td>
<td title="&amp;cent;" class="Spwr">¢</td>
<td title="&amp;pound;" class="Spwr">£</td>
<td title="&amp;euro;" class="Spwr">&#8364;</td>
<td title="&amp;#402;" class="Spwr">&#402;</td>
<td title="&amp;spades;" class="Spwr">&#9824;</td>
<td title="&amp;clubs;" class="Spwr">&#9827;</td>
</tr><tr>
<td title="&amp;hearts;" class="Spwr">&#9829;</td>
<td title="&amp;amp;" class="Spwr">&amp;</td>
<td title="&amp;ordf;" class="Spwr">ª</td>
<td title="&amp;not;" class="Spwr">¬</td>
<td title="&amp;macr;" class="Spwr">¯</td>
<td title="&amp;deg;" class="Spwr">°</td>
<td title="&amp;plusmn;" class="Spwr">±</td>
<td title="&amp;#338;" class="Spwr">&#338;</td>
<td title="&amp;ordm;" class="Spwr">º</td>
<td title="&amp;sup1;" class="Spwr">¹</td>
<td title="&amp;sup2;" class="Spwr">²</td>
<td title="&amp;sup3;" class="Spwr">³</td>
<td title="&amp;acute;" class="Spwr">´</td>
<td title="&amp;micro;" class="Spwr">µ</td>
<td title="&amp;para;" class="Spwr">¶</td>
<td title="&amp;middot;" class="Spwr">·</td>
<td title="&amp;cedil;" class="Spwr">¸</td>
<td title="&amp;iquest;" class="Spwr">¿</td>
<td title="&amp;Agrave;" class="Spwr">À</td>
<td title="&amp;ccedil;" class="Spwr">ç</td>
<td title="&amp;Aacute;" class="Spwr" style="background-color: rgb(255, 255, 255); color: rgb(0, 0, 0);">Á</td>
<td title="&amp;Acirc;" class="Spwr">Â</td>
</tr><tr>
<td title="&amp;Atilde;" class="Spwr">Ã</td>
<td title="&amp;Auml;" class="Spwr">Ä</td>
<td title="&amp;Aring;" class="Spwr">Å</td>
<td title="&amp;AElig;" class="Spwr">Æ</td>
<td title="&amp;Ccedil;" class="Spwr">Ç</td>
<td title="&amp;Egrave;" class="Spwr">È</td>
<td title="&amp;Eacute;" class="Spwr">É</td>
<td title="&amp;Ecirc;" class="Spwr">Ê</td>
<td title="&amp;Euml;" class="Spwr">Ë</td>
<td title="&amp;Igrave;" class="Spwr">Ì</td>
<td title="&amp;Iacute;" class="Spwr">Í</td>
<td title="&amp;Icirc;" class="Spwr">Î</td>
<td title="&amp;Iuml;" class="Spwr">Ï</td>
<td title="&amp;ETH;" class="Spwr">Ð</td>
<td title="&amp;Ntilde;" class="Spwr">Ñ</td>
<td title="&amp;Ograve;" class="Spwr">Ò</td>
<td title="&amp;Oacute;" class="Spwr">Ó</td>
<td title="&amp;Ocirc;" class="Spwr">Ô</td>
<td title="&amp;Otilde;" class="Spwr">Õ</td>
<td title="&amp;Ouml;" class="Spwr">Ö</td>
<td title="&amp;aelig;" class="Spwr">æ</td>
<td title="&amp;Oslash;" class="Spwr">Ø</td>
</tr><tr>
<td title="&amp;Ugrave;" class="Spwr">Ù</td>
<td title="&amp;Uacute;" class="Spwr">Ú</td>
<td title="&amp;Ucirc;" class="Spwr">Û</td>
<td title="&amp;Uuml;" class="Spwr">Ü</td>
<td title="&amp;Yacute;" class="Spwr">Ý</td>
<td title="&amp;THORN;" class="Spwr">Þ</td>
<td title="&amp;szlig;" class="Spwr">ß</td>
<td title="&amp;agrave;" class="Spwr">à</td>
<td title="&amp;aacute;" class="Spwr">á</td>
<td title="&amp;acirc;" class="Spwr">â</td>
<td title="&amp;atilde;" class="Spwr">ã</td>
<td title="&amp;auml;" class="Spwr">ä</td>
<td title="&amp;aring;" class="Spwr">å</td>
<td title="&amp;aelig;" class="Spwr">æ</td>
<td title="&amp;ccedil;" class="Spwr">ç</td>
<td title="&amp;egrave;" class="Spwr">è</td>
<td title="&amp;eacute;" class="Spwr">é</td>
<td title="&amp;ecirc;" class="Spwr">ê</td>
<td title="&amp;euml;" class="Spwr">ë</td>
<td title="&amp;igrave;" class="Spwr">ì</td>
<td title="&amp;iacute;" class="Spwr">í</td>
<td title="&amp;icirc;" class="Spwr">î</td>
</tr><tr>
<td title="&amp;iuml;" class="Spwr">ï</td>
<td title="&amp;eth;" class="Spwr">ð</td>
<td title="&amp;ntilde;" class="Spwr">ñ</td>
<td title="&amp;ograve;" class="Spwr">ò</td>
<td title="&amp;oacute;" class="Spwr">ó</td>
<td title="&amp;ocirc;" class="Spwr">ô</td>
<td title="&amp;otilde;" class="Spwr">õ</td>
<td title="&amp;ouml;" class="Spwr">ö</td>
<td title="&amp;oslash;" class="Spwr">ø</td>
<td title="&amp;ugrave;" class="Spwr">ù</td>
<td title="&amp;uacute;" class="Spwr">ú</td>
<td title="&amp;ucirc;" class="Spwr">û</td>
<td title="&amp;uuml;" class="Spwr">ü</td>
<td title="&amp;uuml;" class="Spwr">ü</td>
<td title="&amp;yuml;" class="Spwr">ÿ</td>
<td title="&amp;yacute;" class="Spwr">ý</td>
<td title="&amp;thorn;" class="Spwr">þ</td>
<td title="&amp;yuml;" class="Spwr">ÿ</td>
<td title="&amp;#8482;" class="Spwr">&#8482;</td>
<td title="&amp;#8240;" class="Spwr">&#8240;</td>
<td title="&amp;#8218;" class="Spwr">&#8218;</td>
<td title="&amp;nbsp;" class="Spwr">&nbsp;</td>
</tr></tbody></table></div>
<span style="left: 545px; top: 5px; width: 67px; height: 79px;" class="Preview" id="previewchar"></span>
<input style="left: 545px; top: 90px; padding-top: 3px;" type="text" name="insert" class="input" id="insert">
<input onclick="CloseWindow();" onmouseout="this.className='btn';" onmouseover="this.className='btnov';" value="<fmt:message key='richTextBox.cancel'/>" type="button" style="left: 545px; top: 118px;" class="btn">
</body></html>