

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Classe utilitária responsável por escrever valores monetários por extenso.
 */
public class DinheiroUtils {

	private ArrayList<Integer> nro;
	private BigInteger num;

	private String[][] qualificadores = { { "Centavo", "Centavos" }, { "", "" }, { "Mil", "Mil" },
			{ "Milhão", "Milhões" }, { "Bilhão", "Bilhões" }, { "Trilhão", "Trilhões" },
			{ "Quatrilhão", "Quatrilhões" }, { "Quintilhão", "Quintilhões" }, { "Sextilhão", "Sextilhões" },
			{ "Septilhão", "Septilhões" } };
	private String[][] numeros = {
			{ "Zero", "Um", "Dois", "Três", "Quatro", "Cinco", "Seis", "Sete", "Oito", "Nove", "Dez", "Onze", "Doze",
					"Treze", "Quatorze", "Quinze", "Desesseis", "Desessete", "Dezoito", "Desenove" },
			{ "Vinte", "Trinta", "Quarenta", "Cinquenta", "Sessenta", "Setenta", "Oitenta", "Noventa" },
			{ "Cem", "Cento", "Duzentos", "Trezentos", "Quatrocentos", "Quinhentos", "Seiscentos", "Setecentos",
					"Oitocentos", "Novecentos" } };

	public DinheiroUtils() {
		nro = new ArrayList<>();
	}

	public DinheiroUtils(BigDecimal dec) {
		this();
		setNumber(dec);
	}

	@SuppressWarnings("deprecation")
	public void setNumber(BigDecimal dec) {
		// Converte para inteiro arredondando os centavos
		num = dec.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).toBigInteger();
		// Adiciona valores
		nro.clear();
		if (num.equals(BigInteger.ZERO)) {
			// Centavos
			nro.add((0));
			// Valor
			nro.add((0));
		} else {
			// Adiciona centavos
			addRemainder(100);
			// Adiciona grupos de 1000
			while (!num.equals(BigInteger.ZERO)) {
				addRemainder(1000);
			}
		}
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		int ct;

		for (ct = nro.size() - 1; ct > 0; ct--) {
			// Se ja existe texto e o atual não é zero
			if (buf.length() > 0 && !ehGrupoZero(ct)) {
				buf.append(" e ");
			}
			buf.append(numToString((nro.get(ct)).intValue(), ct));
		}
		if (buf.length() > 0) {
			insereGrupoeValor(buf);
		}
		if ((nro.get(0)).intValue() != 0) {
			buf.append(numToString((nro.get(0)).intValue(), 0));
		}
		return buf.toString();
	}

	private boolean ehPrimeiroGrupoUm() {
		return (nro.get(nro.size() - 1)).intValue() == 1;
	}

	private void addRemainder(int divisor) {
		// Encontra newNum[0] = num modulo divisor, newNum[1] = num dividido divisor
		BigInteger[] newNum = num.divideAndRemainder(BigInteger.valueOf(divisor));
		// Adiciona modulo
		nro.add(newNum[1].intValue());
		// Altera numero
		num = newNum[0];
	}

	private boolean ehUnicoGrupo() {
		if (nro.size() <= 3)
			return false;
		if (!ehGrupoZero(1) && !ehGrupoZero(2))
			return false;
		boolean hasOne = false;
		for (int i = 3; i < nro.size(); i++) {
			if ((nro.get(i)).intValue() != 0) {
				if (hasOne)
					return false;
				hasOne = true;
			}
		}
		return true;
	}

	boolean ehGrupoZero(int ps) {
		if (ps == nro.size())
			return true;
		return (nro.get(ps)).intValue() == 0;
	}

	private String numToString(int numero, int escala) {
		int unidade = (numero % 10);
		int dezena = (numero % 100); // * nao pode dividir por 10 pois verifica de 0..19
		int centena = (numero / 100);
		StringBuilder buf = new StringBuilder();

		if (numero != 0) {
			if (centena != 0) {
				adicionaCentena(dezena, centena, buf);
			}
			if ((buf.length() > 0) && (dezena != 0)) {
				buf.append(" e ");
			}
			if (dezena > 19) {
				dezena /= 10;
				buf.append(numeros[1][dezena - 2]);
				if (unidade != 0) {
					buf.append(" e ");
					buf.append(numeros[0][unidade]);
				}
			} else if (centena == 0 || dezena != 0) {
				buf.append(numeros[0][dezena]);
			}

			adicionaQualificadores(escala, numero, buf);

		}
		return buf.toString();
	}

	private StringBuilder adicionaCentena(int dezena, int centena, StringBuilder buf) {
		if (dezena == 0 && centena == 1) {
			buf.append(numeros[2][0]);
		} else {
			buf.append(numeros[2][centena]);
		}
		return buf;
	}

	private StringBuilder adicionaQualificadores(int escala, int numero, StringBuilder buf) {
		buf.append(" ");
		if (numero == 1) {
			buf.append(qualificadores[escala][0]);
		} else {
			buf.append(qualificadores[escala][1]);
		}
		return buf;
	}

	private StringBuilder insereGrupoeValor(StringBuilder buf) {
		if (ehUnicoGrupo())
			buf.append(" de ");
		while (buf.toString().endsWith(" "))
			buf.setLength(buf.length() - 1);
		if (ehPrimeiroGrupoUm())
			buf.insert(0, "");
		if (nro.size() == 2 && (nro.get(1)).intValue() == 1) {
			buf.append(" real");
		} else {
			buf.append(" reais");
		}
		if ((nro.get(0)).intValue() != 0) {
			buf.append(" e ");
		}
		return buf;
	}

}
