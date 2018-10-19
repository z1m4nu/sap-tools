/**
 * 
 */
package org.crossroad.test;

import org.crossroad.ant.sap.SAPComponentApplication;
import org.crossroad.ant.sap.StartApplication;
import org.crossroad.ant.sap.StopApplication;

import com.sap.engine.services.dc.ant.SAPJ2EEEngine;

/**
 * @author e.soden
 *
 */
public class SAPClientTest {

	/**
	 * 
	 */
	public SAPClientTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SAPClientTest client = new SAPClientTest();
		client.test();
	}

	protected void test() throws Exception {
		SAPJ2EEEngine engine = new SAPJ2EEEngine();
		
		engine.setServerHost("Bavsw036056.tally-weijl.ch");
		engine.setServerPort(50004);
		engine.setUserName("SODEE");
		engine.setUserPassword("Crossroad1");
		
		SAPComponentApplication application = new SAPComponentApplication();
		application.setApplication("com.adobe/AdobeDocumentServices");
		
		
		StopApplication stop = new StopApplication();
		stop.addSAPComponentApplication(application);
		stop.addSAPJ2EEEngine(engine);
		stop.setTimeout(3000L);
		stop.setDryRun(true);
		
		StartApplication start = new StartApplication();
		start.setWarningAsError(false);
		start.addSAPComponentApplication(application);
		start.addSAPJ2EEEngine(engine);
		start.setTimeout(3000L);
		start.setDryRun(true);
		
		stop.execute();
		start.execute();
	}

}
