package com.tms.collab.resourcemanager.model;

import kacang.model.DefaultModule;
import kacang.model.DefaultDataObject;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageException;
import kacang.Application;
import kacang.util.Log;

import java.util.*;

import com.tms.collab.calendar.model.*;

import org.apache.commons.collections.SequencedHashMap;

public class ResourceManager extends DefaultModule
{
    private ResourceManagerDao  dao;
    public final static int RESOURCE_ACCESS_TYPE_USER = 1;
    public final static int RESOURCE_ACCESS_TYPE_GROUP = 2;
    public static final int RESOURCE_IMAGE_TYPE_UPLOAD = 1;
    public static final int RESOURCE_IMAGE_TYPE_URL = 2;
    public static final int RESOURCE_CLASSIFICATION_TYPE_PUBLIC= 1;
    public static final int RESOURCE_CLASSIFICATION_TYPE_PRIVATE= 2;
    public static final String RESOURCE_BOOKING_PENDING = "P";
    public static final String RESOURCE_BOOKING_APPROVED = "C";
    public static final String RESOURCE_BOOKING_DECLINED = "D";
    public static final String RESOURCE_BOOKING_RETURNED = "R";
    public static final String RESOURCE_STATUS_AVAILABLE = "A";
    public static final String  RESOURCE_STATUS_PENDING = "P";
    public static final String RESOURCE_STATUS_APPROVED = "C";
    public static final String PERMISSION_APPROVE_RESOURCE = "com.tms.collab.resourcemanager.ApproveResource";
    public static final String PERMISSION_ADD_RESOURCE = "com.tms.collab.resourcemanager.AddResource";
    public static final String PERMISSION_EDIT_RESOURCE = "com.tms.collab.resourcemanager.EditResource";
    public static final String PERMISSION_INACTIVATE_RESOURCE = "com.tms.collab.resourcemanager.InactivateResource";
    public static final String PERMISSION_ACTIVATE_RESOURCE = "com.tms.collab.resourcemanager.ActivateResource";
    public static final String PERMISSION_DISCARD_RESOURCE = "com.tms.collab.resourcemanager.DiscardResource";
    public static final String PERMISSION_APPROVE_BOOKING = "com.tms.collab.resourcemanager.ApproveBooking";


    public ResourceManager()
    {
        dao = (ResourceManagerDao) getDao();
    }

    public String addResource(Resource resource)
    {
        dao = (ResourceManagerDao) getDao();
        return dao.addResource(resource);
    }

    public void updateResource(Resource resource){
        dao = (ResourceManagerDao) getDao();
        dao.updateResource(resource);

        // delete unused categories
        dao.deleteUnusedCategories();
    }

    public void updateAuthorizedMembers(Resource resource,String[] userIds){
        dao = (ResourceManagerDao) getDao();
        dao.removeAuthorizedUser(resource);
/*
        Collection col =resource.getAuthorities();
        String[] userIds = new String[col.size()];
        int i  = 0;
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            String o = (String) iterator.next();
            userIds[i] = o;
            i++;
        }
*/
        addAuthorizedUsers(resource.getId(),userIds);
    }

    public void deleteAuthorizedUsers(Resource resource){
        dao = (ResourceManagerDao) getDao();
        dao.removeAuthorizedUser(resource);
    }

    public void addAuthorizedUsers(String resourceId, String[] userIds){
        dao = (ResourceManagerDao) getDao();
        for (int i = 0; i < userIds.length; i++)
        {
            String userId = userIds[i];
            dao.insertAuthorizedUser(resourceId,userId);
        }
    }

    public void deleteResource(String resourceId,String userId) throws DaoException
    {
        Resource resource = getResource(resourceId,false);
        resource.setDeleted(true);
        resource.setModificationDate(new Date());
        resource.setModifiedBy(userId);
        updateResource(resource);
    }

    public void undeleteResource(String resourceId,String userId) throws DaoException
    {
        Resource resource = getResource(resourceId,false);
        resource.setDeleted(false);
        resource.setModificationDate(new Date());
        resource.setModifiedBy(userId);
        updateResource(resource);
    }

