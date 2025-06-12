package br.com.cpqd.tecsw.agos.utils;

import br.com.cpqd.tecsw.agos.domain.Semente;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CicloPeriodoValidator implements ConstraintValidator<CicloPeriodoValido, Semente> {

    @Override
    public boolean isValid(Semente value, ConstraintValidatorContext context) {
        if (value == null || value.cicloInic() == null || value.cicloFim() == null) {
            return true;
        }

        try {
            int inicio = Integer.parseInt(value.cicloInic());
            int fim = Integer.parseInt(value.cicloFim());

            return fim >= inicio;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
