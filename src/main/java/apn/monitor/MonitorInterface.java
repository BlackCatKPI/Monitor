package apn.monitor;

public interface MonitorInterface 
{
	public void addCaller(Caller caller, String url, int port, float period);
	public void addCaller(Caller caller, String url, int port, float period, float graceTime);
	public void stopMonitoring();
}
