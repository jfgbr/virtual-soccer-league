package com.jgalante.vsl.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_campeonato")
public class TipoCampeonato extends BaseEntity {

	@Column(name = "descricao", nullable = false, length = 50)
	private String descricao;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoCampeonato", cascade = CascadeType.ALL)
	private Set<Campeonato> campeonatos = new HashSet<Campeonato>(0);
	
	public static final TipoCampeonato MATA_MATA = new TipoCampeonato(1L,"Mata-Mata");
	public static final TipoCampeonato PONTOS_CORRIDOS = new TipoCampeonato(2L,"Pontos Corridos");
	public static final TipoCampeonato GRUPO = new TipoCampeonato(3L,"Grupo e Mata-Mata");
	
	public TipoCampeonato() {
	}
	
	public TipoCampeonato(Long id, String descricao) {
		setId(id);
		this.descricao = descricao;
	}
	
	public TipoCampeonato(TipoCampeonato tipoCampeonato) {
		setId(tipoCampeonato.getId());
		this.descricao = tipoCampeonato.getDescricao();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Set<Campeonato> getCampeonatos() {
		return campeonatos;
	}

	public void setCampeonatos(Set<Campeonato> campeonatos) {
		this.campeonatos = campeonatos;
	}

	@Override
	public String getTextoExibicao() {
		return getDescricao();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoCampeonato other = (TipoCampeonato) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public List<String> getOrderBy() {
		List<String> lista = new ArrayList<String>();
		lista.add("descricao");
		return lista;
	}

}
