/**
 * 
 */
package org.crossroad.sap.drf;

/**
 * @author e.soden
 *
 */
public class T {

	/**
	 * 
	 */
	public T() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String a="insert into SAPTWP.DMF_D_ART values ('100', 'DDF','_ARTICLE_CODE_','','X','X','X','','','');";
		
		System.out.println(a.replace("_ARTICLE_CODE_", "test\\1"));
	}
}
