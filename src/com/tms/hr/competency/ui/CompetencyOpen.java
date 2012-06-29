package com.tms.hr.competency.ui;

import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyHandler;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class CompetencyOpen extends CompetencyForm
{
    public void refresh()
    {
        super.refresh();
        if(!(competencyId == null || "".equals(competencyId)))
        {
            try
            {
                User user = getWidgetManager().getUser();
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_UPDATE, CompetencyHandler.class.getName(), null))
                {
                    CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                    Competency competency = handler.getCompetency(competencyId);
                    if(competency != null)
                    {
                        competencyName.setValue(competency.getCompetencyName());
                        competencyType.setSelectedOptions(new String[] {competency.getCompetencyType()});
                        newCompetencyType.setValue("");
                        competencyDescription.setValue(competency.getCompetencyDescription());
                        unit.setSelectedOptions(new String[] {competency.getUnitId()});
                    }
                }
            }
            catch (Exception e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
    }

    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        if(!(competencyId == null || "".equals(competencyId)))
        {
            try
            {
                User user = getWidgetManager().getUser();
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                if(service.hasPermission(user.getId(), CompetencyHandler.PERMISSION_COMPETENCY_UPDATE, CompetencyHandler.class.getName(), null))
                {
                    CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
                    Competency competency = generateCompetency();
                    competency.setCompetencyId(competencyId);
                    handler.updateCompetency(competency);
                    forward = new Forward(FORWARD_SUCCESSFUL);
                }
            }
            catch (Exception e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
        return forward;
    }

    public void setCompetencyId(String competencyId)
    {
        super.setCompetencyId(competencyId);
        init();
    }

	public boolean isEditMode() {
		return true;
	}
	
}
