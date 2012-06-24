package com.patbaumgartner.zbw;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.patbaumgartner.zbw.domain.Event;
import com.patbaumgartner.zbw.utils.IcsWriter;
import com.patbaumgartner.zbw.utils.XlsParser;

public class ZbWEinsatzplanungIcsConverter {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			Option help = new Option("help", "print this message");
			Option file = OptionBuilder.withArgName("file").hasArg().withDescription("use a given Excel file for parsing").create("file");

			Options options = new Options();
			options.addOption(help);
			options.addOption(file);

			CommandLineParser parser = new PosixParser();
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("file")) {
				// read events from Excel file
				XlsParser xlsParser = new XlsParser(line.getOptionValue("file"));
				xlsParser.initHSSFWorkbook();
				List<Event> events = xlsParser.collectEvents();

				// write events as iCal file
				IcsWriter writer = new IcsWriter();
				writer.addEvents(events);
				writer.exportCalendar();
			}
			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ZbW Einsatzplanung ICS Converter", options );
			}

		} catch (Exception e) {
			System.err.println("Oops, something went wrong.  Reason: " + e.getMessage());
		}
	}

}
