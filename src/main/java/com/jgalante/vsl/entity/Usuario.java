package com.jgalante.vsl.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Usuario extends BaseEntity {

	@Column(length = 25, unique = true, nullable = false)
	@Size(max = 25, message="{entidade.usuario.nome.tamanho}")
	@NotEmpty(message = "{entidade.usuario.nome.obrigatorio}")
	private String nome;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	private Set<Participante> participantes = new HashSet<Participante>(0);

	public Usuario() {
	}
	
	public Usuario(Long id) {
		setId(id);
	}

	public Usuario(String firstName) {
		this.nome = firstName;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<Participante> getParticipantes() {
		return participantes;
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
		lista.add("id");
		return lista;
	}

}
