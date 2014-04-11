package com.jgalante.vsl.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table
public class Pais extends BaseEntity {

	@Column(name = "nome", nullable = false, length = 45)
	@Size(max = 45, message="{time.nome.tamanho}")
	@NotEmpty(message = "{time.nome.obrigatorio}")
	private String nome;
	
	@Column(name = "sigla", nullable = false, length = 3)
	@Size(max = 3, min = 3, message="{time.sigla.tamanho}")
	@NotEmpty(message = "{time.sigla.obrigatorio}")
	private String sigla;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pais")
	private Set<Time> times = new HashSet<Time>(0);

	public Pais() {
	}

	public Pais(String nome, String sigla) {
		this.nome = nome;
		this.sigla = sigla;
	}

	public Pais(String nome, String sigla, Set<Time> times) {
		this.nome = nome;
		this.sigla = sigla;
		this.times = times;
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

	public Set<Time> getTimes() {
		return this.times;
	}

	public void setTimes(Set<Time> times) {
		this.times = times;
	}

	@Override
	public String getTextoExibicao() {
		return getNome();
	}

	@Override
	public List<String> getOrderBy() {
		List<String> lista = new ArrayList<String>();
		lista.add("nome");
		lista.add("id");
		return lista;
	}

}
