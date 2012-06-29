<%--<html>--%>
<!--
- moved strikethru to drop down menu
- reorganised code
- attempted to make a button to cycle through caps / lowercase / leading caps
- Added setCaretAtEnd (field) to onload of main editor table to start with claret at the end of the text
- Added setCaretAtEndOfRange() after formatting to return claret to end of selected text
- Added more macro's to macro drop down (but need to check that they are all there)
- Added css colours to macro's drop down to make it easier to see what is what.
- Added prompt to insert url
- Put classes at the bottom of the format drop down
-->

<%--<head>
<title> Wiki Editor </title>--%>
<%@ include file="/common/header.jsp" %>
<c:set var="cn" value="${param.widget}"/>
<c:set var="textbox" value="${widgets[cn]}"/>


<%--<table class="regionCenterWrapper" height="*" width="100%" >
<tr><td height="*" valign="top" style="padding-bottom:5px" onload="{SetEditFocus();setCaretAtEnd('text');}"/>--%>

<script language="javascript" type="text/javascript" charset="{@encoding}">

 //
 // **********************************************************************************
 //
 // Utility functions
//
// ***********************************************************************************
//

//
// Function trim(s)
// takes string and returns it with leading and trialing spaces removed
// I think IE has this built into javascript but put this in for a measure of cross browser support.
//

function trim(s)
{
	reg=/^ *(\S.+[\n]*.+\S) *$/;
	s=s.replace(reg,"$1");
	return s;
}

//
// function SetEditFocus()
// returns focus to textarea
//

function SetEditFocus()
{
	document.<c:out value="${textbox.rootForm.absoluteName}"/>.text.focus();
}


//
// Function setCaretAtEnd (field)
// Function to move claret to end of form / textarea
// Added to page load along with SetEditFocus
//

function setCaretAtEnd (field) {
  if (field.createTextRange) {
    var r = field.createTextRange();
    r.moveStart('character', field.value.length);
    r.collapse();
    r.select();
  }
}

//
// function setCaretAtEndOfRange (range)
// sets claret at end of textrange, ie after formatting selected text
//

function setCaretAtEndOfRange (range) {
 {
    var r = range;
    r.collapse(false);
    r.select();
  }
}



//
// *****************************************************************
//
// functions to insert wiki markup.
//
// *****************************************************************

//
// Function format_sel(s,e)
// Inserts Wiki markup (ie s(tart) and e(nd) around selected text
// Used for buttons
// leading and trailing spaces removed
//

function format_sel(s,e) {

  	var str = document.selection.createRange().text;
  	//added to remove white space at start or end
  	var sPre = "", sPost = ""
  	if (str.charAt(0) == " "){sPre = " "}
  	if (str.charAt(str.length - 1) == " "){sPost = " "}
  	str = trim(str);
  	SetEditFocus();
  	var sel = document.selection.createRange();
	sel.text = sPre + unescape(s) + str + unescape(e) + sPost ;
	setCaretAtEndOfRange(sel);
	return;
}

//
// function insert_code(v)
//
// Inserts wiki code at selected point
//

function insert_code(v) {
 	 var str = document.selection.createRange().text;
 	 SetEditFocus()
	 var sel = document.selection.createRange();
	 //sel.text =  v + " ";
	sel.text =  unescape(v);
	setCaretAtEndOfRange(sel);
 	 return;
}



//
//
// Function head(what)
// Inserts heading markup from 'Headings' dropdown
// Text is surrounded by markup value from dropdown.
//
//

function head(what)
{
	if (what.selectedIndex != 1);
	{
	var str = document.selection.createRange().text;
	var v = what.value;
	SetEditFocus();
 	var sel = document.selection.createRange();
  	sel.text = v + " " + str + " " + v ;
  	setCaretAtEndOfRange(sel);
	what.selectedIndex = 0;
	return;
	}
}

//
// Function dd_insertcode(what)
// Inserts macro code from drop down.
//

function dd_insertcode(what)
{
	if (what.selectedIndex != 1);
	{
	var str = document.selection.createRange().text;
  	var sPre = "", sPost = ""
  	if (str.charAt(0) == " "){sPre = " "}
  	if (str.charAt(str.length - 1) == " "){sPost = " "}
	//added to remove white space at start or end
  	str = trim(str);
	var v = what.value;
	pos = v.indexOf(",");
	var len = v.length;
	s = v.substring(0,pos);
	e = v.substring(pos + 1,len);
	SetEditFocus();
 	var sel = document.selection.createRange();
	sel.text = sPre + unescape(s) + str +  unescape(e) + sPost;
	setCaretAtEndOfRange(sel);
	what.selectedIndex = 0;
	return;
	}
}


