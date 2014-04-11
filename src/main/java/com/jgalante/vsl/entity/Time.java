package com.jgalante.vsl.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table
public class Time extends BaseEntity {

	@Column(name = "nome", nullable = false, length = 45)
	@Size(max = 45, message="{time.nome.tamanho}")
	@NotEmpty(message = "{time.nome.obrigatorio}")
	private String nome;

	@Column(name = "sigla", nullable = true, length = 3)
	@Size(max = 3, min = 3, message="{time.sigla.tamanho}")
	@NotEmpty(message = "{time.sigla.obrigatorio}")
	private String sigla;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPais", nullable = false)
	private Pais pais;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "time")
	private Set<Participante> participantes = new HashSet<Participante>(0);

	public Time() {
		this.pais = new Pais();
	}
	
	public Time(Long id) {
		setId(id);
	}

	public Time(Pais pais, String nome, String sigla) {
		this.pais = pais;
		this.nome = nome;
		this.sigla = sigla;
	}

	public Time(Pais pais, String nome, String sigla,
			Set<Participante> participantes) {
		this.pais = pais;
		this.nome = nome;
		this.sigla = sigla;
		this.participantes = participantes;
	}

	public Pais getPais() {
		return this.pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return this.sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Set<Participante> getParticipantes() {
		return this.participantes;
	}

	public void setParticipantes(Set<Participante> participantes) {
		this.participantes = participantes;
	}

	@Override
	public String getTextoExibicao() {
		return getNome();
	}

	@Override
	public List<String> getOrderBy() {
		List<String> lista = new ArrayList<String>();
		lista.add("nome");
		lista.add("pais.nome");
		lista.add("id");
		return lista;
	}

}
