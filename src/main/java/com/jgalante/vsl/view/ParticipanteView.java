package com.jgalante.vsl.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.vsl.entity.BaseEntity;
import com.jgalante.vsl.entity.Campeonato;
import com.jgalante.vsl.persistence.NegocioDao;

@Named("participante")
@ViewScoped
public class ParticipanteView extends CrudView {

	private static final long serialVersionUID = 1L;
//	
	private Long opcaoDeSelecaoDeCampeonatos;
	private List<Campeonato> lstCampeonatos;
	
	@Inject
	private NegocioDao negocioDao;
	
	private static String JOIN = "LEFT JOIN FETCH o.time t LEFT JOIN FETCH t.pais LEFT JOIN FETCH o.usuario LEFT JOIN FETCH o.campeonato LEFT JOIN FETCH o.partidasMandante LEFT JOIN FETCH o.partidasVisitante";
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		setJoinClause(JOIN);
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
			setQueryBusca("select o FROM Participante o "+ JOIN +" where o.campeonato.id = "
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
