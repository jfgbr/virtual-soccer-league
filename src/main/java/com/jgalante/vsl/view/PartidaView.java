package com.jgalante.vsl.view;

import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.vsl.entity.Campeonato;
import com.jgalante.vsl.entity.Partida;
import com.jgalante.vsl.persistence.NegocioDao;

@Named("partida")
@ViewScoped
public class PartidaView extends BaseView<Partida> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private NegocioDao negocioDao;
	private Long opcaoDeSelecaoDeCampeonatos;
	private List<Campeonato> lstCampeonatos;

	@Override
	public void salvar() {
		getEntidade().setMandante(
				negocioDao.obterParticipante(getEntidade().getMandante()));
		getEntidade().setVisitante(
				negocioDao.obterParticipante(getEntidade().getVisitante()));
//		getEntidade().getMandante().getPartidasMandante().add(getEntidade());
//		getEntidade().getVisitante().getPartidasVisitante().add(getEntidade());
		super.salvar();
	}

	public void obterListaDeCampeonatos(ValueChangeEvent evento) {
		opcaoDeSelecaoDeCampeonatos = null;
		try {
			opcaoDeSelecaoDeCampeonatos = Long.parseLong(evento.getNewValue()
					.toString());
			obterListaDeCampeonatos();
		} catch (NumberFormatException e) {
			setQueryBusca(null);
		}
	}

	public void obterListaDeCampeonatos() {
		if (opcaoDeSelecaoDeCampeonatos != 0L) {
			setQueryBusca("select p FROM Partida p where p.mandante.campeonato.id = "
					+ opcaoDeSelecaoDeCampeonatos.toString());
		}else
			setQueryBusca(null);
		
		limpaEntidade();
	}
	
	@SuppressWarnings("unchecked")
	public List<Campeonato> getLstCampeonatos(){
		if (lstCampeonatos == null)
			lstCampeonatos = (List<Campeonato>)baseDao.getListaEntidades(Campeonato.class, "o.nome");
		return lstCampeonatos;
	}
}
