package com.jgalante.vsl.persistence;

import java.util.List;

import com.jgalante.vsl.entity.Grupo;
import com.jgalante.vsl.entity.Participante;
import com.jgalante.vsl.entity.Partida;
import com.jgalante.vsl.entity.TipoCampeonato;

public interface NegocioDaoLocal {
	
	public Participante obterParticipante(Participante participante);

	public List<Partida> listarPorCampeonatoParticipanteOrdenado(Long idCampeonato, Long idParticipante);
	
	public List<TipoCampeonato> listarTipoCampeonato();
	
	public List<Participante> listarPorGrupo(Long idGrupo);
	
	public List<Grupo> listarGruposCampeonato(Long idCampeonato);
	
	public BaseDaoLocal getBaseDao();
}
