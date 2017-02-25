package apn.monitor;

import java.util.HashMap;

import apn.util.LoggingUnit;

public class Monitor  implements MonitorInterface
{

	private HashMap<String,Service> services; 
	private boolean isRunning;
		
	public Monitor()
	{
		services = new HashMap<>();
		//monitoring thread	
		Thread monitoring = new Thread(new Runnable(){
			@Override
			public void run() 
			{
				LoggingUnit.log.debug("Monitor thread is up");
				isRunning = true;
				while (isRunning)
				{
					//updating services status
					for (String key: services.keySet())
						services.get(key).update();
					//switch threads if needed
					try 
					{
						Thread.sleep(50);
					}
					catch (InterruptedException e) 
					{
						stopMonitoring();
					}
				}
			}});
		
		monitoring.start();
	}
	
	/**
	 * stops Monitoring thread
	 */
	public void stopMonitoring()
	{
		isRunning = false;
		LoggingUnit.log.debug("Monitor thread shutting down");
	}
	
	/**
	 * adding Caller without grace time
	 * @param caller - Caller object
	 * @param url - Service url
	 * @param port - Service port
	 * @param period -Service poling period in seconds
	 */
	public void addCaller(Caller caller, String url, int port, float period)
	{
		if (!services.containsKey(url+":"+String.valueOf(port)))
			services.put(url+":"+String.valueOf(port), new Service(url,port));
		services.get(url+":"+String.valueOf(port)).addCaller(caller, period); 
			
	}
	
	/**
	 * adding Caller without grace time
	 * @param caller - Caller object
	 * @param url - Service url
	 * @param port - Service port
	 * @param period -Service poling period in seconds
	 * @param graceTime -Waiting time before notifying caller that service is Down
	 */
	public void addCaller(Caller caller, String url, int port, float period, float graceTime)
	{
		if (!services.containsKey(url+":"+String.valueOf(port)))
			services.put(url+":"+String.valueOf(port), new Service(url,port));
		services.get(url+":"+String.valueOf(port)).addCaller(caller, period,graceTime); 
	}
	
	public static void main(String args[])
	{
		new Monitor();		 
	}
}
