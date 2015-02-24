package com.thoughtworks.wechat_core.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;

public final class DateTimeExtension {
    public static int toUnixTimestampInt(DateTime dateTime) {
        return (int) (dateTime.toDateTime(DateTimeZone.UTC).getMillis() / 1000);
    }

    public static Timestamp toUnixTimestamp(DateTime dateTime) {
        return new Timestamp(dateTime.toDateTime(DateTimeZone.UTC).getMillis());
    }

    public static DateTime toUTCDateTime(int timeStamp) {
        return new DateTime(timeStamp * 1000L).toDateTime(DateTimeZone.UTC);
    }

    public static DateTime toUTCDateTime(long timeStamp) {
        return new DateTime(timeStamp).toDateTime(DateTimeZone.UTC);
    }
}
