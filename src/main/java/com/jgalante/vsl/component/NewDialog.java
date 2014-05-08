package com.jgalante.vsl.component;

import javax.faces.component.FacesComponent;

import org.primefaces.component.dialog.Dialog;

@FacesComponent("newDialog")
public class NewDialog extends Dialog{
//	protected enum PropertyKeys {
//
//		value, viewOnly, processAceitar, updateAceitar, actionAceitar, actionCancelar;
//
//		String toString;
//
//		PropertyKeys(String toString) {
//			this.toString = toString;
//		}
//
//		PropertyKeys() {
//		}
//
//		public String toString() {
//			return ((this.toString != null) ? this.toString : super.toString());
//		}
//	}

	public static final String COMPONENT_TYPE = "com.jgalante.vsl.component.NewDialog";
	public static final String COMPONENT_FAMILY = "com.jgalante.vsl.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.component.dialog.DialogRenderer";

	public NewDialog() {
		setRendererType(DEFAULT_RENDERER);
	}
}
