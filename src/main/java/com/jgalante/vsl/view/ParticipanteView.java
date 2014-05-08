package com.jgalante.vsl.view;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.jgcrud.controller.GenericController;
import com.jgalante.jgcrud.entity.BaseEntity;
import com.jgalante.jgcrud.model.DelegateDataModel;
import com.jgalante.jgcrud.model.Filter;
import com.jgalante.jgcrud.model.Filter.Operator;
import com.jgalante.vsl.entity.Campeonato;
import com.jgalante.vsl.entity.Time;
import com.jgalante.vsl.entity.Usuario;

@Named("participante")
@ViewScoped
public class ParticipanteView extends CrudView {

	private static final long serialVersionUID = 1L;
	
	private Long opcaoDeSelecaoDeCampeonatos;
	private List<BaseEntity> campeonatos;
	
	@Inject
	private GenericController genericController;
	
	private List<BaseEntity> usuarios;

	private List<BaseEntity> times;
	
	
	@PostConstruct
	public void init() {
		super.init();
		genericController.setEntityClass(Usuario.class);
		usuarios = new LinkedList<BaseEntity>(genericController.findAll());
		genericController.setEntityClass(Campeonato.class);
		campeonatos = new LinkedList<BaseEntity>(genericController.findAll());
	}
	
	@Override
	public void newEntity() {
		genericController.setEntityClass(Time.class);
//		genericController.getDataModel().addJoinField("pais");		
		List<Filter> filters = new LinkedList<>();
		filters.add(new Filter("pais", Operator.JOIN));
		times = new LinkedList<BaseEntity>(genericController.search(null, filters));
//		genericController.setDataModel(new DelegateDataModel<BaseEntity>(getController(), getJoinFields().getValue()));
//		genericController.getDataModel().removeJoinField("pais");
		super.newEntity();
	}
	
	public void obterListaDeCampeonatos(ValueChangeEvent evento) {
		opcaoDeSelecaoDeCampeonatos = null;
		try {
			opcaoDeSelecaoDeCampeonatos = Long.parseLong(evento.getNewValue()
					.toString());
			obterListaDeCampeonatos();
		} catch (NumberFormatException e) {
//			setQueryBusca(null);
		}
	}

	public void obterListaDeCampeonatos() {
		if (opcaoDeSelecaoDeCampeonatos != 0L) {
			getController().setDataModel(new DelegateDataModel<BaseEntity>(getController()));
			getController().addFilter(new Filter("campeonato.id", opcaoDeSelecaoDeCampeonatos.toString(), Operator.EQUAL));
//			setQueryBusca("select p FROM Participante p where p.campeonato.id = "
//					+ opcaoDeSelecaoDeCampeonatos.toString());
		}
//else
//			setQueryBusca(null);
		
//		limpaEntidade();
	}
	
	public List<BaseEntity> getUsuarios() {
		return usuarios;
	}
	
	public List<BaseEntity> getLstCampeonatos(){
		return campeonatos;
	}

	public List<BaseEntity> getTimes() {
		return times;
	}
	
	
}
