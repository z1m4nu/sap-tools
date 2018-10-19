/**
 * 
 */
package org.crossroad.test;

import org.apache.tools.ant.BuildException;
import org.crossroad.ant.sap.AbstractDCClient;
import org.crossroad.ant.sap.SAPApplicationHelper;
import org.crossroad.ant.sap.SAPComponentApplication;
import org.crossroad.ant.sap.StartApplication;
import org.crossroad.ant.sap.StopApplication;

import com.sap.engine.services.dc.ant.SAPJ2EEEngine;
import com.sap.engine.services.dc.api.lcm.LCMStatus;
import com.sap.engine.services.dc.api.selfcheck.SelfChecker;
import com.sap.engine.services.dc.api.selfcheck.SelfCheckerResult;

/**
 * @author e.soden
 *
 */
public class SAPAppTest extends AbstractDCClient{

	/**
	 * 
	 */
	public SAPAppTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SAPAppTest client = new SAPAppTest();
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
		
		addSAPJ2EEEngine(engine);
		
		
		connect(engine);
		LCMStatus status = getClient().getLifeCycleManager().getLCMStatus("AdobeDocumentServices","com.adobe");
		System.out.println(status.getLCMStatusDetails().toString() );
		
		
	}

	@Override
	protected void doExecute() throws BuildException {
		// TODO Auto-generated method stub
		
	}

}