//
// Function insert_rss ()
// Inserts RSS feed as entered in a prompt
//

function insert_rss () {
  	var str = document.selection.createRange().text;
  	SetEditFocus();
  	var my_link = prompt("Enter URL:","http://");
  	if (my_link != null)    {
   		var sel = document.selection.createRange();
		var v =  '<Syndicate(\"';
		v+= my_link;
		v+= '\",120) /> '
		sel.text = unescape(v);
		setCaretAtEndOfRange(sel);
		 	 			}
  			return;
			}


//
// function insert_link ()
// Inserts url link as entered in a prompt
//

function insert_link () {
  	var str = document.selection.createRange().text;
  	SetEditFocus();
  	var my_link = prompt("Enter URL:","http://");
  	if (my_link != null)    {
   		var sel = document.selection.createRange();
		sel.text = unescape( my_link);
		setCaretAtEndOfRange(sel);
		 	 			}
  			return;
			}

//
// Function insert_interwiki(what)
// Inserts interwiki code from dropdown
//


function insert_interwiki(what) {
	if (what.selectedIndex != 1);
{
	var v = what.value;
  	SetEditFocus();
  	var sel = document.selection.createRange();
  	sel.text =  v;
	what.selectedIndex = 0;
	setCaretAtEndOfRange(sel);
	return;
	}
}


// **********************************************************************
// capitalise etc
// still writing...
//
// See caps_dd() that is called by the drop down select menu
// What I want is an equivalent that runs off a button and that cycles through the 3 options. This is what caps() tries (but fails) to do.

function caps(what) {
 counts=0;
 getCount()
 switch (counts)
{
	case 0:
		capitalise();
	break
	case 1:
		lowercase();
	break
	case 2:
		uppercase();
	break
	default:
		return;
}

}

 function getCount(){
  counts = counts + 1;
  counts = counts % 3;
  return counts;}

  //
  // called by drop down
  //

  function caps_dd(what) {

  if (what.selectedIndex != 0)
{
	var v = what.value;
	what.selectedIndex = 0;
	switch (v)
{
	case "caps":
		capitalise();
	break;
	case "lowercase":
		lowercase();
	break;
	case "uppercase":
		uppercase();
	break;
	default:
		return;
}
		return;
}
}

//
// Function capitalise()
// from various places, function to turn selected text into leading capitals.
//

function capitalise() {
  	var str = document.selection.createRange().text;
  	SetEditFocus()
  	var sel = document.selection.createRange();
  	 if ( !str.length ) return;
	str=str.toLowerCase();
	str=str.replace(/\b\w+\b/g, function(word) {
	return word.substring(0,1).toUpperCase()+ word.substring(1);
});
sel.text=str
return;
}

//
// Function lowercase()
// function to turn selected text into lowercase.
//

function lowercase() {
  	var str = document.selection.createRange().text;
  	SetEditFocus()
  	var sel = document.selection.createRange();
	str=str.toLowerCase();
	sel.text=str;
	return;
}

//
// Function uppercase()
// function to turn selected text into lowercase.
//

function uppercase() {
  	var str = document.selection.createRange().text;
  	SetEditFocus()
  	var sel = document.selection.createRange();
	str=str.toUpperCase();
	sel.text=str;
	return;
}



//
//  By Michael Meyers
//

function indent_line( s){
  var new_s="";
  if ( s.match(/^\s+$/) ) return s;
  if ( s.match(/^\s*:|^\s*\*\s|^\s*[0-9]+\.|^\s*[a-z]+\.|^\s*;/) ){
     new_s = "  " + s;
  }else{
     new_s = "  : " + s;
  }

  return new_s;
}

function dd_indent()
{

         var str = document.selection.createRange().text;
         var sel = document.selection.createRange();
         if ( !str.length ) return;
         if ( str.indexOf( "\n" ) == -1 ){
           sel.text = indent_line( str ) + "\n";
           return;
         }

         var new_str = "";
         str += "\n";
         pos = str.indexOf( "\n" );

         do {

           line = str.substring( 0, pos );
           new_str += indent_line( line );
           str = str.substring( pos+1 , str.length );
           pos = str.indexOf( "\n" );

         }while( pos != -1 );

         // process remainder
         if ( str.length != 0 ) {
           new_str += indent_line( str )  ;
         }else{
           new_str += "\n";
         }


         sel.text =  new_str;

}

