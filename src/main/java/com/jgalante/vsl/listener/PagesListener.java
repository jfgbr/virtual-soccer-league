package com.jgalante.vsl.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class PagesListener implements PhaseListener {
	
	private static final long serialVersionUID = 23123123213L;
    
    // Init ---------------------------------------------------------------------------------------
    
	
	private Logger logger = LoggerFactory.getLogger(PagesListener.class);

    // Actions ------------------------------------------------------------------------------------

    /**
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {

        return PhaseId.ANY_PHASE;
    }

    /**
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(PhaseEvent event) {

        // Prepare.
        FacesContext facesContext = event.getFacesContext();
        
        try {
	        String page = facesContext.getApplication().getViewHandler().getActionURL(
	                facesContext, facesContext.getViewRoot().getViewId());
	        logger.info("beforePhase: event=[" + event.getPhaseId().toString()+"]; crudPage=[" + containsKey(facesContext, "crudPage") +"]; previousPage=["+page+"]");
        } catch (Exception e) {
        	logger.info("beforePhase: event=[" + event.getPhaseId().toString()+"]; crudPage=[" + containsKey(facesContext, "crudPage") +"];");
        }
            
    }

    /**
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        String page = facesContext.getApplication().getViewHandler().getActionURL(
                facesContext, facesContext.getViewRoot().getViewId());
        logger.info("afterPhase : event=[" + event.getPhaseId().toString()+"]; crudPage=[" + containsKey(facesContext, "crudPage") +"]; previousPage=["+page+"]");
        
    		
    }

    // Helpers ------------------------------------------------------------------------------------

    private String containsKey(FacesContext facesContext, String key) {
    	return String.valueOf(facesContext.getExternalContext().getFlash().containsKey(key)) +  "|";// + String.valueOf(ClassHelper.containsKeyPathScopeMap(facesContext, key));
    }
}
