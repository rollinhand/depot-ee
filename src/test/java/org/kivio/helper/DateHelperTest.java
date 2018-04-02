package org.kivio.helper;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DateHelperTest {
    final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Date periodBegin;
    private Date periodEnd;

    @Before
    public void setUp() throws ParseException {
        // Vier Monate
        periodBegin = DATE_FORMAT.parse("2017-11-12");
        periodEnd = DATE_FORMAT.parse("2018-03-02");
    }

    @Test
    public void testMonthsBetween() {
        assertThat(DateHelper.monthsBetween(periodBegin, periodEnd), is(4));
    }
}
