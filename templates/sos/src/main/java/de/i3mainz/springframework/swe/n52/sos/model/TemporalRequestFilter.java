/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.model;

import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.valueDomains.time.TimeResolution;

/**
 * @author Nikolai Bock
 *
 */
public class TemporalRequestFilter {

    private static final DateFormat df = TimeFactory.ISO8601LocalFormat;

    private Date timestamp;
    private Date begin;
    private Date end;
    private String timeResolution;

    public TemporalRequestFilter(Date timestamp) {
        setTimestamp(timestamp);
    }

    public TemporalRequestFilter(Date begin, Date end) {
        setBegin(begin);
        setEnd(end);
    }

    public TemporalRequestFilter(Date begin, Date end, String timeResolution) {
        this(begin, end);
        setTimeResolution(timeResolution);
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the begin
     */
    public Date getBegin() {
        return begin;
    }

    /**
     * @param begin
     *            the begin to set
     */
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end
     *            the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * @return the timeResolution
     */
    public String getTimeResolution() {
        return timeResolution;
    }

    /**
     * @param timeResolution
     *            the timeResolution to set
     */
    public final void setTimeResolution(String timeResolution) {
        if (Pattern.matches(TimeResolution.RESOLUTION_PATTERN, timeResolution)) {
            this.timeResolution = timeResolution;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.begin != null && this.end != null) {
            builder.append(df.format(begin)).append("/").append(df.format(end));
            if (this.timeResolution != null && !this.timeResolution.isEmpty()) {
                builder.append("/").append(this.timeResolution);
            }
        } else if (this.timestamp != null) {
            builder.append(df.format(timestamp));
        } else {
            return null;
        }
        return builder.toString();
    }

}
