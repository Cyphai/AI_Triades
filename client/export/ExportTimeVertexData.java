package client.export;

import java.io.Serializable;

/*
 * enregistre les modification a effectuer sur un sommet pour un temps d'action donnée
 */

public class ExportTimeVertexData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2513120406212602925L;

	protected Integer actionTime;

	/*
	 * info sur les changement
	 */

	public ExportTimeVertexData(Integer actionTime) {
		this.actionTime = actionTime;
	}
}
