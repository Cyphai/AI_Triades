package dataPack;

import encrypting.Encrypting;
import encrypting.Key;
import encrypting.KeyE;
import encrypting.KeyI;
import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.ExecutionMode;
import graphicalUserInterface.KeyGenerator;
import graphicalUserInterface.Program;
import graphicalUserInterface.UnlockingPopup;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.ActionTime;
import main.ActionTimeListe;
import main.JeuRelation;
import main.RelationComplete;
import models.Brick;
import models.BrickEdge;
import models.BrickVertex;
import relations.EnsembleRelation;
import relations.Goal;
import relations.Mean;
import relations.RelationDescription;
import relations.RelationPossibility;
import tools.GenericTools;
import tools.StringCreator;
import translation.Messages;
import Mailing.MailSender;
import client.Session;
import client.export.ExportedDatapackModule;
import client.stringTranslator.StringTranslator;

public class DataPack implements SavableObject, ObjectInputValidation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5641347596624328060L;

	public short majorVersion;
	public short mediumVersion;
	public short minorVersion;

	protected Vector<Brick> brickList;
	// protected ActeursEntreprise actors;
	protected ActionTimeListe actionTimeListe;
	protected Vector<String> steps; 


	protected ActivitesEntreprise activities;
	protected MoyenEntreprise moyens;
	protected String name;
	protected String filePath;
	protected Vector<JeuActeur> jeuxActeur;
	protected Integer newActorId;
	protected Integer newGroupeId;
	protected ModelActeursEntreprise actorsModel;
	protected EnsembleRelation relationSet;
	protected CompanyInfo companyInfo;
	protected transient FiltreActeursEntreprise dataPackActorsView;

	protected transient Session currentSession;

	protected ExportedDatapackModule exportModule;

	protected HashMap<Integer, MyDefaultMutableTreeNode> allActors;
	protected StringTranslator stringTranslator;
	protected Calendar endDemoCalendar;
	protected Vector<Integer> isDemoValid;
	private boolean unlocked;
	private String unlockLogin;
	
	private static DataPack lastLoadedDatapack;
	
	protected ProgramSettings programSettings;

	public DataPack(String _name) {
		newActorId = new Integer(0);
		newGroupeId = new Integer(0);
		actorsModel = new ModelActeursEntreprise(this);
		brickList = new Vector<Brick>();
		activities = new ActivitesEntreprise();
		moyens = new MoyenEntreprise();
		actionTimeListe = new ActionTimeListe();
		jeuxActeur = new Vector<JeuActeur>();
		steps = new Vector<String>();
		newActorId = new Integer(0);
		name = _name;
		filePath = null;
		allActors = new HashMap<Integer, MyDefaultMutableTreeNode>();
		dataPackActorsView = null;
		relationSet = new EnsembleRelation();
		exportModule = null;
		companyInfo = new CompanyInfo();
		stringTranslator = new StringTranslator();
		endDemoCalendar = null;
		isDemoValid = null;
		programSettings = new ProgramSettings();


		majorVersion = Program.majorVersion;
		mediumVersion = Program.mediumVersion;
		minorVersion = Program.minorVersion;
	}

	public void init() {
		if(endDemoCalendar != null && endDemoCalendar.before(Calendar.getInstance())) {
			isDemoValid = new Vector<Integer>();
			isDemoValid.add(new Integer(42));
			isDemoValid.add(new Integer(39076));
		}

		// Supprime la date de validité 
		//		{DialogHandlerFrame.showErrorDialog("Mise à jour du datapack, plus de date de validité");
		//		endDemoCalendar = null;
		//		isDemoValid = null;
		//		Program.save(Program.myMainFrame.datapack, true);
		//		System.exit(0);}

		checkValidity();
		checkVersion();
		sortBrick();

		actorsModel.setDataPack(this);
	}
	
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();
		ois.registerValidation(this, 10);
	}

	public void addAnActor(Acteur newNode) {
		allActors.put(newNode.getId(), newNode);
	}

	public Activite addActivity(String nom, int iconId) {
		return activities.ajouterActivite(nom, iconId);
	}

	public boolean renameActivity(String oldName, String newName)
	{
		return activities.renameActivity(oldName,newName);
	}

	public Moyen addMoyen(String nom, Integer idGenerique) {
		if (idGenerique != null) {
			Moyen newMoyen = moyens.ajouterMoyen(idGenerique, nom);
			allActors.put(newMoyen.getId(), newMoyen);
			return newMoyen;
		}
		return null;
	}

	public void addActionTime(String name) {
		ActionTime newActionTime = actionTimeListe.addActionTime(name);
		for (Brick brick : brickList) {
			for (BrickEdge edge : brick.getEdges()) {
				edge.getCompleteRelation().addJeuRelation(newActionTime);
			}
		}
	}

	public boolean removeActionTime(ActionTime actionTime) {
		int index = actionTimeListe.values().indexOf(actionTime);
		boolean ok = true;
		for (Brick brick : getBrickList()) {
			for (BrickEdge edge : brick.getEdges()) {
				RelationComplete relation = edge.getCompleteRelation();
				if (!relation.getJeuRelation(index).isEmpty(
						RelationPossibility.RELATIONSTRUCTURELLE))
				{
					ok = false;
					break;
				}
			}
		}

		if (!ok && DialogHandlerFrame.showYesNoCancelDialog(Messages.getString("DataPack.0")) != JOptionPane.YES_OPTION) //$NON-NLS-1$
			return false;


		for (Brick brick : getBrickList()) {
			for (BrickEdge edge : brick.getEdges()) {
				RelationComplete relation = edge.getCompleteRelation();
				relation.removeJeuRelation(index);
			}
		}

		actionTimeListe.removeActionTime(actionTime);



		return true;
	}

	public boolean renameActionTime(String oldName, String newName)
	{
		if (actionTimeListe.getActionTimeByName(newName) != null)
			return false;

		actionTimeListe.getActionTimeByName(oldName).setName(newName);
		return true;
	}

	public void addStep(String name) {
		steps.add(name);
	}

	public boolean removeStep(String name) {
		boolean ok = true;
		for (Brick brick : brickList) {
			if (brick.getNoTranslatedStep().equals(name))
				ok = false;
		}

		if (ok)
			steps.remove(name);
		else
			JOptionPane
			.showMessageDialog(
					null,
					Messages.getString("DataPack.1"), //$NON-NLS-1$
					Messages.getString("DataPack.2"), //$NON-NLS-1$
					JOptionPane.WARNING_MESSAGE);

		return ok;
	}

	public boolean renameStep(String oldName, String newName)
	{
		if (steps.contains(newName))
			return false;
		int index = steps.indexOf(oldName);
		steps.remove(oldName);
		steps.insertElementAt(newName, index);

		for (Brick brick : brickList) 
			if (brick.getNoTranslatedStep().equals(oldName))
				brick.setStep(newName);


		return true;
	}

	public boolean addActor(Integer idGenerique, String nom) {
		Acteur newActor = dataPackActorsView.getTreeListener().ajouterActeur(
				idGenerique, nom);
		if (newActor != null) {
			allActors.put(newActor.idActeur, newActor);
			return true;
		}
		return false;
	}

	public boolean addGroup(String nom) {
		return dataPackActorsView.getTreeListener().ajouterGroupe(nom) != null;
	}



	public synchronized void removeActor() {
		Vector<Content> oldContents = dataPackActorsView.getTreeListener().getSelectedContents();
		//		Content oldContent = dataPackActorsView.getTreeListener().deleteActor();

		if (oldContents != null && oldContents.size() > 0) {
			if(deleteContentsConfirmation(oldContents) == false) {
				return;
			}

			removeContentsInBrick(oldContents);
			dataPackActorsView.getTreeListener().deleteActor();
		}
	}

	private boolean deleteContentConfirmation(Content oldContent) {
		Vector<Content> oldContents = new Vector<Content>();
		oldContents.add(oldContent);
		return deleteContentsConfirmation(oldContents);
	}

	private boolean deleteContentsConfirmation(Collection<Content> oldContents) {
		String message = Messages.getString("DataPack.3"); //$NON-NLS-1$

		int brickMax = 2;
		int contentMax = 2;

		int contentCount = 0;
		boolean askConfirmation = false;
		for(Content content : oldContents) {
			int brickCount = 0;
			for(Brick brick : brickList) {
				if(brick.contains(content)) {
					if(brickCount == 0) {
						contentCount++;
					}

					if(brickCount < brickMax && contentCount < contentMax) {
						if(brickCount == 0) {
							askConfirmation = true;
							message += "    " + content + " : \n"; //$NON-NLS-1$ //$NON-NLS-2$
						}
						message += "        - " + brick + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
					}

					brickCount++;
				}
			}

			if(brickCount > brickMax) {
				message += "        - ..." + (brickCount - brickMax) + Messages.getString("Version_0_9_9_9.DataPack.28"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		if(contentCount > contentMax+1) {
			message += "    ..." + (contentCount - contentMax) + Messages.getString("Version_0_9_9_9.DataPack.30"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if(askConfirmation) {
			if(DialogHandlerFrame.showYesNoDialog(message) != JOptionPane.YES_OPTION) {
				return false;
			}
		}

		return true;
	}

	protected synchronized void removeContentInBrick(Content oldContent) {
		for (Brick brick : brickList)
			brick.removeContent(oldContent);
	}

	protected synchronized void removeContentsInBrick(Vector<Content> oldContents) {
		for(Content oldContent : oldContents) {
			for (Brick brick : brickList) {
				brick.removeContent(oldContent);
			}
		}
	}

	public MyDefaultMutableTreeNode getActor(Integer id) {
		MyDefaultMutableTreeNode node = allActors.get(id);
		if (node == null) {
			System.out.println(allActors.keySet() + "\n On cherchait : " + id); //$NON-NLS-1$
			throw new NullPointerException();
		}
		return allActors.get(id);
	}

	public Vector<Brick> getBrickList() {
		return brickList;
	}

	public Vector<Moyen> getMoyenListe() {
		return moyens.getMoyenVector();
	}

	public Moyen getMoyen(Integer id) {
		return moyens.getMoyen(id);
	}

	public MoyenEntreprise getMoyens() {
		return moyens;
	}

	public Vector<ActionTime> getActionTimeList() {
		return actionTimeListe.values();
	}

	public ActivitesEntreprise getActivities() {
		return activities;
	}


	public ModelActeursEntreprise getActorsModel() {
		return actorsModel;
	}

	public Integer getNewActorId() {
		Integer newId = newActorId;
		newActorId = new Integer(newActorId.intValue() + 1);
		return newId;
	}

	public Integer getNewGroupeId() {
		Integer newId = newGroupeId;
		newGroupeId = new Integer(newGroupeId.intValue() + 1);
		return newId;
	}



	@Override
	public String toString() {
		return name;
	}

	@Override
	synchronized public String getFilePath() {
		//		System.out.println("getFilePath (" + filePath + ")" + Thread.currentThread().getStackTrace()); //$NON-NLS-1$ //$NON-NLS-2$
		return filePath;
	}

	@Override
	synchronized public void setFilePath(String _filePath) {
		//		System.out.println("setFilePath (" + _filePath + ")" + Thread.currentThread().getStackTrace()); //$NON-NLS-1$ //$NON-NLS-2$
		filePath = _filePath;
	}



	public Vector<String> getSteps() {
		return steps;
	}

	public Vector<JeuActeur> getJeuxActeur() {
		return jeuxActeur;
	}

	public JeuActeur getJeuActeurByName(String name) {
		for (JeuActeur ja : jeuxActeur) {
			if (name.equals(ja.toString())) {
				return ja;
			}
		}
		return null;
	}

	public void initDataPackActorsView(FiltreActeursEntreprise view) {
		if (dataPackActorsView == null) {
			dataPackActorsView = view;
		}
	}

	public void addJeuActeur(JeuActeur jA) {
		// ajoute toutes lesbases
		for (Acteur newActor : jA.bases) {
			allActors.put(newActor.idActeur, newActor);
		}

		// ajoute tout les corps
		for (String corps : jA.corpsMetier) {
			for (Acteur actor : jA.getCorps(corps)) {
				allActors.put(actor.idActeur, actor);
			}
		}

		jeuxActeur.add(jA);
	}

	public void removeJeuActeur(JeuActeur deletedActorSet) {
		for (String nomCorps : deletedActorSet.getListeCorps()) {
			for (ActeurBase actor : deletedActorSet.getCorps(nomCorps)) {
				removeContentInBrick(actor);
			}
		}
		for (ActeurBase actor : deletedActorSet.getListeBase()) {
			removeContentInBrick(actor);
		}

		jeuxActeur.remove(deletedActorSet);
	}

	public void updateAllActors() {
		for (JeuActeur ja : jeuxActeur) {
			addJeuActeur(ja);
		}
	}

	public boolean removeActivity(Activite deletedActivity) {
		boolean ok = true;
		for (Brick brick : brickList) {
			if (brick.getActivity().equals(deletedActivity))
				ok = false;
		}
		if (ok)
			activities.supprimerActivite(deletedActivity);
		else
			JOptionPane
			.showMessageDialog(
					null,
					Messages.getString("DataPack.5"), //$NON-NLS-1$
					Messages.getString("DataPack.6"), //$NON-NLS-1$
					JOptionPane.WARNING_MESSAGE);
		return ok;
	}

	public boolean removeMoyen(Moyen deletedMoyen) {
		if(deleteContentConfirmation(deletedMoyen) == false) {
			return false;
		}
		removeContentInBrick(deletedMoyen);
		moyens.supprimerMoyen(deletedMoyen.getId());
		return true;
		//		boolean ok = true;
		//		for (Brick brick : brickList) {
		//			for (BrickVertex vertex : brick.getVertices()) { // Fusionner la suppresion des acteur et des moyen demander confirmation si c'est present dans une brique
		//				if (vertex.getContent().equals(deletedMoyen.getId())) {
		//					ok = false;
		//					break;
		//				}
		//			}
		//		}
		//
		//		if (ok) {
		//			removeContentInBrick(deletedMoyen);
		//			moyens.supprimerMoyen(deletedMoyen.getId());
		//		} else {
		//			JOptionPane
		//			.showMessageDialog(
		//					null,
		//					Messages.getString("DataPack.7"), //$NON-NLS-1$
		//					Messages.getString("DataPack.8"), //$NON-NLS-1$
		//					JOptionPane.WARNING_MESSAGE);
		//		}
		//		
		//		return ok;
	}

	public void exportDataPack() {

		/*	if (!checkRelationForExport())
		{
			DialogHandlerFrame.showErrorDialog("Ce datapack ne peut pas être exporté. Veuillez corriger les erreurs signalées avant de relancer la procédure d'exportation.");
			return;
		}*/

		boolean undefinedRelation = false;
		String result = ""; //$NON-NLS-1$

		for(Brick brick : getBrickList()) {
			for(BrickEdge edge : brick.getEdges()) {
				for(JeuRelation relation : edge.getCompleteRelation().getEnsembleRelation()) {
					if(relation.getState() == RelationComplete.RELATION_INCOMPLETE) {
						result += Messages.getString("Version_0_9_9_9.DataPack.32") + brick.toString()+ "\"\n"; //$NON-NLS-1$ //$NON-NLS-2$
						undefinedRelation = true;
					}
				}
			}
		}

		if(undefinedRelation) {
			if(DialogHandlerFrame.showYesNoDialog(Messages.getString("Version_0_9_9_9.DataPack.34") + result) != JOptionPane.YES_OPTION) { //$NON-NLS-1$
				return;
			}
		}

		String oldFilePath = filePath;
		filePath = null;

		exportModule = new ExportedDatapackModule(this);

		Calendar oldCalendaire = endDemoCalendar;
		Key exportKey = null;
		int validationTime = 0;

		if(ExecutionMode.isDebug()) {
			//Demande de limite de temps et d'une nouvelle clefs.
			validationTime = new Integer(JOptionPane.showInputDialog("Durée de validité du datapack en jours (0 pour aucune limite de temps)"));
			int firstIndex = new Integer(JOptionPane.showInputDialog("Premier numero"));
			int secondIndex = new Integer(JOptionPane.showInputDialog("Second numero"));

			exportKey = new KeyE(firstIndex, secondIndex);
			if(validationTime > 0) {
				endDemoCalendar = Calendar.getInstance();
				endDemoCalendar.add(Calendar.DAY_OF_MONTH, validationTime);
			} else {
				endDemoCalendar = null;
			}

			System.out.println("Export d'un datapck depuis une version Debug\n  - Temps de validité = " + validationTime + "jours\n  - Premier nombre = " + firstIndex + "\n  - Second nombre = " + secondIndex);
			DialogHandlerFrame.showErrorDialog("Datapack exporté avec " + validationTime + " jours de validation\nPremier nombre = " + firstIndex + "\nSecond nombre = " + secondIndex);

		} else {
			//Pas de limite de temps et clef de robert
			System.out.println("DataPack.exportDataPack() en mode extern");
			exportKey = new KeyI();
			endDemoCalendar = null;
		}

		Vector<Brick> addedBrick = new Vector<Brick>();

		Vector<Brick> brickListTemps = new Vector<Brick>(brickList);

		for (Brick brick : brickListTemps) {
			if (brick.isGeneric()) {
				Collection<Brick> exportedBrick = brick.createNoGenericBrick();
				addedBrick.addAll(exportedBrick);
				brickList.addAll(exportedBrick);
			}
		}
		try {
			//TODO faire une fenêtre pour donner une clef de cryptage
			Encrypting.getInstance().setKey(exportKey.getKey());
			Program.save(this, Messages.getString("DataPack.9"), Messages.getString("DataPack.10"), true); //$NON-NLS-1$ //$NON-NLS-2$
			Encrypting.getInstance().setProgramKey();
		} catch (Exception e) {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("DataPack.11") //$NON-NLS-1$
					+ e.toString());
			e.printStackTrace();
		}

		endDemoCalendar = oldCalendaire;
		exportModule = null;
		filePath = oldFilePath;
		brickList.removeAll(addedBrick);
	}

	public boolean checkRelationForExport()
	{
		boolean ok = true;
		for (Brick b : brickList)
		{
			for (BrickEdge bE : b.getEdges())
			{
				RelationPossibility possibility = relationSet.getRelationPossibility(bE.getSource().getContent(), bE.getDestination().getContent(), true);
				int i = 0;
				for (JeuRelation jR : bE.getCompleteRelation().getEnsembleRelation())
				{
					ArrayList<Mean> means = possibility.getMap().get(jR.objectifStructurel);

					if (jR.objectifStructurel.equals(RelationPossibility.UNDEFINED_GOAL))
					{
						DialogHandlerFrame.showErrorDialog(Messages.getString("DataPack.15")+b.getName()+Messages.getString("DataPack.16")+bE.getSource()+Messages.getString("DataPack.17")+bE.getDestination()+Messages.getString("DataPack.18")+actionTimeListe.values().elementAt(i)+Messages.getString("DataPack.19")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
						ok = false;
					}

					if (means == null || !means.contains(jR.moyenStructurel))
					{
						DialogHandlerFrame.showErrorDialog(Messages.getString("DataPack.20")+b.getName()+Messages.getString("DataPack.21")+bE.getSource()+Messages.getString("DataPack.22")+bE.getDestination()+Messages.getString("DataPack.23")+actionTimeListe.values().elementAt(i)+Messages.getString("DataPack.24")+RelationPossibility.UNDEFINED_STRING+"."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
						ok = false;
						jR.moyenStructurel = RelationPossibility.UNDEFINED_MEAN;
						jR.objectifStructurel = RelationPossibility.UNDEFINED_GOAL;
					}
					i++;
				}
			}
		}
		return ok;
	}

	public EnsembleRelation getRelations() {
		return relationSet;

	}

	public void setRelations(EnsembleRelation ensembleRelation) {
		System.out.println(Messages.getString("DataPack.12")); //$NON-NLS-1$
		relationSet = ensembleRelation;

	}

	public ExportedDatapackModule getExportModule() {
		return exportModule;
	}

	public CompanyInfo getCompanyInfo() {
		if(companyInfo == null) {
			companyInfo = new CompanyInfo(); //TODO a virer dans la fonction de mise a jour de version du datapack
		}
		return companyInfo;
	}

	public Collection<MyDefaultMutableTreeNode> getAllActors() {
		return allActors.values();
	}

	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	public StringTranslator getStringTranslator() {
		return stringTranslator;
	}

	public void setStringTranslator(StringTranslator stringTranslator) {
		this.stringTranslator = stringTranslator;
	}

	public void loadNewTranslation()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter(Messages.getString("Version_1_0_1.DataPack.31"), Messages.getString("Version_1_0_1.DataPack.30")));
		int returnVal = fileChooser.showOpenDialog(Program.myMainFrame);
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;
		File f = fileChooser.getSelectedFile();
		Object loadedObject = null;
		boolean retry = false;
		try{
			loadedObject = Program.loadSavableObject(f.getAbsolutePath(), null, null, true); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (Exception e)
		{
			e.printStackTrace();

			retry = true;
		}
		if (retry || loadedObject == null)
		{
			if (Program.isTriades() || Program.isTriadesLoading())
			{
				Moyen.setOldHashCodeMode(true);
				try{
					loadedObject = Program.loadSavableObject(f.getAbsolutePath(), null, null, true); //$NON-NLS-1$ //$NON-NLS-2$

				}
				catch (Exception er)
				{
					DialogHandlerFrame.showErrorDialog("Error during translation file loading"); //$NON-NLS-1$
					er.printStackTrace();
					return;
				}
			}
			else
			{
				DialogHandlerFrame.showErrorDialog("Error during translation file loading");//$NON-NLS-1$
				return;
			}
		}
		if (loadedObject != null && loadedObject instanceof StringTranslator)
		{
			StringTranslator newTranslation = (StringTranslator)loadedObject;
			setStringTranslator(newTranslation);
			if (Program.myMainFrame != null)
				Program.myMainFrame.repaint();

			DialogHandlerFrame.showInformationDialog(Messages.getString("Version_1_0_3.DataPack.32")); //$NON-NLS-1$
		}
	}

	public void removeBrick(Brick brick) {
		brickList.remove(brick);
	}

	public void checkDatapackValidity()
	{
		if (activities.activities == null)
		{
			activities.activities = new Vector<Activite>();
			for (Brick b : brickList)
			{
				if (!activities.activities.contains(b.getActivity()))
				{
					activities.activities.add(b.getActivity());
				}
			}
			DialogHandlerFrame.showErrorDialog(Messages.getString("DataPack.26")); //$NON-NLS-1$
		}

		if (actionTimeListe.values() == null)
		{
			actionTimeListe.restoreFromOldVersion();
		}


	}

	public void cleanRelation() {
		int okMeanings = 0, orphanMeanings = 0, okGoals = 0, orphanGoals = 0;
		int nullEntry = 0;
		int scannedGoals = 0;
		int recreatedPossibility = 0;
		int deletedEmptyMean = 0;

		for (RelationDescription rD : relationSet.getRelationDescriptions())
		{
			Vector<Goal> toAdd = new Vector<Goal>();
			for (Goal g : rD.getPossibility().getMap().keySet())
			{
				if (rD.getPossibility().getMap().get(g) == null)
				{
					toAdd.add(g);
					recreatedPossibility ++;
				}

			}
			for( Goal g : toAdd)
				rD.getPossibility().getMap().put(g, new ArrayList<Mean>());

			for (Entry<Goal, ArrayList<Mean>> entry : rD.getPossibility().getMap().entrySet())
			{
				boolean removed = false;
				for (int i = 0; i < entry.getValue().size(); i += (removed?0:1))
				{
					removed = false;
					if (entry.getValue().get(i).toString().trim().equals(""))
					{
						entry.getValue().remove(i);
						removed = true;
						deletedEmptyMean++;
					}
				}
			}
		}

		String orphanGoalList = "", orphanMeaningList = ""; //$NON-NLS-1$ //$NON-NLS-2$

		for (Brick b : brickList)
		{
			for (BrickEdge bE : b.getEdges())
			{
				RelationPossibility possiblity = getRelations().getRelationPossibility(bE.getSource().getContent(), bE.getDestination().getContent(), false);
				for (JeuRelation jR : bE.getCompleteRelation().getEnsembleRelation())
				{
					jR.objectifReel = RelationPossibility.UNDEFINED_GOAL;
					jR.moyenReel = RelationPossibility.UNDEFINED_MEAN;

					if (jR.moyenStructurel == null || jR.objectifStructurel == null)
					{
						jR.moyenStructurel = RelationPossibility.UNDEFINED_MEAN;
						jR.objectifStructurel = RelationPossibility.UNDEFINED_GOAL;
						nullEntry ++;
					}
					else
					{

						Goal structuralGoal = findGoal(jR.objectifStructurel, jR.moyenStructurel, possiblity);

						Mean structuralMeaning = findMean(jR.objectifStructurel,jR.moyenStructurel, possiblity);
						if (structuralMeaning != null)
						{
							if (structuralMeaning != jR.moyenStructurel)
							{
								jR.moyenStructurel = structuralMeaning;
								okMeanings ++;
							}
						}
						else
						{
							orphanMeaningList += " "+jR.moyenStructurel.toString()+" ("+b.getName()+Messages.getString("Version_0_9_9_9.DataPack.36")+bE.getSource()+Messages.getString("Version_0_9_9_9.DataPack.39")+bE.getDestination()+") "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
							jR.moyenStructurel = RelationPossibility.UNDEFINED_MEAN;
							orphanMeanings ++;
							orphanMeaningList += "\n"; //$NON-NLS-1$

						}

						if (structuralGoal != null)
						{
							if (structuralGoal != jR.objectifStructurel)
							{
								jR.objectifStructurel = structuralGoal;
								okGoals++;
							}
						}
						else
						{
							orphanGoalList += " "+jR.objectifStructurel.toString()+" ("+b.getName()+Messages.getString("Version_0_9_9_9.DataPack.46")+bE.getSource()+Messages.getString("Version_0_9_9_9.DataPack.47")+bE.getDestination()+") \n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
							jR.objectifStructurel = RelationPossibility.UNDEFINED_GOAL;
							orphanGoals++;
						}
					}



					scannedGoals += 2;
				}
			}
		}

		DialogHandlerFrame.showErrorDialog(Messages.getString("DataPack.37")+scannedGoals+Messages.getString("DataPack.38")+okGoals+"/"+orphanGoals+Messages.getString("DataPack.40")+okMeanings+"/"+orphanMeanings+" - Moyens vides supprimés : "+deletedEmptyMean); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		System.out.println(Messages.getString("DataPack.42")+orphanGoalList+Messages.getString("DataPack.43")+orphanMeaningList+Messages.getString("Version_0_9_9_9.DataPack.4")+recreatedPossibility+Messages.getString("Version_0_9_9_9.DataPack.50")+nullEntry+Messages.getString("Version_0_9_9_9.DataPack.51")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	}

	public Goal findGoal(Goal originalGoal, Mean originalMean, RelationPossibility possibilities)
	{
		if (originalGoal.toString().equalsIgnoreCase(RelationPossibility.UNDEFINED_GOAL.toString()))
			return RelationPossibility.UNDEFINED_GOAL;
		if (originalGoal.toString().equalsIgnoreCase(RelationPossibility.NORELATION_GOAL.toString()))
			return RelationPossibility.NORELATION_GOAL;



		for (Goal g : possibilities.getMap().keySet())
		{
			if (g.toString().equalsIgnoreCase(originalGoal.toString()))

			{
				ArrayList<Mean> meanings = possibilities.getMap().get(g);
				for (Mean m : meanings)
				{
					if (m.toString().trim().equalsIgnoreCase(originalMean.toString().trim()))
						return g;
				}

			}

		}

		return null;
	}

	public Mean findMean(Goal originalGoal, Mean originalMean, RelationPossibility possibilities)
	{
		if (originalMean.toString().equalsIgnoreCase(RelationPossibility.UNDEFINED_MEAN.toString()))
			return RelationPossibility.UNDEFINED_MEAN;
		if (originalMean.toString().equalsIgnoreCase(RelationPossibility.NORELATION_MEAN.toString()))
			return RelationPossibility.NORELATION_MEAN;


		for (Goal g : possibilities.getMap().keySet())
		{
			if (g.toString().equalsIgnoreCase(originalGoal.toString()))

			{
				ArrayList<Mean> meanings = possibilities.getMap().get(g);
				for (Mean m : meanings)
				{
					if (m.toString().trim().equalsIgnoreCase(originalMean.toString().trim()))
						return m;
				}

			}

		}

		return null;
	}

	public void checkActors()
	{
		int deleted = 0;
		int replaced = 0;
		Vector<Acteur> toBeDeleted = new Vector<Acteur>();
		HashMap<Acteur, Acteur> translation = new HashMap<Acteur, Acteur>();
		System.out.println(Messages.getString("Version_0_9_9_9.DataPack.52")); //$NON-NLS-1$
		for (Entry<Integer, Acteur> entry : actorsModel.allActeurs.entrySet())
		{
			if (entry.getValue().getParent() == null)
			{
				Acteur newActor = findValidActorByName(entry.getValue().getPoste());
				if (newActor != null)
				{
					translation.put(entry.getValue(), newActor);
					System.out.println(Messages.getString("Version_0_9_9_9.DataPack.53")+entry.getValue()+Messages.getString("Version_0_9_9_9.DataPack.54")+newActor); //$NON-NLS-1$ //$NON-NLS-2$
					replaced++;
				}
				else
				{
					toBeDeleted.add(entry.getValue());
					deleted++;
					System.out.println(Messages.getString("Version_0_9_9_9.DataPack.55")+entry.getValue()+Messages.getString("Version_0_9_9_9.DataPack.56")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}

		for (Brick b : brickList)
		{

			for (BrickVertex bV : b.getVertices())
			{
				if (bV.getContent() instanceof Acteur)
				{
					if (((Acteur)bV.getContent()).getParent() == null)
					{
						Acteur newActor = translation.get(bV.getContent());
						if (newActor == null)
						{
							DialogHandlerFrame.showErrorDialog(Messages.getString("Version_0_9_9_9.DataPack.57")+bV.getContent()+Messages.getString("Version_0_9_9_9.DataPack.58")); //$NON-NLS-1$ //$NON-NLS-2$
							return;

						}
						bV.setContent(newActor);
					}

				}

			}

			LinkedList<BrickVertex> toRemove = new LinkedList<BrickVertex>();
			for (BrickVertex bV : b.getGenericActors())
			{
				if (b.getBrickVertexByContent(bV.getContent()) == null)
					toRemove.add(bV);

			}
			for (BrickVertex bV : toRemove)
				b.getGenericActors().remove(bV);

		}
		int temp = allActors.size();
		allActors.values().retainAll(actorsModel.allActeurs.values());
		temp -= allActors.size();
		System.out.println(Messages.getString("Version_0_9_9_9.DataPack.59")); //$NON-NLS-1$

		for (Acteur a : toBeDeleted)
		{
			actorsModel.allActeurs.remove(a.getId());
			allActors.remove(a.getId());
		}
		for (Acteur a : translation.keySet())
		{
			actorsModel.allActeurs.remove(a.getId());
		}

		DialogHandlerFrame.showErrorDialog(replaced+Messages.getString("Version_0_9_9_9.DataPack.60")+deleted+Messages.getString("Version_0_9_9_9.DataPack.61")+temp+Messages.getString("Version_0_9_9_9.DataPack.62")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private Acteur findValidActorByName(String name)
	{
		for (Entry<Integer, Acteur> entry : actorsModel.allActeurs.entrySet())
		{
			if (entry.getValue().getPoste().trim().equalsIgnoreCase(name.trim()) && entry.getValue().getParent() != null)
				return entry.getValue();

		}
		return null;
	}

	private boolean isValid() {
		return isDemoValid == null;
	}

	private void checkValidity() {
		if(isValid() == false) {
			Program.save(this, true);
			if (DialogHandlerFrame.showYesNoCancelDialog(Messages.getString("Version_1_0_3.Datapack.39")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
			{
				if (!UnlockingPopup.showPopup())
					System.exit(0);
			}
			else	
				System.exit(0);
		}
	}

	private String getDatapackVersionString() {
		return StringCreator.createVersionString(majorVersion, mediumVersion, minorVersion);
	}

	private void checkVersion() {
		if(majorVersion <= 0) {
			majorVersion = 1;
			mediumVersion = 0;
			minorVersion = 0;
		}

		if(majorVersion != Program.majorVersion || mediumVersion != Program.mediumVersion) {
			DialogHandlerFrame.showErrorDialog(Messages.getString("Version_0_9_9_9.DataPack.64") + getDatapackVersionString() + Messages.getString("Version_0_9_9_9.DataPack.65") + Program.getSoftwareString()); //$NON-NLS-1$ //$NON-NLS-2$
			if(ExecutionMode.isDebug() == false) {
				System.exit(0);
			}
		}
	}

	public int countGoalUsage(Goal goal) {
		int result = 0;
		for (Brick b : brickList)
		{
			for (BrickEdge bE : b.getEdges())
			{
				if (bE.getCompleteRelation().contains(goal))
					result ++;
			}
		}
		return result;
	}

	public void renameAllRelations() {
		Vector<Goal> toModifyGoals = new Vector<Goal>();
		Vector<String> newNames = new Vector<String>();
		boolean stop = false;
		for(RelationDescription relation : relationSet.getRelationDescriptions()) {
			toModifyGoals.clear();
			newNames.clear();


			for(Goal goal : relation.getPossibility().getMap().keySet()) {

				String newName = (String)JOptionPane.showInputDialog(Program.myMainFrame, Messages.getString("Version_0_9_9_9.DataPack.66") + goal.toString(), "", JOptionPane.QUESTION_MESSAGE, null, null, goal.toString().trim()); //$NON-NLS-1$ //$NON-NLS-2$

				if(newName == null) {
					stop = true;
					break;
				}

				newName = newName.trim();
				if(newName.equalsIgnoreCase("") == false) { //$NON-NLS-1$
					toModifyGoals.add(goal);
					newNames.add(newName);

				}
			}

			for (int i = 0; i < toModifyGoals.size(); i++)
			{
				ArrayList<Mean> meanings = relation.getPossibility().getMap().get(toModifyGoals.elementAt(i));
				relation.getPossibility().getMap().remove(toModifyGoals.elementAt(i));
				toModifyGoals.elementAt(i).setName(newNames.elementAt(i));
				relation.getPossibility().getMap().put(toModifyGoals.elementAt(i),meanings);
			}

			if (stop)
				return;

		}

		for(RelationDescription relation : relationSet.getRelationDescriptions()) {
			for(Goal goal : relation.getPossibility().getMap().keySet()) {
				for(Mean mean : relation.getPossibility().getMap().get(goal)) {
					if (!mean.toString().trim().equals("")) //$NON-NLS-1$
					{
						String newName = (String)JOptionPane.showInputDialog(Program.myMainFrame, Messages.getString("Version_0_9_9_9.DataPack.70") + mean.toString(), "", JOptionPane.QUESTION_MESSAGE, null, null, mean.toString().trim()); //$NON-NLS-1$ //$NON-NLS-2$

						if(newName == null) {
							return;
						}

						newName = newName.trim();
						if(newName.equalsIgnoreCase("") == false) { //$NON-NLS-1$
							mean.setName(newName);
						}
					}
				}
			}
		}
	}

	public void sortBrick() {
		Collections.sort(getBrickList(), new Comparator<Brick>() {
			@Override
			public int compare(Brick o1, Brick o2) {
				return Collator.getInstance().compare(o1.toString(), o2.toString());
			}
		});
	}

	public ProgramSettings getProgramSettings()
	{
		if (programSettings == null)
			programSettings = new ProgramSettings();
		return programSettings;
	}

	public boolean unlockDatapack(String login, String pass) {

		pass = pass.trim();
		login = login.toLowerCase(Locale.FRENCH).trim();
		if (login.length() < 5)
		{
			DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.Datapack.41"));//$NON-NLS-1$
			return false;
		}

		if (pass.length() != 12)
		{
			DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.Datapack.44"));//$NON-NLS-1$
			return false;
		}



		long codeVal = Long.parseLong(pass.substring(0, pass.length() - 4));
		String baseKey = pass.substring(pass.length() - 4, pass.length());
		int keyVal = Integer.parseInt(baseKey);
		int modVal = Integer.parseInt(pass.substring(pass.length()-8, pass.length()-4));
		modVal = GenericTools.modOnDigits(modVal, 3);
		keyVal -= modVal;
		if (codeVal == (Math.abs(login.hashCode())%100000000))
		{
			isDemoValid = null;
			endDemoCalendar = null;
			Program.save(this, true);
			DialogHandlerFrame.showInformationDialog(Messages.getString("Version_1_0_3.Datapack.45")); //$NON-NLS-1$ 
			MailSender.sendAutoMail(login, "Unlock d'un datapack", ""+getCompanyInfo().getName()+" - "+login+" - "+pass+" - "+keyVal);//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			unlocked = true;
			unlockLogin = login;
			return true;
		}
		else
		{
			DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.Datapack.51"));//$NON-NLS-1$
			return false;
		}

	}

	public void copyDemoData(DataPack oldDatapack, String updateAddress) {
		
		if (oldDatapack.unlocked)
		{
			unlocked = true;
			unlockLogin = oldDatapack.unlockLogin;
			endDemoCalendar = null;
			isDemoValid = null;
			MailSender.sendAutoMail("AutoMail", "Unlock via maj d'un datapack", "Unlock d'un datapack via l'identifiant "+unlockLogin+" téléchargé à l'adresse "+updateAddress); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

		if (oldDatapack.endDemoCalendar != null)
		{
			if (endDemoCalendar == null || endDemoCalendar.after(oldDatapack.endDemoCalendar))
				endDemoCalendar = oldDatapack.endDemoCalendar;
		}
		
		if (oldDatapack.isDemoValid != null && isDemoValid == null)
			isDemoValid = oldDatapack.isDemoValid; 
		
			
	}

	public Calendar getEndDemoCalendar() {
		return endDemoCalendar;
	}

	public void exportLimitedDatapack() {
		if (KeyGenerator.checkKey() != 0)
		{
			String result = JOptionPane.showInputDialog(Program.myMainFrame, Messages.getString("Version_1_0_3.Datapack.56"), "150"); //$NON-NLS-1$ //$NON-NLS-2$
			if (result == null)
				return;
			int dayCount  = 0;
			try{
				dayCount = Integer.parseInt(result);
			}catch (NumberFormatException e)
			{
				DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.Datapack.58")); //$NON-NLS-1$
				return;
			}
			if (dayCount > 365)
			{
				DialogHandlerFrame.showInformationDialog(Messages.getString("Version_1_0_3.Datapack.59")); //$NON-NLS-1$
				dayCount = 365;
			}
			
			if (dayCount < 0)
				dayCount = 0;
			
			endDemoCalendar = Calendar.getInstance();
			if (dayCount > 0)
				endDemoCalendar.add(Calendar.DAY_OF_MONTH, dayCount);
			else
				endDemoCalendar.set(0, 0, 0);
			
			unlocked = false;
			String oldPath = getFilePath();
			setFilePath(null);
			Program.save(this, true);
			setFilePath(oldPath);
			endDemoCalendar = null;
			unlocked = true;
			Program.save(this,true);
		}
		
	}

	public boolean isLocked() {
		return !unlocked;
	}

	@Override
	public void validateObject() throws InvalidObjectException {
		lastLoadedDatapack = this;
		
	}
	
	public static DataPack getLastLoadedDatapack()
	{
		return lastLoadedDatapack;
	}
}









