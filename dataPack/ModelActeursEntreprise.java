package dataPack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import translation.Messages;

/*
 * Représente le model des acteurs, moyens, briques et autres.
 *
 */

public class ModelActeursEntreprise extends DefaultTreeModel {

	private static final long serialVersionUID = -6046384331733497154L;

	private final Groupe groupeActeurs;
	private final Groupe groupeMoyens;

	private transient HashMap<Integer, Groupe> groupMap;
	
	private DataPack dataPack;

	HashMap<Integer, Acteur> allActeurs;

	public ModelActeursEntreprise(DataPack dataPack) {
		super(new Groupe("ActeursEntreprise"), true); //$NON-NLS-1$

		groupeActeurs = new GroupeRoot(Messages.getString("ModelActeursEntreprise.1")); //$NON-NLS-1$
		groupeMoyens = new GroupeRoot(Messages.getString("ModelActeursEntreprise.2")); //$NON-NLS-1$

		insertNodeInto(groupeActeurs, getRoot(), getRoot().getChildCount());
		insertNodeInto(groupeMoyens, getRoot(), getRoot().getChildCount());

		this.dataPack = dataPack;

		allActeurs = new HashMap<Integer, Acteur>();
		
	}

	public void ajouterActeur(Acteur acteur, MutableTreeNode father) {
		// possibilité d'ajouter les acteurpar ordre alphabétique
		insertNodeInto(acteur, father, 0);

		allActeurs.put(acteur.getId(), acteur);
	}

	public void ajouterGroupe(Groupe groupe, MutableTreeNode father) {
		insertNodeInto(groupe, father, father.getChildCount());
	}

	public void ajouterMoyen(Moyen moyen) {
		insertNodeInto(moyen, groupeMoyens, groupeMoyens.getChildCount());
	}

	public void ajouterBase(ActeurBase base, JeuActeur jeuActeur) {
		DefaultMutableTreeNode groupeBase = jeuActeur.getGroupeBase();
		insertNodeInto(base, groupeBase, 0);

		allActeurs.put(base.getId(), base);
	}

	public void ajouterCorps(ActeurBase corps, JeuActeur jeuActeur) {
		DefaultMutableTreeNode groupeCorps = jeuActeur.getGroupeCorps(corps.getCorpsMetier());
		insertNodeInto(corps, 
				groupeCorps, groupeCorps
					.getChildCount());

		allActeurs.put(corps.getId(), corps);
	}

//	private void cleanListener() {
//		System.out.println("cleanListener");
//		Vector<JTreeActors> jtas = new Vector<JTreeActors>();
//		for (Object listener : listenerList.getListenerList()) {
//			if(listener instanceof JTreeActors) {
//				JTreeActors treeListener = (JTreeActors)listener;
//				if(treeListener.getParent() == null || treeListener.dataPack == null) {
//					jtas.add(treeListener);
//				}
//			}
//		}
//		
//		for (JTreeActors treeListener : jtas) {
//			removeTreeModelListener(treeListener);
//		}
//	}

	public void supprimerAnyActeur(Acteur acteur) {
//		cleanListener();
		removeNodeFromParent(acteur);
		allActeurs.remove(acteur.getId());
	}

	public void supprimerGroupe(Groupe groupe) {
//		cleanListener();
		removeNodeFromParent(groupe);
	}

	public void supprimerMoyen(Moyen moyen) {
//		cleanListener();
		removeNodeFromParent(moyen);
	}

	@Override
	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode)super.getRoot();
	}

	public Groupe getGroupeMoyens() {
		return groupeMoyens;
	}

	public Groupe getGroupeActeurs() {
		return groupeActeurs;
	}

	@Override
	public DefaultMutableTreeNode getChild(Object parent, int index) {
		return (DefaultMutableTreeNode) super.getChild(parent, index);
	}
	
	public void setDataPack(DataPack dataPack) {
		this.dataPack = dataPack;
	}

	public DataPack getDataPack() {
		return dataPack;
	}

	public Collection<Acteur> getAllActors() {
		return allActeurs.values();
	}
	
	public String getActorName(Integer idActor) {
		return getActorById(idActor).toString();
	}
	
	public Acteur getActorById(Integer idActor) {
		return allActeurs.get(idActor);
	}
	
	public boolean isRacine(DefaultMutableTreeNode node) {
		return node == groupeActeurs || node == groupeMoyens;
	}
	
	public boolean isBase(Integer actorId) {
		Acteur acteur = allActeurs.get(actorId);		
		if (acteur instanceof ActeurBase) {
			return ((ActeurBase)acteur).isBase();
		}
		
		return false;
	}

	public JeuActeur getJeuActeurOfGenericActor(Integer actorGenericId) {
		Acteur actor = allActeurs.get(actorGenericId);
		if (actor instanceof ActeurBase) {
			ActeurBase genericActor = (ActeurBase)actor;
			if (genericActor.isBase()) {
				return genericActor.getJeuActeur();
			}
		}
		
		throw new IllegalArgumentException("Cet acteur n'est pas une base !!"); //$NON-NLS-1$
	}
	
	@Override
	public void reload() {
		super.reload();
	}
	
	public void registerGroup(Groupe g)
	{
		if (groupMap == null)
		{
			groupMap = new HashMap<Integer, Groupe>();
		}
		groupMap.put(g.getId(), g);
	}
	
	public Groupe getGroupById(Integer id)
	{
		if (groupMap == null)
			return null;
		return groupMap.get(id);
	}
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		
		ois.defaultReadObject();
		listenerList = new EventListenerList();
		((Groupe)root).add(groupeActeurs);
		((Groupe)root).add(groupeMoyens);
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException 
	{
		
		EventListenerList oldList = listenerList;
		listenerList = null;
		
		oos.defaultWriteObject();
		listenerList = oldList;
	}

	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
		
	}
	
	public EventListenerList popListenerList()
	{
		EventListenerList result = listenerList;
		listenerList = null;
		return result;
	}
	

}

