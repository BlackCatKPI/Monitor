package apn.monitor;

import apn.monitor.Service.ServiceStatus;
import apn.util.LoggingUnit;

public class CallerChecker 
{
	
	private ServiceStatus sentStatus,currentStatus;
	private float graceTime,graceTimeCounter;
	private float period,periodCounter;
	private Caller caller;
	private boolean requestPoll; 
	private String serviceID;
	private String callerID;
	
	public CallerChecker(Caller caller, String serviceID, float period, float graceTime)
	{
		this.caller=caller;
		this.serviceID=serviceID;
		this.period=period;
		this.graceTime=graceTime;
		
		currentStatus=null;
		sentStatus = null;
		requestPoll=true;
		
		callerID = "caller_"+caller.toString();
	}
	
	public CallerChecker(Caller caller, String serviceID, float period)
	{
		this(caller, serviceID, period, 0f);
	}
	
	public void update(float deltaTime)
	{
		if (currentStatus!=null)
		if (currentStatus.equals(ServiceStatus.ON))
		{
			actionsWhenOn(deltaTime);
		}
		else
			actionsWhenOff(deltaTime);
			
		
	}
	
	private void actionsWhenOn(float deltaTime)
	{
		if (sentStatus.equals(ServiceStatus.OFF))
		{
			sentStatus=ServiceStatus.ON;
			caller.notify("Service " + serviceID + " is UP");
			LoggingUnit.log.info("Service " + serviceID + " is UP for " + callerID);
			if (graceTimeCounter>0) graceTimeCounter=0;
		}
		
		regularPoll(deltaTime);

	}
	
	private void actionsWhenOff(float deltaTime)
	{
		
		if (sentStatus.equals(ServiceStatus.ON))
		{
			if (graceTimeCounter>graceTime)
			{
				sentStatus = ServiceStatus.OFF;
				caller.notify("Service " + serviceID + " is DOWN");
				LoggingUnit.log.info("Service " + serviceID + " is DOWN for " + callerID);
			}
			else
			{
				graceTimeCounter+=deltaTime;
				if (graceTimeCounter>=graceTime)
				{
					requestPoll = true;
				}
			}
		}
		
		regularPoll(deltaTime);
	}
	
	private void regularPoll(float deltaTime)
	{
		periodCounter+=deltaTime;
		
		if(periodCounter>period)
		{
			periodCounter=0;
			requestPoll=true;
		}
	}
	
	public boolean isRequestPoll()
	{
		return requestPoll;
	}
	
	
	//updating current service status
	public void setCurrentStatus(ServiceStatus currentStatus)
	{
		if(currentStatus==null)
		{
			LoggingUnit.log.warn("Wrong service status");
		}
		else
		{
			if(sentStatus == null)
			{
				sentStatus = currentStatus;
				if (currentStatus.equals(ServiceStatus.ON))
				{
					caller.notify("Service " + serviceID + " is UP");
					LoggingUnit.log.info("Service " + serviceID + " is UP for " + callerID);
				}
				else
				{
					caller.notify("Service " + serviceID + " is DOWN");
					LoggingUnit.log.info("Service " + serviceID + " is DOWN for " + callerID);
				}
			}
			this.currentStatus=currentStatus;
			requestPoll = false;
		}
		
	}
	
}
