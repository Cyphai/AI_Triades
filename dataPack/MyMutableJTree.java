package dataPack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class MyMutableJTree extends JTree implements TreeModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7868105658865493729L;

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		reloadModel();
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		reloadModel();
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		reloadModel();
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		reloadModel();
	}

	protected void reloadModel() {
		Enumeration<TreePath> expendedPaths = getExpandedDescendants(new TreePath(((DefaultMutableTreeNode)getModel().getRoot()).getPath()));
		final int[] selectedNodes = getSelectionRows();

		if(expendedPaths != null) {
			while (expendedPaths.hasMoreElements()) {
				final TreePath path = expendedPaths.nextElement();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						expandPath(path);
					}
				});
				makeVisible(path);
			}	
		}

		if (selectedNodes != null) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					setSelectionRows(selectedNodes);
				}
			});
		}

		validate();
	}
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();
		System.out.println("MyMutableJTree.readObject()");
	}
}
