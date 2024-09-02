
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "municipio")
public class Municipio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_municipio")
	private Integer idMunicipio;

	@Column(name = "cd_municipio_ibge")
	private String cdMunicipioIbge;

	@Column(name = "nm_municipio")
	private String nmMunicipio;

	@Column(name = "cd_municipio_bacen")
	private String cdMunicipioBacen;

	@ManyToOne
	@JoinColumn(name = "id_uf")
	private Uf idUf;

	public String getNomeUFSigla() {
		return this.getNmMunicipio() + " - " + this.idUf.getNmSiglaUf();
	}

	public String getNomeUF() {
		return this.getNmMunicipio() + " - " + this.idUf.getNmUf();
	}
}
