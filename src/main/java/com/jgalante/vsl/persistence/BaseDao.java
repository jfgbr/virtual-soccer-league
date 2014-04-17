package com.jgalante.vsl.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import com.jgalante.jgcrud.persistence.GenericDAO;
import com.jgalante.vsl.entity.BaseEntity;
import com.jgalante.vsl.entity.Participante;


public class BaseDao extends GenericDAO {
	
	private static final long serialVersionUID = 1L;
	
	//	private static Logger log = LoggerFactory.getLogger(BaseDao.class);
	public static final Integer PAGESIZE = 10;

	
	public <T extends BaseEntity> T salvar(T entity) {
//		if (entity.getId() == null){
//			getEntityManager().merge(entity);
//			return entity;
//		}else{
			return getEntityManager().merge(entity);
//		}
	}
	
	
	public <T extends BaseEntity> void remover(T entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	
	public <T extends BaseEntity> void refresh(T entity) {
		if (getEntityManager().contains(entity)) {
			getEntityManager().refresh(entity);
		}
	}
	
	
	public <T extends BaseEntity> T merge(T entity) {
		 return getEntityManager().merge(entity);
	}

	
	public <T extends BaseEntity> T getEntidade(Class<T> classeEntidade, Long id ){
		return getEntityManager().find(classeEntidade, id);
	}
	
	
	public List<?> getListaEntidades(Class<?> classeEntidade){
		return getEntityManager().createQuery("SELECT object(o) FROM "
				+ classeEntidade.getSimpleName() + " AS o").getResultList();		
	}
	
	
	public List<?> getListaEntidades(Class<?> classeEntidade, String orderBy){
		return getEntityManager().createQuery("SELECT object(o) FROM "
				+ classeEntidade.getSimpleName() + " AS o order by "+ orderBy).getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	public List<BaseEntity> buscaPaginada(String jpql,
			int firstResult, int pageSize) {
		
		Query query = getEntityManager().createQuery(jpql);
		
		query.setFirstResult(firstResult);

		if (pageSize <= 0) {
			pageSize = PAGESIZE;
		}
		query.setMaxResults(pageSize);

		return query.getResultList();

	}
	
	
	public List<BaseEntity> buscaPaginada(Class<?> classeEntidade,
			int firstResult, int pageSize, String orderBy) {
		String sql = "SELECT object(o) FROM " + classeEntidade.getSimpleName()
				+ " AS o order by " + orderBy;
		return buscaPaginada(sql, firstResult, pageSize);
	}
	
	
	public Integer totalRegistros(String jpql){
		Query query;
		String[] lst = jpql.split(" ", 3);
		if (lst.length > 1){
			query = getEntityManager().createQuery("SELECT count(" +lst[1] +") " + lst[2]);
		}else
			query = getEntityManager().createQuery("SELECT count(o) FROM " + jpql + " o");
		
		return ((Long)query.getSingleResult()).intValue();
	}

	
	public List<?> findByJPQL(String jpql) {
		Query query = getEntityManager().createQuery(jpql);
		List<?> result = query.getResultList();

		return result;
	}
	
	
	public List<?> findByJPQLParam(String jpql, Map<String, Object> params) {

		Query q = getEntityManager().createQuery(jpql);
		for (String chave : params.keySet()) {
			q.setParameter(chave, params.get(chave));

		}
		List<?> result = q.getResultList();

		return result;
	}
	
	@SuppressWarnings("unchecked")
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
