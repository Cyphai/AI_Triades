package main;

import java.util.Vector;

import relations.EnsembleRelation;
import relations.RelationDescription;

import dataPack.Acteur;
import dataPack.DataPack;

public class RelationsPossibles {

	public Vector<String> objectifs;
	public Vector<String[]> moyens;


	protected RelationsPossibles(Vector<String> objStruct,
			Vector<String[]> moyStruct) {
		objectifs = objStruct;
		moyens = moyStruct;

	}

	public static RelationsPossibles getRelationsPossibles(Integer aArg,
			Integer bArg, DataPack datapack, int typeRelation) {
		EnsembleRelation relationSet = datapack.getRelations();
		Vector<String> objectif = new Vector<String>();
		Vector<Vector<String>> moyens = new Vector<Vector<String>>();

		for (RelationDescription description : relationSet.getRelationsPossibles(a, b))

			RelationsPossibles result = new RelationsPossibles(null, null);
	}


	System.out.println("Relation possible avant traitement : " + aArg +
			"   -   " + bArg); Integer statutA; if (aArg.intValue() <= -10 &&
					aArg.intValue() > -50) statutA = new Integer(6); else if (aArg.intValue()
							<= -50) statutA = aArg; else if (aArg.intValue() >= 0) statutA = new
							Integer(Statut.values()[((Acteur) datapack
									.getActor(aArg)).getIdStatut().intValue()].rang); else statutA = new
									Integer(1);

			Integer statutB; if (bArg.intValue() <= -10 && bArg.intValue() > -50)
				statutB = new Integer(6); else if (bArg.intValue() <= -50) statutB =
					bArg; else if (bArg.intValue() >= 0) statutB = new
					Integer(Statut.values()[((Acteur) datapack
							.getActor(bArg)).getIdStatut().intValue()].rang); else statutB = new
							Integer(1);

			System.out .println("RelationPossibles : " + statutA + "  -  " +
					statutB); Vector<String[]> moyens = new Vector<String[]>();
					Vector<String> objectif = new Vector<String>(); String[] undefArray = {
							UNDEFINED }; objectif.add(UNDEFINED); moyens.add(undefArray);
							RelationsPossibles result;

							if (statutA.intValue() > -10 && statutB.intValue() > -10)
							{ 
								int rangA = statutA.intValue(); 
								int rangB = statutB.intValue();

								// Les deux poles dont des acteurs 
								for (ObjectifLienPartenaire obj :ObjectifLienPartenaire.values()) 
								{ 
									objectif.add(obj.toString());
									moyens.add(Liens.getStrings(obj)); 
								}

								objectif.add(ObjectifFormesDecisionnelles.sujetActeur.toString());
								moyens.add(Liens.getStrings(ObjectifFormesDecisionnelles.sujetActeur));

								if (rangA != -1 && rangB != -1) { if (rangA <= rangB) { 
									for (ObjectifLienHierarchique obj : ObjectifLienHierarchique .values()) {
										objectif.add(obj.toString()); moyens.add(Liens.getStrings(obj)); 
									}
								} 
								else
								{ 
									for (ObjectifRetourLienHierarchique obj : ObjectifRetourLienHierarchique .values()) 
									{
										objectif.add(obj.toString());
										moyens.add(Liens.getStrings(obj)); 
									} 
								} 
								} 
							} 
							else 
							{ 
								if (statutA.intValue()<= -50 && statutB.intValue() <= -50) 
								{ 
									if (statutA <= -100) 
									{ 
										for (ObjectifObjetMoyen obj : ObjectifObjetMoyen.values()) 
										{
											objectif.add(obj.toString()); 
											moyens.add(Liens.getStrings(obj)); 
										}

									} else if (statutB <= -100) 
									{ 
										for (ObjectifMoyenObjet obj :ObjectifMoyenObjet.values())
										{ 
											objectif.add(obj.toString());
											moyens.add(Liens.getStrings(obj));
										} 
									} 
									else 
									{ 
										for (ObjectifMoyenMoyen obj: ObjectifMoyenMoyen.values()) 
										{ 
											objectif.add(obj.toString());
											moyens.add(Liens.getStrings(obj)); 
										} 
									} 
								} 
								else 
								{ 
									if (statutA.intValue() <=-50) 
									{ 
										if (statutA.intValue() <= -100)
										{
											for (ObjectifObjetActeur obj :ObjectifObjetActeur .values()) 
											{ 
												objectif.add(obj.toString());
												moyens.add(Liens.getStrings(obj));
											} 
										} 
										else
										{ 
											for (ObjectifMoyenActeur obj : ObjectifMoyenActeur .values()) 
											{ 
												objectif.add(obj.toString());
												moyens.add(Liens.getStrings(obj)); 
											}
										} 
									} 
									else 
									{ 
										if (statutB <= -100) 
										{
											for (ObjectifActeurObjet obj : ObjectifActeurObjet .values()) {
												objectif.add(obj.toString()); 
												moyens.add(Liens.getStrings(obj));
											} 
										} 
										else
										{ 
											for (ObjectifActeurMoyen obj : ObjectifActeurMoyen .values()) 
											{
												objectif.add(obj.toString());
												moyens.add(Liens.getStrings(obj)); 
											}
										} 
									} 
								}

							} 

							if (typeRelation == RELATIONREELLE) { for (ObjectifFormesDecisionnelles
									forme : ObjectifFormesDecisionnelles.values()) {
								objectif.add(forme.toString()); moyens.add(Liens.getStrings(forme)); } }

							result = new RelationsPossibles(objectif, moyens);

							return result;

}


}
