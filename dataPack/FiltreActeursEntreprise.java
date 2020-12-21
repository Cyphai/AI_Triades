package dataPack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class FiltreActeursEntreprise extends DefaultTreeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6095674535112875250L;
	private final ModelActeursEntreprise model;
	private final Vector<Object> hidedNodes;
	private final boolean isDataPackFilter;

	private transient TreeListener treeListener;

	public FiltreActeursEntreprise(ModelActeursEntreprise model, boolean isDataPack) {
		super(model.getRoot());
		if(model.getRoot() == null) {
			System.out.println("\n\nRoot null !!!\n"); //$NON-NLS-1$
		}
		this.model = model;
		//		model.addTreeModelListener(this);

		hidedNodes = new Vector<Object>();
		listenerList = new EventListenerList();
		isDataPackFilter = isDataPack;
	}

	public TreeModelEvent convertTreeModelEvent(TreeModelEvent e) {

		Object source = e.getSource();
		TreePath path = new TreePath(e.getPath());
		Object[] children = e.getChildren();
		int[] childIndices = null;
		if (children != null) {
			childIndices = new int[children.length];
			for (int i = 0; i < childIndices.length; i++) {
				DefaultMutableTreeNode father = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				childIndices[i] = e.getChildIndices()[i];

				Enumeration<DefaultMutableTreeNode> fatherChildren = father
						.children();
				int childIndiceModel = e.getChildIndices()[i];
				while (fatherChildren.hasMoreElements() && childIndiceModel > 0) {
					if (hidedNodes.contains(fatherChildren.nextElement())) {
						childIndices[i]--;
					}

					childIndiceModel--;
				}
			}
		}

		TreeModelEvent event = new TreeModelEvent(source, path, childIndices,
				children);
		return event;
	}


	@Override
	protected void fireTreeNodesRemoved(Object source, Object[] path, 
			int[] childIndices, 
			Object[] children) {
		TreeModelEvent treeModelEvent = convertTreeModelEvent(new TreeModelEvent(source, path, childIndices, children));
		super.fireTreeNodesRemoved(treeModelEvent.getSource(), treeModelEvent.getPath(), treeModelEvent.getChildIndices(), treeModelEvent.getChildren());
	}

	public void treeNodesChanged(TreeModelEvent e) {
		TreeModelEvent event = convertTreeModelEvent(e);

		if (event.getSource() != null) {
			fireTreeNodesChanged(this, event.getPath(),
					event
					.getChildIndices(), event.getChildren());
		}
	}

	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		if(isDataPackFilter) {
			model.addTreeModelListener(listener);
		}
		super.addTreeModelListener(listener);
	}

	public boolean isHidedNode(DefaultMutableTreeNode node) {
		return hidedNodes.contains(node);
	}

	public boolean hideNode(DefaultMutableTreeNode node) {
		if (node == getRoot() || hidedNodes.contains(node)) {
			return false;
		}

		hidedNodes.add(node);

		if (isLeaf(node) == false) {
			Enumeration<TreeNode> childrenEnum = node.children();

			while (childrenEnum.hasMoreElements()) {
				hideNode((DefaultMutableTreeNode) childrenEnum.nextElement());
			}
		}

		DefaultMutableTreeNode father = getFather(node);
		if (father != null &&  getChildCount(father) == 0) {
			if (hideNode(father) == false) {
				reload();
			}
		} else {
			reload();
		}

		return true;
	}

	public void hideNodes(Collection<? extends DefaultMutableTreeNode> nodes) {
		if (nodes != null) {
			for (DefaultMutableTreeNode node : nodes) {
				System.out.println("FiltreActeursEntreprise.hideNodes() : Groupe caché : "+node);
				hideNode(node);
			}
		}
	}

	public boolean shwoNode(DefaultMutableTreeNode node) {
		if (node == getRoot()
				|| hidedNodes.contains(node) == false)
			return false;
		
		hidedNodes.remove(node);
		
		if (shwoNode(getFather(node)) == false) {
			reload();
		}

		return true;
	}

	public void shwoNodes(Collection<? extends DefaultMutableTreeNode> nodes) {
		if (nodes != null) {
			for (DefaultMutableTreeNode node : nodes) {
				shwoNode(node);
			}
		}
	}
	
	private DefaultMutableTreeNode getFather(DefaultMutableTreeNode node) {
		if(node instanceof Acteur) {
			return (DefaultMutableTreeNode)model.getActorById(((Acteur)node).getId()).getParent();
		} else {
			return (DefaultMutableTreeNode)node.getParent();
		}
	}

	@Override
	public Object getChild(Object parent, int index) {
		int modelChildCount = model.getChildCount(parent);
		int childIndex = 0;
		for (int i = 0; i < modelChildCount; i++) {
			Object child = model.getChild(parent, i);
			if (hidedNodes.contains(child) == false) {
				if (index == childIndex) {
					return child;
				} else {
					childIndex++;
				}
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		int modelChildCount = model.getChildCount(parent);
		int childCount = 0;
		for (int i = 0; i < modelChildCount; i++) {
			Object child = model.getChild(parent, i);
			if (hidedNodes.contains(child) == false) {
				childCount++;
			} else {
				//				System.out.println("Node " + child.toString() + " caché");
			}
		}
		//System.out.println("childcount de " + parent.toString() + " : " + childCount + "(hided : " + hidedNodes + ")");
		return childCount;
	}

	@Override
	public boolean isLeaf(Object node) {
		return getChildCount(node) == 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		int modelChildCount = model.getChildCount(parent);
		int childIndex = 0;
		for (int i = 0; i < modelChildCount; i++) {
			Object modelChild = model.getChild(parent, i);

			if (hidedNodes.contains(modelChild) == false) {
				if (modelChild == child) {
					return childIndex;
				}
				childIndex++;
			}
		}
		return -1;
	}

	public void setTreeListener(TreeListener treeListener) {
		this.treeListener = treeListener;
	}

	public TreeListener getTreeListener() {
		return treeListener;
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException
	{
		EventListenerList oldList = listenerList;
		listenerList = null;
		oos.defaultWriteObject();
		listenerList = oldList;
	}
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();
		listenerList = new EventListenerList();
		
	}
	
	
}
