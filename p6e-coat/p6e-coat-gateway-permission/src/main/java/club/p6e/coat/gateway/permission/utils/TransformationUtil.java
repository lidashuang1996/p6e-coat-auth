package club.p6e.coat.gateway.permission.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 转换帮助类
 *
 * @author lidashuang
 * @version 1.0
 */
public final class TransformationUtil {

    public static Integer objectToInteger(Object o) {
        if (o == null) {
            return null;
        } else {
            return Double.valueOf(String.valueOf(o)).intValue();
        }
    }

    public static Long objectToLong(Object o) {
        if (o == null) {
            return null;
        } else {
            return Double.valueOf(String.valueOf(o)).longValue();
        }
    }

    public static String objectToString(Object o) {
        if (o == null) {
            return null;
        } else {
            return String.valueOf(o);
        }
    }

    public static LocalDateTime objectToLocalDateTime(Object o) {
        if (o instanceof Timestamp) {
            return ((Timestamp) o).toLocalDateTime();
        } else {
            return null;
        }
    }

    public static LocalDate objectToLocalDate(Object o) {
        if (o instanceof Date) {
            return ((Date) o).toLocalDate();
        } else {
            return null;
        }
    }

    public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return timestamp.toLocalDateTime();
        }
    }

    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.toLocalDate();
        }
    }
}