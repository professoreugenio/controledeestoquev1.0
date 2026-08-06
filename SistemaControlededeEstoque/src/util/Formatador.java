package util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Formatador {

    private static final Locale LOCALE_BRASIL =
            Locale.forLanguageTag("pt-BR");

    public static String moeda(BigDecimal valor) {

        if (valor == null) {
            valor = BigDecimal.ZERO;
        }

        NumberFormat formato =
                NumberFormat.getCurrencyInstance(LOCALE_BRASIL);

        return formato.format(valor);
    }
}