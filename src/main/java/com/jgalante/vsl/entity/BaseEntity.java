package com.jgalante.vsl.entity;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntity extends com.jgalante.jgcrud.entity.BaseEntity{

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRegistro;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	@Transient
	public abstract String getTextoExibicao();
	
	@PrePersist
	public void inicializaDataBase() {
		if (dataRegistro == null) {
			dataRegistro = new Date();
		}
		dataAlteracao = dataRegistro;
	}

	@PreUpdate
	public void atualizaDataBase() {
		dataAlteracao = new Date();
	}
	
	public abstract List<String> getOrderBy(); 

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime
//				* result
//				+ ((getTextoExibicao() == null) ? 0 : getTextoExibicao().hashCode());
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
