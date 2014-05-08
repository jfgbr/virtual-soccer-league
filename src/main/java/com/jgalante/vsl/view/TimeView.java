package com.jgalante.vsl.view;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.jgcrud.controller.GenericController;
import com.jgalante.jgcrud.entity.BaseEntity;
import com.jgalante.vsl.entity.Pais;

@Named("time")
@ViewScoped
public class TimeView extends CrudView {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private GenericController genericController;

	private List<BaseEntity> pais;
	
	@PostConstruct
	public void init() {
		super.init();
		genericController.setEntityClass(Pais.class);
		pais = new LinkedList<BaseEntity>(genericController.findAll());
	}

	public List<BaseEntity> getPais() {
		return pais;
	}
	
	
}
