package apn.monitor;

import java.util.HashMap;

public class Monitor 
{

	private HashMap<String,Service> services; 
	
	public Monitor()
	{
		services = new HashMap<>();
		
	}
	
	public void addCaller(Caller caller, String url, int port, float period)
	{
		
	}
	
	public void addCaller(Caller caller, String url, int port, float period, float graceTime)
	{
		
	}
	
	public static void main(String args[])
	{
		new Monitor();		 
	}
}
