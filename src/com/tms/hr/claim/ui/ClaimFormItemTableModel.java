/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndex;
import com.tms.hr.claim.model.ClaimFormIndexModule;
import com.tms.hr.claim.model.ClaimFormItem;
import com.tms.hr.claim.model.ClaimFormItemModule;
import com.tms.cms.subscription.Subscriber;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat;


class ClaimFormItemTableModel extends TableModel
   {
        String formId;
        String cn;



      public ClaimFormItemTableModel(String formId, String cn)
      {
            this.formId = formId;
            addFilter(new TableFilter ("keyword"));
            this.cn = cn;
            setColumns();

        }

        public void setColumns()
        {
            TableColumn tc1 = new TableColumn("timeFromStr", Application.getInstance().getMessage("claims.label.date", "Date"));
            //tc1.setSortable(false);
         addColumn(tc1);
            TableColumn tc2 = new TableColumn("remarks", Application.getInstance().getMessage("claims.label.purpose", "Purpose"));
           /** Removing. Doesn't seem to serve any function
           if(!("jsp_owner_list_closed.table1".equalsIgnoreCase(getCn())) && !("jsp_owner_list_approved.table1".equalsIgnoreCase(getCn())) && !("jsp_approver_approve.pending".equalsIgnoreCase(getCn())))
            tc2.setUrlParam("id");
            */
            //tc2.setSortable(false);

         addColumn(tc2);
            TableColumn tc3 = new TableColumn("categoryName", Application.getInstance().getMessage("claims.label.category", "Category"));
            //tc3.setSortable(false);




         addColumn(tc3);
            TableColumn tc4 = new TableColumn("standardTypeName", Application.getInstance().getMessage("claims.type.name", "Name"));
            //tc4.setSortable(false);
         addColumn(tc4);
            TableColumn tc5 = new TableColumn("projectName", Application.getInstance().getMessage("claims.label.project", "Project"));
            //tc5.setSortable(false);
         addColumn(tc5);
//         addColumn(new TableColumn("currency", "Currency"));
            TableColumn tc6=new TableColumn("amountInStr", Application.getInstance().getMessage("claims.label.amount", "Amount"));

       //     tc6.setSortable(false);
         addColumn(tc6);
//       addColumn(new TableColumn("rejectReason", "rejectReason"));

//       addFilter(new TableFilter("name"));
         Application application = Application.getInstance();
         ClaimFormIndexModule indexModule = (ClaimFormIndexModule)
               application.getModule(ClaimFormIndexModule.class);
         ClaimFormIndex indexObj = indexModule.selectObject(formId);
         if(indexObj.getState().equals(ClaimFormIndexModule.STATE_CREATED))
         {
             //add new item
             addAction(new TableAction("add",Application.getInstance().getMessage("claims.label.add", "Add")));
             addAction(new TableAction("delete", Application.getInstance().getMessage("claims.label.delete", "Delete"), 
            		 Application.getInstance().getMessage("claims.message.delete", "Delete Selected Items?")));
             addAction(new TableAction("Print",Application.getInstance().getMessage("claims.label.print", "Print")));


         }

      }

      public Collection getTableRows()
      {

         String keyword = (String) getFilterValue("keyword");

         Application application = Application.getInstance();
         ClaimFormItemModule module = (ClaimFormItemModule)
               application.getModule(ClaimFormItemModule.class);
         return module.findObjects(keyword, new String[]{" formId = '"+formId+"' "},
                        getSort(), isDesc(),getStart(), getRows());

      }

      public int getTotalRowCount()
      {
           String keyword = (String) getFilterValue("keyword");
           Application application = Application.getInstance();
            ClaimFormItemModule module = (ClaimFormItemModule)
                  application.getModule(ClaimFormItemModule.class);
            return module.countObjects(keyword,new String[]{" formId = '"+formId+"' "});
      }

      public String getTableRowKey()
      { return "id"; }

      public Forward processAction(Event evt, String action, String[] selectedKeys)
      {



         if ("delete".equals(action))
         {
            Application application = Application.getInstance();
            ClaimFormItemModule module = (ClaimFormItemModule)
                  application.getModule(ClaimFormItemModule.class);
            for (int i=0; i<selectedKeys.length; i++)
            {
               //// Only allow claim form with STATE_CREATED to delete
               //// the items
               ClaimFormIndexModule indexModule = (ClaimFormIndexModule)
                        application.getModule(ClaimFormIndexModule.class);
               ClaimFormItem itemObj = module.selectObject(selectedKeys[i]);
               ClaimFormIndex indexObj = indexModule.selectObject(itemObj.getFormId());
               if(!indexObj.getState().equals(ClaimFormIndexModule.STATE_CREATED))
                  { continue;}
               module.deleteObject(selectedKeys[i]);
            }
         }
          else if ("add".equals(action)) {


              return new Forward("add");
          }
          else if("Print".equals(action)){

             return new Forward("print");
         }

         return null;
      }


       public String getCn() {
           return cn;
       }

       public void setCn(String cn) {
           this.cn = cn;
       }

   }




