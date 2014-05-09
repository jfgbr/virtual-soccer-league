package com.jgalante.vsl.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import com.jgalante.vsl.entity.BaseEntity;
import com.jgalante.vsl.entity.Campeonato;
import com.jgalante.vsl.entity.Grupo;
import com.jgalante.vsl.entity.Participante;
import com.jgalante.vsl.entity.Partida;
import com.jgalante.vsl.entity.Time;
import com.jgalante.vsl.entity.TipoCampeonato;
import com.jgalante.vsl.entity.TipoPartida;
import com.jgalante.vsl.entity.Usuario;
import com.jgalante.vsl.persistence.NegocioDao;
import com.jgalante.vsl.util.Estado;

@Named("campeonato")
@ViewScoped
public class CampeonatoView extends CrudView {
	
	private static final long serialVersionUID = 1L;
	
	private static final Estado SELECIONA = new Estado(4, "Seleciona");
	private static final Estado TABELA = new Estado(5, "Tabela");
	
	@Inject
	private NegocioDao negocioDao;
	
	private List<TipoCampeonato> lstTipoCampeonato; 
	private DualListModel<Usuario> usuarios;
	private DualListModel<Time> times;
	private List<Grupo> grupos;
	
	private boolean exibirSelecionaParticipantes;
	private boolean exibirDadosGrupo;
	private boolean sorteia;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		exibirSelecionaParticipantes = false;
		usuarios = null;
		times = null;
		
		lstTipoCampeonato = negocioDao.listarTipoCampeonato(); 

		alteraTipoCampeonato(TipoCampeonato.GRUPO.getId());
		
