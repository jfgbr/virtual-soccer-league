package com.jgalante.vsl.view;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.jgalante.vsl.entity.BaseEntity;
import com.jgalante.vsl.persistence.BaseDao;
import com.jgalante.vsl.util.Estado;

public abstract class BaseView<T extends BaseEntity> implements Serializable{

//	private static final String PATH = "/pages/";
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private T entidade;

	@Inject
	protected BaseDao baseDao;

	private LazyDataModel<T> dataModel;
	private List<T> listaEntidades;

	private int primeiroPagina = 0;
	private int tamanhoPagina = BaseDao.PAGESIZE;
	private int total = 0;

	private String queryBusca;
	private boolean mudouBusca = true;
	private String ordenacao;

	private Estado estado = Estado.LISTAR;

	public T getEntidade() {
		if (entidade == null) {
			entidade = buscaEntidade();
		}
		return entidade;
	}

	protected T buscaEntidade() {
		if (isIdDisponivel()) {
			return carregaEntidade();
		} else {
			return criaEntidade();
		}
	}

	protected T criaEntidade(){
		try {
			return getTipoClasse().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected T carregaEntidade(){
		return baseDao.getEntidade(getTipoClasse(), getId());
	}
	
	protected void limpaEntidade(){
		entidade = null;
		id = null;
		dataModel.setWrappedData(getDataModel().load(primeiroPagina, tamanhoPagina, null, null, null));
//		dataModel.setRowCount(getTotal());
//		dataModel.setWrappedData(null);
		listaEntidades = null;
	}
	
	
	public void salvar() {

		try {
			baseDao.salvar(getEntidade());
			total++;
			limpaEntidade();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							getMessage("salvar.sucesso"),
							null));
			
			listar();
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("erro"), null));
		}

	}

	public void remover() {
		try {
			baseDao.remover(((BaseEntity) dataModel.getRowData()));
			total--;
			limpaEntidade();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							getMessage("remover.sucesso"),
							null));
			
			listar();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("erro"), null));
		}
	}

	public void cancelar() {
//		if (isAtualizacao()) {
		limpaEntidade();
		listar();
//		} else {
//			ViewUtil.redirect("/home.jsf");
//		}
	}

	public void novo() {
		estado = Estado.INCLUIR;
//		ViewUtil.redirect(PATH
//				+ getTipoClasse().getSimpleName().toLowerCase());
	}

	public void alterar() {
		estado = Estado.ALTERAR;
		setId(((BaseEntity) dataModel.getRowData()).getId());
		entidade = carregaEntidade();
//		ViewUtil.redirect(PATH
//				+ getTipoClasse().getSimpleName().toLowerCase()
//				+ "Editar");
	}

	public void listar() {
		estado = Estado.LISTAR;
//		ViewUtil.redirect(PATH
//				+ getTipoClasse().getSimpleName().toLowerCase());
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isIdDisponivel() {
		return id != null;
	}

	public boolean isAtualizacao() {
		return getEntidade().getId() != null;
	}

	@SuppressWarnings("unchecked")
	private Class<T> getTipoClasse() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	public LazyDataModel<T> getDataModel() {
		if (dataModel == null) {
			dataModel = new LazyDataModel<T>() {
				private static final long serialVersionUID = 1L;

				public int getRowCount(){
					if (mudouBusca){
						if (queryBusca == null)
							setTotal(baseDao.totalRegistros(getTipoClasse().getSimpleName()));
						else
							setTotal(baseDao.totalRegistros(queryBusca));
						mudouBusca = false;
					}
					return getTotal();
				};
				
				@Override
				public List<T> load(int first, int pageSize, String sortField,
						SortOrder sortOrder, Map<String,String> filters) {

					setTamanhoPagina(pageSize);
					setPrimeiroPagina(first);
					// System.out.println("FIRST: " + first);
					// System.out.println("PAGESIZE: " + pageSize);
					
					return buscaPaginada(first, pageSize);
				}
			};
//			if (queryBusca == null)
//				setTotal(baseDao.totalRegistros(getTipoClasse().getSimpleName()));
//			else
//				setTotal(baseDao.totalRegistros(queryBusca));
			dataModel.setPageSize(tamanhoPagina);
//			dataModel.setRowCount(getTotal());
		}
		return dataModel;
	}
	
	public void setDataModel(LazyDataModel<T> dataModel) {
		this.dataModel = dataModel;
	}

	@SuppressWarnings("unchecked")
	protected List<T> buscaPaginada(int first, int pageSize){
		if (queryBusca == null){
			return (List<T>) baseDao.buscaPaginada(getTipoClasse(), first, pageSize, getOrdenacao());
		}else{
			return (List<T>) baseDao.buscaPaginada(queryBusca, first, pageSize);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> getListaEntidades() {
		if (listaEntidades == null)
			listaEntidades = (List<T>) baseDao.getListaEntidades(getTipoClasse(), getOrdenacao());
		return listaEntidades;
	}

	public String getQueryBusca() {
		return queryBusca;
	}

	public void setQueryBusca(String QueryBusca) {
		mudouBusca = true;
		this.queryBusca = QueryBusca;
	}

	protected String getOrdenacao(){
		if (ordenacao == null){
			ordenacao = "";
			List<String> lista = criaEntidade().getOrderBy();
			for (String item : lista) {
				if (!ordenacao.equals(""))
					ordenacao += ",";
				ordenacao += "o." + item;
			}
		}
		return ordenacao;
	}
	
	public void setPrimeiroPagina(int primeiroPagina) {
		this.primeiroPagina = primeiroPagina;
	}

	public void setTamanhoPagina(int tamanhoPagina) {
		this.tamanhoPagina = tamanhoPagina;
	}
	
	public int getTamanhoPagina() {
		return tamanhoPagina;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	protected String getMessage(String key){
		try{
			return ResourceBundle.getBundle("MessageResources")
					.getString(getTipoClasse().getSimpleName().toLowerCase() + "." + key);
		}catch(MissingResourceException e){
			return ResourceBundle.getBundle("MessageResources")
					.getString("padrao." + key);
		}
	}
	
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

}
