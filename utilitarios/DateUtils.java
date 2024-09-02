
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

	private DateUtils() {
		throw new IllegalStateException("Classe de utilitário para datas");
	}

	public static String formatDate(LocalDateTime date, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return date.format(formatter);
	}

	public static String formatDate(LocalDate date, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return date.format(formatter);
	}

	public static String formatDate(Date date, String format) {
		Format formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static LocalDateTime formatDate(String date, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return LocalDateTime.parse(date, formatter);
	}

	public static LocalDateTime configuraTimestamp(LocalDateTime data) {
		LocalTime midnight = LocalTime.MIDNIGHT;
		LocalDate localDate = LocalDate.of(data.getYear(), data.getMonth(), data.getDayOfMonth());
		return LocalDateTime.of(localDate, midnight);
	}

	public static String formatDateLocalDate(LocalDate date, String format) {
		return date.format(DateTimeFormatter.ofPattern(format));		
	}

	public static String formatDataDocumento(String valor) {
		return DateUtils.formatDateLocalDate(LocalDate.parse(valor), "dd/MM/yyyy");
	}

	public static String formatHoraDocumento() {
		DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("HH:mm:ss");
		return dtf5.format(LocalDateTime.now());
	}

}
