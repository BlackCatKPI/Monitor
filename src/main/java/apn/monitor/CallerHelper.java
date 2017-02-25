package apn.monitor;

public class CallerHelper 
{
	public static enum ServiceStatus {ON, OFF};
	private ServiceStatus sentStatus;
	private float graceTime,graceTimeCounter;
	private float period,periodCounter;
	
	private boolean requestPoll; 
	
	public CallerHelper(float period, float graceTime)
	{
		this.period=period;
		this.graceTime=graceTime;
		sentStatus = ServiceStatus.OFF;
		requestPoll=true;
	}
	
	public CallerHelper(float period)
	{
		this(period, 0f);
	}
	
	public void update(float deltaTime, ServiceStatus serviceStatus)
	{
		if ((graceTime>0)&&(graceTimeCounter<=0)) requestPoll =true;
	}
	
	public boolean isRequestPoll()
	 {
		return requestPoll;
	 }
}
