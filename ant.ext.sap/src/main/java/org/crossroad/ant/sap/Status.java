/**
 * 
 */
package org.crossroad.ant.sap;

import java.util.ArrayList;
import java.util.List;

import com.sap.engine.services.dc.api.event.LCEvent;
import com.sap.engine.services.dc.api.model.Sdu;

/**
 * @author e.soden
 *
 */
public class Status {

	private int status = AbstractStopStartApp.OK;
	private Sdu sdu = null;
	private List<String> message = new ArrayList<String>();

	private String operation = null;
	private Status()
	{
		
	}
	
	
	
	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}



	/**
	 * @param operation the operation to set
	 */
	private void setOperation(String operation) {
		this.operation = operation;
	}



	public List<String> getMessage() {
		return message;
	}
	
	private void addMessage(String message)
	{
		this.message.add(message);
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	private void setStatus(int status) {
		this.status = status;
	}


	
	/**
	 * @return the sdu
	 */
	public Sdu getSdu() {
		return sdu;
	}

	/**
	 * @param sdu the sdu to set
	 */
	private void setSdu(Sdu sdu) {
		this.sdu = sdu;
	}

	public static Status factory(LCEvent event)
	{
		Status status = new Status();
		
		status.setOperation(event.getLCEventAction().getName());
		
		if(event.hasErrors())
		{
			status.setStatus(AbstractStopStartApp.ERROR);
			
			for(String e:event.getErrors())
			{
				status.addMessage(e);
			}
		} else if (event.hasWarnings())
		{
			status.setStatus(AbstractStopStartApp.WARNING);
			for(String e:event.getWarnings())
			{
				status.addMessage(e);
			}
		} else {
			status.setStatus(AbstractStopStartApp.OK);
		}
		
		status.setSdu(event.getSdu());
			
		return status;
	}

}
