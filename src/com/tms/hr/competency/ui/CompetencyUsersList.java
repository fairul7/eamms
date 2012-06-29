package com.tms.hr.competency.ui;

import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;

import java.util.*;

import com.tms.hr.competency.CompetencyHandler;
import com.tms.hr.competency.Competency;
import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Jan 25, 2006
 * Time: 9:26:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class CompetencyUsersList extends Table {



    public CompetencyUsersList(){


    }


     public void init() {
        super.init();

        setModel(new CompetencyUsersListModel());
    }


     public class CompetencyUsersListModel extends TableModel
    {
         Map competencyMap;
         SelectBox competencyNameBox;
        public CompetencyUsersListModel()
        {


            Application app = Application.getInstance();
            CompetencyHandler module = (CompetencyHandler) app.getModule(CompetencyHandler.class);

            addColumn(new TableColumn("username", app.getMessage("security.label.username", "Username")));
            addColumn(new TableColumn("competencyName", app.getMessage("general.label.name", "Name")));
            addColumn(new TableColumn("competencyType", app.getMessage("general.label.type", "Type")));
            addColumn(new TableColumn("competencyLevel", app.getMessage("general.label.level", "Level")));

            TableFilter tf = new TableFilter("tf1");
            TextField keywordText = new TextField("keywordText");
            tf.setWidget(keywordText);
            addFilter(tf);

            TableFilter tf2 = new TableFilter("tf2");
            competencyNameBox = new SelectBox("competencyNameBox");

            competencyMap = new SequencedHashMap();
            competencyMap.put("-1",app.getMessage("project.label.selectcompetencies"));

            for (Iterator icount = module.retrieveAllCompetencies().iterator(); icount.hasNext();) {

               Competency object = (Competency) icount.next();

                competencyMap.put(object.getCompetencyId(),object.getCompetencyName());
            }


            competencyNameBox.setOptionMap(competencyMap);
            tf2.setWidget(competencyNameBox);
            addFilter(tf2);


            //get competencies label
             SelectBox competencyLevel = new SelectBox("competencyLevel");
             String levels = Application.getInstance().getProperty(Competency.PROPERTY_COMPETENCY_LEVELS);
             StringTokenizer tokenizer = new StringTokenizer(levels, Competency.DEFAULT_DELIMITER);
             competencyLevel.addOption("-1",app.getMessage("project.label.level"));
             while(tokenizer.hasMoreTokens())
             {
            String level = tokenizer.nextToken();
            competencyLevel.addOption(level, level);
            }

            TableFilter tf3 = new TableFilter("tf3");
            tf3.setWidget(competencyLevel);
            addFilter(tf3);







        }




         public Collection getTableRows()
        {
            Application app = Application.getInstance();
            CompetencyHandler module = (CompetencyHandler) app.getModule(CompetencyHandler.class);

            String keywordText ="";
             if(getFilterValue("tf1")!=null){

                  keywordText = (String)getFilterValue("tf1");

             }
            else
             keywordText =null;

             //retrieve competency list
            List competencyNameList = (List) getFilterValue("tf2");
            String competencyName ="";
            if(competencyNameList.size() >0)
            competencyName = (String)competencyNameList.get(0);
            else competencyName =null;

           if(competencyName !=null){
            if(competencyName.equals("-1"))
            competencyName =null;

           }

           //get level
            List competencyLevel =(List) getFilterValue("tf3");
            String level ="";
            if(competencyLevel.size() >0)
            level = (String) competencyLevel.get(0);
            else
            level =null;

            if(level !=null){
                if(level.equals("-1"))
                  level="%%";
            }



            //reload the competency select box
            competencyMap.put("-1",app.getMessage("project.label.selectcompetencies"));

                      for (Iterator icount = module.retrieveAllCompetencies().iterator(); icount.hasNext();) {

                         Competency object = (Competency) icount.next();

                          competencyMap.put(object.getCompetencyId(),object.getCompetencyName());
                      }

              competencyNameBox.setOptionMap(competencyMap);
            //reload end

            return module.retrieveCompetencies(keywordText,competencyName,level,getSort(),isDesc(),getStart(),getRows());
        }

         public int getTotalRowCount(){

            Application app = Application.getInstance();
            CompetencyHandler module = (CompetencyHandler) app.getModule(CompetencyHandler.class);

            String keywordText ="";
             if(getFilterValue("keywordText")!=null){

                  keywordText = (String)getFilterValue("keywordText");

             }
            else
             keywordText =null;

            //retrieve competency list
            List competencyNameList = (List) getFilterValue("tf2");
            String competencyName ="";
            if(competencyNameList.size() >0)
            competencyName = (String)competencyNameList.get(0);
            else competencyName =null;

            if(competencyName !=null){
             if(competencyName.equals("-1"))
                 competencyName =null;
            }


             //get level
              List competencyLevel =(List) getFilterValue("tf3");
              String level ="";
              if(competencyLevel.size() >0)
              level = (String) competencyLevel.get(0);
              else
              level =null;

             if(level !=null){
                            if(level.equals("-1"))
                              level="%%";
                        }


             return module.countRetrieveCompetencies(keywordText,competencyName,level);

         }


    }






}
