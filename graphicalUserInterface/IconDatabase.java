package graphicalUserInterface;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;

public final class IconDatabase {

	static public String defaultIconsDirectory = "/Icones/16x16/configure.png"; //$NON-NLS-1$

	// 16x16 icons
	public static ImageIcon iconNew;

	public static ImageIcon iconOpen;
	public static ImageIcon iconSave;
	public static ImageIcon iconSaveAs;
	public static ImageIcon iconExport;
	public static ImageIcon iconExportPopUp;
	public static ImageIcon iconClose;
	public static ImageIcon iconExit;
	public static ImageIcon iconUndo;
	public static ImageIcon iconRedo;
	public static ImageIcon iconCopy;
	public static ImageIcon iconCut;
	public static ImageIcon iconPaste;
	public static ImageIcon iconDelete;
	public static ImageIcon iconSearch;
	public static ImageIcon iconTree;
	public static ImageIcon iconSchema;
	public static ImageIcon iconAnalysis;
	public static ImageIcon iconResolution;
	public static ImageIcon iconAudit;
	public static ImageIcon iconHelp;
	public static ImageIcon iconUpdate;
	public static ImageIcon iconAbout;
	public static ImageIcon iconMinimize;
	public static ImageIcon iconMaximize;
	public static ImageIcon iconLeftArrow;
	public static ImageIcon iconRightArrow;
	public static ImageIcon iconDuplicateArrow;
	public static ImageIcon iconConfigure;
	public static ImageIcon iconArrowDownLeft;
	public static ImageIcon iconArrowDownRight;
	public static ImageIcon iconArrowUpLeft;
	public static ImageIcon iconArrowUpRight;
	public static ImageIcon iconUnfold;
	public static ImageIcon iconAdd;
	public static ImageIcon iconButtonOk;
	public static ImageIcon iconButtonCancel;
	public static ImageIcon iconRemoveActor0;
	public static ImageIcon iconRemoveActor1;
	public static ImageIcon iconRemoveActor2;
	public static Vector<ImageIcon> vectorIconActorsMin;
	public static ImageIcon iconMoyen;
	public static ImageIcon iconBrickMin;
	public static ImageIcon iconFileClose;
	public static ImageIcon iconFileOpen;
	public static ImageIcon iconFileEmpty;
	public static Vector<ImageIcon> vectorIconActivitiesMin;
	public static Vector<ImageIcon> vectorIconMoyensMin;
	public static ImageIcon iconRename;

	// 22x22 icons
	public static ImageIcon iconRemoveTab;
	public static ImageIcon iconAddActor;
	public static ImageIcon iconAddGroup;
	public static ImageIcon iconAddActivity;
	public static ImageIcon iconAddBrick;
	public static ImageIcon iconAddActionTime;
	public static ImageIcon iconAddMoyen;
	public static ImageIcon iconAddStep;

	// 32x32 icons
	public static ImageIcon iconDatapack;
	public static ImageIcon iconModel;
	public static ImageIcon iconBrick;
	public static ImageIcon iconNewFile;
	public static ImageIcon iconOpenFile;
	public static ImageIcon iconUpgrade;
	public static ImageIcon iconActorSet;

	// 48x48 icons
	public static ImageIcon iconTriad;
	public static ImageIcon iconHumanActor;
	public static ImageIcon iconHumanActorSelected;
	public static ImageIcon iconArrowOut;
	public static ImageIcon iconArrowOutSelected;
	public static ImageIcon iconActivity;
	public static ImageIcon iconActivitySelected;
	public static ImageIcon iconGroup;
	public static ImageIcon iconGroupSelected;
	public static ImageIcon iconLeftArrowMin;
	public static ImageIcon iconRightArrowMin;
	public static ImageIcon iconLeftArrowMax;
	public static ImageIcon iconRightArrowMax;
	public static Vector<ImageIcon> vectorIconActivities;
	public static Vector<ImageIcon> vectorIconActivitiesUnvalide;
	public static Vector<ImageIcon> vectorIconActivitiesValide;
	public static Vector<ImageIcon> vectorIconActivitiesNoCompleted;
	
	public static Vector<ImageIcon> vectorIconMoyens;
	public static Vector<ImageIcon> vectorIconActorsBig;

	// other
	public static ImageIcon iconTitleNew;
	public static ImageIcon iconTitleDataPack;
	public static ImageIcon iconTitleNewBrick;
	public static ImageIcon iconTitleModel;
	public static ImageIcon iconTitleTriad;
	public static ImageIcon iconAssistant;
	public static ImageIcon iconCorpsBases;

