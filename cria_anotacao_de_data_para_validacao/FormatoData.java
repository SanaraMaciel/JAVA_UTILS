package br.com.cpqd.tecsw.agos.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FormatoDataValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FormatoData {
    String message() default "A data deve estar no formato DD/MM/AAAA";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}