
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.text.MaskFormatter;

public class DocumentoUtils {

	private DocumentoUtils() {
		throw new IllegalStateException("Classe utilitária para validar documentos.");
	}

	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static final int[] pesoCNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	public static boolean isValidDocument(String document) {
		if (isValidCnpj(document) || isValidCpf(document)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidCpf(String cpf) {
		return (validaCPf(cpf));
	}

	public static boolean isValidCnpj(String cnpj) {
		return (validaCnpj(cnpj));
	}

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1; indice >= 0; indice--) {
			int digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	private static String padLeft(String text, char character) {
		return String.format("%11s", text).replace(' ', character);
	}

	private static boolean validaCPf(String cpf) {
		cpf = cpf.trim().replace(".", "").replace("-", "");
		if ((cpf == null) || (cpf.length() != 11))
			return false;

		for (int j = 0; j < 10; j++)
			if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpf))
				return false;

		Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
		Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
		return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
	}

	private static boolean validaCnpj(String cnpj) {
		cnpj = cnpj.trim().replace(".", "").replace("/", "").replace("-", "");
		if ((cnpj == null) || (cnpj.length() != 14)) {
			return false;
		}

		Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
		Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
		return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
	}

	/**
	 * insere máscara de cpf e cnpj params String cpf/Cnpj return String
	 * 
	 * @throws ParseException
	 */
	public static String insereMascaraCpfCnpj(String cpfCnpj) throws ParseException {

		MaskFormatter maskCNPJ = new MaskFormatter("##.###.###/####-##");
		maskCNPJ.setValueContainsLiteralCharacters(false);

		MaskFormatter maskCPF = new MaskFormatter("###.###.###-##");
		maskCPF.setValueContainsLiteralCharacters(false);

		if (cpfCnpj != null && !cpfCnpj.isBlank() && cpfCnpj.length() == 14) {
			cpfCnpj = maskCNPJ.valueToString(cpfCnpj);
		} else if (cpfCnpj != null && !cpfCnpj.isBlank() && cpfCnpj.length() == 11) {
			cpfCnpj = maskCPF.valueToString(cpfCnpj);
		}

		return cpfCnpj;
	}

	/**
	 * formata um valor conforme moeda Brasileira R$
	 * 
	 * @param número a ser formatado bigDecimal
	 * @return número formatado no padrão moeda brasileira
	 */
	public static String formataNumeroMoeda(BigDecimal numero) {
		DecimalFormat decFormat = new java.text.DecimalFormat("R$ #,###,##0.00");
		return decFormat.format(numero);
	}

	/**
	 * remove máscara de moeda monetária e gera valor em BigDecimal
	 * 
	 * @param número a ser convertido em bigDecimal
	 * @return número bigDecimal convertido
	 */
	public static BigDecimal convertStringToBigDecimal(String valor) {
		String valorFormatado = valor.replace("R$", "").replace(".", "").replace(",", ".");
		return BigDecimal.valueOf(Double.valueOf(valorFormatado));
	}

	/**
	 * formata um valor conforme número em porcentagem
	 * 
	 * @param número a ser formatado bigDecimal
	 * @return número formatado em porcentagem
	 */
	public static String formataNumeroPorcentagem(String numero) {
		BigDecimal val = BigDecimal.valueOf(Double.valueOf(numero));
		DecimalFormat decFormat = new java.text.DecimalFormat("00.00");
		return decFormat.format(val);
	}

	/**
	 * formata um valor conforme número
	 * 
	 * @param número a ser formatado bigDecimal
	 * @return número formatado
	 */
	public static String formataNumeroValor(String numero) {
		BigDecimal val = BigDecimal.valueOf(Double.valueOf(numero));
		DecimalFormat decFormat = new java.text.DecimalFormat("0.#####");
		return decFormat.format(val);
	}

}