		setJoinClause("LEFT JOIN FETCH o.tipoCampeonato LEFT JOIN FETCH o.participantes");
		
		
	}
	
	@Override
	protected <T extends BaseEntity> T verifyCrudPage(String page) {
		return super.verifyCrudPage("Campeonato");
	}
	
	@Override
	public void salvar() {
		if (validaDadosCampeonato())
			super.salvar();

	}
	
	@Override
	public void alterar() {
		super.alterar();
		setEstado(SELECIONA);
	}
	
	public void defineParticipantes() {
		boolean erro = false;
		
		// Valida se tem times suficientes para sortear entre os participantes
		if (usuarios.getTarget().size() > times.getTarget().size()){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Número de times tem que ser maior ou igual ao de participantes!"));
			erro = true;
		}
		
		// Valida a quantidade de participantes
		erro = !validaDadosCampeonato(usuarios.getTarget().size());
		
		if (!erro){
			
			ArrayList<Grupo> grupos = new ArrayList<Grupo>();
			Integer qtdParticipantes = usuarios.getTarget().size();
			List<Participante> participantes = new ArrayList<Participante>();
			
			if (sorteia){
				Collections.shuffle(times.getTarget());
				Collections.shuffle(usuarios.getTarget());
			}
			
			Campeonato entidade = (Campeonato) getEntidade();
			
			if (entidade.getTipoCampeonato().equals(TipoCampeonato.MATA_MATA)){
				// Define os participantes
				for (Integer i = 0; i < qtdParticipantes; i++){
					participantes.add(new Participante(entidade,
							usuarios.getTarget().get(i), 
							times.getTarget().get(i)));
				}
				
				// 
				Collections.shuffle(participantes);
				
			}else{
				Integer qtdParticipantesPorGrupo = qtdParticipantes / entidade.getQtdGrupos();
				
				// Cria os grupos
				for (Integer i = 0; i < entidade.getQtdGrupos(); i++) {
					grupos.add(new Grupo(Character.toString((char)(i+65))));
				}
				
				if (sorteia)
					Collections.shuffle(grupos);

				int posicao = 0;
				// Define os participantes e os grupos
				for (Integer i = 0; i < grupos.size();i++){
					// permitir grupo com menos participantes
					if (i == grupos.size() - 1)
						qtdParticipantesPorGrupo = qtdParticipantes - (qtdParticipantesPorGrupo * i);
					for (Integer j = 0; j < qtdParticipantesPorGrupo; j++, posicao++){
						participantes.add(new Participante(entidade, grupos.get(i),
								usuarios.getTarget().get(posicao), 
								times.getTarget().get(posicao)));
					}
				}
				
				
			}
			
			entidade.setParticipantes(new HashSet<Participante>(participantes));
			geraPartidas();
	
			try {
				salvar();
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Campeonato gerado com sucesso!"));
				
//				inicializa();
				listar();
	
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										"Ocorreu um erro ao gravar o resultado do sorteio!"));
			}
		}
	}
	
	
	public void alteraTipoCampeonato(ValueChangeEvent evento){
		Long tipoCampeonato = null;
		try {
			tipoCampeonato = Long.parseLong(evento.getNewValue().toString());

			alteraTipoCampeonato(tipoCampeonato);
		}catch (Exception e) {
			
		}

	}
	
	private void alteraTipoCampeonato(Long idTipoCampeonato){
		exibirDadosGrupo = false;
		
		if (TipoCampeonato.GRUPO.getId().equals(idTipoCampeonato))
			exibirDadosGrupo = true;		
	}
	
	public boolean validaDadosCampeonato(Integer qtdParticipantes) {
		boolean result = true;
		if (qtdParticipantes == null || qtdParticipantes <= 1){
			FacesContext
			.getCurrentInstance()
			.addMessage(
					null,
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Quantidade de participantes deve ser maior do que 1!",
							null));
			result = false;
		}
		
		Campeonato entidade = (Campeonato) getEntidade();
		
		if(TipoCampeonato.MATA_MATA.equals(entidade.getTipoCampeonato())){
			// Verifica se a quantidade de participantes é uma potência de dois
			// para poder montar as partidas do mata-mata.
			if (!IsPotenciaDeDois(qtdParticipantes)){
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Quantidade de participantes deve ser uma potência de 2!",
										null));
				result = false;
			}
		}else if (TipoCampeonato.GRUPO.equals(entidade.getTipoCampeonato())) {
			// Verifica se a quantidade de participantes 
			Integer qtdMinimaParticipantes = entidade.getQtdClassificados() * entidade.getQtdGrupos();
			if (qtdMinimaParticipantes > qtdParticipantes){
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Quantidade de participantes deve ser no mínimo "+qtdMinimaParticipantes+"!",
										null));
				result = false;
			}
		}
				
		return (result && validaDadosCampeonato());
	}
	
	public boolean validaDadosCampeonato() {

		boolean result = true;
		
		Campeonato entidade = (Campeonato) getEntidade();
		
		// Atribui um valor padrão para o tipo de campeonato
		if(TipoCampeonato.PONTOS_CORRIDOS.equals(entidade.getTipoCampeonato()))
			entidade.setQtdGrupos(1);
		else if(TipoCampeonato.MATA_MATA.equals(entidade.getTipoCampeonato()))
			entidade.setQtdGrupos(0);

		// Válida os dados passados para gerar o campeonato
		if (entidade.getNome() == null || entidade.getNome().isEmpty()){
			FacesContext
			.getCurrentInstance()
			.addMessage(
					null,
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Preencher o nome do campeonato!",
							null));
			result = false;
		}
		
		
		
		if (TipoCampeonato.GRUPO.equals(entidade.getTipoCampeonato())) {

			// Verifica a quantidade de grupos informado. Deve existir mais de um 1 grupo 
			if (entidade.getQtdGrupos() == null || entidade.getQtdGrupos() <= 1){
				FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Quantidade de grupos deve ser maior do que 1!",
								null));
				result = false;
			}else if (entidade.getQtdGrupos() > 25){
				// Verifica se a quantida de grupos ultrapassou o limite de 25
				FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Quantidade de grupos deve ser menor do que 25!",
								null));
				result = false;
			}
			
			// Verifica quantos times estariam classificados para o mata-mata
			if ((entidade.getQtdClassificados() == null || entidade.getQtdClassificados() == 0) ||
				entidade.getQtdGrupos() != null && entidade.getQtdGrupos() > 1 && 
				!(IsPotenciaDeDois(entidade.getQtdGrupos()* entidade.getQtdClassificados()))){
				
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Número total de classificados deve ser uma potência de 2!",
										null));
				result = false;
			}
			
		}	

		return result;
	}
	
	private boolean IsPotenciaDeDois(Integer x) {
		return (x != 0) && ((x & (x - 1)) == 0);
	}
	
	public void geraPartidas() {
		if(TipoCampeonato.MATA_MATA.equals(((Campeonato) getEntidade()).getTipoCampeonato())){
			geraPartidasMataMata();
		}else{
			geraPartidasComGrupos();
		}
	}
	
	private void geraPartidasMataMata() {
		Set<Participante> participantes = ((Campeonato) getEntidade()).getParticipantes();
		List<Participante> participantesClassificados = new ArrayList<Participante>();
		Integer qtdPartidas = 0;
		Partida terceiroLugar = new Partida();
		terceiroLugar.setTipoPartida(TipoPartida.TERCEIRO);
		
		for (Participante participante : participantes) {
			List<Partida> partidas = negocioDao
					.listarPorCampeonatoParticipanteOrdenado(getEntidade().getId(), participante.getId());
			
			if (partidas.size() > 0){
				Partida partida = partidas.get(partidas.size()-1);
				
				if (partida.getGolsMandante() > partida.getGolsVisitante() ||
						partida.getGolsMandantePenalty() > partida.getGolsVisitantePenalty()){
					
					participantesClassificados.add(partida.getMandante());
					if (partida.getTipoPartida().equals(TipoPartida.SEMIFINAL)){
						terceiroLugar.setMandante(partida.getVisitante());
						partida.getVisitante().getPartidasMandante().add(terceiroLugar);
					}
					
				}else{
					participantesClassificados.add(partida.getVisitante());
					if (partida.getTipoPartida().equals(TipoPartida.SEMIFINAL)){
						terceiroLugar.setVisitante(partida.getMandante());
						partida.getMandante().getPartidasMandante().add(terceiroLugar);
					}
				}
				
				participantes.remove(partida.getMandante());
				participantes.remove(partida.getVisitante());
				
			}else
				participantesClassificados.add(participante);
		}
		
		qtdPartidas = participantesClassificados.size()/2;
		
		for (int i = 0; i < participantesClassificados.size(); i+=2) {
			Participante mandante = participantesClassificados.get(i);
			Participante visitante = participantesClassificados.get(i+1);
			
			if (mandante.getPartidasMandante() == null)
				mandante.setPartidasMandante(new HashSet<Partida>());
			if (visitante.getPartidasVisitante() == null)
				visitante.setPartidasVisitante(new HashSet<Partida>());
			
			Partida partida = new Partida(mandante, visitante);
			partida.setTipoPartida(retornaFase(qtdPartidas));
			
			mandante.getPartidasMandante().add(partida);
			visitante.getPartidasVisitante().add(partida);
		}		
		
	}
	
	private TipoPartida retornaFase(Integer qtdPartidas){
		List<TipoPartida> tipoPartidas = new ArrayList<TipoPartida>();
		tipoPartidas.add(TipoPartida.FASE_GRUPOS);
		tipoPartidas.add(TipoPartida.OITAVAS_FINAL);
		tipoPartidas.add(TipoPartida.QUARTAS_FINAL);
		tipoPartidas.add(TipoPartida.SEMIFINAL);
		tipoPartidas.add(TipoPartida.FINAL);
		
		for (TipoPartida tipoPartida : tipoPartidas) {
			if (tipoPartida.getQtdPartidas().equals(qtdPartidas))
				return tipoPartida;
		}
		return TipoPartida.FASE_GRUPOS;
	}
	
	private void geraPartidasComGrupos() {
		List<Grupo> grupos = new ArrayList<Grupo>();
		
		Campeonato entidade = (Campeonato) getEntidade();
		
		if (TipoCampeonato.GRUPO.equals(entidade.getTipoCampeonato())){
			grupos = negocioDao.listarGruposCampeonato(entidade.getId());
		}else
			grupos.add(new Grupo("A"));

		for (Grupo grupo : grupos) {
			Set<Participante> participantes = entidade.getParticipantes(); //listarPorGrupo(grupo.getId());
			System.out.println(grupo.getDescricao());
			for (int i = 0; i < participantes.size(); i++) {
				Participante mandante = (Participante) participantes.toArray()[i];
				System.out.println(mandante.getUsuario().getNome());
				if (mandante.getGrupo().equals(grupo)){
					
					if (mandante.getPartidasMandante() == null)
						mandante.setPartidasMandante(new HashSet<Partida>());
					
					for (int j = i + 1; j < participantes.size(); j++) {
						Participante visitante = (Participante) participantes.toArray()[j];
						if (visitante.getGrupo().equals(grupo)){
							if (!mandante.equals(visitante)) {
								System.out.println(visitante.getUsuario().getNome());
								if (visitante.getPartidasVisitante() == null)
									visitante.setPartidasVisitante(new HashSet<Partida>());
								
								Partida partida = new Partida(mandante, visitante);
								partida.setTipoPartida(TipoPartida.FASE_GRUPOS);
								
								mandante.getPartidasMandante().add(partida);
								visitante.getPartidasVisitante().add(partida);
								System.out.println("Partida gerada !!!!!!!!");
								System.out.println(partida.getMandante()
										.getUsuario().getNome()
										+ " - "
										+ partida.getVisitante().getUsuario()
												.getNome());
							}
						}
					}
				}
			}
		}
	}
	
	public void gerarTabela(){
		alterar();
		setEstado(TABELA);
				
		if (((Campeonato) getEntidade()).getTipoCampeonato().equals(TipoCampeonato.GRUPO)){ 
			grupos = negocioDao.listarGruposCampeonato(getEntidade().getId());
			for (Grupo grupo : grupos) {
				geraTabelaDeGrupos(grupo);
			}
		}
	}
	
	public void geraTabelaDeGrupos(Grupo grupo) {
//		participantes.add(negocioDao.listarPorGrupo(grupo.getId()));
//		grupo.setParticipantes(new HashSet<Participante>(participantes.get(participantes.size()-1)));
		for (Participante participante : grupo.getParticipantes()) {
			computaPartidas(participante);
		}
	}

	private void computaPartidas(Participante participante) {

		Integer vitorias = 0;
		Integer empates = 0;
		Integer derrotas = 0;
		Integer golsPro = 0;
		Integer golsContra = 0;
		Integer numeroJogos = 0;

		if (participante.getPartidasMandante() != null) {
			for (Partida partida : participante.getPartidasMandante()) {
				if (partida.getGolsMandante() == null
						|| !partida.getTipoPartida().equals(
								TipoPartida.FASE_GRUPOS))
					continue;
				numeroJogos++;
				golsPro += partida.getGolsMandante();
				golsContra += partida.getGolsVisitante();
				if (partida.getGolsMandante() > partida.getGolsVisitante())
					vitorias++;
				else if (partida.getGolsMandante() < partida.getGolsVisitante())
					derrotas++;
				else
					empates++;
			}
		}
		if (participante.getPartidasVisitante() != null) {
			for (Partida partida : participante.getPartidasVisitante()) {
				if (partida.getGolsMandante() == null
						|| !partida.getTipoPartida().equals(
								TipoPartida.FASE_GRUPOS))
					continue;
				numeroJogos++;
				golsPro += partida.getGolsVisitante();
				golsContra += partida.getGolsMandante();
				if (partida.getGolsMandante() < partida.getGolsVisitante())
					vitorias++;
				else if (partida.getGolsMandante() > partida.getGolsVisitante())
					derrotas++;
				else
					empates++;
			}
		}
		participante.setNumeroJogos(numeroJogos);
		participante.setVitorias(vitorias);
		participante.setDerrotas(derrotas);
		participante.setEmpates(empates);
		participante.setGolsPro(golsPro);
		participante.setGolsContra(golsContra);
	}
	
	@SuppressWarnings("unchecked")
	public DualListModel<Usuario> getUsuarios() {
		if (usuarios == null) {
			List<Usuario> source = new ArrayList<Usuario>();
			List<Usuario> target = new ArrayList<Usuario>();
			source = (List<Usuario>)baseDao.getListaEntidades(Usuario.class, "o.nome");
			usuarios = new DualListModel<Usuario>(source, target);
		}
		return usuarios;
	}

	public void setUsuarios(DualListModel<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@SuppressWarnings("unchecked")
	public DualListModel<Time> getTimes() {
		if (times == null) {
			List<Time> source = new ArrayList<Time>();
			List<Time> target = new ArrayList<Time>();
			source = (List<Time>)baseDao.getListaEntidades(Time.class, "o.nome");
			times = new DualListModel<Time>(source, target);
		}
		return times;
	}

	public void setTimes(DualListModel<Time> times) {
		this.times = times;
	}
	
	public List<TipoCampeonato> getLstTipoCampeonato() {
		return lstTipoCampeonato;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}
	
	public boolean isExibirSelecionaParticipantes() {
		return exibirSelecionaParticipantes;
	}

	public void setExibirSelecionaParticipantes(boolean exibirSelecionaParticipantes) {
		this.exibirSelecionaParticipantes = exibirSelecionaParticipantes;
	}

	public boolean isExibirDadosGrupo() {
		return exibirDadosGrupo;
	}

	public void setExibirDadosGrupo(boolean exibirDadosGrupo) {
		this.exibirDadosGrupo = exibirDadosGrupo;
	}


	public boolean isSorteia() {
		return sorteia;
	}

	public void setSorteia(boolean sorteia) {
		this.sorteia = sorteia;
	}

	public boolean isSeleciona() {
		return getEstado().equals(SELECIONA);
	}

	public boolean isTabela() {
		return getEstado().equals(TABELA);
	}
}
