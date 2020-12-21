package dataPack;

import graphicalUserInterface.IconDatabase;
import graphicalUserInterface.Program;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import client.stringTranslator.StringTranslator;

public class Groupe extends MyDefaultMutableTreeNode implements Serializable {

	private static final long serialVersionUID = -560872084918416287L;

	protected Integer idGroupe;
	protected String nom;
	protected Integer idFather;
	protected boolean addedToGroupList; 


	public Groupe(String n_nom) {
		nom = n_nom;

		DataPack dataPack = Program.myMainFrame.getDataPack();

		if (dataPack != null) {
			idGroupe = Program.myMainFrame.getDataPack().getNewGroupeId();
		} else {
			idGroupe = new Integer(-1);
		}
	}

	protected Groupe(String name, Integer idGroupe, Integer idFather)
	{
		nom = name;
		this.idGroupe = idGroupe;
		this.idFather = idFather;
	}

	public Integer getIdGroupe() {
		return idGroupe;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public JLabel getJComponent(boolean selected, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		if (getChildCount() == 0) {
			return TreeActorsCellRendere.createDefaultLabel(toString(),
					IconDatabase.iconFileEmpty, selected);
		} else {
			if (expanded) {
				return TreeActorsCellRendere.createDefaultLabel(toString(),
						IconDatabase.iconFileOpen, selected);
			} else {
				return TreeActorsCellRendere.createDefaultLabel(toString(),
						IconDatabase.iconFileClose, selected);
			}
		}
	}

	@Override
	public Icon getIcon(boolean bigIcon) {
		return null;
	}

	@Override
	public MyTreeNodeType getType() {
		return MyTreeNodeType.GroupeType;
	}

	@Override
	public void changeStringValue(String newString) {
		setNom(newString);
	}

	@Override
	public Integer getId() {
		return idGroupe;
	}

	public String getNoTranslatedString() {
		return nom;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idGroupe == null) ? 0 : idGroupe.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Groupe))
			return false;
		Groupe other = (Groupe) obj;
		if (idGroupe == null) {
			if (other.idGroupe != null)
				return false;
		} else if (!idGroupe.equals(other.idGroupe))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringTranslator.getTranslatedString(this, StringTranslator.StringType.groupType);
	}


	@Override
	public void add(MutableTreeNode newNode)
	{
		if (children == null || !children.contains(newNode))
			super.add(newNode);
		else
		{
			System.err.println("Tentative d'ajout d'un élément à un groupe alors qu'il appartient déjà aux enfants.");
		}
	}


	protected Object writeReplace()
	{
			return new GroupProxy(this);
	}


	@Override
	public void validateObject() throws InvalidObjectException {
		if (!addedToGroupList)
		{
			DataPack.getLastLoadedDatapack().getActorsModel().registerGroup(this);
			addedToGroupList = true;
		}
		else
		{
			if (idFather != null)
			{
				Groupe father = null;
				if (idFather.intValue() == -1)
					father = DataPack.getLastLoadedDatapack().getActorsModel().getGroupeActeurs();
				else
					father = DataPack.getLastLoadedDatapack().getActorsModel().getGroupById(idFather);
				if (father != null)
				{
					father.add(this);
				}
			}
		}
	}


	static class GroupProxy implements Serializable, ObjectInputValidation{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected String name;
		protected Integer id;
		protected boolean isGroupRoot;
		protected Integer idFather;
		protected transient ObjectInputStream ois;
		
		protected static HashMap<Integer, Groupe> alreadyLoadedGroups = null;

		protected ArrayList<GroupProxy> subGroups;


		public GroupProxy(Groupe g)
		{
			this(g,false);
		}

		public GroupProxy(Groupe g, boolean root) {
			name = g.nom;
			id = g.idGroupe;
			isGroupRoot = root;
			if (g.idFather != null)
				idFather = g.idFather;
			else
			{
				if (g.parent != null)
				{
					idFather = ((Groupe)g.parent).idGroupe;
				}
			}
			subGroups = new ArrayList<Groupe.GroupProxy>();
			if (g.children != null)
			{
				for (Object dmt : g.children)
				{
					if (dmt instanceof Groupe && ! (dmt instanceof GroupeRoot))
					{
						subGroups.add(new GroupProxy((Groupe) dmt));
					}
				}
			}

		}

		private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
		{
			this.ois = ois;
			ois.defaultReadObject();
		}

		private Object readResolve() throws NotActiveException, InvalidObjectException
		{
			if (alreadyLoadedGroups == null)
			{
				ois.registerValidation(this, -1);
				alreadyLoadedGroups = new HashMap<Integer, Groupe>();
			}
			
			if (alreadyLoadedGroups.containsKey(id))
				return alreadyLoadedGroups.get(id);
			
			Groupe result = null;

			if (isGroupRoot)
				result = new GroupeRoot(name,id, idFather);
			else
				result = new Groupe(name, id, idFather);
			if (id != -1)
				alreadyLoadedGroups.put(id, result);
			ois.registerValidation(result, 1);
			ois.registerValidation(result, 0);
			return result;


		}

		@Override
		public void validateObject() throws InvalidObjectException {
			alreadyLoadedGroups.clear();
			alreadyLoadedGroups = null;
			
		}
	}


}