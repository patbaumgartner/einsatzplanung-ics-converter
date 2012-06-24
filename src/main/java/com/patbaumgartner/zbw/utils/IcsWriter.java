package com.patbaumgartner.zbw.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import org.apache.commons.io.FileUtils;

import com.patbaumgartner.zbw.domain.Event;

public class IcsWriter {

	Calendar calendar = null;

	public IcsWriter() {
		calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public File exportCalendar() throws IOException, ValidationException {
		File file = FileUtils.getFile("zbw-esp-calendar.ics");
		FileOutputStream fout = new FileOutputStream(file);
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, fout);
		return file;
	}

	public void addEvents(List<Event> events) throws SocketException {
		for (Event event : events) {
			addEventEntry(event);
		}
	}

	public void addEventEntry(Event event) throws SocketException {
		// Create a TimeZone
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("Europe/Zurich");
		VTimeZone tz = timezone.getVTimeZone();

		// Start Date is on: April 1, 2008, 9:00 am
		java.util.Calendar startDate = event.getStartDate();
		startDate.setTimeZone(timezone);

		// End Date is on: April 1, 2008, 13:00
		java.util.Calendar endDate = event.getEndDate();
		endDate.setTimeZone(timezone);

		// Create the event
		String eventName = "[ZbW] " + event.getCourse() + " - " + event.getTitle();
		DateTime start = new DateTime(startDate.getTime());
		DateTime end = new DateTime(endDate.getTime());
		VEvent meeting = new VEvent(start, end, eventName);

		// add timezone info..
		meeting.getProperties().add(tz.getTimeZoneId());
		meeting.getProperties().add(new Location("ZbW, Zimmer " + event.getRoom()));
		meeting.getProperties().add(new Description(event.getTitle()));
		
		// generate unique identifier..
		UidGenerator ug = new UidGenerator("uidGen");
		Uid uid = ug.generateUid();
		meeting.getProperties().add(uid);

		// Add the event
		calendar.getComponents().add(meeting);
	}

}
