package br.com.cpqd.tecsw.agos.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FormatoDataValidator implements ConstraintValidator<FormatoData, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        try {
            LocalDate.parse(value, FORMATTER);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
