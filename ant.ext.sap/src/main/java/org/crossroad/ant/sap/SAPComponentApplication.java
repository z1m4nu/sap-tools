/**
 * 
 */
package org.crossroad.ant.sap;

/**
 * @author e.soden
 *
 */
public class SAPComponentApplication {
	private String vendor = null;
	private String name = null;

	/**
	 * 
	 */
	public SAPComponentApplication() {
		// TODO Auto-generated constructor stub
	}
	
	public void setApplication(String app)
	{
		String arr[] = app.split("/");
		setVendor(arr[0]);
		setName(arr[1]);
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String component) {
		this.name = component;
	}

	public boolean isValid() {
		return (name != null && vendor != null);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Vendor name: '" + this.vendor + "', Component name '" + this.name + "'.";
	}

}
