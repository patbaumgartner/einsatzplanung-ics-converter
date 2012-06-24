package com.patbaumgartner.zbw.utils;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

import com.patbaumgartner.zbw.domain.Event;

public class XlsParserTest {

	String fileName = "ESP_Baumgartner_Patrick.xls";
	XlsParser parser;

	@Before
	public void setup() {
		parser = new XlsParser(fileName);
	}

	@Test
	public void testInitParser() throws IOException {
		Workbook wb = parser.initHSSFWorkbook();
		assertNotNull(wb);
	}

	@Test
	public void testCollectEvents() throws IOException {
		parser.initHSSFWorkbook();
		List<Event> events = parser.collectEvents();
		assertNotNull(events);
	}

}
