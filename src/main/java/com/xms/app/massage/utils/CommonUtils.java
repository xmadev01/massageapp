package com.xms.app.massage.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CommonUtils {

    public static String formatCurrencyData(BigDecimal amount) {
        NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(Locale.US);
        return dollarFormat.format(amount);
    }
}
