package br.com.sanara.integration.viacep.utils;

import java.util.Objects;

import br.com.sanara.integration.viacep.exception.ViaCepException;

public final class EnderecoUtils {
	
	private EnderecoUtils() {}

    public static void validaCep(String cep){
        if (Objects.isNull(cep) || cep.isEmpty() || cep.isBlank())
            throw new ViaCepException("Cep informado está nulo ou vazio.");
        if (cep.length() > 9)
            throw new ViaCepException("CEP não está formatado.");
        if (cep.length() < 8)
            throw new ViaCepException("CEP faltando números.");
    }

}
