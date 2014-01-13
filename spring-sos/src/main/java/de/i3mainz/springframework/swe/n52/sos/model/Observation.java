package de.i3mainz.springframework.swe.n52.sos.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Observation {

	private Date time;
	private Object value;

	public Observation(Date time, Object value) {
		super();
		this.time = time;
		this.value = value;
	}

	/**
	 * @return the time
	 */
	public final Date getTime() {
		return time;
	}
	
	public final String getTimeAsSTring() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		return dateFormat.format(getTime());
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public final void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the value
	 */
	public final Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public final void setValue(Object value) {
		this.value = value;
	}

}
