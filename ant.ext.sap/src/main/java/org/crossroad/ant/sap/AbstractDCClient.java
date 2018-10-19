/**
 * 
 */
package org.crossroad.ant.sap;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import com.sap.engine.services.dc.ant.SAPJ2EEEngine;
import com.sap.engine.services.dc.api.Client;
import com.sap.engine.services.dc.api.ClientFactory;
import com.sap.engine.services.dc.api.ConnectionException;

/**
 * @author e.soden
 *
 */
public abstract class AbstractDCClient extends AbstractAntClassHelper {
	private Client client = null;
	protected boolean dryRun = false;
	protected List<SAPJ2EEEngine> engines = new ArrayList<SAPJ2EEEngine>();

	/**
	 * 
	 */
	public AbstractDCClient() {
		// TODO Auto-generated constructor stub
	}

	public void setDryRun(boolean dryRun) {
		this.dryRun = dryRun;
	}

	protected void connect(SAPJ2EEEngine engine) throws Exception {
		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		try {

			client = ClientFactory.getInstance().createClient(engine.getServerHost(), engine.getServerPort(),
					engine.getUserName(), engine.getUserPassword());

			log("You can have access to log file in directory ["+client.getLog().getLocPath()+"]");
		} catch (Exception e) {
			throw new BuildException(e);
		} finally {

		}
	}

	protected Client getClient() {
		return client;
	}

	protected void close() {
		if (client != null) {
			try {
				client.close();
			} catch (ConnectionException e) {
				log("Unable to close the client", e, Project.MSG_ERR);
			}
		}
	}

	public void addSAPJ2EEEngine(SAPJ2EEEngine engine) {
		this.engines.add(engine);
	}

	@Override
	protected void validate() throws BuildException {
		if (engines.isEmpty()) {
			throw new BuildException(
					"Operation should be triggered on one engine -  the originator of the j2ee client libraries. Currently "
							+ this.engines.size()
							+ " are specified. Please check the occurance(s) of nested 'sapj2eeengine' declarations!");
		} else {
			for (SAPJ2EEEngine engine : this.engines) {
				engine.validate();
			}
		}

	}
}
