package br.com.sanara.integration.viacep.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "Endereco", description = "Modelo de entidade responsável pela estrutura de dados para o Endereço obtido em uma fonte externa, no caso o ViaCEP, para utilização no fluxo principal da aplicação")
public class Endereco implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("cep")
    @ApiModelProperty(name = "cep", value = "Indicador do valor correspondente ao Código de Endereçamento Postal", example = "01001-000")
    private String cep;

    @JsonProperty("logradouro")
    @ApiModelProperty(name = "logradouro", value = "Indicador do valor correspondente ao nome do Logradouro", example = "Praça da Sé")
    private String logradouro;

    @JsonProperty("complemento")
    @ApiModelProperty(name = "complemento", value = "Indicador do valor correspondente ao Complemento do Logradouro", example = "lado impar")
    private String complemento;

    @JsonProperty("bairro")
    @ApiModelProperty(name = "bairro", value = "Indicador do valor correspondente ao Bairro onde se encontra o endereço", example = "Sé")
    private String bairro;

    @JsonProperty("municipio")
    @ApiModelProperty(name = "municipio", value = "Indicador do valor correspondente ao Munícipio onde se encontra o endereço", example = "São Paulo")
    private String localidade;

    @JsonProperty("uf")
    @ApiModelProperty(name = "uf", value = "Indicador do valor correspondente ao Estado (Unidade Federativa) onde se encontra o endereço", example = "SP")
    private String uf;

}
