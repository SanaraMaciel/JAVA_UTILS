
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.cpqd.pcri.apiadm.dto.ViaCepDTO;
import br.com.cpqd.pcri.apiadm.model.Municipio;
import br.com.cpqd.pcri.apiadm.service.ImovelService;
import br.com.cpqd.pcri.apiadm.service.MunicipioService;

@RestController
@RequestMapping("/viacep")
public class ViaCepController {

	@Autowired
	private MunicipioService municipioService;

	@Autowired
	private ImovelService imovelService;

	@Value("${viacepHost}")
	private String viacepHost;

	@GetMapping("/{cep}")
	public ResponseEntity<Map<String, Object>> findCep(@PathVariable String cep) {
		String url = viacepHost + cep;
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> map = new HashMap<>();
		try {
			ViaCepDTO resultado = restTemplate.getForObject(url, ViaCepDTO.class);
			if (resultado != null) {
				Municipio municipio = municipioService.findByNmMunicipio(resultado.getMunicipio());
				resultado.setIdMunicipio(municipio.getIdMunicipio());
				resultado.setIdUf(municipio.getIdUf().getIdUf());
				JsonNode coordinates = imovelService.getCoordinates(
						resultado.getLogradouro() + ' ' + resultado.getBairro() + ' ' + resultado.getMunicipio());
				resultado.setLatitude(coordinates.get("lat").asDouble());
				resultado.setLongitude(coordinates.get("lng").asDouble());
			}
			map.put("data", resultado);
			return ResponseEntity.ok().body(map);
		} catch (HttpClientErrorException e) {
			map.put("data", e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		}
	}
}
