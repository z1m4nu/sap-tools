/**
 * 
 */
package org.crossroad.ant.sap;

import org.crossroad.ant.sap.exception.StartException;

import com.sap.engine.services.dc.api.Client;
import com.sap.engine.services.dc.api.event.EventMode;
import com.sap.engine.services.dc.api.lcm.LifeCycleManager;



/**
 * @author e.soden
 *
 */
public class StartApplication extends AbstractStopStartApp  {
	

	/**
	 * 
	 */
	public StartApplication() {

	}

	@Override
	protected void detailExecute(Client client, String vendor, String component) throws Exception {
		LifeCycleManager manager = client.getLifeCycleManager();
		manager.addLCEventListener(this, EventMode.SYNCHRONOUS);
		
		manager.start(component, vendor);
	}

	@Override
	protected void fireCheckException(String message) throws Exception {
		throw new StartException(message);
	}

	
	
}
