package apn.monitor;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import apn.util.LoggingUnit;

public class Service 
{
	public static enum ServiceStatus {ON, OFF};
	
	HashMap<Caller, CallerChecker> callers;
	private String serviceID;
	private String url;
	private int port;
	private long lastTime;
	private float deltaTime;
	private float timeSinceLast;
	private ServiceStatus lastStatus;
	public Service(String url, int port)
	{
		callers = new HashMap<>();
		this.url=url;
		this.port=port;
		serviceID = url+":"+String.valueOf(port);
		lastTime = System.nanoTime();
		timeSinceLast = 2f;
	}
	
	public void addCaller(Caller caller, float period, float graceTime)
	{
		callers.put(caller, new CallerChecker(caller, serviceID, period, graceTime));
	}
	
	public void addCaller(Caller caller, float period)
	{
		callers.put(caller, new CallerChecker(caller, serviceID, period));
	}
	
	public void update()
	{   deltaTime = ((float)(System.nanoTime()-lastTime))/1000000000f;
		lastTime = System.nanoTime();
		timeSinceLast+=deltaTime;
		for(Caller caller: callers.keySet())
		{
			CallerChecker checker =  callers.get(caller);
			checker.update(deltaTime);
			if (checker.isRequestPoll())
			{
				if (timeSinceLast>1f)
				{
					checkServiceStatus();
					timeSinceLast = 0f;
				}
				checker.setCurrentStatus(lastStatus);
			}	
		}
	}
	
	//checking if service is up or down
	public void checkServiceStatus()
	{
		LoggingUnit.log.debug("Checking service " +serviceID+ " status");
		try (Socket s = new Socket(url, port))
		{
			lastStatus = ServiceStatus.ON;
		}
		catch(IOException e)
		{
			lastStatus = ServiceStatus.OFF;
		}
	}
	
}
