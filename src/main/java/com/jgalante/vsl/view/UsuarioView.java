package com.jgalante.vsl.view;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import com.jgalante.vsl.entity.Usuario;

@Named("usuario")
@ViewScoped
public class UsuarioView extends BaseView<Usuario> {
	private static final long serialVersionUID = 1L;
	
}
