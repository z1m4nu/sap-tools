/**
 * 
 */
package org.crossroad.ant.sap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.sap.engine.services.dc.ant.SAPListFile;

/**
 * @author e.soden
 *
 */
public abstract class AbstractSAPApplication extends AbstractDCClient{
	private List<SAPComponentApplication> components = new ArrayList<SAPComponentApplication>();
	private SAPListFile componentListFile = null;
	/**
	 * 
	 */
	public AbstractSAPApplication() {
	}

	public void addSAPComponentApplication(SAPComponentApplication component) {
		this.components.add(component);
	}

	public void addSAPApplicationList(SAPListFile file) {
		this.componentListFile = file;
	}
	
	@Override
	protected void validate() throws BuildException {
		super.validate();
		if (this.componentListFile != null) {
			if ((this.componentListFile.getListFilePath() == null)
					|| (this.componentListFile.getListFilePath().trim().equals(""))) {
				throw new BuildException(
						"The component list is empty or missing. Please check the 'sapapplicationlist' element.");
			}
			loadApplicationFromFile();
		}

		if (this.components.isEmpty()) {
			throw new BuildException(
					"The application component is empty or missing. Please check the 'sapapplication' element or 'sapapplicationlist'.");

		} else {
			for (SAPComponentApplication component : this.components) {
				if (!component.isValid()) {
					throw new BuildException("Application definition should be in the form of VENDOR/COMPONENT");
				}
			}
		}
	}

	private void loadApplicationFromFile() {
		File lisFile = new File(this.componentListFile.getListFilePath());
		try {
			FileInputStream fis = new FileInputStream(lisFile);
			FileReader fr = new FileReader(lisFile);
			BufferedReader br = new BufferedReader(fr);
			String sLine = br.readLine();
			while (sLine != null) {
				SAPComponentApplication cpm = new SAPComponentApplication();
				String arr[] = sLine.trim().split("/");
				cpm.setVendor(arr[0]);
				cpm.setName(arr[1]);
				addSAPComponentApplication(cpm);
				sLine = br.readLine();
			}
			br.close();
			fr.close();
			fis.close();
		} catch (IOException e) {
			throw new BuildException(e);
		}

	}
	
	
	protected List<SAPComponentApplication> getComponents() {
		return components;
	}

	
}
