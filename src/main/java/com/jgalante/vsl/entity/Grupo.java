package com.jgalante.vsl.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class Grupo extends BaseEntity {

	public static Grupo GRUPO_A = new Grupo(1L, "A");
	public static Grupo GRUPO_B = new Grupo(2L, "B");
	public static Grupo GRUPO_C = new Grupo(3L, "C");
	public static Grupo GRUPO_D = new Grupo(4L, "D");
	public static Grupo GRUPO_E = new Grupo(5L, "E");
	public static Grupo GRUPO_F = new Grupo(6L, "F");
	public static Grupo GRUPO_G = new Grupo(7L, "G");
	public static Grupo GRUPO_H = new Grupo(8L, "H");
	public static Grupo GRUPO_1 = new Grupo(9L, "1");
	public static Grupo GRUPO_2 = new Grupo(10L, "2");
	public static Grupo GRUPO_3 = new Grupo(11L, "3");
	public static Grupo GRUPO_4 = new Grupo(12L, "4");
	public static Grupo GRUPO_5 = new Grupo(13L, "5");
	public static Grupo GRUPO_6 = new Grupo(14L, "6");
	public static Grupo GRUPO_7 = new Grupo(15L, "7");
	public static Grupo GRUPO_8 = new Grupo(16L, "8");

	@Column(name = "descricao", nullable = false, length = 45)
	private String descricao;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grupo")
	private Set<Participante> participantes = new HashSet<Participante>();

	@Transient
	private List<Participante> lstParticipantes;

	public Grupo() {
	}

	public Grupo(Long idGrupo) {
		setId(idGrupo);
	}
	
	public Grupo(Long idGrupo, String descricao) {
		setId(idGrupo);
		this.descricao = descricao;
	}

	public Grupo(String descricao) {
		this.descricao = descricao;
	}

	public Grupo(String descricao, Set<Participante> participantes) {
		this.descricao = descricao;
		this.participantes = participantes;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Set<Participante> getParticipantes() {
		return this.participantes;
	}
	
	public void setParticipantes(Set<Participante> participantes) {
		this.participantes = participantes;
	}

	@SuppressWarnings("unchecked")
	public List<Participante> getLstParticipantes(){
		if (lstParticipantes == null || lstParticipantes.isEmpty()){
			lstParticipantes = new ArrayList<Participante>(this.participantes);
			Collections.sort(lstParticipantes, new ParticipanteComparator());
		}
		return lstParticipantes;
	}
	
	@SuppressWarnings("rawtypes")
	static class ParticipanteComparator implements Comparator
	{		
		public int compare(Object o1, Object o2) {
			Participante p1 = (Participante) o1;
			Participante p2 = (Participante) o2;
			int result = p2.getPontos().compareTo(p1.getPontos());
			if (result == 0)
				result = p2.getVitorias().compareTo(p1.getVitorias());
			if (result == 0)
				result = p2.getSaldoGols().compareTo(p1.getSaldoGols());
			if (result == 0)
				result = p2.getGolsPro().compareTo(p1.getGolsPro());
			return result;
		}
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
				+ ((descricao == null) ? 0 : descricao.hashCode());
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
		Grupo other = (Grupo) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		
		return true;
	}

	@Override
	public List<String> getOrderBy() {
		List<String> lista = new ArrayList<String>();
		lista.add("descricao");
		lista.add("id");
		return lista;
	}
	
}
