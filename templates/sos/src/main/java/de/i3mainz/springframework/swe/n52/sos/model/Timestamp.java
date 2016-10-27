/**
 * 
 */
package de.i3mainz.springframework.swe.n52.sos.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Nikolai Bock
 *
 */
public class Timestamp extends Date {

    private static final long serialVersionUID = 1L;

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX':00'");

    @Override
    public String toString() {
        return df.format(this);
    }

    public Timestamp setDate(Date date) {
        super.setTime(date.getTime());
        return this;
    }
}
