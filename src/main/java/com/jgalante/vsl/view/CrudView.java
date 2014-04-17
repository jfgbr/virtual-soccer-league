package com.jgalante.vsl.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.annotation.Param;
import com.jgalante.faces.ViewScoped;
import com.jgalante.jgcrud.view.GenericView;
import com.jgalante.util.Parameter;
import com.jgalante.vsl.entity.BaseEntity;

@Named
@ViewScoped
public class CrudView extends GenericView implements Serializable{

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
		setEntity(verifyCrudPage(crudPage.getValue()));
		
		setEntities(findAll());
	}
	
	@Override
	public String save() {
		save(getEntity());
		setEntities(findAll());
		return null;
	}
	
	@SuppressWarnings({ "unchecked" })
	private <T extends BaseEntity> T verifyCrudPage(String page) {
		try {
			Class<T> cls = (Class<T>) Class.forName("com.jgalante.vsl.entity."+page);
			setEntityClass(cls);
			return (T) cls.newInstance();
		} catch (Exception e) {
			return null;
		}
//		if ("usuario".equals(page)) {
//			setEntityClass(Usuario.class);
//			return (T) new Usuario();
//		}
//		return null;
	}

	public Parameter<String> getCrudPage() {
		return crudPage;
	}

	public void setCrudPage(Parameter<String> crudPage) {
		this.crudPage = crudPage;
	}

}