    public void destroyResource(String resourceId) throws StorageException, DaoException
    {
        Resource resource = getResource(resourceId,false);
        if(resource.getImageType()==ResourceManager.RESOURCE_IMAGE_TYPE_UPLOAD
                &&resource.getImage()!=null&&resource.getImage().trim().length()>0){
                    StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
                    ss.delete(new StorageFile("/vote/"+resourceId));
                }
        dao = (ResourceManagerDao) getDao();
        dao.deleteResource(resourceId);

        // delete unused categories
        dao.deleteUnusedCategories();
    }

    public void updateBooking(String resourceId,String eventId,String instanceId,String userId,Date startDate,Date endDate)throws ConflictException, DaoException{
        ResourceBooking booking = new ResourceBooking(eventId,instanceId,resourceId,userId,startDate,endDate,RESOURCE_BOOKING_PENDING);
/*
        Collection col = getBookingConflicts(booking);
        if(col!=null&&col.size()>0){
            for (Iterator iterator = col.iterator(); iterator.hasNext();)
            {
                ResourceBooking resource = (ResourceBooking) iterator.next();
                if(resource.getEventId().equals(resourceId)){
                    iterator.remove();
                }
            }
            if(col.size()>0){
                ConflictException ce = new ConflictException();
                ce.setResourcesList(col);
                throw ce;
            }
        }
*/
        deleteBooking(resourceId,eventId,instanceId);
        Resource resource = getResource(resourceId,true);
        if(resource.isRequireApproval()){
            resource.setStatus(ResourceManager.RESOURCE_STATUS_PENDING);
            resource.setBookingId(eventId);
        }else{
            resource.setStatus(ResourceManager.RESOURCE_STATUS_AVAILABLE);
            resource.setBookingId("");
            booking.setStatus(ResourceManager.RESOURCE_BOOKING_APPROVED);
        }
        dao = (ResourceManagerDao) getDao();
        dao.insertBooking(booking);
        dao.updateResource(resource);
    }

    public void deleteBooking(String resourceId,String eventId,String instanceId) throws DaoException
    {
        dao = (ResourceManagerDao) getDao();
        dao.deleteBooking(resourceId,eventId,instanceId);
    }

