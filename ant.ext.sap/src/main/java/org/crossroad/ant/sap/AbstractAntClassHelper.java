/**
 * 
 */
package org.crossroad.ant.sap;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author e.soden
 *
 */
public abstract class AbstractAntClassHelper extends Task{

	/**
	 * 
	 */
	public AbstractAntClassHelper() {
		
	}

	/**
	 * Main method validator
	 */
	protected abstract void validate() throws BuildException;
	
	protected abstract void doExecute() throws BuildException;
	
	@Override
	public void execute() throws BuildException {
		super.execute();
		
		validate();
		
		doExecute();
	}
	
	
}
