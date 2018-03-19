package changhongyun.NettySerialPortOpenSource;

import org.apache.log4j.Logger;

import changhongyun.NettySerialPortOpenSource.serialPort.netty.RxtxClient;

/**
 * Hello world!ok
 *
 */
public class App 
{
	private static final Logger log = Logger.getLogger(App.class.getName());
    public static void main( String[] args )
    {
    	log.info("start deal serialPort");
        RxtxClient rxtxClient = RxtxClient.newRxtxClient();
		try{
			rxtxClient.initialize();
			rxtxClient.launch();
		}
		catch(Exception e) {
			log.error("error:",e);
		}
    }
}
