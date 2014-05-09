package com.jgalante.vsl.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.vsl.entity.BaseEntity;
import com.jgalante.vsl.entity.Campeonato;
import com.jgalante.vsl.entity.Partida;
import com.jgalante.vsl.persistence.NegocioDao;

@Named("partida")
@ViewScoped
public class PartidaView extends CrudView {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private NegocioDao negocioDao;
	private Long opcaoDeSelecaoDeCampeonatos;
	private List<Campeonato> lstCampeonatos;

	private static String JOIN = "LEFT JOIN FETCH o.mandante m LEFT JOIN FETCH m.time LEFT JOIN FETCH m.usuario LEFT JOIN FETCH m.campeonato LEFT JOIN FETCH o.visitante v LEFT JOIN FETCH v.time LEFT JOIN FETCH v.usuario ";
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		setJoinClause(JOIN);
	}
	
	@Override
	public void salvar() {
		Partida partida = (Partida)getEntidade();
		partida.setMandante(
				negocioDao.obterParticipante(partida.getMandante()));
		partida.setVisitante(
				negocioDao.obterParticipante(partida.getVisitante()));
//		getEntidade().getMandante().getPartidasMandante().add(getEntidade());
//		getEntidade().getVisitante().getPartidasVisitante().add(getEntidade());
		super.salvar();
	}
	
	@Override
	protected BaseEntity carregaEntidade() {
		setId(null);
		baseDao.refresh(getEntidade());
		return getEntidade();
	}

	public void obterListaDeCampeonatos(ValueChangeEvent evento) {
		opcaoDeSelecaoDeCampeonatos = null;
		try {
			opcaoDeSelecaoDeCampeonatos = Long.parseLong(evento.getNewValue()
					.toString());
			obterListaDeCampeonatos();
		} catch (NumberFormatException e) {
			setQueryBusca(null);
		} catch (NullPointerException e) {
		}
	}

	public void obterListaDeCampeonatos() {
		if (opcaoDeSelecaoDeCampeonatos != 0L) {
			setQueryBusca("select o FROM Partida o "+ JOIN +" where m.campeonato.id = "
					+ opcaoDeSelecaoDeCampeonatos.toString());
		}else
			setQueryBusca(null);
		
		limpaEntidade();
	}
	
	public List<Campeonato> getLstCampeonatos(){
		if (lstCampeonatos == null)
			lstCampeonatos = negocioDao.listarCampeonatos();
		return lstCampeonatos;
	}
}
