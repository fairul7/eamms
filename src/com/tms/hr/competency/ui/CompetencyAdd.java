package com.tms.hr.competency.ui;

import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyHandler;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class CompetencyAdd extends CompetencyForm
{
    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        try
        {
            User user = getWidgetManager().getUser();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_ADD, CompetencyHandler.class.getName(), null))
            {
                Competency competency = generateCompetency();
                competency.setCompetencyId(UuidGenerator.getInstance().getUuid());
                CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                handler.addCompetency(competency);
                init();
                forward = new Forward(FORWARD_SUCCESSFUL);
            }
        }
        catch (Exception e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return forward;
    }
}
