package apn.monitor;

import apn.monitor.Service.ServiceStatus;

public class CallerChecker 
{
	
	private ServiceStatus sentStatus,currentStatus;
	private float graceTime,graceTimeCounter;
	private float period,periodCounter;
	private Caller caller;
	private boolean requestPoll; 
	private String serviceID;
	
	public CallerChecker(Caller caller, String serviceID, float period, float graceTime)
	{
		this.caller=caller;
		this.serviceID=serviceID;
		this.period=period;
		this.graceTime=graceTime;
		
		sentStatus = ServiceStatus.OFF;
		requestPoll=true;
	}
	
	public CallerChecker(Caller caller, String serviceID, float period)
	{
		this(caller, serviceID, period, 0f);
	}
	
	public void update(float deltaTime)
	{
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
			caller.notify("Service " + serviceID + " is UP");
			sentStatus.equals(ServiceStatus.ON);
			if (graceTimeCounter>0) graceTimeCounter=0;
			
		}
		
		periodCounter+=deltaTime;
		
		if(periodCounter>period)
		{
			periodCounter=0;
			requestPoll=true;
		}

	}
	
	private void actionsWhenOff(float deltaTime)
	{
		
		if (sentStatus.equals(ServiceStatus.ON))
		{
			if (graceTimeCounter>graceTime)
			{
				sentStatus = ServiceStatus.OFF;
				caller.notify("Service " + serviceID + " is DOWN");
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
	
	public void setCurrentStatus(ServiceStatus currentStatus)
	{
		this.currentStatus=currentStatus;
		requestPoll = false;
	}
	
}
