package org.crossroad.sap.drf.impl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.crossroad.sap.drf.exception.CompareException;

public class DRFComparator extends AbstractLogger {

	private String id = null;
	private List<String> maraList = new ArrayList<String>();
	private List<String> carList = new ArrayList<String>();
	private List<String> missingArticles = new ArrayList<String>();
	private List<String> dartArticles = new ArrayList<String>();
	private List<String> sqlOrders = new ArrayList<String>();

	public DRFComparator(String id) {
		this.id = id;
	}

	public void compare(List<String> articles) throws CompareException {
		try {
			listInMara(articles);

			if (!maraList.isEmpty()) {
				checkInCar();
			}

			log.info("Comparing SAP ECC and SAP CAR result");

			StringBuffer buffer = new StringBuffer();
			buffer.append("\nInput\n");
			buffer.append("\t- File #" + articles.size() + " articles\n");
			buffer.append("SAP ECC\n");
			buffer.append("\t- MARA #" + maraList.size() + " articles\n");
			buffer.append("\t- D_ART #" + dartArticles.size() + " articles\n");
			buffer.append("SAP CAR\n");
			buffer.append("\t- /DMF/PROD_EXT_XR #" + carList.size() + " articles\n");
			log.info(buffer.toString());
			
			maraList.removeAll(carList);
			missingArticles.addAll(maraList);

			
			log.info("Missing articles #" + missingArticles.size() + "\n");

			
		} catch (Exception e) {
			throw new CompareException(e);
		}
	}

	public List<String> getDartArticles() {
		return dartArticles;
	}

	/**
	 * Check article in MARA
	 * 
	 * @param articles
	 * @throws Exception
	 */
	private void listInMara(List<String> articles) throws Exception {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rsData = null;

		try {
			log.info("Checking data in SAP ECC (MARA)");
			String url = CheckManagerConfig.getInstance().getUrl(id, CheckManagerConfig.SOURCE);
			String username = CheckManagerConfig.getInstance().getUser(id, CheckManagerConfig.SOURCE);
			String passwd = CheckManagerConfig.getInstance().getPassword(id, CheckManagerConfig.SOURCE);
			String prefixSQL = CheckManagerConfig.getInstance().getSQL(id, CheckManagerConfig.SOURCE, "mara");

			StringBuffer suffixSQL = new StringBuffer();

			for (String article : articles) {
				if (suffixSQL.length() > 0) {
					suffixSQL.append(" OR ");
				} else {
					suffixSQL.append(" AND ");
				}
				suffixSQL.append(" MATNR LIKE '" + article + "%'");
			}
			String query = prefixSQL + suffixSQL.toString();
			sqlOrders.add(query);

			Driver driver = (Driver) com.sap.db.jdbc.Driver.class.newInstance();
			Properties props = new Properties();
			props.setProperty("user", username);
			props.setProperty("password", passwd);
			connection = driver.connect(url, props);

			pstmt = connection.prepareStatement(query);

			rsData = pstmt.executeQuery();

			while (rsData.next()) {
				maraList.add(rsData.getString("MATNR"));
			}

			rsData.close();
			pstmt.close();

			log.info("Checking data in SAP ECC (DMF_D_ART)");
			query = CheckManagerConfig.getInstance().getSQL(id, CheckManagerConfig.SOURCE, "select.d_art") + suffixSQL;
			sqlOrders.add(query);
			pstmt = connection.prepareStatement(query);

			rsData = pstmt.executeQuery();

			while (rsData.next()) {
				dartArticles.add(rsData.getString("MATNR"));
			}

		} finally {
			if (rsData != null) {
				try {
					rsData.close();
				} catch (SQLException e) {
					log.error("Unable to close resultSet", e);
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					log.error("Unable to close prepareStatement", e);
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					log.error("Unable to close connection", e);
				}
			}
		}

	}

	private void checkInCar() throws Exception {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rsData = null;

		try {
			log.info("Checking data in SAP CAR (/DMF/PROD_EXT_XR)");
			String url = CheckManagerConfig.getInstance().getUrl(id, CheckManagerConfig.DESTINATION);
			String username = CheckManagerConfig.getInstance().getUser(id, CheckManagerConfig.DESTINATION);
			String passwd = CheckManagerConfig.getInstance().getPassword(id, CheckManagerConfig.DESTINATION);
			String prefixSQL = CheckManagerConfig.getInstance().getSQL(id, CheckManagerConfig.DESTINATION, "dmf");
			StringBuffer suffixSQL = new StringBuffer();

			for (String article : maraList) {
				if (suffixSQL.length() > 0) {
					suffixSQL.append(" OR ");
				} else {
					suffixSQL.append(" AND ");
				}
				suffixSQL.append(" EXT_PROD_ID LIKE '" + article + "%'");
			}

			String query = prefixSQL + suffixSQL.toString();
			sqlOrders.add(query);
			Driver driver = (Driver) com.sap.db.jdbc.Driver.class.newInstance();
			Properties props = new Properties();
			props.setProperty("user", username);
			props.setProperty("password", passwd);
			connection = driver.connect(url, props);

			pstmt = connection.prepareStatement(query);

			rsData = pstmt.executeQuery();

			while (rsData.next()) {
				carList.add(rsData.getString("EXT_PROD_ID"));
			}

		} finally {
			if (rsData != null) {
				try {
					rsData.close();
				} catch (SQLException e) {
					log.error("Unable to close resultSet", e);
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					log.error("Unable to close prepareStatement", e);
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					log.error("Unable to close connection", e);
				}
			}
		}

	}

	public List<String> getCarList() {
		return carList;
	}

	public List<String> getMissingArticles() {
		return missingArticles;
	}

	public List<String> getSqlOrders() {
		return sqlOrders;
	}

}
