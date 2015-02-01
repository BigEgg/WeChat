package com.thoughtworks.wechat_io.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static com.thoughtworks.wechat_io.util.DateTimeExtension.toUTCDateTime;
import static com.thoughtworks.wechat_io.util.DateTimeExtension.toUnixTimeStamp;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DateTimeExtensionTest {

    @Test
    public void testToUnixTimeStamp() throws Exception {
        int timeStamp = toUnixTimeStamp(new DateTime(2015, 2, 1, 14, 23, 43, DateTimeZone.UTC));
        assertThat(timeStamp, equalTo(1422800623));
    }

    @Test
    public void testToDateTime() throws Exception {
        DateTime dateTime = toUTCDateTime(1422800623);
        assertThat(dateTime.toString("yyyy-MM-dd HH:mm:ss"), equalTo("2015-02-01 14:23:43"));
    }
}