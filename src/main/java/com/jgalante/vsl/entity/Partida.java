package com.jgalante.vsl.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table
public class Partida extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinColumn(name = "idMandante", nullable = false)
	private Participante mandante;

	@Column(name = "golsMandante")
	private Integer golsMandante;

	@Column(name = "golsMandantePenalty")
	private Integer golsMandantePenalty;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinColumn(name = "idVisitante", nullable = false)
	private Participante visitante;

	@Column(name = "golsVisitante")
	private Integer golsVisitante;

	@Column(name = "golsVisitantePenalty")
	private Integer golsVisitantePenalty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTipoPartida", nullable = false)
	private TipoPartida tipoPartida;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dataPartida", length = 19)
	private Date dataPartida;
	

	public Partida() {
		this.mandante = new Participante();
		this.visitante = new Participante();
		this.tipoPartida = TipoPartida.AMISTOSO;
		this.dataPartida = new Date();
	}

	public Partida(Participante visitante, Participante mandante) {
		this.visitante = visitante;
		this.mandante = mandante;
		this.tipoPartida = TipoPartida.AMISTOSO;
	}

	public Partida(Participante visitante, 
			Participante participanteMandante, Integer golsMandante,
			Integer golsVisitante, TipoPartida tipoPartida, Date dataPartida) {
		this.visitante = visitante;
		this.mandante = participanteMandante;
		this.golsMandante = golsMandante;
		this.golsVisitante = golsVisitante;
		this.tipoPartida = tipoPartida;
		this.dataPartida = dataPartida;
	}

	public Participante getMandante() {
		return mandante;
	}

	public void setMandante(Participante mandante) {
		this.mandante = mandante;
	}

	public Integer getGolsMandante() {
		return golsMandante;
	}

	public void setGolsMandante(Integer golsMandante) {
		this.golsMandante = golsMandante;
	}

	public Integer getGolsMandantePenalty() {
		return golsMandantePenalty;
	}

	public void setGolsMandantePenalty(Integer golsMandantePenalty) {
		this.golsMandantePenalty = golsMandantePenalty;
	}

	public Participante getVisitante() {
		return visitante;
	}

	public void setVisitante(Participante visitante) {
		this.visitante = visitante;
	}

	public Integer getGolsVisitante() {
		return golsVisitante;
	}

	public void setGolsVisitante(Integer golsVisitante) {
		this.golsVisitante = golsVisitante;
	}

	public Integer getGolsVisitantePenalty() {
		return golsVisitantePenalty;
	}

	public void setGolsVisitantePenalty(Integer golsVisitantePenalty) {
		this.golsVisitantePenalty = golsVisitantePenalty;
	}

	public TipoPartida getTipoPartida() {
		return tipoPartida;
	}

	public void setTipoPartida(TipoPartida tipoPartida) {
		this.tipoPartida = tipoPartida;
	}

	public Date getDataPartida() {
		return dataPartida;
	}

	public void setDataPartida(Date dataPartida) {
		this.dataPartida = dataPartida;
	}

	@Override
	public String toString() {
		return "Partida [idPartida=" + getId() + ", visitante=" + visitante
				+ ", mandante=" + mandante + "]";
	}

	@Override
	public String getTextoExibicao() {
		return null;
	}

	@Override
	public List<String> getOrderBy() {
		List<String> lista = new ArrayList<String>();
		lista.add("id DESC");
		return lista;
	}

}
