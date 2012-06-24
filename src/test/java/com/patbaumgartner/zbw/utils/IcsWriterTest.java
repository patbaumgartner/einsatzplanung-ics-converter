package com.patbaumgartner.zbw.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.patbaumgartner.zbw.domain.Event;

public class IcsWriterTest {

	IcsWriter writer;

	@Before
	public void setup() {
		writer = new IcsWriter();
	}

	@Test
	public void testCreateCalendar() {
		Calendar calendar = writer.getCalendar();
		assertNotNull(calendar);
	}

	@Test
	public void testExportCalendarWithoutComponent() throws IOException {
		File file = null;
		try {
			file = writer.exportCalendar();
		} catch (ValidationException e) {
		}
		assertNull(file);
	}

	@Test
	public void testExportCalendarWithComponent() throws IOException, ValidationException {
		Event event = new Event();
		event.setDate(java.util.Calendar.getInstance());
		event.setStartDate(java.util.Calendar.getInstance());
		event.setEndDate(java.util.Calendar.getInstance());
		event.setCourse("course");
		event.setTitle("title");
		event.setRoom("room");
		writer.addEventEntry(event);
		File file = writer.exportCalendar();
		assertNotNull(file);
		assertTrue(FileUtils.sizeOf(file) > 0);
	}
}
