package com.tms.collab.resourcemanager.ui;

import kacang.stdui.*;
import kacang.services.security.SecurityService;
import kacang.services.security.Group;
import kacang.services.storage.StorageException;
import kacang.Application;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.WidgetManager;

import java.util.*;

import com.tms.collab.resourcemanager.model.ResourceManager;

public class ResourceTable extends Table
{
    ResourceTableModel tm ;
    public void init()
    {
        super.init();
        tm = new ResourceTableModel();
        setModel(tm);
        setWidth("100%");
        setNumbering(true);

    }

   /* public Forward onSelection(Event evt)
    {
        try{
        evt.getResponse().sendRedirect("rview.jsp?id="+evt.getRequest().getParameter("id"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return super.onSelection(evt);
    }*/

    public void onRequest(Event event)
    {
        super.onRequest(event);
        tm.refreshCategory();
    }

    public class ResourceTableModel extends TableModel{
        private Collection col = null;
        private boolean displayDeleted = false;
        private Map cmap;
        boolean approvePermission = false;
        int totalRows = 0;
        private SelectBox categoryList;
        TableColumn statusColumn;
        public ResourceTableModel()
        {
            TableColumn deleteColumn = null;
            TableColumn nameColumn = new TableColumn("name",Application.getInstance().getMessage("resourcemanager.label.ResourceName","Resource Name"),true);
            nameColumn.setUrl("");
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);
            TableColumn descriptionColumn = new TableColumn("description",Application.getInstance().getMessage("resourcemanager.label.Description","Description"),true);
            addColumn(descriptionColumn);

            TableFilter resourceFilter = new TableFilter("name");
            addFilter(resourceFilter);
            TableFilter catFilter = new TableFilter("category1");

            categoryList = new SelectBox("categoryList");
            categoryList.addOption("-1",Application.getInstance().getMessage("resourcemanager.label.Allcategories","All categories"));
            try
            {
                cmap = ((ResourceManager)Application.getInstance().getModule(ResourceManager.class)).getResourceCategories();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            for(Iterator i = cmap.keySet().iterator();i.hasNext();)
            {
                String key = (String) i.next();
                categoryList.addOption(key,(String)cmap.get(key));
            }

            catFilter.setWidget(categoryList);
            addFilter(catFilter);
            TableColumn categoryColumn = new TableColumn("categoryId",Application.getInstance().getMessage("resourcemanager.label.Category","Category"),true);
            categoryColumn.setFormat(new TableFormat(){
                public String format(Object value)
                {
                    if(value==null) return "";
                    return (String)cmap.get(value);
                }
            });
            addColumn(categoryColumn);
            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            try{
                if(ss.hasPermission(getWidgetManager().getUser().getId(),
                        ResourceManager.PERMISSION_ADD_RESOURCE,
                        null,null)){
                            TableAction addAction = new TableAction("add",Application.getInstance().getMessage("resourcemanager.label.Add","Add"));
                            addAction(addAction);
                        }
                statusColumn = new TableColumn("status",Application.getInstance().getMessage("resourcemanager.label.Booking","Booking"),true);
                statusColumn.setFormat(new TableFormat(){
                    public String format(Object o)
                    {

                        String status = (String) o;
                       /* if(status.equals(ResourceManager.RESOURCE_STATUS_APPROVED)){
                            return "Booked";
                        }else if(status.equals(ResourceManager.RESOURCE_STATUS_AVAILABLE)){
                            return "Available";
                        }else*/
                        String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
                        if(status.trim().equals(ResourceManager.RESOURCE_STATUS_PENDING)){
                            return "<img src=\""+contextPath +"/common/table/booleantrue.gif\">";
                        }
                        return null;
                    }
                });


                addColumn(statusColumn);

                if(ss.hasPermission(getWidgetManager().getUser().getId(),
                   ResourceManager.PERMISSION_APPROVE_RESOURCE,null,null)){
                       TableColumn approvedColumn = new TableColumn("approved",Application.getInstance().getMessage("resourcemanager.label.Approved","Approved"),true);
                       String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
                       approvedColumn.setFormat(new TableBooleanFormat("<img src=\""+contextPath +"/common/table/booleantrue.gif\">",""));
                       addColumn(approvedColumn);
                       TableAction approveAction = new TableAction("approve",Application.getInstance().getMessage("resourcemanager.label.Approve","Approve"));
                       setMultipleSelect(true);
                       addAction(approveAction);
                }
                if(ss.hasPermission(getWidgetManager().getUser().getId(),
                   ResourceManager.PERMISSION_ACTIVATE_RESOURCE,null,null)){
                        deleteColumn = new TableColumn("deleted",Application.getInstance().getMessage("resourcemanager.label.Active","Active"),true);
                        String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
                        deleteColumn.setFormat(new TableBooleanFormat("","<img src=\""+contextPath +"/common/table/booleantrue.gif\">"));
                        addColumn(deleteColumn);
                        addAction(new TableAction("undelete",Application.getInstance().getMessage("resourcemanager.label.SetActive","Set Active")));
                        setMultipleSelect(true);
                        displayDeleted = true;
                   }
                if(ss.hasPermission(getWidgetManager().getUser().getId(),
                        ResourceManager.PERMISSION_INACTIVATE_RESOURCE,
                        null,null)){
                            TableAction deleteAction = new TableAction("delete",Application.getInstance().getMessage("resourcemanager.label.SetInactive","Set Inactive"));
                            addAction(deleteAction);
                            setMultipleSelect(true);
                        }
                if(ss.hasPermission(getWidgetManager().getUser().getId(),
                        ResourceManager.PERMISSION_DISCARD_RESOURCE,null,null)){
                            if(deleteColumn==null){
                                deleteColumn = new TableColumn("deleted",Application.getInstance().getMessage("resourcemanager.label.Active","Active"),true);
                                String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
                                deleteColumn.setFormat(new TableBooleanFormat("","<img src=\""+contextPath +"/common/table/booleantrue.gif\">"));
                                addColumn(deleteColumn);
                            }
                            addAction(new TableAction("discard",Application.getInstance().getMessage("resourcemanager.label.Delete","Delete")));
                            setMultipleSelect(true);
                            displayDeleted = true;
                        }

            }catch(Exception e){
                Log.getLog(getClass()).error(e);
            }
        }

