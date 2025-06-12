package br.com.cpqd.tecsw.agos.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CicloPeriodoValidator.class)
@Target({ElementType.RECORD_COMPONENT, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CicloPeriodoValido {
    String message() default "O campo ciclo_fim não pode ser menor que o campo ciclo_inic.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
