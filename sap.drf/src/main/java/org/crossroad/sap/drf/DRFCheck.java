/**
 * 
 */
package org.crossroad.sap.drf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.crossroad.sap.drf.exception.CMDLineParserException;
import org.crossroad.sap.drf.exception.DRFCheckException;
import org.crossroad.sap.drf.impl.AbstractLogger;
import org.crossroad.sap.drf.impl.DRFConfigManager;
import org.crossroad.sap.drf.impl.DRFComparator;

/**
 * @author e.soden
 *
 */
public class DRFCheck extends AbstractLogger {
	private String checkId = null;
	private String infile = null;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		DRFCheck check = new DRFCheck();
		check.parseCMDLine(args);

	}

	protected void parseCMDLine(String[] args) throws CMDLineParserException {
		log.info("Parsing command line");
		try {
			CommandLineParser parser = new DefaultParser();

			Options options = new Options();

			options.addOption("d", "directory", true, "Specify Home directory.");
			options.addOption("i", "input", true, "Input file containing the data.");
			options.addOption("c", "check-id", true, "Check id defines within the configuration file.");
			options.addOption("h", "help", false, "Display current help");

			// Instantiate the helpformatter
			HelpFormatter formatter = new HelpFormatter();

			// Parse the command line
			CommandLine cmdLine = parser.parse(options, args);

			if (cmdLine.hasOption('h')) {
				formatter.printHelp("DRF Check", options);
				System.exit(0);
			}
			if (cmdLine.hasOption('d')) {

				if (cmdLine.hasOption('c'))
					checkId = cmdLine.getOptionValue('c');

				if (cmdLine.hasOption('i')) {
					infile = cmdLine.getOptionValue('i');
				}

				/*
				 * Load configuration files
				 */
				DRFConfigManager.getInstance().load(cmdLine.getOptionValue('d'));

				doCompare(checkId, infile);

			} else {
				throw new CMDLineParserException("Home directory should be set");
			}

		} catch (CMDLineParserException c) {
			log.error(c);
			throw c;
		} catch (Exception e) {
			log.error(e);
			throw new CMDLineParserException(e);
		}
	}

	private void doCompare(String id, String inputFile) throws Exception {
		List<String> matnrList = new ArrayList<String>();

		BufferedReader reader = null;

		try {
			

			if (inputFile != null) {
				log.info("Read input file");
				reader = new BufferedReader(new FileReader(inputFile));
				String line = null;
				while ((line = reader.readLine()) != null) {
					matnrList.add(line);
				}
			} else {
				log.warn("!!!WARNING!!! you are checking complete SAP ECC content against SAP CAR this could lead to performance issues.");
			}
			DRFComparator comparator = new DRFComparator(id);
			comparator.compare(matnrList);

			// Write files
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("YYYYMMDD-HHmmss");
			String dtFile = format.format(new Date());

			writeToFile(DRFConfigManager.getInstance().getOutDirectory() + File.separator + dtFile + "-car.txt",
					comparator.getCarList());
			writeToFile(DRFConfigManager.getInstance().getOutDirectory() + File.separator + dtFile + "-missing.txt",
					comparator.getMissingArticles());
			writeToFile(DRFConfigManager.getInstance().getOutDirectory() + File.separator + dtFile + "-sql.txt",
					comparator.getSqlOrders());
			
			String tplSQL = DRFConfigManager.getInstance().getSQL(id, DRFConfigManager.SOURCE, "insert.d_art");

			if (tplSQL != null) {
				List<String> dartSQL = new ArrayList<String>();
				for (String article : comparator.getMissingArticles()) {
					if (!comparator.getDartArticles().contains(article)) {
						dartSQL.add(tplSQL.replace("_ARTICLE_CODE_", article));
					}
				}

				writeToFile(DRFConfigManager.getInstance().getOutDirectory() + File.separator + dtFile + "-dart.sql",
						dartSQL);
				writeToFile(DRFConfigManager.getInstance().getOutDirectory() + File.separator + dtFile + "-dart.txt",
						comparator.getDartArticles());
			}

		} catch (Exception e) {
			throw new DRFCheckException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error("Error while closing " + inputFile, e);
				}
			}
		}

	}

	private void writeToFile(String file, List<String> content) throws Exception {
		BufferedWriter writer = null;
		try {

			log.info("Creating file [" + file + "]");

			writer = new BufferedWriter(new FileWriter(file));

			for (String line : content) {
				writer.write(line + "\n");
				writer.flush();
			}
		} finally {
			writer.close();
		}

	}

}
