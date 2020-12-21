package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ToolTipManager;
import javax.swing.border.EtchedBorder;

import main.ActionTime;
import main.RelationComplete;
import models.BrickEdge;
import models.BrickVertex;
import models.TriadeEditingMousePlugin;
import relations.Goal;
import relations.Mean;
import relations.RelationPossibility;
import translation.Messages;

public class RelationChooserView extends JPanel {

	private static final long serialVersionUID = 1124347489987701165L;

	protected Vector<JComboBox> goal;
	protected Vector<JComboBox> ressources;
	protected RelationComplete relationSet;
	protected RelationPossibility possibleRelationSet;
	protected BrickEdge returnRelation;
	protected int mode;

	public RelationChooserView(BrickEdge modelEdge,
			TriadeEditingMousePlugin<BrickVertex, BrickEdge> mousePlugin,
			RelationPossibility _possibleRelationSet, int _mode,
			RelationChooserPopUp parent) {
		relationSet = modelEdge.getCompleteRelation();
		possibleRelationSet = _possibleRelationSet;
		mode = _mode;
		returnRelation = mousePlugin.getModelView().getReturnRelation(modelEdge);
		goal = new Vector<JComboBox>();
		ressources = new Vector<JComboBox>();
		final RelationChooserView chooser = this;
		int j = 0;
		Vector<Goal> sortedGoal = new Vector<Goal>(possibleRelationSet.getMap().keySet());
		sortedGoal.remove(RelationPossibility.UNDEFINED_GOAL);
		sortedGoal.remove(RelationPossibility.NORELATION_GOAL);
		Collections.sort(sortedGoal, new Comparator<Goal>() {

			@Override
			public int compare(Goal o1, Goal o2) {
				return Collator.getInstance().compare(o1.toString(), o2.toString());
			}
		});
		sortedGoal.add(0, RelationPossibility.NORELATION_GOAL);
		sortedGoal.add(0, RelationPossibility.UNDEFINED_GOAL);

		for (@SuppressWarnings("unused")
		ActionTime action : Program.myMainFrame.datapack.getActionTimeList()) {
			JComboBox goalSingle = new JComboBox(sortedGoal);
			goalSingle.setRenderer(new ToolTipListCellRenderer(goalSingle.getRenderer()));


			goal.add(goalSingle);
			for (int i = 0; i < goalSingle.getItemCount(); i++) {
				if (mode == RelationPossibility.RELATIONSTRUCTURELLE) {
					if (goalSingle.getItemAt(i).equals(
							relationSet.getJeuRelation(j).objectifStructurel)) {
						goalSingle.setSelectedIndex(i);
						break;
					}
				} else {
					if (goalSingle.getItemAt(i).equals(
							relationSet.getJeuRelation(j).objectifReel)) {
						goalSingle.setSelectedIndex(i);
						break;
					}
				}

			}
			final JComboBox ressourcesSingle = new JComboBox();
			ressourcesSingle.setRenderer(new ToolTipListCellRenderer(ressourcesSingle.getRenderer()));
			ressources.add(ressourcesSingle);
			ressourcesSingle.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					ressourcesSingle.setToolTipText(prepareToolTip(ressourcesSingle.getSelectedItem()));
				}
			});
			
			if (goalSingle.getItemCount() > 0)
				updateRessources(j);
			for (int i = 0; i < ressourcesSingle.getItemCount(); i++) {
				if (mode == RelationPossibility.RELATIONSTRUCTURELLE) {
					if (ressourcesSingle.getItemAt(i).equals(
							relationSet.getJeuRelation(j).moyenStructurel)) {
						ressourcesSingle.setSelectedIndex(i);
						break;
					}
				} else {
					if (ressourcesSingle.getItemAt(i).equals(
							relationSet.getJeuRelation(j).moyenReel)) {
						ressourcesSingle.setSelectedIndex(i);
						break;
					}
				}

			}
			j++;
		}

		for (final JComboBox goalSingle : goal) {
			goalSingle.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int index = goal.indexOf(e.getSource());
					chooser.updateRessources(index);
					goalSingle.setToolTipText(prepareToolTip(goalSingle.getSelectedItem()));
				}

			});
		}
		
		setLayout(new BorderLayout());
		build(parent, modelEdge, mousePlugin);
	}

	protected class ToolTipListCellRenderer implements ListCellRenderer{

		protected ListCellRenderer source;

		public ToolTipListCellRenderer(ListCellRenderer source)
		{
			super();
			this.source = source;
		}

		public 	Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			JComponent c = (JComponent)source.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (isSelected || index == -1)
			{
				String tooltip = prepareToolTip(value);
				c.setToolTipText(null);
				c.validate();
				//ToolTipManager.sharedInstance().setEnabled(false);
				c.setToolTipText(tooltip);			
				//ToolTipManager.sharedInstance().setEnabled(true);
			}
			else
			{
				c.setToolTipText(null);
			}
			return c;
		}
	}

	protected String prepareToolTip(Object value)
	{
		String tooltip;
		if (value instanceof Mean)
		{
			Mean m = (Mean)value;
			String s1 = m.getNoTranslatedString();
			String s2 = m.toString();
			if (s1.equals(s2))
			{
				tooltip = "<html>"+s1+"</html>";
			}
			else
			{
				tooltip ="<html>"+ s2 +"<br/>("+s1+")</html>";
			}
		}
		else if (value instanceof Goal)
		{
			Goal g = (Goal)value;
			String s1 = g.getNoTranslatedString();
			String s2 = g.toString();
			if (s1.equals(s2))
			{
				tooltip = "<html>"+s1+"</html>";
			}
			else
			{
				tooltip ="<html>"+ s2 +"<br/>("+s1+")</html>";
		}
		}
		else
			tooltip = null;

		return tooltip;
	}
	
	protected void updateRessources(int index) {
		Vector<Mean> newMeans = new Vector<Mean>();
		ressources.elementAt(index).removeAllItems();
		if (possibleRelationSet.getMap().containsKey(goal.elementAt(index).getSelectedItem()))
		{
			Goal selectedGoal = (Goal) goal.elementAt(index).getSelectedItem();

			if (selectedGoal == null)
			{
				selectedGoal = (Goal) goal.elementAt(index).getItemAt(0);

				if (selectedGoal == null)
				{
					System.out
					.println("RelationChooserView.updateRessources() : Aucun objectif dans la listes, impossible de trouver les moyens"); //$NON-NLS-1$
					return;
				}
				goal.elementAt(index).setSelectedIndex(0);
			}


			if (selectedGoal == null || possibleRelationSet.getMap().get(goal.elementAt(index).getSelectedItem()) == null)
			{
				System.err.println("Goal : "+selectedGoal+" correspond a rien dans la table"); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			else
			{
				for (Mean str : possibleRelationSet.getMap().get(goal.elementAt(index).getSelectedItem())) {
					newMeans.add(str);			
				}
			}
		}

		Collections.sort(newMeans, new Comparator<Mean>() {

			@Override
			public int compare(Mean o1, Mean o2) {
				return Collator.getInstance().compare(o1.toString(), o2.toString());
			}
		});
		for (Mean m : newMeans)
			ressources.elementAt(index).addItem(m);

		ressources.elementAt(index).validate();

	}

	private void build(final RelationChooserPopUp parent,
			final BrickEdge modelEdge,
			final TriadeEditingMousePlugin<BrickVertex, BrickEdge> mousePlugin) {
		// Declarations
		JPanel panelLeft = new JPanel();
		JPanel panelRight = new JPanel(new BorderLayout());
		JPanel north = new JPanel(new BorderLayout());
		JPanel center = new JPanel();
		JPanel south = new JPanel(new BorderLayout());
		JPanel pChoices = new JPanel();
		JPanel pButtons = new JPanel(new GridLayout(1, 4));
		BoxLayout layoutCenter = new BoxLayout(center, BoxLayout.Y_AXIS);
		BoxLayout layoutChoices = new BoxLayout(pChoices, BoxLayout.Y_AXIS);
		BoxLayout layoutLeft = new BoxLayout(panelLeft, BoxLayout.Y_AXIS);
		JLabel title = new JLabel(
				Messages.getString("RelationChooserView.1")); //$NON-NLS-1$
		JButton showReturnRelation = new JButton(Messages.getString("RelationChooserView.10")); //$NON-NLS-1$
		if (returnRelation != null)
		{
			showReturnRelation.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mousePlugin.selectEdge(returnRelation);
					saveState();
					parent.showRelationChooserView(returnRelation, mousePlugin, Messages.getString("SchemaEditingMousePlugin.2"), false); //$NON-NLS-1$
				}
			});
		}
		else
		{
			showReturnRelation.setText(Messages.getString("RelationChooserView.11")); //$NON-NLS-1$
			showReturnRelation.setEnabled(false);
		}
		JButton ok = new JButton(Messages.getString("RelationChooserView.2")); //$NON-NLS-1$
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveState();
				parent.removeRelationChooserView();
				mousePlugin.getModelView().repaint();
			}

		});
		JButton cancel = new JButton(Messages.getString("RelationChooserView.3")); //$NON-NLS-1$
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.removeRelationChooserView();
				if (mode == RelationPossibility.RELATIONSTRUCTURELLE
						&& relationSet.isEmpty(mode))
				{
					mousePlugin.selectEdge(null);
					mousePlugin.getEditedAbstractSchema().removeModelEdge(
							modelEdge);



				}
				mousePlugin.getModelView().repaint();
			}

		});

		// Barre de titre
		north.setBackground(Color.white);
		title.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		north.add(title, BorderLayout.CENTER);
		north.add(new JSeparator(), BorderLayout.SOUTH);

		// Left panel
		panelLeft.setLayout(layoutLeft);
		panelLeft.add(Box.createVerticalStrut(65));

		// Choix possibles
		pChoices.setLayout(layoutChoices);
		JPanel pnGrid = new JPanel(new GridLayout(1, 2));
		JPanel pnLeft = new JPanel();
		pnLeft.setLayout(new BoxLayout(pnLeft, BoxLayout.X_AXIS));
		JPanel pnRight = new JPanel();
		pnRight.setLayout(new BoxLayout(pnRight, BoxLayout.X_AXIS));
		pnLeft.add(Box.createGlue());
		pnLeft.add(new JLabel(IconDatabase.iconLeftArrow));
		pnLeft.add(new JLabel(Messages.getString("RelationChooserView.4"))); //$NON-NLS-1$
		pnLeft.add(new JLabel(IconDatabase.iconRightArrow));
		pnLeft.add(Box.createGlue());
		pnRight.add(Box.createGlue());
		pnRight.add(new JLabel(IconDatabase.iconLeftArrow));
		pnRight.add(new JLabel(Messages.getString("RelationChooserView.5"))); //$NON-NLS-1$
		pnRight.add(new JLabel(IconDatabase.iconRightArrow));
		pnRight.add(Box.createGlue());
		pnGrid.add(pnLeft);
		pnGrid.add(pnRight);
		pChoices.add(pnGrid);
		int isTriade = Program.isTriades() ? 1 : 0;
		for (int i = 0; i < Program.myMainFrame.datapack.getActionTimeList()
				.size(); i++) {
			final JPanel pnl = new JPanel(new GridLayout(1 + isTriade, 2));
			pnl.setBorder(BorderFactory
					.createTitledBorder(Messages.getString("RelationChooserView.6") //$NON-NLS-1$
							+ Program.myMainFrame.getDataPack()
							.getActionTimeList().get(i)));
			JLabel truc = new JLabel(" " //$NON-NLS-1$
					+ relationSet.getJeuRelation(i).objectifStructurel);
			truc.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));
			JLabel machin = new JLabel(" " //$NON-NLS-1$
					+ relationSet.getJeuRelation(i).moyenStructurel);
			machin.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));
			if (isTriade == 1) {
				pnl.add(truc);
				pnl.add(machin);
			}
			pnl.add(goal.elementAt(i));
			pnl.add(ressources.elementAt(i));
			pChoices.add(pnl);
			final int _i = i;
			if (i % 2 == 1 || i > 0) {
				JToolBar jtb = new JToolBar();
				JButton button = new JButton(IconDatabase.iconDuplicateArrow);
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						int indexGoal = goal.elementAt(_i - 1)
								.getSelectedIndex();
						int indexRessources = ressources.elementAt(_i - 1)
								.getSelectedIndex();
						goal.elementAt(_i).setSelectedIndex(indexGoal);
						ressources.elementAt(_i).setSelectedIndex(
								indexRessources);
					}

				});
				jtb.setFloatable(false);
				button.setFocusable(false);
				jtb.add(button);
				panelLeft.add(jtb);
				panelLeft.add(Box.createVerticalStrut(35));
			}
		}
		// panelLeft.add(Box.createVerticalStrut(50));
		center.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 2));
		center.setLayout(layoutCenter);
		center.add(Box.createGlue());
		center.add(pChoices);
		center.add(Box.createGlue());

		// Barre des boutons
		pButtons.add(Box.createGlue());
		pButtons.add(showReturnRelation);
		pButtons.add(Box.createGlue());
		pButtons.add(ok);
		pButtons.add(cancel);
		pButtons.add(Box.createGlue());
		pButtons.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		south.add(new JSeparator(), BorderLayout.NORTH);
		south.add(pButtons, BorderLayout.CENTER);

		// Panel right
		panelRight.add(center, BorderLayout.CENTER);

		// Panel total
		add(north, BorderLayout.NORTH);
		add(panelLeft, BorderLayout.WEST);
		add(panelRight, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
	}

	protected void saveState() {
		for (int i = 0; i < goal.size(); i++) {
			if (mode == RelationPossibility.RELATIONSTRUCTURELLE) {
				if (goal.elementAt(i).getSelectedItem() != null)
					relationSet.getJeuRelation(i).objectifStructurel = (Goal)goal
					.elementAt(i).getSelectedItem();
				if (ressources.elementAt(i).getSelectedItem() != null)
					relationSet.getJeuRelation(i).moyenStructurel = (Mean)ressources
					.elementAt(i).getSelectedItem();
			} else {

				if (goal.elementAt(i).getSelectedItem() != null)
					relationSet.getJeuRelation(i).objectifReel = (Goal)goal.elementAt(i)
					.getSelectedItem();
				if (ressources.elementAt(i).getSelectedItem() != null)
					relationSet.getJeuRelation(i).moyenReel = (Mean)ressources
					.elementAt(i).getSelectedItem();
			}
		}
		if (relationSet.isNoRelation())
		{
			DialogHandlerFrame.showErrorDialog(Messages.getString("RelationChooserView.9")); //$NON-NLS-1$
		}

		Program.myMainFrame.repaint();

	}

}
