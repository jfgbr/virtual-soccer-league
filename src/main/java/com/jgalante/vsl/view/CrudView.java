package com.jgalante.vsl.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.jgalante.annotation.Param;
import com.jgalante.util.Parameter;
import com.jgalante.vsl.entity.BaseEntity;

//@Named
//@ViewScoped
public class CrudView extends BaseView<BaseEntity> implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	@Param
	private Parameter<String> crudPage;
	
//	private Usuario usuario;
//	private Pais pais;

//	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
//		usuario = new Usuario();
//		pais = new Pais();
		setEntidade(verifyCrudPage(crudPage.getValue()));
		
		listar();
	}
	
	@Override
	public void salvar() {
		super.salvar();
		listar();
	}
	
	@Override
	public void listar() {
		super.listar();
		setListaEntidades(null);		
	}
	
	@Override
	public void novo() {
		super.novo();
		setEntidade(verifyCrudPage(crudPage.getValue()));
		crudPage.keep();
	}
	
	public void fechar() {
		listar();
		crudPage.keep();
		setEntidade(verifyCrudPage(crudPage.getValue()));
	}
	
	@SuppressWarnings({ "unchecked" })
	protected <T extends BaseEntity> T verifyCrudPage(String page) {
		try {
			Class<T> cls = (Class<T>) Class.forName("com.jgalante.vsl.entity."+page);
			setClasseEntidade(cls);
			return (T) cls.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	public Parameter<String> getCrudPage() {
		return crudPage;
	}

	public void setCrudPage(Parameter<String> crudPage) {
		this.crudPage = crudPage;
	}

}
