package com.jgalante.vsl.util;

public class Estado {

	public static final Estado LISTAR = new Estado(0, "Listar");
	public static final Estado INCLUIR = new Estado(1, "Incluir");
	public static final Estado ALTERAR = new Estado(2, "Alterar");
	public static final Estado EXCLUIR = new Estado(3, "Excluir");

	private Integer id;

	private String descricao;

	public Estado(Integer id, String descricao) {
		super();
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isListar(){
		return this.equals(LISTAR);
	}
	
	public boolean isIncluir(){
		return this.equals(INCLUIR);
	}
	
	public boolean isAlterar(){
		return this.equals(ALTERAR);
	}
	
	public boolean isExcluir(){
		return this.equals(EXCLUIR);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Estado other = (Estado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
