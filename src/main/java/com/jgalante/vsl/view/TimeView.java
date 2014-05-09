package com.jgalante.vsl.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.vsl.entity.BaseEntity;
import com.jgalante.vsl.persistence.NegocioDao;

@Named("time")
@ViewScoped
public class TimeView extends CrudView {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private NegocioDao negocioDao;
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		setJoinClause("LEFT JOIN FETCH o.pais LEFT JOIN FETCH o.participantes");
	}
	
	@Override
	protected <T extends BaseEntity> T verifyCrudPage(String page) {
		return super.verifyCrudPage("Time");
	}
	
	@Override
	public List<BaseEntity> getListaEntidades() {
		return negocioDao.listarTimes();
	}
	
	@Override
	protected BaseEntity carregaEntidade() {
		setId(null);
		baseDao.refresh(getEntidade());
		return getEntidade();
	}
}
