
    function getBlockCookieVal (offset) {
      var endstr = document.cookie.indexOf (";", offset);
      if (endstr == -1)
        endstr = document.cookie.length;
      return unescape(document.cookie.substring(offset, endstr));
    }

    function getBlockCookie (name) {
      var uid = "";
      name = uid + name;
      var arg = name + "=";
      var alen = arg.length;
      var clen = document.cookie.length;
      var i = 0;
      while (i < clen) {
        var j = i + alen;
        if (document.cookie.substring(i, j) == arg)
          return getBlockCookieVal (j);
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) break;
      }
      return null;
    }

    function blockSave(b) {
      var uid = "";
      var cookieStr = "";
//      var expDays = 3650;
//      var exp = new Date();
//      exp.setTime(exp.getTime() + (expDays*24*60*60*1000));
//      cookieStr = uid + b + "=" + document.getElementById(b).style.display + "; expires=" + exp.toGMTString();
      cookieStr = uid + b + "=" + document.getElementById(b).style.display + "; path=/";
      document.cookie = cookieStr;

      var sessionId = getBlockCookie("JSESSIONID");
      var sessionCookie = "JSESSIONID=" + sessionId + "; path=/;";
      document.cookie = sessionCookie;
    }

    function blockLoad(b) {
      var c = getBlockCookie(b);
      if (c == "block" || c == "" || c == null) {
        blockShow(b);
      }
      else
        blockHide(b);
    }

    function blockHide(b) {
      var node = document.getElementById(b);
      var icon = document.getElementById("toggle_" + b);
      if (node != null)
          node.style.display = "none";
      if (icon != null)
          icon.value = "+";
    }

    function blockShow(b) {
      var node = document.getElementById(b);
      var icon = document.getElementById("toggle_" + b);
      if (node != null)
          node.style.display = "block";
      if (icon != null)
          icon.value = "-";
    }

    function blockToggle(id) {
        var node = document.getElementById(id);
        if (node.style.display == 'block') {
            blockHide(id);
        }
        else {
            blockShow(id);
        }
        blockSave(id);
    }
