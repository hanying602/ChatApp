package com.lhy.pku.chatapp.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static String formatDateToString(Date date) {
        String sDate;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        sDate = sd.format(date);
        return sDate;
    }
}
