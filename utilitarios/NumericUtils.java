

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumericUtils {
	
	private NumericUtils() {
		throw new IllegalStateException("Classe utilitária para formatar números.");
	}

    public static String formataNumero(BigDecimal numero) {
        DecimalFormat decFormat = new java.text.DecimalFormat("R$ #,###,##0.00");
        return decFormat.format(numero);
    }
}
