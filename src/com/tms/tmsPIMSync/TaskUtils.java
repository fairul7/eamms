package com.tms.tmsPIMSync;

import com.funambol.foundation.pdi.task.SIFT;
import com.funambol.foundation.pdi.utils.SourceUtils;
import com.funambol.foundation.pdi.utils.TimeUtils;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.calendar.model.CalendarModule;
import kacang.Application;

import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.xml.sax.SAXException;

public class TaskUtils implements SIFT {
    public static DateFormat dateOnlyFormat;

     static{
        dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateOnlyFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    public Map ekpTaskToMap(Task task, String ownerId) throws Exception {
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        Map map = new HashMap();
        map.put(SourceUtils.ROOT_NAME, ROOT_TAG);
        if(task.getDescription()!=null) map.put(BODY, task.getDescription());
        if(task.getDueDate()!=null) map.put(DUEDATE, (TimeUtils.convertDateTo(task.getDueDate(), "yyyy-MM-dd")));
        if(task.getCategoryId()!=null){
            map.put(CATEGORIES,tm.getCategory(task.getCategoryId()).getName());
        }
        if(task.getReminderDate()!=null){
            map.put(REMINDER_SET, "1");
            map.put(REMINDER_TIME, TimeUtils.convertLocalDateToUTC(TimeUtils.convertDateTo(task.getReminderDate(), TimeUtils.PATTERN_UTC_WOZ), TimeZone.getDefault()));
        }else{
            map.put(REMINDER_SET, "0");
        }

        if(task.getTaskPriority()!=null){
            if(task.getTaskPriority().equals("1")){
                // if it is high
                map.put(IMPORTANCE, "2");
            }else if(task.getTaskPriority().equals("3")){
                // if it is normal
                map.put(IMPORTANCE, "1");
            }else if(task.getTaskPriority().equals("5")){
                // if it is low
                map.put(IMPORTANCE, "0");
            }else if(task.getTaskPriority().equals("7")){
                // ongoing
                map.put(STATUS, "1"); //set outlook in progress
            }
        }

        map.put(START_DATE, TimeUtils.convertLocalDateToUTC(TimeUtils.convertDateTo(task.getStartDate(), TimeUtils.PATTERN_UTC_WOZ), TimeZone.getDefault()));

        Assignee assignee = tm.getAssignee(task.getId(), ownerId);

        if(assignee != null){
            if(assignee.getStartDate() == null) map.put(STATUS, "0"); // task not started
            else{                
                map.put(STATUS, "1");

            }
            if(assignee.getCompleteDate() != null){
                map.put(STATUS, "2"); // task completed
                map.put(DATE_COMPLETED, TimeUtils.convertLocalDateToUTC(TimeUtils.convertDateTo(assignee.getCompleteDate(), TimeUtils.PATTERN_UTC_WOZ), TimeZone.getDefault()));
                map.put(COMPLETE, "1");
            }
            map.put(PERCENT_COMPLETE, Long.toString(assignee.getProgress()));
        }

        if(task.getClassification().equals(CalendarModule.CLASSIFICATION_PUBLIC))map.put(SENSITIVITY, "0"); else map.put(SENSITIVITY, "2");
        if(task.getTitle()!=null) map.put(SUBJECT, task.getTitle());
        return map;
    }

    public String ekpTaskToXml(Task task, String ownerId) throws Exception {
        Map map = ekpTaskToMap(task, ownerId);
        return SourceUtils.hashMapToXml(map);
    }
   
}