    public void deleteBooking(String eventId){
        dao = (ResourceManagerDao) getDao();
        CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
        try {
            CalendarEvent event = cm.getCalendarEvent(eventId);
            Collection resources = event.getResources();
            dao.deleteBooking(eventId);
            for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                Resource resource = (Resource) iterator.next();
                String resourceId = resource.getId();
                if(!dao.hasPendingBooking(resourceId)){
                    resource.setStatus(ResourceManager.RESOURCE_STATUS_AVAILABLE);
                    updateResource(resource);
                }

            }
        } catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error("Couldn't get the calendar event with id "+eventId,e);  //To change body of catch statement use Options | File Templates.
        } catch (CalendarException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }

    }

    public Map getResourceCategories() throws DaoException
    {
        dao = (ResourceManagerDao) getDao();
        Collection col = dao.selectCategories();
        Map map = new SequencedHashMap();
        for(Iterator i = col.iterator();i.hasNext();){
            DefaultDataObject obj = (DefaultDataObject)i.next();
            map.put(obj.getId(),obj.getProperty("name"));
        }
        return map;
    }

    public Collection getBookedResources(String eventId,String instanceId) throws DaoException
    {
        dao = (ResourceManagerDao) getDao();
        return dao.selectBookedResources(eventId,instanceId);
    }

    public void bookResource(String resourceId,String eventId,String instanceId,String userId,Date startDate,Date endDate,boolean ignoreConflicts) throws ConflictException, DaoException{
        dao = (ResourceManagerDao) getDao();
        ResourceBooking booking = new ResourceBooking(eventId,instanceId,resourceId,userId,startDate,endDate,RESOURCE_BOOKING_PENDING);
        if(!ignoreConflicts){
            Collection col = getBookingConflicts(booking);
            if(col!=null&&col.size()>0){
                for (Iterator iterator = col.iterator(); iterator.hasNext();)
                {
                    ResourceBooking resource = (ResourceBooking) iterator.next();
                    if(resource.getEventId().equals(eventId)){
                        iterator.remove();
                    }
                }
                if(col.size()>0){
                    ConflictException ce = new ConflictException();
                    ce.setResourcesList(col);
                    throw ce;
                }
            }

        }
        Resource resource = getResource(resourceId,true);
        if(resource.isRequireApproval()){
            resource.setStatus(ResourceManager.RESOURCE_STATUS_PENDING);
            resource.setBookingId(eventId);
        }else{
            resource.setStatus(ResourceManager.RESOURCE_STATUS_AVAILABLE);
            resource.setBookingId("");
            booking.setStatus(ResourceManager.RESOURCE_BOOKING_APPROVED);
        }
        dao.insertBooking(booking);
        dao.updateResource(resource);
    }


    public Collection getBookingConflicts(ResourceBooking booking){
        dao = (ResourceManagerDao) getDao();
        return dao.selectBookingConflicts(booking);

    }

    public Collection getBookingConflictEvents(ResourceBooking booking){
        try{
            dao = (ResourceManagerDao) getDao();
            Collection events = new TreeSet();
            Collection bookings = dao.selectBookingConflicts(booking);
            CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
            for (Iterator iterator = bookings.iterator(); iterator.hasNext();)
            {
                ResourceBooking resourceBooking = (ResourceBooking) iterator.next();
                CalendarEvent event = cm.getCalendarEvent(resourceBooking.getEventId(),resourceBooking.getInstanceId());
                event.setAttendees(cm.getAttendees(event.getEventId(),event.getInstanceId()));
                events.add(event);
            }
            return events;
        }catch(Exception e){
            Log.getLog(ResourceManager.class).error(e);
        }
        return null;
    }

    public Collection getBookingConflictEvents(String resourceId, Date from, Date to){
        try{
            dao = (ResourceManagerDao) getDao();
            Collection events = new TreeSet();
            Collection bookings = dao.selectBookingConflicts(resourceId,from,to);
            CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
            for (Iterator iterator = bookings.iterator(); iterator.hasNext();)
            {
                try{
                    ResourceBooking resourceBooking = (ResourceBooking) iterator.next();
                    CalendarEvent event = cm.getCalendarEvent(resourceBooking.getEventId(),resourceBooking.getInstanceId());
                    event.setAttendees(cm.getAttendees(event.getEventId(),event.getInstanceId()));
                    events.add(event);
                }catch(DataObjectNotFoundException de){
                    Log.getLog(ResourceManager.class).error(de);
                }
            }
            return events;
        }catch(Exception e){
            Log.getLog(ResourceManager.class).error(e);
        }
        return null;
    }

    public Collection getBookingConflicts(String eventId){

        return null;
    }
    public Collection getBookingConflicts(CalendarEvent event) throws DaoException
    {
        Collection resources = event.getResources();
        Collection recurrences = CalendarUtil.getRecurringEvents(event,event.getStartDate(),event.getEndDate());
//        Collection resoureceConflicts = new TreeSet();
        Collection resoureceConflicts = new ArrayList();
        if(resources!=null){
            for (Iterator iterator = resources.iterator(); iterator.hasNext();)
            {
                Resource resource = (Resource) iterator.next();
                ResourceBooking booking = new ResourceBooking(event.getEventId(),event.getInstanceId(),resource.getId(),
                        event.getUserId(),event.getStartDate()==null?event.getCreationDate():event.getStartDate(),event.getEndDate(),ResourceManager.RESOURCE_BOOKING_PENDING);
                booking.setResource(resource);
                Collection col = getBookingConflictEvents(booking);
				if(col != null)
				{
					for (Iterator it = col.iterator(); it.hasNext();)
					{
						CalendarEvent temp = (CalendarEvent)it.next();
						if(temp.getEventId().equals(event.getEventId()))
							it.remove();
						else{
							temp.setResources(getBookedResources(temp.getEventId(),temp.getInstanceId()));
						}
					}
				}
                if(col!=null&&col.size()>0)
                    booking.setConflictEvents(col);

                //recurring events resource conflicts
                for (Iterator i = recurrences.iterator(); i.hasNext();)
                {
                    CalendarEvent revent = (CalendarEvent) i.next();
                    booking.setStartDate(revent.getStartDate());
                    booking.setEndDate(revent.getEndDate());
                    col = getBookingConflictEvents(booking);
					if(col != null)
					{
						for (Iterator it = col.iterator(); it.hasNext();)
						{
							CalendarEvent temp = (CalendarEvent)it.next();
							if(temp.getEventId().equals(event.getEventId()))
								it.remove();
							else{
								temp.setResources(getBookedResources(temp.getEventId(),temp.getInstanceId()));
							}
						}
					}
                    if(col!=null&&col.size()>0)
                        booking.setConflictEvents(col);
                }
                if(booking.getConflictEvents()!=null&& booking.getConflictEvents().size()>0){
                    booking.setInstanceId(CalendarModule.DEFAULT_INSTANCE_ID);
                    booking.setStartDate(event.getStartDate());
                    booking.setEndDate(event.getEndDate());
                    if(!(resoureceConflicts.contains(booking)))
                        resoureceConflicts.add(booking);
                }
                    //                    resoureceConflicts.addAll(col);
            }
        }
        return resoureceConflicts;
    }

    public Collection getAuthorizedMembers(String resourceId){
        dao = (ResourceManagerDao) getDao();
        return dao.selectAuthorizedMembers(resourceId);
/*
        Collection userIds = new ArrayList();
        Collection col =  dao.selectAuthorizedMembers(resourceId);
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            HashMap map = (HashMap)iterator.next();
            userIds.add(map.get("userId"));
        }
        return userIds;
*/
    }

    public Collection getResourceBookings(String resourceId){
        dao = (ResourceManagerDao) getDao();
        return dao.selectResourceBookings(resourceId);
    }

    public Collection getResourceBookings(String resourceId,Date startDate,Date endDate) throws CalendarException, DataObjectNotFoundException{
        dao = (ResourceManagerDao) getDao();
        
        Application application = Application.getInstance();
        CalendarModule handler = (CalendarModule) application.getModule(CalendarModule.class);
        Collection resourceBookings = dao.selectResourceBookings(resourceId,startDate,endDate);
        
        for(Iterator i=resourceBookings.iterator(); i.hasNext(); ){
        	ResourceBooking book = (ResourceBooking) i.next();
        	book.setLoginUserInvolved(false);
        	CalendarEvent event = handler.getCalendarEvent(book.getEventId());

        	Collection attendees = event.getAttendees();
        	
        	//if the login user is the one who created the event
        	if(event.getUserId().equals(application.getCurrentUser().getId())){
        		book.setLoginUserInvolved(true);
        		break;
        	}
        	
        	//if the login user is one of the attendees
        	for(Iterator j=attendees.iterator(); j.hasNext(); ){
        		Attendee att = (Attendee) j.next();
        		if(att.getUserId().equals(application.getCurrentUser().getId())){
        			book.setLoginUserInvolved(true);
        			break;
        		}
        	}

        	book.setEvent(event);
        }
        
        return resourceBookings;
    }

    public ResourceBooking getResourceBooking(String resourceId, String eventId,String instanceId){
        dao = (ResourceManagerDao) getDao();
        Collection col = dao.selectResourceBooking(resourceId,eventId,instanceId);
        if(col!=null && col.size()>0)
            return (ResourceBooking)col.iterator().next();
        return null;
    }

    public void addNewCategory(String id,String name){
        dao = (ResourceManagerDao) getDao();
        dao.insertCategory(id,name);
    }

    public void updateResourceAccess(Resource resource,Map users,Map groups){
        dao = (ResourceManagerDao) getDao();
        dao.removeResourceAccess(resource);
        addResourceAccess(resource,users,groups);
    }

    public void deleteResourceAccess(Resource resource){
        dao = (ResourceManagerDao) getDao();
        dao.removeResourceAccess(resource);
    }

    public Collection getResources(String userId,boolean onlyApproved) throws SecurityException, DaoException
    {
        dao =  (ResourceManagerDao) getDao();
        String [] groupIds = null;
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            Collection col = ss.getUserGroups(userId);
            groupIds = new String[col.size()];
            int g=0;
            for(Iterator i=col.iterator();i.hasNext();){
                groupIds[g] = ((Group)i.next()).getId();
                g++;
            }
        return dao.selectResources(null,null,new String[]{userId},groupIds,true,false,false,"r.name",false,0,-1);
        //return null;
    }

    public Resource getResource(String resourceId,boolean onlyApproved) throws DaoException
    {
        dao = (ResourceManagerDao) getDao();
        Resource resource =  dao.selectResource(resourceId,onlyApproved);
        if(resource!=null)
            resource.setAuthorities(getAuthorizedMembers(resourceId));
        return resource;
    }

    public Collection getResource(String filter,String catFilter, String [] userIds,String []groupIds,boolean onlyApproved,boolean includeDeleted,boolean includePrivate,String sort, boolean isDesc,int startIndex,int maxRows) throws DaoException
    {
        dao = (ResourceManagerDao) getDao();
        Collection col = dao.selectResources(filter,catFilter ,userIds,groupIds,onlyApproved,includeDeleted,includePrivate,sort,isDesc,startIndex,maxRows);
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            Resource resource = (Resource)iterator.next();
            resource.setAuthorities(getAuthorizedMembers(resource.getId()));
        }
        return col;
    }

    public Collection getResourceAccessIds(String resourceId,boolean onlyApproved) throws DaoException
    {
        dao = (ResourceManagerDao) getDao();
        Collection col = dao.selectResourceAccessIds(resourceId,onlyApproved);
        Collection ids = new ArrayList();
        for(Iterator i= col.iterator();i.hasNext();){
            HashMap hm = (HashMap)i.next();
            ids.add(hm.get("id"));
        }
        return ids;
    }

    public void addResourceAccess(Resource resource,Map users,Map groups){
        dao = (ResourceManagerDao) getDao();
        for(Iterator i = users.keySet().iterator();i.hasNext();){
            String idKey = (String)i.next();
            dao.insertResourceAccess(resource.getId(),idKey,RESOURCE_ACCESS_TYPE_USER,(String)users.get(idKey));
        }
        for(Iterator i = groups.keySet().iterator();i.hasNext();){
            String idKey = (String)i.next();
            dao.insertResourceAccess(resource.getId(),idKey,RESOURCE_ACCESS_TYPE_GROUP,(String)groups.get(idKey));
        }
    }

    public synchronized void approveBooking(String resourceId, String eventId,String instanceId){
        try{
            dao = (ResourceManagerDao) getDao();
            dao.updateBookingStatus(resourceId,eventId,instanceId,ResourceManager.RESOURCE_BOOKING_APPROVED,null);
            if(!dao.hasPendingBooking(resourceId)){
                Resource resource = getResource(resourceId,true);
                resource.setStatus(ResourceManager.RESOURCE_STATUS_AVAILABLE);
                updateResource(resource);
            }
            //Collection col = getResourceBookings(resourceId);
          /*  for (Iterator iterator = col.iterator(); iterator.hasNext();)
            {
                Object o = (Object) iterator.next();

            }*/

        }catch(Exception e){
            Log.getLog(getClass()).error(e);
        }
    }

    public synchronized void rejectBooking(String resourceId, String eventId,String instanceId,String reason){
        try{
            dao = (ResourceManagerDao) getDao();
            dao.updateBookingStatus(resourceId,eventId,instanceId,ResourceManager.RESOURCE_BOOKING_DECLINED,reason);
            if(!dao.hasPendingBooking(resourceId)){
                Resource resource = getResource(resourceId,true);
                resource.setStatus(ResourceManager.RESOURCE_STATUS_AVAILABLE);
                updateResource(resource);
            }
        }catch(Exception e){
            Log.getLog(getClass()).error(e);
        }
    }

    public void approveResource(String resourceId) throws DaoException
    {
        Resource resource = getResource(resourceId,false);
        resource.setApproved(true);
        updateResource(resource);
    }


    public int getTotalResourcesCount(User user) throws SecurityException, DaoException
    {
            dao = (ResourceManagerDao) getDao();
            String userId = user.getId();
            SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
            String[] groupIds = null;
            Collection groups = ss.getUserGroups(userId);
            if(groups.size()>0){
                groupIds = new String[groups.size()];
                int j=0;
                for(Iterator i = groups.iterator();i.hasNext();){
                    groupIds[j] =((Group)i.next()).getId();
                    j++;
                }
            }
            return  dao.getResourcesCount(new String[]{userId},groupIds);
    }

    public void scheduleMakeResourceAvailable(String resourceId, Date triggerDate)  {
        // TODO: kc to provide integration for this

        Log.getLog(getClass()).info("scheduleMakeResourceAvailable() has not been implemented yet");
    }
}
