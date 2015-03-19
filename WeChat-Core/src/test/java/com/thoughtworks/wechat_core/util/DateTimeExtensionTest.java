package com.thoughtworks.wechat_core.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.sql.Timestamp;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DateTimeExtensionTest {
    @Test
    public void testToUnixTimeStampInt() throws Exception {
        final int millis = toUnixTimestampInt(new DateTime(2015, 2, 1, 14, 23, 43, DateTimeZone.UTC));
        assertThat(millis, equalTo(1422800623));
    }

    @Test
    public void testToUnixTimeStamp() throws Exception {
        final Timestamp timeStamp = toUnixTimestamp(new DateTime(2015, 2, 1, 14, 23, 43, DateTimeZone.UTC));
        assertThat(timeStamp.getTime(), equalTo(1422800623000L));
    }

    @Test
    public void testToDateTime_Int() throws Exception {
        final DateTime dateTime = toUTCDateTime(1422800623);
        assertThat(dateTime.toString("yyyy-MM-dd HH:mm:ss"), equalTo("2015-02-01 14:23:43"));
    }

    @Test
    public void testToDateTime_Long() throws Exception {
        final DateTime dateTime = toUTCDateTime(1422800623000L);
        assertThat(dateTime.toString("yyyy-MM-dd HH:mm:ss"), equalTo("2015-02-01 14:23:43"));
    }
}