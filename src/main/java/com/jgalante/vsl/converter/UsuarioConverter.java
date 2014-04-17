package com.jgalante.vsl.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.jgalante.vsl.entity.Usuario;

@FacesConverter(value="usuarioConverter")
public class UsuarioConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
            	String[] dados = submittedValue.split(":");
            	Long number = Long.parseLong(dados[0]);
                Usuario usuario = new Usuario();
                usuario.setId(number);
                usuario.setNome(dados[1]);
                return usuario;
                
            } catch(NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Usuario"));
            }
        }

    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return null;
        } else {
        	Usuario usuario = (Usuario) value; 
            return usuario.getId()+ ":" + usuario.getNome();
        }
    }
}