package com.jgalante.vsl.view;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import com.jgalante.vsl.entity.Time;

@Named("time")
@ViewScoped
public class TimeView extends BaseView<Time> {
	private static final long serialVersionUID = 1L;
}
