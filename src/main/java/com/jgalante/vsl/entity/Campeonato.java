package com.jgalante.vsl.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
@Table(name = "campeonato")
public class Campeonato extends BaseEntity {
	
	public static Campeonato AMISTOSO = new Campeonato(1L,"Amistoso");

	@Column(name = "nome", nullable = false, length = 100)
	@Size(max = 100, message="{campeonato.nome.tamanho}")
	@NotEmpty(message = "{campeonato.nome.obrigatorio}")
	private String nome;

	@Column(name = "qtdGrupos")
	private Integer qtdGrupos;

	@Column(name = "qtdClassificados")
	private Integer qtdClassificados;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "campeonato", cascade = CascadeType.ALL)
	private Set<Participante> participantes = new HashSet<Participante>(0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTipoCampeonato")
	private TipoCampeonato tipoCampeonato;

	public Campeonato() {
		// this.qtdGrupos = 0;
		// this.qtdClassificados = 0;
		this.tipoCampeonato = new TipoCampeonato(TipoCampeonato.GRUPO);
	}

	public Campeonato(String nome) {
		// this.qtdGrupos = 0;
		// this.qtdClassificados = 0;
		this.nome = nome;
		this.tipoCampeonato = new TipoCampeonato(TipoCampeonato.GRUPO);
	}
	
	public Campeonato(Long id, String nome) {
		setId(id);
		this.nome = nome;
	}

	public Campeonato(Long id) {
		// this.qtdGrupos = 0;
		// this.qtdClassificados = 0;
		setId(id);
		this.tipoCampeonato = new TipoCampeonato(TipoCampeonato.GRUPO);
	}

	public Campeonato(String nome, Integer qtdGrupos, Integer qtdClassificados,
			Set<Participante> participantes) {
		this.nome = nome;
		this.qtdGrupos = qtdGrupos;
		this.qtdClassificados = qtdClassificados;
		this.participantes = participantes;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getQtdGrupos() {
		if (qtdGrupos != null && qtdGrupos == 0)
			qtdGrupos = null;
		return this.qtdGrupos;
	}

	public void setQtdGrupos(Integer qtdGrupos) {
		this.qtdGrupos = qtdGrupos;
	}

	public Integer getQtdClassificados() {
		if (qtdClassificados != null && qtdClassificados == 0)
			qtdClassificados = null;
		return qtdClassificados;
	}

	public void setQtdClassificados(Integer qtdClassificados) {
		this.qtdClassificados = qtdClassificados;
	}

	public Set<Participante> getParticipantes() {
		return this.participantes;
	}

	public void setParticipantes(Set<Participante> participantes) {
		this.participantes = participantes;
	}

	public Integer getQtdParticipantes() {
		if (this.participantes == null)
			return 0;
		return this.participantes.size();
	}
	
	public TipoCampeonato getTipoCampeonato() {
		return tipoCampeonato;
	}

	public void setTipoCampeonato(TipoCampeonato tipoCampeonato) {
		this.tipoCampeonato = tipoCampeonato;
	}

	@Override
	public String getTextoExibicao() {
		return getNome();
	}

	@Override
	public List<String> getOrderBy() {
		List<String> lista = new ArrayList<String>();
		lista.add("nome");
		lista.add("tipoCampeonato.descricao");
		return lista;
	}
	
}
