package com.jgalante.vsl.view;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.jgalante.vsl.entity.BaseEntity;

@Named("usuario")
@ViewScoped
public class UsuarioView extends CrudView {
	private static final long serialVersionUID = 1L;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		setJoinClause("LEFT JOIN FETCH o.participantes");
	}
	
	@Override
	protected <T extends BaseEntity> T verifyCrudPage(String page) {
		return super.verifyCrudPage("Usuario");
	}
}
