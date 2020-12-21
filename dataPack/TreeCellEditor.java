package dataPack;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.JTree;

public class TreeCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = -6234212637865246632L;

	public TreeCellEditor() {
		super(new JTextField());
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		if (value instanceof MyDefaultMutableTreeNode) {
			JTextField result = (JTextField) super.getTreeCellEditorComponent(
					tree, value, isSelected, expanded, leaf, row);

			if (value instanceof Acteur) {
				result.setText(((Acteur) value).poste);
			} else if (value instanceof Moyen) {
				result.setText(((Moyen) value).nom);
			} else {
				result.setText(value.toString());
			}
			
			result.setPreferredSize(new Dimension(200, 25));
			result.validate();
			
			return result;
		} else {
			throw new IllegalStateException(
					"Un élément de l'arbre n'est pas un MyDefaultMutableTreeNode"); //$NON-NLS-1$
		}
	}
}
