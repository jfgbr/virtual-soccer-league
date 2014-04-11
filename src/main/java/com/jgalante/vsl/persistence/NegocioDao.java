package com.jgalante.vsl.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.jgalante.vsl.entity.Grupo;
import com.jgalante.vsl.entity.Participante;
import com.jgalante.vsl.entity.Partida;
import com.jgalante.vsl.entity.TipoCampeonato;

@Stateless
public class NegocioDao implements NegocioDaoLocal {

	@Inject
	protected BaseDaoLocal baseDao;
	
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
		
		List<Participante> listParticipantes = (List<Participante>) baseDao
				.findByJPQLParam(jpql, params);
	
		if (listParticipantes.size() > 0){			
			return listParticipantes.get(0);
		}else{
//			baseDao.getEntityManager().persist(participante);
			return participante;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Partida> listarPorCampeonatoParticipanteOrdenado(Long idCampeonato, Long idParticipante) {
		String jpql = "select p from Partida p "
//				+ "join fetch p.visitante pv "
//				+ "join fetch p.mandante pm "
//				+ "join fetch pv.time " + "join fetch pm.time "
				+ "where p.mandante.campeonato.id = :idCampeonato "
				+ "and (p.mandante.id = :idParticipante or p.visitante.id = :idParticipante) "
				+ "order by p.dataRegistro asc";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idCampeonato", idCampeonato);
		params.put("idParticipante", idParticipante);
		return (List<Partida>)baseDao.findByJPQLParam(jpql, params);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Participante> listarPorGrupo(Long idGrupo) {
		String jpql = "select p from Participante p " + "where "
				+ "p.grupo.id = :idGrupo";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idGrupo", idGrupo);
		return (List<Participante>)baseDao.findByJPQLParam(jpql, params);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoCampeonato> listarTipoCampeonato() {
//		String jpql = "select t from TipoCampeonato t order by t.descricao";
		return (List<TipoCampeonato>)baseDao.getListaEntidades(TipoCampeonato.class, "o.descricao");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Grupo> listarGruposCampeonato(Long idCampeonato) {
		String jpql = "select distinct(p.grupo) from Participante p " 
				+ "where "
				+ "p.campeonato.id = :idCampeonato order by p.grupo.descricao";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idCampeonato", idCampeonato);
		return (List<Grupo>)baseDao.findByJPQLParam(jpql, params);
	}

	public BaseDaoLocal getBaseDao() {
		return baseDao;
	}

}
