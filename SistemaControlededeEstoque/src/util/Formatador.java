package util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Formatador {

    public static String moeda(BigDecimal valor) {

        if (valor == null) {
            valor = BigDecimal.ZERO;
        }

        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formato.format(valor);
    }
}
