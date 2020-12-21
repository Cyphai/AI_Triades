package dataPack;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeActorsCellRendere extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1296032005781927796L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		if(value instanceof MyDefaultMutableTreeNode) {
			MyDefaultMutableTreeNode myNode = (MyDefaultMutableTreeNode) value;
			return myNode
					.getJComponent(selected, expanded, leaf, row, hasFocus);
		} else {
			throw new IllegalStateException("Un élément de l'arbre n'est pas un MyDefaultMutableTreeNode"); //$NON-NLS-1$
		}
	}

	static public JLabel createDefaultLabel(String labelText, Icon icon, boolean selected) {
		JLabel label = new JLabel(labelText, icon, SwingConstants.LEFT);
//		label.setPreferredSize(new Dimension(350, 20));
	//	label.setMinimumSize(new Dimension(350, 20));
	//	label.setMaximumSize(new Dimension(800, 20));
		label.setBackground(selected ? new Color(208, 227, 252) : Color.WHITE);
		label.setOpaque(true);
		if (selected)
			System.out.println("TreeActorsCellRendere.createDefaultLabel() : "+labelText);
		return label;
	}
}