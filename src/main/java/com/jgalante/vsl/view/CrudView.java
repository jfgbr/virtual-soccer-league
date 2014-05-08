package com.jgalante.vsl.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.jgalante.annotation.Param;
import com.jgalante.faces.ViewScoped;
import com.jgalante.jgcrud.entity.BaseEntity;
import com.jgalante.jgcrud.model.DelegateDataModel;
import com.jgalante.jgcrud.view.GenericView;
import com.jgalante.util.Parameter;
import com.jgalante.vsl.util.Estado;

@Named
@ViewScoped
public class CrudView extends GenericView implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	@Param
	private Parameter<String> crudPage;
	
	@Inject
	@Param
	private Parameter<String> joinFields;
	
	private Estado currentState;
	
	@PostConstruct
	public void init() {
		getController().setDataModel(new DelegateDataModel<BaseEntity>(getController(), joinFields.getValue()));
		currentState = Estado.LISTAR;
		setEntity(verifyCrudPage(crudPage.getValue()));
//		setEntities(findAll());
	}
	
	@Override
	public String save() {
		super.save();
		currentState = Estado.LISTAR;
		setEntity(verifyCrudPage(crudPage.toString()));
		crudPage.keep();
		return null;
	}
	
//	@Override
//	public String remove() {
//		super.remove();
//		crudPage.keep();
//		return null;
//	}
	
	public void newEntity() {
		currentState = Estado.INCLUIR;
		crudPage.keep();
//		return "/pages/" + crudPage.toString().toLowerCase();
	}
	
	public void close() {
		currentState = Estado.LISTAR;
		crudPage.keep();
		setEntity(verifyCrudPage(crudPage.toString()));
	}
	
	@SuppressWarnings({ "unchecked" })
	private <T extends BaseEntity> T verifyCrudPage(String page) {
		try {
			Class<T> cls = (Class<T>) Class.forName("com.jgalante.vsl.entity."+page);
			setEntityClass(cls);
			return (T) cls.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Page is not initialized!");
		}
	}

	public Parameter<String> getCrudPage() {
		return crudPage;
	}

	public void setCrudPage(Parameter<String> crudPage) {
		this.crudPage = crudPage;
	}
	
	public Parameter<String> getJoinFields() {
		return joinFields;
	}

	public Estado getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Estado currentState) {
		this.currentState = currentState;
	}

}
