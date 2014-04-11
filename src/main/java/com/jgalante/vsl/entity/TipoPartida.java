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
@Table(name = "tipo_partida")
public class TipoPartida extends BaseEntity {

	@Column(name = "descricao", nullable = false, length = 50)
	private String descricao;
	
	@Column(name = "qtdPartidas")
	private Integer qtdPartidas;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoPartida", cascade = CascadeType.ALL)
	private Set<Partida> partidas = new HashSet<Partida>(0);
	
	public static TipoPartida AMISTOSO = new TipoPartida(1L,"Amistoso", 1);
	public static TipoPartida FASE_GRUPOS = new TipoPartida(2L, "Fase de Grupos", 0);
	public static TipoPartida OITAVAS_FINAL = new TipoPartida(3L, "Oitavas-de-final", 8);
	public static TipoPartida QUARTAS_FINAL = new TipoPartida(4L, "Quartas-de-final", 4);
	public static TipoPartida SEMIFINAL = new TipoPartida(5L, "Semifinal", 2);
	public static TipoPartida TERCEIRO = new TipoPartida(6L, "Terceiro lugar", 1);
	public static TipoPartida FINAL = new TipoPartida(7L, "Final", 1);

	public TipoPartida() {
	}

	public TipoPartida(Long id) {
		this.setId(id);
	}

	public TipoPartida(String descricao) {
		this.descricao = descricao;
	}

	public TipoPartida(Long id, String descricao, Integer qtdPartidas) {
		this.setId(id);
		this.descricao = descricao;
		this.qtdPartidas = qtdPartidas;
	}

	public TipoPartida(String descricao, Integer qtdPartidas) {
		this.descricao = descricao;
		this.qtdPartidas = qtdPartidas;
	}
	
	public void setIdTipoPartida(Long id) {
		this.setId(id);
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQtdPartidas() {
		return this.qtdPartidas;
	}

	public void setQtdPartidas(Integer qtdPartidas) {
		this.qtdPartidas = qtdPartidas;
	}

	public Set<Partida> getPartidas() {
		return partidas;
	}

	public void setPartidas(Set<Partida> partidas) {
		this.partidas = partidas;
	}

	@Override
	public String getTextoExibicao() {
		return getDescricao();
	}

	@Override
	public List<String> getOrderBy() {
		List<String> lista = new ArrayList<String>();
		lista.add("id");
		return lista;
	}

}