        public void refreshCategory(){
            categoryList.removeAllOptions();
            categoryList.addOption("-1",Application.getInstance().getMessage("resourcemanager.label.Allcategories","All categories"));
            try
            {
                cmap = ((ResourceManager)Application.getInstance().getModule(ResourceManager.class)).getResourceCategories();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            for(Iterator i = cmap.keySet().iterator();i.hasNext();)
            {
                String key = (String) i.next();
                categoryList.addOption(key,(String)cmap.get(key));
            }
        }

    /*    public void setStatusLink(boolean editable){
            try{
                if(editable){
                    SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                    if(ss.hasPermission(getWidgetManager().getUser().getId(),
                            ResourceManager.PERMISSION_APPROVE_BOOKING,null,null)){
                                statusColumn.setUrl("");
                                statusColumn.setUrlParam("bookingId");
                            }
                } else{
                    statusColumn.setUrl(null);
                    statusColumn.setUrlParam(null);
                }
            }catch(Exception e) {
                Log.getLog(ResourceTable.class).debug(e);
            }
        }*/

        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
            if("delete".equals(action)){
                String userId = getWidgetManager().getUser().getId();
                int pageRequired=0;
                ResourceManager rm =(ResourceManager) Application.getInstance().getModule(ResourceManager.class);
                for(int i=0;i<selectedKeys.length;i++)
                {
                    try
                    {
                        rm.deleteResource(selectedKeys[i],userId);
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                }
                int totalRows = getTotalRowCount();
                if(totalRows<=getPageSize()) setCurrentPage(1);
                else
                {
                    pageRequired = totalRows/getPageSize();
                    if(totalRows % getPageSize()!=0)
                        pageRequired+=1;
                    if(getCurrentPage()>pageRequired)
                        setCurrentPage(pageRequired);
                }
            } else if("add".equals(action)){
                return new Forward("AddResource");
            } else if("approve".equals(action)){
                ResourceManager rm =(ResourceManager) Application.getInstance().getModule(ResourceManager.class);
                for(int i=0;i<selectedKeys.length;i++)
                {
                    try
                    {
                        rm.approveResource(selectedKeys[i]);
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                }
            } else if("undelete".equals(action)){
                ResourceManager rm =(ResourceManager) Application.getInstance().getModule(ResourceManager.class);
                String userId = getWidgetManager().getUser().getId();
                for(int i=0;i<selectedKeys.length;i++)
                {
                    try
                    {
                        rm.undeleteResource(selectedKeys[i],userId);
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                }
            }else if("discard".equals(action)){
                ResourceManager rm =(ResourceManager) Application.getInstance().getModule(ResourceManager.class);
                for(int i=0;i<selectedKeys.length;i++){
                    try
                    {
                        rm.destroyResource(selectedKeys[i]);
                    } catch (StorageException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                }
            }
            return super.processAction(evt,action,selectedKeys);
        }

        public Collection getTableRows()
        {
            try{
                String[] groupIds = null;
                String userId = getWidgetManager().getUser().getId();
                SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                if(ss.hasPermission(userId,ResourceManager.PERMISSION_APPROVE_RESOURCE,null,null))
                {
                    approvePermission = true;
                    userId = null;
                }
                else{
                    Collection groups = ss.getUserGroups(userId);
                    if(groups.size()>0){
                        groupIds = new String[groups.size()];
                        int j=0;
                        for(Iterator i = groups.iterator();i.hasNext();){
                            groupIds[j] =((Group)i.next()).getId();
                            j++;
                        }
                    }
                }
                ResourceManager rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
                //String cat = null;
                List sb = (List)getFilterValue("category1");
/*
                SelectBox b = (SelectBox)getFilter("category1").getWidget();
*/
/*                Map map = b.getOptionMap();*/
                String selectedCat = null;
                /*if(selectedMap!=null&&selectedMap.size()>0)
                    selectedCat = (String)selectedMap.keySet().iterator().next();//(String)((List)getFilterValue("category")).get(0);
               */
                boolean showPrivate =true;//ss.hasPermission(userId,ResourceManager.PERMISSION_EDIT_RESOURCE,null,null);
                if(sb!=null&&sb.size()>0)
                    selectedCat = (String)sb.get(0);
                if(selectedCat!=null&&selectedCat.equals("-1"))
                    selectedCat = null;
                totalRows = rm.getResource((String)getFilterValue("name"),selectedCat,new String[]{userId},groupIds,(approvePermission?false:true),(displayDeleted?true:false),showPrivate,null,false,0,-1).size();
                col = rm.getResource((String)getFilterValue("name"),selectedCat,new String[]{userId},groupIds,(approvePermission?false:true),(displayDeleted?true:false),showPrivate,getSort(),isDesc(),getStartIndex(),getRows());

            }catch(Exception e){
                Log.getLog(getClass()).error(e);
            }
            return col;
        }

        public String getTableRowKey()
        {
            return "id";
        }

        public int getTotalRowCount()
        {
            return totalRows;
        }
    }
}
