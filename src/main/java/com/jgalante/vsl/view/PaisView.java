package com.jgalante.vsl.view;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import com.jgalante.vsl.entity.Pais;

@Named("pais")
@ViewScoped
public class PaisView  extends BaseView<Pais> {
	private static final long serialVersionUID = 1L;
}
