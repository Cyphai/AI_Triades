package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import main.Statut;
import translation.Messages;
import dataPack.ActeurBase;
import dataPack.JeuActeur;

public class ActorSetView extends JPanel {

	private static final long serialVersionUID = -8042407423948049392L;

	private final JeuActeur ja;
	private final JPanel panelTable = new JPanel();
	private final Vector<JPanel> rowsView = new Vector<JPanel>();

	// Utiles pour les FocusListener
	private int indexCol = 0;
	private int indexRow = 0;

	public ActorSetView(JeuActeur ja) {
		super(new BorderLayout());
		this.ja = ja;
		buildAll();
	}

	/**
	 * Construction du panel global
	 */
	private void buildAll() {

		panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.Y_AXIS));

		JPanel firstRow = createFirstRow();
		rowsView.add(firstRow);
		panelTable.add(firstRow);
		// Remplissage si jeu d'acteur non vide
		for (String name : ja.getListeCorps()) {
			addRow(name);
		}

		setBorder(BorderFactory.createTitledBorder(Messages.getString("ActorSetView.0") + ja.toString())); //$NON-NLS-1$

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(panelTable, BorderLayout.WEST);
		panel.add(Box.createGlue(), BorderLayout.CENTER);
		panel.add(Box.createGlue(), BorderLayout.SOUTH);
		JScrollPane jsp = new JScrollPane(panel);
		jsp
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(createButtonsPanel(), BorderLayout.NORTH);
		add(jsp, BorderLayout.CENTER);
	}

	/**
	 * Construction panel 1ere ligne (contenant le nom des bases)
	 */
	private JPanel createFirstRow() {

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.add(new JLabel(IconDatabase.iconCorpsBases));
		for (ActeurBase ab : ja.getListeBase()) {
			panel.add(createJTFCombo(ab));
			indexCol++;
		}

		return panel;

	}

	/**
	 * Construction d'un panel contenant uniquement l'entête de ligne
	 * (jtextfield + jcombobox)
	 */
	private JPanel createRowTitle(String name) {

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JTextField jtf = createJTF(name);
		jtf.setName("C"); //$NON-NLS-1$
		jtf.addFocusListener(generateFocusListener(jtf, indexRow, null, 0));
		panel.add(jtf);

		return panel;

	}

	/**
	 * Remplissage de la ligne corps de nom "name" pour toutes les bases
	 */
	private void generateRowContent(JPanel rowPanel, String name) {

		Vector<ActeurBase> vect = ja.getCorps(name);
		for (int i = rowPanel.getComponentCount() - 1; i < vect.size(); i++) {
			JTextField jtf = createJTF(vect.elementAt(i).toString());
			jtf.setName("P"); //$NON-NLS-1$
			jtf.addFocusListener(generateFocusListener(jtf, i, null, rowsView
					.indexOf(rowPanel) - 1));
			rowPanel.add(jtf);
		}
		rowPanel.validate();
	}

	/**
	 * Update des produits d'une ligne
	 */
	private void updateRowContent(JPanel rowPanel, String name) {

		int i = 1;
		for (ActeurBase ab : ja.getCorps(name)) {
			((JTextField) rowPanel.getComponent(i)).setText(ab.toString());
			i++;
		}
		rowPanel.validate();
	}

	/**
	 * Update des produits de toutes les lignes
	 */
	private void updateAllRowContent() {

		int i = 1;
		for (String name : ja.getListeCorps()) {
			updateRowContent(rowsView.elementAt(i), name);
			i++;
		}

	}

	/**
	 * Ajout d'une ligne AVEC REMPLISSAGE AUTOMATIQUE DES PRODUITS (Corps)
	 */
	private void addRow(String rowName) {

		JPanel rowPanel = createRowTitle(rowName);
		rowsView.add(rowPanel);
		if (rowsView.firstElement().getComponentCount() > 1) {
			generateRowContent(rowPanel, rowName);
		}
		panelTable.add(rowPanel);
		validate();
		indexRow++;
	}

	/**
	 * Ajout d'une colonne (Base)
	 */
	private void addCol(ActeurBase acteurBase) {

		rowsView.firstElement().add(createJTFCombo(acteurBase));
		int i = 1;
		for (String s : ja.getListeCorps()) {
			generateRowContent(rowsView.elementAt(i), s);
			i++;
		}
		validate();
		indexCol++;
	}

	/**
	 * Fonction de création d'un panel cellule de type jtextfield (Base ou
	 * Produit)
	 */
	private JTextField createJTF(String name) {

		JTextField jtf = new JTextField(name);
		jtf.setMinimumSize(new Dimension(200, 60));
		jtf.setPreferredSize(new Dimension(200, 60));
		jtf.setMaximumSize(new Dimension(200, 60));

		return jtf;
	}

	/**
	 * Fonction de création d'un panel cellule de type jtextfield/jcombobox
	 * (Corps)
	 */
	private JPanel createJTFCombo(ActeurBase base) {

		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(200, 60));
		panel.setPreferredSize(new Dimension(200, 60));
		panel.setMaximumSize(new Dimension(200, 60));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JTextField jtf = new JTextField(base.toString());
		jtf.setName("B"); //$NON-NLS-1$
		jtf.addFocusListener(generateFocusListener(jtf, indexCol, null, 0));
		JComboBox<Statut> jcb = new JComboBox<Statut>(Statut.values());

		jcb.addFocusListener(generateFocusListener(jcb, 0, jtf, 0));
		jcb.setSelectedIndex(base.getIdStatut());
		panel.add(jtf);
		panel.add(jcb);

		return panel;

	}

	/**
	 * Création panel contenant les deux boutons pour ajouter lignes et colonnes
	 */
	private JPanel createButtonsPanel() {

		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
		JButton col = new JButton(Messages.getString("ActorSetView.4")); //$NON-NLS-1$
		col.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActeurBase nouvelleBase = ja.ajouterBase(Messages.getString("ActorSetView.5") + (indexCol + 1), new Integer(0)); //$NON-NLS-1$
				addCol(nouvelleBase); //$NON-NLS-1$
			}
		});
		col.setFocusable(false);
		JButton row = new JButton(Messages.getString("ActorSetView.7")); //$NON-NLS-1$
		row.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ja.ajouterCorps(Messages.getString("ActorSetView.8") + (indexRow + 1)); //$NON-NLS-1$
				addRow(Messages.getString("ActorSetView.9") + (indexRow + 1)); //$NON-NLS-1$
			}
		});
		row.setFocusable(false);
		JButton close = new JButton(Messages.getString("ActorSetView.10")); //$NON-NLS-1$
		close.addActionListener(Program.myMainFrame.controller.actionRemoveTab);
		close.setFocusable(false);
		JButton apply = new JButton(Messages.getString("ActorSetView.11")); //$NON-NLS-1$
		panelButtons.add(col);
		panelButtons.add(row);
		panelButtons.add(Box.createHorizontalGlue());
		panelButtons.add(apply);
		panelButtons.add(close);

		return panelButtons;
	}

	/**
	 * Génération d'un FocusListener à destination de (au choix) - Cellule Base
	 * "B" - Cellule Corps "C" - Cellule Produit "P"
	 */
	private FocusListener generateFocusListener(final Object o,
			final int index, final JTextField aux, final int indexBis) {

		return new FocusListener() {

			String temp;

			@Override
			public void focusLost(FocusEvent e) {
				if (o.getClass() == JTextField.class) {
					JTextField jtf = (JTextField) o;
					if (!jtf.getText().equals(temp)) {

						String name = jtf.getName();
						if (name.equals("B")) { // Base //$NON-NLS-1$
							if (ja.getBaseIndexByName(jtf.getText()) != -1) {
								DialogHandlerFrame
										.showErrorDialog(Messages.getString("ActorSetView.13")); //$NON-NLS-1$
								jtf.setText(temp);
								return;
							}
							if (jtf.getText().trim().isEmpty()) {
								DialogHandlerFrame
										.showErrorDialog(Messages.getString("ActorSetView.14")); //$NON-NLS-1$
								jtf.setText(temp);
								return;
							}

							ja.getListeBase().elementAt(index).setPoste(
									jtf.getText());
							ja.computeName();
							updateAllRowContent();
						} else {
							if (name.equals("C")) { // Corps //$NON-NLS-1$
								if (ja.getListeCorps().indexOf(jtf.getText()) != -1) {
									DialogHandlerFrame
											.showErrorDialog(Messages.getString("ActorSetView.16")); //$NON-NLS-1$
									jtf.setText(temp);
									return;
								}
								if (jtf.getText().trim().isEmpty()) {
									DialogHandlerFrame
											.showErrorDialog(Messages.getString("ActorSetView.17")); //$NON-NLS-1$
									jtf.setText(temp);
									return;
								}

								ja.modifierCorps(temp, jtf.getText());
								ja.computeName();
								updateAllRowContent();
							} else if (name.equals("P")) { // Produit //$NON-NLS-1$

								String s = ja.getListeCorps().elementAt(
										indexBis);
								ja.getCorps(s).elementAt(index).setPoste(
										jtf.getText());

							}
						}
					}
				} else if (o.getClass() == JComboBox.class) {
					JComboBox jcb = (JComboBox) o;
					if (aux != null) {
						Integer id = ((Statut) jcb.getSelectedItem()).id;
						System.out.println("Nom corps = " + aux.getText() //$NON-NLS-1$
								+ " index statut : " + id); //$NON-NLS-1$
						ja.getListeBase().elementAt(
								ja.getBaseIndexByName(aux.getText()))
								.setIdStatut(id);
					}
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (o.getClass() == JTextField.class) {
					temp = ((JTextField) o).getText();
				}
				return;
			}
		};
	}

}
