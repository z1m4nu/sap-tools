/**
 * 
 */
package org.crossroad.ant.sap;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.sap.engine.services.dc.ant.SAPJ2EEEngine;
import com.sap.engine.services.dc.api.Client;
import com.sap.engine.services.dc.api.event.LCEvent;
import com.sap.engine.services.dc.api.event.LCEventListener;
import com.sap.engine.services.dc.api.model.Sdu;

/**
 * @author e.soden
 *
 */
public abstract class AbstractStopStartApp extends AbstractSAPApplication implements LCEventListener {
	protected static final int OK = 0;
	protected static final int WARNING = 1;
	protected static final int ERROR = 2;

	private boolean warningaserror = false;

	private List<Status> statusList = new ArrayList<Status>();
	
	private Long timeout = 0L;

	

	/**
	 * 
	 */
	public AbstractStopStartApp() {
		// TODO Auto-generated constructor stub
	}

	public void setWarningAsError(boolean b) {
		this.warningaserror = b;
	}

	

	/**
	 * @return the timeout
	 */
	public Long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	@Override
	protected void doExecute() throws BuildException {
		printSetupData();

		for (SAPComponentApplication component : this.getComponents()) {
			for (SAPJ2EEEngine engine : this.engines) {
				if (!this.dryRun) {
					try {
						connect(engine);

						detailExecute(getClient(), component.getVendor(), component.getName());

						checkStatus();

					} catch (Exception e) {
						throw new BuildException(e);
					} finally {
						close();
					}

				} else {
					log(this.getClass().getSimpleName() + " " + component.getVendor() + "/" + component.getName() + " on host "
							+ engine.getServerHost() + ":" + engine.getServerPort() + " using " + engine.getUserName());

				}
			}
		}

	}

	private void checkStatus() throws Exception {

		for (Status status : this.statusList) {
			StringBuffer buffer = new StringBuffer();
			Sdu sdu = status.getSdu();
			buffer.append("Component: ");

			if (sdu != null) {
				buffer.append(sdu.getVendor()).append("/").append(sdu.getName());
			} else {
				buffer.append("Unknown");
			}

			buffer.append(" [").append(status.getOperation()).append("]\n");

			if (status.getStatus() == ERROR) {
				for (String s : status.getMessage()) {
					buffer.append(s);
				}
				fireCheckException(buffer.toString());
			} else if (status.getStatus() == WARNING) {
				for (String s : status.getMessage()) {
					buffer.append(s);
				}

				if (warningaserror) {
					fireCheckException(buffer.toString());
				} else {
					log(buffer.toString());
				}
			} else {
				log(buffer.toString());
			}
		}
	}

	protected abstract void fireCheckException(String message) throws Exception;

	protected abstract void detailExecute(Client client, String vendor, String component) throws Exception;

	private void printSetupData() {
		log("Starting deployment with the following properties:");
		log("The targeted SAP J2EE Engines are: " + this.engines);
		log("Component list: " + this.getComponents());

	}

	protected void validate() throws BuildException {
		super.validate();

		if ((this.timeout != null) && (this.timeout.longValue() <= 0L)) {
			throw new BuildException("The given deploy timeout " + this.timeout
					+ " is not correct. It must be valid integer greater than 0. Correct its value in used ant xml.");
		}
		
	}

	
	@Override
	public void lifeCycleEventTriggered(LCEvent arg0) {
		this.statusList.add(Status.factory(arg0));
	}
}