	// Exported icons
	public static Vector<ImageIcon> vectorExportedIcons;
	public static Vector<String> originalExportedIcons;
	public static HashMap<String, ImageIcon> icons;

	public static int exportedImageSize = 48;

	private static IconDatabase singleton = new IconDatabase();
	
	public IconDatabase() {

		if (iconNew == null) {
			iconNew = new ImageIcon(getClass().getResource(
					"/Icones/16x16/nouveau.png")); //$NON-NLS-1$

			iconOpen = new ImageIcon(getClass().getResource(
			"/Icones/16x16/ouvrir.png")); //$NON-NLS-1$
			iconSave = new ImageIcon(getClass().getResource(
			"/Icones/16x16/enregistrer.png")); //$NON-NLS-1$
			iconSaveAs = new ImageIcon(getClass().getResource(
			"/Icones/16x16/enregistrer_sous.png")); //$NON-NLS-1$
			iconUnfold = new ImageIcon(getClass().getResource(
			"/Icones/16x16/derouler.png")); //$NON-NLS-1$
			iconClose = new ImageIcon(getClass().getResource(
			"/Icones/16x16/fermer.png")); //$NON-NLS-1$
			iconExit = new ImageIcon(getClass().getResource(
			"/Icones/16x16/quitter.png")); //$NON-NLS-1$
			iconDelete = new ImageIcon(getClass().getResource(
			"/Icones/16x16/supprimer.png")); //$NON-NLS-1$
			iconTree = new ImageIcon(getClass().getResource(
			"/Icones/16x16/arborescence.png")); //$NON-NLS-1$
			iconSchema = new ImageIcon(getClass().getResource(
			"/Icones/16x16/schema.png")); //$NON-NLS-1$
			iconHelp = new ImageIcon(getClass().getResource(
			"/Icones/16x16/aide.png")); //$NON-NLS-1$
			iconUpdate = new ImageIcon(getClass().getResource(
			"/Icones/16x16/maj.png")); //$NON-NLS-1$
			iconAbout = new ImageIcon(getClass().getResource(
			"/Icones/16x16/a_propos.png")); //$NON-NLS-1$
			iconMinimize = new ImageIcon(getClass().getResource(
			"/Icones/16x16/minimiser.png")); //$NON-NLS-1$
			iconMaximize = new ImageIcon(getClass().getResource(
			"/Icones/16x16/maximiser.png")); //$NON-NLS-1$
			iconLeftArrow = new ImageIcon(getClass().getResource(
			"/Icones/16x16/flecheGauche.png")); //$NON-NLS-1$
			iconRightArrow = new ImageIcon(getClass().getResource(
			"/Icones/16x16/flecheDroite.png")); //$NON-NLS-1$
			iconDuplicateArrow = new ImageIcon(getClass().getResource(
			"/Icones/16x16/flecheDupliquer.png")); //$NON-NLS-1$
			iconConfigure = new ImageIcon(getClass().getResource(
			"/Icones/16x16/configure.png")); //$NON-NLS-1$
			iconArrowDownLeft = new ImageIcon(getClass().getResource(
			"/Icones/16x16/flecheBasGauche.png")); //$NON-NLS-1$
			iconArrowDownRight = new ImageIcon(getClass().getResource(
			"/Icones/16x16/flecheBasDroite.png")); //$NON-NLS-1$
			iconArrowUpLeft = new ImageIcon(getClass().getResource(
			"/Icones/16x16/flecheHautGauche.png")); //$NON-NLS-1$
			iconArrowUpRight = new ImageIcon(getClass().getResource(
			"/Icones/16x16/flecheHautDroite.png")); //$NON-NLS-1$
			iconRemoveActor0 = new ImageIcon(getClass().getResource(
			"/Icones/16x16/removeActor0.png")); //$NON-NLS-1$
			iconRemoveActor1 = new ImageIcon(getClass().getResource(
			"/Icones/16x16/removeActor1.png")); //$NON-NLS-1$
			iconRemoveActor2 = new ImageIcon(getClass().getResource(
			"/Icones/16x16/removeActor2.png")); //$NON-NLS-1$

			
			vectorIconActorsMin = new Vector<ImageIcon>();
			vectorIconActorsMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/userBlack.png"))); //$NON-NLS-1$
			vectorIconActorsMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/userRed.png"))); //$NON-NLS-1$
			vectorIconActorsMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/userGreen.png"))); //$NON-NLS-1$
			vectorIconActorsMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/userYellow.png"))); //$NON-NLS-1$
			vectorIconActorsMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/userPurple.png"))); //$NON-NLS-1$
			vectorIconActorsMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/userBrown.png"))); //$NON-NLS-1$
			
			/*
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainSelected.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainNoir.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainRouge.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainVert.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainJaune.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainViolet.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainMarron.png"))); //$NON-NLS-1$			 */
			
			iconMoyen = new ImageIcon(getClass().getResource(
			"/Icones/16x16/moyen.png")); //$NON-NLS-1$
			iconBrickMin = new ImageIcon(getClass().getResource(
			"/Icones/16x16/brick.png")); //$NON-NLS-1$
			iconFileClose = new ImageIcon(getClass().getResource(
			"/Icones/16x16/fichierFerme.png")); //$NON-NLS-1$
			iconFileOpen = new ImageIcon(getClass().getResource(
			"/Icones/16x16/fichierOuvert.png")); //$NON-NLS-1$
			iconFileEmpty = new ImageIcon(getClass().getResource(
			"/Icones/16x16/fichierVide.png")); //$NON-NLS-1$
			iconRename = new ImageIcon(getClass().getResource(
			"/Icones/16x16/contrat.png")); //$NON-NLS-1$
			vectorIconActivitiesMin = new Vector<ImageIcon>();
			vectorIconActivitiesMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/activite.png"))); //$NON-NLS-1$
			vectorIconActivitiesMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/contact.png"))); //$NON-NLS-1$
			vectorIconActivitiesMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/contrat.png"))); //$NON-NLS-1$
			vectorIconActivitiesMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/processus.png"))); //$NON-NLS-1$
			vectorIconActivitiesMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/compas.png"))); //$NON-NLS-1$
			vectorIconActivitiesMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/note.png"))); //$NON-NLS-1$
			vectorIconActivitiesMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/reunion.png"))); //$NON-NLS-1$
			vectorIconActivitiesMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/aide.png"))); //$NON-NLS-1$
			vectorIconMoyensMin = new Vector<ImageIcon>();
			vectorIconMoyensMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/moyenGenerique.png"))); //$NON-NLS-1$
			vectorIconMoyensMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/outils.png"))); //$NON-NLS-1$
			vectorIconMoyensMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/methode.png"))); //$NON-NLS-1$
			vectorIconMoyensMin.add(new ImageIcon(getClass().getResource(
			"/Icones/16x16/stock.png"))); //$NON-NLS-1$

			// 22x22 icons
			iconRemoveTab = new ImageIcon(getClass().getResource(
			"/Icones/22x22/removeTab.png")); //$NON-NLS-1$
			iconAddActor = new ImageIcon(getClass().getResource(
			"/Icones/22x22/addActor.png")); //$NON-NLS-1$
			iconAddGroup = new ImageIcon(getClass().getResource(
			"/Icones/22x22/addGroup.png")); //$NON-NLS-1$
			iconAddActivity = new ImageIcon(getClass().getResource(
			"/Icones/22x22/addActivity.png")); //$NON-NLS-1$
			iconAddBrick = new ImageIcon(getClass().getResource(
			"/Icones/22x22/addBrick.png")); //$NON-NLS-1$
			iconAddActionTime = new ImageIcon(getClass().getResource(
			"/Icones/22x22/addActionTime.png")); //$NON-NLS-1$
			iconAddMoyen = new ImageIcon(getClass().getResource(
			"/Icones/22x22/addMoyen.png")); //$NON-NLS-1$
			iconAddStep = new ImageIcon(getClass().getResource(
			"/Icones/22x22/step.png")); //$NON-NLS-1$

			// 32x32 icons
			iconDatapack = new ImageIcon(getClass().getResource(
			"/Icones/32x32/datapack.png")); //$NON-NLS-1$
			iconModel = new ImageIcon(getClass().getResource(
			"/Icones/32x32/model.png")); //$NON-NLS-1$
			iconBrick = new ImageIcon(getClass().getResource(
			"/Icones/32x32/brick.png")); //$NON-NLS-1$
			iconNewFile = new ImageIcon(getClass().getResource(
			"/Icones/32x32/newFile.png")); //$NON-NLS-1$
			iconOpenFile = new ImageIcon(getClass().getResource(
			"/Icones/32x32/openFile.png")); //$NON-NLS-1$
			iconUpgrade = new ImageIcon(getClass().getResource(
			"/Icones/32x32/upgrade.png")); //$NON-NLS-1$

			// 48x48
			iconTriad = new ImageIcon(getClass().getResource(
			"/Icones/48x48/Triade.png")); //$NON-NLS-1$
			iconHumanActor = new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainNoir.png")); //$NON-NLS-1$
			iconHumanActorSelected = new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainSelected.png")); //$NON-NLS-1$
			iconArrowOut = new ImageIcon(getClass().getResource(
			"/Icones/48x48/flecheVersExterieur.png")); //$NON-NLS-1$
			iconArrowOutSelected = new ImageIcon(getClass().getResource(
			"/Icones/48x48/flecheVersExterieurSelected.png")); //$NON-NLS-1$
			iconActivity = new ImageIcon(getClass().getResource(
			"/Icones/48x48/activite.png")); //$NON-NLS-1$
			iconActivitySelected = new ImageIcon(getClass().getResource(
			"/Icones/48x48/activiteSelected.png")); //$NON-NLS-1$
			iconGroup = new ImageIcon(getClass().getResource(
			"/Icones/48x48/group.png")); //$NON-NLS-1$
			iconGroupSelected = new ImageIcon(getClass().getResource(
			"/Icones/48x48/groupSelected.png")); //$NON-NLS-1$
			iconLeftArrowMin = new ImageIcon(getClass().getResource(
			"/Icones/48x48/FlecheGaucheMin.png")); //$NON-NLS-1$
			iconRightArrowMin = new ImageIcon(getClass().getResource(
			"/Icones/48x48/FlecheDroiteMin.png")); //$NON-NLS-1$
			iconLeftArrowMax = new ImageIcon(getClass().getResource(
			"/Icones/48x48/FlecheGaucheMax.png")); //$NON-NLS-1$
			iconRightArrowMax = new ImageIcon(getClass().getResource(
			"/Icones/48x48/FlecheDroiteMax.png")); //$NON-NLS-1$
			
			vectorIconActivities = new Vector<ImageIcon>();
			vectorIconActivities.add(iconActivity);
			vectorIconActivities.add(iconActivitySelected);
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contact.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contactSelected.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contrat.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contratSelected.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/processus.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/processusSelected.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/compas.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/compasSelected.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/note.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/noteSelected.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/reunion.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/reunionSelected.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/idee.png"))); //$NON-NLS-1$
			vectorIconActivities.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/ideeSelected.png"))); //$NON-NLS-1$
			
			vectorIconActivitiesNoCompleted = vectorIconActivities;
			vectorIconActivitiesValide = vectorIconActivities;
			vectorIconActivitiesUnvalide = vectorIconActivities;
			/*
			vectorIconActivitiesNoCompleted = new Vector<ImageIcon>();
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/activiteNoCompleted.png")));
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/activiteNoCompletedSelected.png")));
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contactNoCompleted.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contactNoCompletedSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contratNoCompleted.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contratNoCompletedSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/processusNoCompleted.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/processusNoCompletedSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/compasNoCompleted.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/compasNoCompletedSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/noteNoCompleted.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/noteNoCompletedSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/reunionNoCompleted.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/reunionNoCompletedSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/ideeNoCompleted.png"))); //$NON-NLS-1$
			vectorIconActivitiesNoCompleted.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/ideeNoCompletedSelected.png"))); //$NON-NLS-1$	
			
			vectorIconActivitiesValide = new Vector<ImageIcon>();
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/activiteValide.png")));
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/activiteValideSelected.png")));
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contactValide.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contactValideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contratValide.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contratValideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/processusValide.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/processusValideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/compasValide.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/compasValideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/noteValide.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/noteValideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/reunionValide.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/reunionValideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/ideeValide.png"))); //$NON-NLS-1$
			vectorIconActivitiesValide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/ideeValideSelected.png"))); //$NON-NLS-1$	
			
			vectorIconActivitiesUnvalide= new Vector<ImageIcon>();
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/activiteUnvalide.png")));
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/activiteUnvalideSelected.png")));
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contactUnvalide.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contactUnvalideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contratUnvalide.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/contratUnvalideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/processusUnvalide.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/processusUnvalideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/compasUnvalide.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/compasUnvalideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/noteUnvalide.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/noteUnvalideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/reunionUnvalide.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/reunionUnvalideSelected.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/ideeUnvalide.png"))); //$NON-NLS-1$
			vectorIconActivitiesUnvalide.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/ideeUnvalideSelected.png"))); //$NON-NLS-1$
			*/
			
			vectorIconMoyens = new Vector<ImageIcon>();
			vectorIconMoyens.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/moyenGenerique.png"))); //$NON-NLS-1$
			vectorIconMoyens.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/moyenGeneriqueSelected.png"))); //$NON-NLS-1$
			vectorIconMoyens.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/outils.png"))); //$NON-NLS-1$
			vectorIconMoyens.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/outilsSelected.png"))); //$NON-NLS-1$
			vectorIconMoyens.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/methode.png"))); //$NON-NLS-1$
			vectorIconMoyens.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/methodeSelected.png"))); //$NON-NLS-1$
			vectorIconMoyens.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/stock.png"))); //$NON-NLS-1$
			vectorIconMoyens.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/stockSelected.png"))); //$NON-NLS-1$
			vectorIconActorsBig = new Vector<ImageIcon>();
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainSelected.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainNoir.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainRouge.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainVert.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainJaune.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainViolet.png"))); //$NON-NLS-1$
			vectorIconActorsBig.add(new ImageIcon(getClass().getResource(
			"/Icones/48x48/acteurHumainMarron.png"))); //$NON-NLS-1$

			// other
			iconTitleNew = new ImageIcon(getClass().getResource(
			"/Icones/big/TitreNouveau.png")); //$NON-NLS-1$
			iconTitleDataPack = new ImageIcon(getClass().getResource(
			"/Icones/big/TitreDataPack.png")); //$NON-NLS-1$
			iconTitleNewBrick = new ImageIcon(getClass().getResource(
			"/Icones/big/TitreNouvelleBrique.png")); //$NON-NLS-1$
			iconTitleModel = new ImageIcon(getClass().getResource(
			"/Icones/big/TitreModel.png")); //$NON-NLS-1$
			iconTitleTriad = new ImageIcon(getClass().getResource(
			"/Icones/big/TitreNouveauTriade.png")); //$NON-NLS-1$
			iconActorSet = new ImageIcon(getClass().getResource(
			"/Icones/big/TitreSelectionActeurs.png")); //$NON-NLS-1$
			iconAssistant = new ImageIcon(getClass().getResource(
			"/Icones/big/TitreAssistant.png")); //$NON-NLS-1$
			iconCorpsBases = new ImageIcon(getClass().getResource(
			"/Icones/big/corps_bases.png")); //$NON-NLS-1$

			// Exported icons
			vectorExportedIcons = new Vector<ImageIcon>();
			vectorExportedIcons.addAll(vectorIconActivities);
			vectorExportedIcons.addAll(vectorIconActorsBig);
			vectorExportedIcons.addAll(vectorIconMoyens);

			originalExportedIcons = new Vector<String>();
			originalExportedIcons.add("/Icones/48x48/acteurHumainSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/acteurHumainNoir.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/acteurHumainVert.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/outils.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/methodeSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/moyenGeneriqueSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/acteurHumainJaune.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/flecheVersExterieur.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/moyenGenerique.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/stock.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/stockSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/flecheVersExterieurSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/Triade.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/idee.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/ideeSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/activiteSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/groupSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/contrat.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/FlecheGaucheMin.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/acteurHumainSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/FlecheDroiteMin.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/group.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/FlecheGaucheMax.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/outilsSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/acteurHumainViolet.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/methode.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/FlecheDroiteMax.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/contact.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/acteurHumainRouge.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/acteurHumainMarron.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/processus.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/acteurHumainNoir.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/contratSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/contactSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/activite.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/compas.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/compasSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/processusSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/note.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/noteSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/reunion.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/48x48/reunionSelected.png"); //$NON-NLS-1$
			originalExportedIcons.add("/Icones/16x16/configure.png"); //$NON-NLS-1$
			
			icons = new HashMap<String, ImageIcon>();
		}
	}

	public static ImageIcon getResizeIcon(String imagePath) {
		ImageIcon result = icons.get(imagePath);

		if(result == null) {
			ImageIcon icon = new ImageIcon(imagePath);

			if(icon.getIconHeight() + icon.getIconWidth() <= 0) {
				return null;
			}

			BufferedImage buf = new BufferedImage(exportedImageSize, exportedImageSize, BufferedImage.TYPE_INT_ARGB);
			/* On dessine sur le Graphics de l'image bufferisée. */
			Graphics2D g = buf.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(icon.getImage(), 0, 0, exportedImageSize, exportedImageSize, null);
			g.dispose();

			result = new ImageIcon(makeColorTransparent((new ImageIcon(buf)).getImage(), Color.WHITE));

			icons.put(imagePath, result);
		}

		return result;
	}

	public static Image makeColorTransparent(Image im, final Color color) {
		ImageFilter filter = new RGBImageFilter() {
			// the color we are looking for... Alpha bits are set to opaque
			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb) {
				if ( ( rgb | 0xFF000000 ) == markerRGB ) {
					// Mark the alpha bits as zero - transparent
					return 0x00FFFFFF & rgb;
				}
				else {
					// nothing to do
					return rgb;
				}
			}
		}; 

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

}
