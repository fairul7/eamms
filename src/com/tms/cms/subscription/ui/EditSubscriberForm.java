package com.tms.cms.subscription.ui;

import com.tms.cms.subscription.Subscriber;
import com.tms.cms.subscription.Subscription;
import com.tms.cms.subscription.SubscriptionHandler;
import com.tms.cms.subscription.SubscriptionHistory;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 15, 2003
 * Time: 9:33:45 PM
 * To change this template use Options | File Templates.
 */
public class EditSubscriberForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "cms/subscription/subscriberEdit";
    private static final String FORWARD_SUCCESS = "com.tms.cms.subscription.ui.EditSubscriberForm.success";
    private static final String FORWARD_FAIL = "com.tms.cms.subscription.ui.EditSubscriberForm.fail";

    private String subscriptionId;
    private String userId;
    private Subscriber subscriber;
    private Collection history;

    private SelectBox paymentMethod;
    private TextField methodNumber;
    private TextField amount;
    private ValidatorIsNumeric validAmount;

    private Button approveButton;
    private Button renewButton;
    private Button cancelButton;
    private String message;
    private boolean state;

    public EditSubscriberForm() {}

    public EditSubscriberForm(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        if(!(getSubscriptionId() == null || getUserId() == null))
        {
            try
            {
                removeChildren();

                Subscription subscription;
                SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                setSubscriber(handler.getSubscriber(getSubscriptionId(), getUserId()));
                subscription = handler.getSubscription(getSubscriptionId());
                DaoQuery properties = new DaoQuery();
                properties.addProperty(new OperatorEquals("subscriptionId", getSubscriptionId(), DaoOperator.OPERATOR_AND));
                properties.addProperty(new OperatorEquals("userId", getUserId(), DaoOperator.OPERATOR_AND));
                setHistory(handler.getSubscriberHistory(properties, 0, -1, "actionDate", true));
                removeChildren();

                Application application = Application.getInstance();
                paymentMethod = new SelectBox("paymentMethod");
                paymentMethod.setMultiple(false);
                paymentMethod.addOption("Cash", application.getMessage("security.label.cash", "Cash"));
                paymentMethod.addOption("Cheque", application.getMessage("security.label.cheque", "Cheque"));
                paymentMethod.addOption("Credit Card", application.getMessage("security.label.creditCard", "Credit Card"));
                methodNumber = new TextField("methodNumber");
                methodNumber.setSize("20");
                amount = new TextField("amount");
                amount.setSize("5");
                renewButton = new Button("renewButton");
                renewButton.setText(application.getMessage("security.label.renew", "Renew"));
                approveButton = new Button("approveButton");
                approveButton.setText(application.getMessage("security.label.approve", "Approve"));
                cancelButton = new Button("cancelButton");
                cancelButton.setText(application.getMessage("security.label.cancel", "Cancel"));
                validAmount = new ValidatorIsNumeric("validAmount");

                addChild(paymentMethod);
                addChild(methodNumber);
                addChild(amount);
                addChild(renewButton);
                addChild(approveButton);
                addChild(cancelButton);
                amount.addChild(validAmount);

                if(!((subscription.getMonths() > 0 || (getSubscriber().getState().equals(Subscriber.STATE_EXPIRED))) && (!(getSubscriber().getState().equals(Subscriber.STATE_PENDING)))))
                    renewButton.setHidden(true);
                if(!(getSubscriber().getState().equals(Subscriber.STATE_PENDING)))
                    approveButton.setHidden(true);

                setState(false);
            }
            catch(Exception e)
            {
                Log.getLog(EditSubscriberForm.class).error(e.toString(), e);
            }
        }
    }

    public Forward onSubmit(Event evt)
    {
        Forward forward = new Forward();
        if(cancelButton.getAbsoluteName().equals(findButtonClicked(evt)))
            setState(true);
        else
            forward = super.onSubmit(evt);
        return forward;
    }

    public Forward onValidate(Event evt)
    {
        if(! isState())
        {
            if(!(getSubscriptionId() == null || getUserId() == null))
            {
                if(renewButton.getAbsoluteName().equals(findButtonClicked(evt)))
                {
                    try
                    {
                        SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                        SubscriptionHistory newHistory = buildHistory(SubscriptionHandler.PERMISSION_SUBSCRIPTION_RENEW);
                        handler.renewSubscription(newHistory);
                        service.unassignGroups(newHistory.getUserId(), new String[] {newHistory.getSubscription().getGroupId()});
                        service.assignGroups(newHistory.getUserId(), new String[] {newHistory.getSubscription().getGroupId()});
                        setState(true);
                        return new Forward(FORWARD_SUCCESS, null, false);
                    }
                    catch(Exception e)
                    {
                        Log.getLog(EditSubscriptionForm.class).error(e.toString(), e);
                    }
                }
                else if(approveButton.getAbsoluteName().equals(findButtonClicked(evt)))
                {
                    try
                    {
                        SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                        SubscriptionHistory newHistory = buildHistory(SubscriptionHandler.PERMISSION_SUBSCRIPTION_APPROVE);
                        if(newHistory.getSubscription().getMonths() > 0)
                        {
                            handler.renewSubscription(newHistory);
                        }
                        else
                        {
                            handler.setSubscriberState(Subscriber.STATE_ACTIVE, getSubscriptionId(), getUserId());
                            handler.addSubscriberHistory(newHistory);
                        }
                        service.unassignGroups(newHistory.getUserId(), new String[] {newHistory.getSubscription().getGroupId()});
                        service.assignGroups(newHistory.getUserId(), new String[] {newHistory.getSubscription().getGroupId()});
                        setState(true);
                        return new Forward(FORWARD_SUCCESS, null, false);
                    }
                    catch(Exception e)
                    {
                        Log.getLog(EditSubscriptionForm.class).error(e.toString(), e);
                    }
                }
                else
                {
                    setState(true);
                }
            }
        }
        return new Forward(FORWARD_FAIL, null, false);
    }

    private SubscriptionHistory buildHistory(String action)
    {
        SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        SubscriptionHistory history = new SubscriptionHistory();
        try
        {
            history.setId(UuidGenerator.getInstance().getUuid());
            history.setSubscriptionId(getSubscriptionId());
            history.setSubscription(handler.getSubscription(getSubscriptionId()));
            history.setUserId(getUserId());
            history.setUser(service.getUser(getUserId()));
            history.setActionDate(new Date());
            history.setAction(action);
            Collection method = (Collection) paymentMethod.getValue();
            history.setMethod((String) method.iterator().next());
            history.setMethodNumber((String) methodNumber.getValue());
            history.setAmount(Double.parseDouble((String) amount.getValue()));
        }
        catch(Exception e)
        {
            Log.getLog(EditSubscriptionForm.class).error(e);
        }
        return history;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public String getSubscriptionId()
    {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Subscriber getSubscriber()
    {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber)
    {
        this.subscriber = subscriber;
    }

    public Button getRenewButton()
    {
        return renewButton;
    }

    public void setRenewButton(Button renewButton)
    {
        this.renewButton = renewButton;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public SelectBox getPaymentMethod()
    {
        return paymentMethod;
    }

    public void setPaymentMethod(SelectBox paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }

    public TextField getMethodNumber()
    {
        return methodNumber;
    }

    public void setMethodNumber(TextField methodNumber)
    {
        this.methodNumber = methodNumber;
    }

    public TextField getAmount()
    {
        return amount;
    }

    public void setAmount(TextField amount)
    {
        this.amount = amount;
    }

    public Button getApproveButton()
    {
        return approveButton;
    }

    public void setApproveButton(Button approveButton)
    {
        this.approveButton = approveButton;
    }

    public Collection getHistory()
    {
        return history;
    }

    public void setHistory(Collection history)
    {
        this.history = history;
    }
}
