package com.jgalante.vsl.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class Participante extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTime", nullable = false)
	private Time time;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCampeonato")
	private Campeonato campeonato;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinColumn(name = "idGrupo")
	private Grupo grupo;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mandante")
	private Set<Partida> partidasMandante = new HashSet<Partida>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "visitante")
	private Set<Partida> partidasVisitante = new HashSet<Partida>(0);

	@Transient
	private Integer numeroJogos = 0;

	@Transient
	private Integer vitorias = 0;

	@Transient
	private Integer empates = 0;

	@Transient
	private Integer derrotas = 0;

	@Transient
	private Integer golsPro = 0;

	@Transient
	private Integer golsContra = 0;

	public Participante() {
		usuario = new Usuario();
		time = new Time();
		campeonato = Campeonato.AMISTOSO;
	}
	
	public Participante(Long id){
		setId(id);
	}
	
	public Participante(Campeonato campeonato, Usuario usuario,	Time time) {
		this.grupo = null;
		this.campeonato = campeonato;
		this.usuario = usuario;
		this.time = time;
	}
	
	public Participante(Campeonato campeonato, Grupo grupo, Usuario usuario,
			Time time) {
		this.grupo = grupo;
		this.campeonato = campeonato;
		this.usuario = usuario;
		this.time = time;
	}
	
	public Participante(Campeonato campeonato, Grupo grupo, Usuario usuario,
			Time time, Set<Partida> partidasVisitante,
			Set<Partida> partidasMandante) {
		this.grupo = grupo;
		this.campeonato = campeonato;
		this.usuario = usuario;
		this.time = time;
		this.partidasVisitante = partidasVisitante;
		this.partidasMandante = partidasMandante;
	}

	@Override
	public String getTextoExibicao() {
		return null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Grupo getGrupo() {
		if (grupo == null)
			return new Grupo(0L);
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Set<Partida> getPartidasMandante() {
		return partidasMandante;
	}

	public void setPartidasMandante(Set<Partida> partidasMandante) {
		this.partidasMandante = partidasMandante;
	}

	public Set<Partida> getPartidasVisitante() {
		return partidasVisitante;
	}

	public void setPartidasVisitante(Set<Partida> partidasVisitante) {
		this.partidasVisitante = partidasVisitante;
	}

	public Integer getNumeroJogos() {
		return numeroJogos;
	}

	public void setNumeroJogos(Integer numeroJogos) {
		this.numeroJogos = numeroJogos;
	}

	public Integer getVitorias() {
		return vitorias;
	}

	public void setVitorias(Integer vitorias) {
		this.vitorias = vitorias;
	}

	public Integer getEmpates() {
		return empates;
	}

	public void setEmpates(Integer empates) {
		this.empates = empates;
	}

	public Integer getDerrotas() {
		return derrotas;
	}

	public void setDerrotas(Integer derrotas) {
		this.derrotas = derrotas;
	}

	public Integer getGolsPro() {
		return golsPro;
	}

	public void setGolsPro(Integer golsPro) {
		this.golsPro = golsPro;
	}

	public Integer getGolsContra() {
		return golsContra;
	}

	public void setGolsContra(Integer golsContra) {
		this.golsContra = golsContra;
	}
	
	@Transient
	public Integer getPontos(){
		return (vitorias*3) + (empates);
	}

	@Transient
	public Integer getSaldoGols() {
		return golsPro - golsContra;
	}

	@Transient
	public Integer getTotalJogos() {
		return partidasMandante.size() + partidasVisitante.size();
	}

	@Transient
	public float getPercentualAproveitamento() {
		if (getNumeroJogos() == 0)
			return 0.0f;
		return (((float) getPontos()) / ((float) (getNumeroJogos() * 3.0f))) * 100;
	}

	@Transient
	public boolean isExclui(){
		return (partidasMandante == null || partidasMandante.size() == 0) &&
		(partidasVisitante == null || partidasVisitante.size() == 0);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((campeonato == null) ? 0 : campeonato.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		Participante other = (Participante) obj;
		if (campeonato == null) {
			if (other.campeonato != null)
				return false;
		} else if (!campeonato.equals(other.campeonato))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		
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
		lista.add("usuario.nome");
		lista.add("time.nome");
		lista.add("time.pais.nome");
		lista.add("id");
		return lista;
	}

}
