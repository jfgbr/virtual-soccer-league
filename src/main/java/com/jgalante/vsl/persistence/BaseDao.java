package com.jgalante.vsl.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jgalante.vsl.entity.BaseEntity;
import com.jgalante.vsl.entity.Participante;
import com.jgalante.vsl.qualifier.DataRepository;


@Stateless
public class BaseDao implements BaseDaoLocal {

//	private static Logger log = LoggerFactory.getLogger(BaseDao.class);
	public static final Integer PAGESIZE = 10;

	@DataRepository
	@Inject
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public <T extends BaseEntity> T salvar(T entity) {
//		if (entity.getId() == null){
//			entityManager.merge(entity);
//			return entity;
//		}else{
			return entityManager.merge(entity);
//		}
	}
	
	@Override
	public <T extends BaseEntity> void remover(T entity) {
		entityManager.remove(entityManager.merge(entity));
	}

	@Override
	public <T extends BaseEntity> void refresh(T entity) {
		if (entityManager.contains(entity)) {
			entityManager.refresh(entity);
		}
	}
	
	@Override
	public <T extends BaseEntity> T merge(T entity) {
		 return entityManager.merge(entity);
	}

	@Override
	public <T extends BaseEntity> T getEntidade(Class<T> classeEntidade, Long id ){
		return entityManager.find(classeEntidade, id);
	}
	
	@Override
	public List<?> getListaEntidades(Class<?> classeEntidade){
		return entityManager.createQuery("SELECT object(o) FROM "
				+ classeEntidade.getSimpleName() + " AS o").getResultList();		
	}
	
	@Override
	public List<?> getListaEntidades(Class<?> classeEntidade, String orderBy){
		return entityManager.createQuery("SELECT object(o) FROM "
				+ classeEntidade.getSimpleName() + " AS o order by "+ orderBy).getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseEntity> buscaPaginada(String jpql,
			int firstResult, int pageSize) {
		
		Query query = entityManager.createQuery(jpql);
		
		query.setFirstResult(firstResult);

		if (pageSize <= 0) {
			pageSize = PAGESIZE;
		}
		query.setMaxResults(pageSize);

		return query.getResultList();

	}
	
	@Override
	public List<BaseEntity> buscaPaginada(Class<?> classeEntidade,
			int firstResult, int pageSize, String orderBy) {
		String sql = "SELECT object(o) FROM " + classeEntidade.getSimpleName()
				+ " AS o order by " + orderBy;
		return buscaPaginada(sql, firstResult, pageSize);
	}
	
	@Override
	public Integer totalRegistros(String jpql){
		Query query;
		String[] lst = jpql.split(" ", 3);
		if (lst.length > 1){
			query = entityManager.createQuery("SELECT count(" +lst[1] +") " + lst[2]);
		}else
			query = entityManager.createQuery("SELECT count(o) FROM " + jpql + " o");
		
		return ((Long)query.getSingleResult()).intValue();
	}

	@Override
	public List<?> findByJPQL(String jpql) {
		Query query = entityManager.createQuery(jpql);
		List<?> result = query.getResultList();

		return result;
	}
	
	@Override
	public List<?> findByJPQLParam(String jpql, Map<String, Object> params) {

		Query q = entityManager.createQuery(jpql);
		for (String chave : params.keySet()) {
			q.setParameter(chave, params.get(chave));

		}
		List<?> result = q.getResultList();

		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Participante obterParticipante(Participante participante) {
		String jpql = "select distinct p from Participante p where " +
				"p.usuario.id = :idUsuario and " +
				"p.time.id = :idTime and p.campeonato.id = :idCampeonato";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idUsuario", participante.getUsuario().getId());
		params.put("idTime", participante.getTime().getId());
		params.put("idCampeonato", participante.getCampeonato().getId());
		
		List<Participante> listParticipantes = (List<Participante>) findByJPQLParam(
				jpql, params);

		if (listParticipantes.size() > 0)
			return listParticipantes.get(0);
		else
			return participante;
	}
}
