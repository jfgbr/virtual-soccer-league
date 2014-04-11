package com.jgalante.vsl.persistence;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import com.jgalante.vsl.entity.BaseEntity;
import com.jgalante.vsl.entity.Participante;

@Local
public interface BaseDaoLocal {
	
	public EntityManager getEntityManager(); 

	public <T extends BaseEntity> T getEntidade(Class<T> classeEntidade, Long id);
	
	public List<?> getListaEntidades(Class<?> classeEntidade);

	public List<?> getListaEntidades(Class<?> classeEntidade, String orderBy);
	
	public Participante obterParticipante(Participante participante);
	
	public <T extends BaseEntity> T salvar(T entity);
	
	public <T extends BaseEntity> void remover(T entity);

	public <T extends BaseEntity> void refresh(T entity);
	
	public <T extends BaseEntity> T merge(T entity);

	/**
	 * Returns a list of teachers with the number of courses they teach.
	 * 
	 * @return List of {@link TeacherCourseCount} objects
	 */
	public List<BaseEntity> buscaPaginada(String jpql,
			int firstResult, int pageSize);
	
	public List<BaseEntity> buscaPaginada(Class<?> classeEntidade,
			int firstResult, int pageSize, String orderBy);
	
	public Integer totalRegistros(String nameClass);
	
	public List<?> findByJPQL(String jpql);
	
	public List<?> findByJPQLParam(String jpql, Map<String, Object> params);
}
