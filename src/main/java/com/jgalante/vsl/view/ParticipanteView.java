package com.jgalante.vsl.view;

import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import com.jgalante.vsl.entity.Campeonato;
import com.jgalante.vsl.entity.Participante;

@Named("participante")
@ViewScoped
public class ParticipanteView extends BaseView<Participante> {

	private static final long serialVersionUID = 1L;
	
	private Long opcaoDeSelecaoDeCampeonatos;
	private List<Campeonato> lstCampeonatos;
	
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
			setQueryBusca("select p FROM Participante p where p.campeonato.id = "
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
