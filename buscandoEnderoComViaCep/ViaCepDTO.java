
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ViaCepDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cep;

	private String logradouro;

	private String complemento;

	private String bairro;

	private String municipio;

	private String uf;
	
	private Integer idMunicipio;
	
	private Integer idUf;
	
	private Double latitude;
	
	private Double longitude;	

}
