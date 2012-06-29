    function checkOut(id) {
        if (confirm("Check Out To Edit?")) {
            window.open('../cmsadmin/frontedit/contentEdit.jsp?action=edit&id=' + id, 'frontedit');
        }
        return false;
    }

    function admin(id) {
        window.open('../cmsadmin/frontedit/contentView.jsp?action=edit&id=' + id, 'frontedit');
        return false;
    }
