package apn.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggingUnit 
{
	public static Logger log;
	public static LoggingUnit instance = new LoggingUnit();
	private LoggingUnit()
	{
		PropertyConfigurator.configure(getClass().getResourceAsStream("/log4j.xml"));
		log = Logger.getRootLogger();//.getLogger("filelogger");
		log.info("Logger Unit: ready");
	}
}
