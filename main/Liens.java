package main;

import java.util.ArrayList;
import java.util.Hashtable;

import relations.Mean;
import translation.Messages;

public class Liens {

	protected static Hashtable<Objectif, String[]> moyens;

	protected static void initTable() {

		moyens = new Hashtable<Objectif, String[]>();

		// Lien hierarchique
		String[] defReg = { Messages.getString("TriadeKernel.Liens.0"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.1"), Messages.getString("TriadeKernel.Liens.2") }; //$NON-NLS-1$ //$NON-NLS-2$
		String[] evalComp = { Messages.getString("TriadeKernel.Liens.3"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.4"), Messages.getString("TriadeKernel.Liens.5") }; //$NON-NLS-1$ //$NON-NLS-2$
		String[] donConsigne = { Messages.getString("TriadeKernel.Liens.6"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.7"), Messages.getString("TriadeKernel.Liens.8"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("TriadeKernel.Liens.9"), Messages.getString("TriadeKernel.Liens.10") }; //$NON-NLS-1$ //$NON-NLS-2$
		String[] defPoli = { Messages.getString("TriadeKernel.Liens.11") }; //$NON-NLS-1$
		String[] evalPoli = { Messages.getString("TriadeKernel.Liens.12"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.13") }; //$NON-NLS-1$
		String[] delegation = { Messages.getString("TriadeKernel.Liens.14"), Messages.getString("TriadeKernel.Liens.15"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("TriadeKernel.Liens.16"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.17") }; //$NON-NLS-1$
		String[] controle = { Messages.getString("TriadeKernel.Liens.18"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.19"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.20"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.21") }; //$NON-NLS-1$
		String[] devComp = { Messages.getString("TriadeKernel.Liens.22"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.23") }; //$NON-NLS-1$
		// String[] resProb = {"Brainstorming", "Triades", "Outils usuels"};
		String[] resConflit = { Messages.getString("TriadeKernel.Liens.24"), Messages.getString("TriadeKernel.Liens.25"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("TriadeKernel.Liens.26"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.27"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.28") }; //$NON-NLS-1$
		// String[] vide = {""};

		String[] nego = {
				Messages.getString("TriadeKernel.Liens.29"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.30") }; //$NON-NLS-1$
		String[] compRend = { Messages.getString("TriadeKernel.Liens.31"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.32"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.33") }; //$NON-NLS-1$

		moyens.put(ObjectifLienHierarchique.defRegles, defReg);
		moyens.put(ObjectifLienHierarchique.evalCompetences, evalComp);
		moyens.put(ObjectifLienHierarchique.donnerConsigne, donConsigne);
		moyens.put(ObjectifLienHierarchique.definitionPolitique, defPoli);
		moyens.put(ObjectifLienHierarchique.evalutationPolitique, evalPoli);
		moyens.put(ObjectifLienHierarchique.delegationActivite, delegation);
		moyens.put(ObjectifLienHierarchique.delegationMission, delegation);
		moyens
				.put(ObjectifLienHierarchique.controleExecutionActivite,
						controle);
		moyens.put(ObjectifLienHierarchique.controleUsageOutils, controle);
		moyens.put(ObjectifLienHierarchique.developpementCompetences, devComp);
		moyens.put(ObjectifLienHierarchique.resolutionConflit, resConflit);
		moyens.put(ObjectifLienHierarchique.resolutionProbleme, resConflit);

		moyens.put(ObjectifRetourLienHierarchique.negociation, nego);
		moyens.put(ObjectifRetourLienHierarchique.compteRendu, compRend);

		// Lien partenaire

		String[] attAccord = { Messages.getString("TriadeKernel.Liens.34") }; //$NON-NLS-1$
		String[] appAccord = { Messages.getString("TriadeKernel.Liens.35") }; //$NON-NLS-1$
		String[] conAccord = { Messages.getString("TriadeKernel.Liens.36") }; //$NON-NLS-1$

		moyens.put(ObjectifLienPartenaire.appliquerAccord, appAccord);
		moyens.put(ObjectifLienPartenaire.atteindreAccord, attAccord);
		moyens.put(ObjectifLienPartenaire.controlerAccord, conAccord);

		// Formes décisionnelles
		/*String[] sujetAct = {
				Messages.getString("TriadeKernel.Liens.37"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.38"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.39"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.40"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.41"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.42"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.43"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.44") }; //$NON-NLS-1$
		String[] sujetDec = { Messages.getString("TriadeKernel.Liens.45"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.46"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.47"), Messages.getString("TriadeKernel.Liens.48"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("TriadeKernel.Liens.49") }; //$NON-NLS-1$
		String[] sujetRai = {
				Messages.getString("TriadeKernel.Liens.50"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.51"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.52"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.53"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.54"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.55"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.56"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.57"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.58"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.59"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.60"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.61"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.62") }; //$NON-NLS-1$
		String[] def = {
				Messages.getString("TriadeKernel.Liens.63"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.64") }; //$NON-NLS-1$
		String[] univRel = {
				Messages.getString("TriadeKernel.Liens.65"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.66"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.67"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.68") }; //$NON-NLS-1$
		 */
		
		String[] seCentrer = {
				Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.0"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.1"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.2"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.3"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.ObjectifFormesDecisionnelles.4")}; //$NON-NLS-1$
		
		moyens.put(ObjectifFormesDecisionnelles.sujetActeur, seCentrer);
		
/*		moyens.put(ObjectifFormesDecisionnelles.sujetActeur, sujetAct);
		moyens.put(ObjectifFormesDecisionnelles.sujetDecideur, sujetDec);
		moyens.put(ObjectifFormesDecisionnelles.sujetRaisonneur, sujetRai);
		moyens.put(ObjectifFormesDecisionnelles.defensive, def);
		moyens.put(ObjectifFormesDecisionnelles.universRelation, univRel);
*/
		// Lien Acteur objet
		String[] concep = { Messages.getString("TriadeKernel.Liens.69") }; //$NON-NLS-1$
		String[] util = { Messages.getString("TriadeKernel.Liens.70"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.71") }; //$NON-NLS-1$
		String[] recup = { Messages.getString("TriadeKernel.Liens.72"), //$NON-NLS-1$
				Messages.getString("TriadeKernel.Liens.73") }; //$NON-NLS-1$
		String[] eval = { Messages.getString("TriadeKernel.Liens.74") }; //$NON-NLS-1$

		moyens.put(ObjectifActeurObjet.conception, concep);
		moyens.put(ObjectifActeurObjet.utilisation, util);
		moyens.put(ObjectifActeurObjet.recupInfo, recup);
		moyens.put(ObjectifActeurObjet.eval, eval);

		// Lien Acteur Moyen
		String[] appl = { Messages.getString("TriadeKernel.Liens.75"), Messages.getString("TriadeKernel.Liens.76") }; //$NON-NLS-1$ //$NON-NLS-2$
		String[] utilise = { Messages.getString("TriadeKernel.Liens.77") }; //$NON-NLS-1$
		moyens.put(ObjectifActeurMoyen.applique, appl);
		moyens.put(ObjectifActeurMoyen.utilise, utilise);

		// Lien Moyen Acteur
		String[] compUtil = { Messages.getString("TriadeKernel.Liens.78") }; //$NON-NLS-1$
		String[] donneInfo = { Messages.getString("TriadeKernel.Liens.79") }; //$NON-NLS-1$

		moyens.put(ObjectifMoyenActeur.evalComp, compUtil);
		moyens.put(ObjectifMoyenActeur.defNormes, donneInfo);
		moyens.put(ObjectifMoyenActeur.securiseAction, donneInfo);

		// Lien Moyen Moyen
		String[] pasMoyen = { Messages.getString("TriadeKernel.Liens.80") }; //$NON-NLS-1$

		moyens.put(ObjectifMoyenMoyen.determineNorme, pasMoyen);
		moyens.put(ObjectifMoyenMoyen.evaluePerti, donneInfo);

		// Lien Moyen Objet

		String[] defNormes = { Messages.getString("TriadeKernel.Liens.81") }; //$NON-NLS-1$

		moyens.put(ObjectifMoyenObjet.estAdapteEff, pasMoyen);
		moyens.put(ObjectifMoyenObjet.cadreRea, defNormes);
		moyens.put(ObjectifMoyenObjet.faciliRea, defNormes);
		moyens.put(ObjectifMoyenObjet.securiseRea, defNormes);

		// Lien Objet Acteur
		String[] evalCompObj = { Messages.getString("TriadeKernel.Liens.82") }; //$NON-NLS-1$

		moyens.put(ObjectifObjetActeur.evalCompetences, evalCompObj);

		// Lien Objet Moyen

		moyens.put(ObjectifObjetMoyen.evaluePerti, donneInfo);
		moyens.put(ObjectifObjetMoyen.valideProc, donneInfo);

		// Lien Objet objet
		// Aucune relation de ce type

	}

	public static ArrayList<Mean> getStrings(Objectif obj) {
		if (moyens == null)
			initTable();
		
		ArrayList<Mean> result = new ArrayList<Mean>();
		
		for(String moyen : moyens.get(obj)) {
			result.add(new Mean(moyen));
		}
		
		return result;
	}

}
