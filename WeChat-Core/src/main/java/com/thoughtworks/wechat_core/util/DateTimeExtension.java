package com.thoughtworks.wechat_core.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public final class DateTimeExtension {
    public static int toUnixTimeStamp(DateTime dateTime) {
        return (int) (dateTime.toDateTime(DateTimeZone.UTC).getMillis() / 1000);
    }

    public static DateTime toUTCDateTime(int timeStamp) {
        return new DateTime(timeStamp * 1000L).toDateTime(DateTimeZone.UTC);
    }
}
