package apn.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import apn.monitor.Caller;
import apn.monitor.Monitor;
import apn.monitor.Service;
import apn.monitor.Service.ServiceStatus;
import apn.util.LoggingUnit;

public class MockitoTest  {

        @Mock
        Caller caller; 

        @Mock
        Caller caller2;
        
        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule(); 

        
        
        private String serviceName ="someservice";
        private int somePort = 8080;
        private final float testingTime = 10f;
        private float testingCounter;
        private long lastTime;
        
        
        //testing 1 caller without graceTime testing time 10 sec
        @Test
        public void test1()  {
        		MonitorTest monitor  = new MonitorTest(); 
                monitor.addCaller(caller, serviceName, somePort, 2f);
                
                testingCounter=0;
                lastTime = System.nanoTime();
                
                ((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.ON);
                
                while (testingCounter<testingTime)
                {
                	testingCounter += ((float)(System.nanoTime()-lastTime))/1000000000f;
                	lastTime = System.nanoTime();
                	//LoggingUnit.log.debug("Testing cycle");
                	if ((testingCounter>6)&&(testingCounter<8))
                		((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.OFF);
                	else
                		((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.ON); 
                	
                   	try {
                	
                		Thread.sleep(100);					
                		}
                	catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                verify(caller, times(2)).notify(Mockito.contains("UP"));
                verify(caller, times(1)).notify(Mockito.contains("DOWN"));
                monitor.stopMonitoring();
        }
        
        
        //testing 1 caller with graceTime testing time 10 sec
        @Test
        public void test2()  
        {
        		MonitorTest monitor  = new MonitorTest(); 
                monitor.addCaller(caller, serviceName, somePort, 2f,6f);
               
                testingCounter=0;
                lastTime = System.nanoTime();
                
                ((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.ON);
                
                while (testingCounter<testingTime)
                {
                	testingCounter += ((float)(System.nanoTime()-lastTime))/1000000000f;
                	lastTime = System.nanoTime();
                	//LoggingUnit.log.debug("Testing cycle");
                	if ((testingCounter>6)&&(testingCounter<8))
                		((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.OFF);
                	else
                		((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.ON); 
                	
                   	try {
                	
                		Thread.sleep(100);					
                		}
                	catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                verify(caller, times(1)).notify(Mockito.contains("UP"));
                monitor.stopMonitoring();
        }
       
        
        //testing 1 caller with graceTime < periodTime testing time 10 sec
        @Test
        public void test3()  
        {
        		MonitorTest monitor  = new MonitorTest(); 
                monitor.addCaller(caller, serviceName, somePort, 6f,3f);
               
                testingCounter=0;
                lastTime = System.nanoTime();
                
                ((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.ON);
                
                while (testingCounter<testingTime)
                {
                	testingCounter += ((float)(System.nanoTime()-lastTime))/1000000000f;
                	lastTime = System.nanoTime();
                	//LoggingUnit.log.debug("Testing cycle");
                	if ((testingCounter>6)&&(testingCounter<10))
                		((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.OFF);
                	else
                		((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.ON); 
                	
                   	try {
                	
                		Thread.sleep(100);					
                		}
                	catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                verify(caller, times(1)).notify(Mockito.contains("UP"));
                verify(caller, times(1)).notify(Mockito.contains("DOWN"));
                monitor.stopMonitoring();
        } 
        
      //testing caller with rapid servicePoll testing time 10 sec
        @Test
        public void test4()  
        {
        		MonitorTest monitor  = new MonitorTest(); 
                monitor.addCaller(caller, serviceName, somePort, 0.5f);
                monitor.addCaller(caller2, serviceName, somePort, 2.5f,3f);
                testingCounter=0;
                lastTime = System.nanoTime();
                
                ((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.ON);
                
                while (testingCounter<testingTime)
                {
                	testingCounter += ((float)(System.nanoTime()-lastTime))/1000000000f;
                	lastTime = System.nanoTime();
                	//LoggingUnit.log.debug("Testing cycle");
                	if ((testingCounter>6)&&(testingCounter<8))
                		((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.OFF);
                	else
                		((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).setReturn(ServiceStatus.ON); 
                	
                   	try {
                	   		Thread.sleep(100);					
                		}
                	catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                
                LoggingUnit.log.debug(((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).getCheckTimes());
                assertTrue (((ServiceTest) monitor.getServices().get( serviceName + ":" + String.valueOf(somePort))).getCheckTimes() <= 11);
                monitor.stopMonitoring();
        }
        
        
        
        //--------------------------------------------------------------------------------------------
        // 						TEST OBJECTS for replacing checkServiceStatus 
        //--------------------------------------------------------------------------------------------
        
        
        private class MonitorTest extends Monitor
        {
        	@Override
        	protected void checkServiceExist(String url, int port)
        	{
        		String serviceID = url+":"+String.valueOf(port); 
        		if (!services.containsKey(serviceID))
        			services.put(serviceID, new ServiceTest(url,port));
        	}
        	
        	public HashMap<String,Service> getServices()
        	{
        		return services;
        	}
        }
        
        private class ServiceTest extends Service
        {
        	private ServiceStatus returnStatus;
        	private int checkTimes;
        	
        	ServiceTest(String url, int port)
        	{
        		super(url,port);
        		returnStatus = ServiceStatus.ON;
        		checkTimes = 0;
        	}
        	
        	public void setReturn(ServiceStatus status)
        	{
        		returnStatus = status;
        	}
        	
        	@Override
        	public  ServiceStatus checkServiceStatus()
        	{
        		checkTimes++;
        		return returnStatus;
        	}
        	
        	public int getCheckTimes()
        	{
        		return checkTimes;
        	}
        }
}