function unindent_line( s){
  var new_s="";
  if ( s.match(/^(\s+)/)  ){
     len = RegExp.$1.length ;

     if ( len == 1 ) {
       if( s.match(/^ |^\t/) ) {
         new_s = new_s = s.substring( 1, s.length );
       }else{
         new_s = s;
       }

     }else if ( len == 2 ){
       new_s = s.substring( 1, s.length );
     }else{
       new_s = s.substring( 2, s.length );
     }
  }else{
     new_s = s;
  }

  return new_s;
}

function dd_unindent()
{

         var str = document.selection.createRange().text;
         var sel = document.selection.createRange();
         if ( !str.length ) return;
         if ( str.indexOf( "\n" ) == -1 ){
           sel.text = unindent_line( str ) + "\n";
           return;
         }

         var new_str = "";
         str += "\n";
         pos = str.indexOf( "\n" );

         do {

           line = str.substring( 0, pos );
           new_str += unindent_line( line );
           str = str.substring( pos+1 , str.length );
           pos = str.indexOf( "\n" );

         }while( pos != -1 );

         // process remainder
         if ( str.length != 0 ) {
           new_str += unindent_line( str )  ;
         }else{
           new_str += "\n";
         }
         sel.text =  new_str;
}


// re-clicking button cycles through styles 1. --> a. -->  1.  ect...
function number_list( s){
  var new_s="";
  if ( s.match(/^(\s+):/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "1." + s.substring( len + 1 , s.length );
  }else if( s.match(/^(\s+)\*\s/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "1." + s.substring( len + 1 , s.length );
  }else if( s.match(/^(\s+)([0-9]+)\./) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "a" + s.substring( len + RegExp.$2.length , s.length );
  }else if( s.match(/^(\s+)([a-z]+)\./) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "1" + s.substring( len + RegExp.$2.length , s.length );
  }else if( s.match(/^(\s+);/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "1." + s.substring( len + 1 , s.length );
  }else{
     new_s = "  1. " + s;
  }

  return new_s;
}


function dd_numberlist()
{

         var str = document.selection.createRange().text;
         var sel = document.selection.createRange();
         if ( !str.length ) return;
         if ( str.indexOf( "\n" ) == -1 ){
           sel.text = number_list( str ) + "\n";
           return;
         }

         var new_str = "";
         str += "\n";
         pos = str.indexOf( "\n" );

         do {

           line = str.substring( 0, pos );
           new_str += number_list( line );
           str = str.substring( pos+1 , str.length );
           pos = str.indexOf( "\n" );

         }while( pos != -1 );

         // process remainder
         if ( str.length != 0 ) {
           new_str += number_list( str )  ;
         }else{
           new_str += "\n";
         }


         sel.text =  new_str;
}

function bullet_list( s){
  var new_s="";
  if ( s.match(/^(\s+):/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len + 1 , s.length );
  }else if( s.match(/^(\s+)\*\s/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len + 1 , s.length );
  }else if( s.match(/^(\s+)([0-9]+\.#?[0-9]*)/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len +  RegExp.$2.length  , s.length );
  }else if( s.match(/^(\s+)([a-z]+\.#?[0-9]*)/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len + RegExp.$2.length, s.length );
  }else if( s.match(/^(\s+);/) ){
     len = RegExp.$1.length ;
     new_s = RegExp.$1 + "*" + s.substring( len + 1 , s.length );
  }else{
     new_s = "  * " + s;
  }

  return new_s;
}

function dd_bulletlist()
{

         var str = document.selection.createRange().text;
         var sel = document.selection.createRange();
         if ( !str.length ) return;
         if ( str.indexOf( "\n" ) == -1 ){
           sel.text = bullet_list( str ) + "\n";
           return;
         }

         var new_str = "";
         str += "\n";

         pos = str.indexOf( "\n" );

         do {

           line = str.substring( 0, pos );
           new_str += bullet_list( line );
           str = str.substring( pos+1 , str.length );
           pos = str.indexOf( "\n" );

         }while( pos != -1 );

         // process remainder
         if ( str.length != 0 ) {
           new_str += bullet_list( str )  ;
         }else{
           new_str += "\n";
         }

         sel.text =  new_str;
}



// Mouse over functions to animate the buttons

function mouseover(el) {
  el.className = "raised";
}

function mouseout(el) {
  el.className = "button";
}

function mousedown(el) {
  el.className = "pressed";
}

function mouseup(el) {
  el.className = "raised";
}

</script>

<link rel="stylesheet" type="text/css" href="wysiwyg.css" />
<link rel="stylesheet" type="text/css" href="ow.css" />
<%--</head>--%>

<%--<form name="frmEdit">--%>


<%--</form>--%>

<%--
</body>
</html>--%>
