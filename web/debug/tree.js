    function getCookieVal (offset) {
      var endstr = document.cookie.indexOf (";", offset);
      if (endstr == -1)
        endstr = document.cookie.length;
      return unescape(document.cookie.substring(offset, endstr));
    }

    function getCookie (name) {
      var uid = "";
      name = uid + name;
      var arg = name + "=";  
      var alen = arg.length;  
      var clen = document.cookie.length;  
      var i = 0;  
      while (i < clen) {    
        var j = i + alen;    
        if (document.cookie.substring(i, j) == arg)
          return getCookieVal (j);    
        i = document.cookie.indexOf(" ", i) + 1;    
        if (i == 0) break;   
      }  
      return null;
    }

    function save(b) {
      var uid = "";
      var cookieStr = "";
      cookieStr = uid + b + "=" + document.getElementById(b).style.display + "; path=/";
      document.cookie = cookieStr;

      var sessionId = getCookie("JSESSIONID");
      var sessionCookie = "JSESSIONID=" + sessionId + "; path=/;";
      document.cookie = sessionCookie;
    }

    function load(b) {
      var c = getCookie(b);
//      if (c == "block" || c == "" || c == null) {
      if (c == "block") {
        show(b);
      }
      else
        hide(b);
    }

    function hide(b) {
      var node = document.getElementById(b);
      var icon = document.getElementById("icon_" + b);
      if (node != null)
          node.style.display = "none";
      if (icon != null)
          icon.innerHTML = "+";
    }

    function show(b) {
      var node = document.getElementById(b);
      var icon = document.getElementById("icon_" + b);
      if (node != null)
          node.style.display = "block";
      if (icon != null)
          icon.innerHTML = "-";
    }

    function toggle(id) {
        var node = document.getElementById(id);
        if (node.style.display == 'block') {
            hide(id);
        }
        else {
            show(id);
        }
        save(id);
    }
