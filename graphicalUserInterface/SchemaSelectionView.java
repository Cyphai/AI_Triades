package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.Brick;
import models.BrickView;

public class SchemaSelectionView extends JPanel {

	private static final long serialVersionUID = -921539312712929027L;

	private Brick brick;
	private int index;

	public SchemaSelectionView(final MainFrameTriades mf,
			final Vector<BrickView> vect, final Vector<Brick> vectSchema) {
		brick = vectSchema.firstElement();
		index = 0;
		setLayout(new BorderLayout());

		JPanel jpNorth = new JPanel();
		JPanel jpSouth = new JPanel();
		final JLabel jlLeftArrow = new JLabel(IconDatabase.iconLeftArrowMin);
		final JLabel jlRightArrow = new JLabel(IconDatabase.iconRightArrowMin);
		final JLabel jlTitle = new JLabel();
		JButton jbSelection = new JButton("Sélectionner");

		// Nord
		jpNorth.setLayout(new BoxLayout(jpNorth, BoxLayout.X_AXIS));
		jpNorth.add(Box.createHorizontalGlue());
		jlTitle.setText("Schéma n°" + (index + 1));
		jpNorth.add(jlTitle);
		jpNorth.add(Box.createHorizontalGlue());
		add(jpNorth, BorderLayout.NORTH);

		// Ouest
		jlLeftArrow.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (index > 0) {
					remove(vect.elementAt(index));
					index--;
					jlTitle.setText("Schéma n°" + (index + 1));
					add(vect.elementAt(index));
				} else
					jlTitle.setText("Schéma n°" + (index + 1) + " (premier)");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				jlLeftArrow.setIcon(IconDatabase.iconLeftArrowMax);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				jlLeftArrow.setIcon(IconDatabase.iconLeftArrowMin);
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});
		add(jlLeftArrow, BorderLayout.WEST);

		// Est
		jlRightArrow.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (index < vect.size() - 1) {
					remove(vect.elementAt(index));
					index++;
					jlTitle.setText("Schéma n°" + (index + 1));
					add(vect.elementAt(index));
				} else
					jlTitle.setText("Schéma n°" + (index + 1) + " (dernier)");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				jlRightArrow.setIcon(IconDatabase.iconRightArrowMax);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				jlRightArrow.setIcon(IconDatabase.iconRightArrowMin);
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});
		add(jlRightArrow, BorderLayout.EAST);

		// Centre
		add(vect.elementAt(index));

		// Sud
		jpSouth.setLayout(new BoxLayout(jpSouth, BoxLayout.X_AXIS));
		jpSouth.add(Box.createHorizontalGlue());
		jbSelection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mf. = vectSchema.elementAt(index);
				mf.tabbedPane.removeAll();
				mf.tabbedPane.add("Schéma sélectionné", mf.myPanel(vect
						.elementAt(index)));
			}
		});
		jpSouth.add(jbSelection);
		jpSouth.add(Box.createHorizontalGlue());
		add(jpSouth, BorderLayout.SOUTH);
	}

	public void setSchema(Schema schema) {
		this.brick = schema;
	}

	public Schema getSchema() {
		return brick;
	}

}
