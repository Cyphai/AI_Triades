package client.export;

import java.io.Serializable;

/*
 * enregistre les modification a effectuer sur une arrete pour un temps d'action donnée
 */

public class ExportTimeEdgeData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157472866785528461L;

	Integer actionTime; // id du temps de l'action

	/*
	 * ajouter les variable pour effectuer les modif
	 */

	public ExportTimeEdgeData(Integer actionTime) {
		this.actionTime = actionTime;
	}

	public Integer getActionTime() {
		return actionTime;
	}

}
