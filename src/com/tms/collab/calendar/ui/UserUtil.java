package com.tms.collab.calendar.ui;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jan 24, 2003
 * Time: 10:42:28 AM
 * To change this template use Options | File Templates.
 */
public class UserUtil {

    /**
     * Retrieves a specified user.
     * @param userId
     * @return
     * @throws SecurityException
     */
    public static User getUser(String userId) throws SecurityException {
        try {
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            return security.getUser(userId);
        }
        catch (SecurityException e) {
            User user = new User();
            user.setId(userId);
            return user;
        }
    }

    /**
     * Retrieves a collection of available User objects.
     * @return
     * @throws SecurityException
     */
/*
    public static Collection getUserList() throws SecurityException {
        SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
        return security.getUsers(new DaoQuery(), 0, -1, "username", false);
    }
*/

    public static Collection getUserListByPermission(String permission) throws SecurityException {
        SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
        return security.getUsersByPermission(permission,Boolean.TRUE,"username",false,0,-1);
        //return security.getUsers(new DaoQuery(), 0, -1, "username", false);
    }

}
