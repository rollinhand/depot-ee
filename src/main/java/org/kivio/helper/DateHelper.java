package org.kivio.helper;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateHelper {
    public static DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static LocalDate fromValuation(String line) {
        LocalDate navDate = LocalDate.now();

        if (StringUtils.isNotEmpty(line)) {
            Matcher m = Pattern.compile(".*(\\d{2}\\.\\d{2}\\.\\d{4}).*").matcher(line);
            if (m.find()) {
                String sDate = m.group(1);
                navDate = LocalDate.parse(sDate, df);
            }
        }

        return navDate;
    }

    public static int monthsBetween(Date periodBegin, Date periodEnd) {
        return getPeriod(
                LocalDateTime.ofInstant(periodBegin.toInstant(), ZoneId.systemDefault()).toLocalDate(),
                LocalDateTime.ofInstant(periodEnd.toInstant(), ZoneId.systemDefault()).toLocalDate())
                .getMonths();
    }

    private static Period getPeriod(LocalDate periodBegin, LocalDate periodEnd) {
        return Period.between(periodBegin, periodEnd.plusMonths(1));    // periodEnd is exclusive
    }



    private DateHelper() {}
}
