/**
 * 
 */
package org.crossroad.ant.sap;

import org.crossroad.ant.sap.exception.StopException;

import com.sap.engine.services.dc.api.Client;
import com.sap.engine.services.dc.api.event.EventMode;
import com.sap.engine.services.dc.api.lcm.LifeCycleManager;



/**
 * @author e.soden
 *
 */
public class StopApplication extends AbstractStopStartApp  {
	

	/**
	 * 
	 */
	public StopApplication() {

	}

	@Override
	protected void detailExecute(Client client, String vendor, String component) throws Exception {
		LifeCycleManager manager = client.getLifeCycleManager();
		log("Stopping application Vendor["+vendor+"] component ["+component+"]");
		manager.addLCEventListener(this, EventMode.SYNCHRONOUS);
		manager.stop(component, vendor);
	}

	@Override
	protected void fireCheckException(String message) throws Exception {
		throw new StopException(message);
	}

	
}
