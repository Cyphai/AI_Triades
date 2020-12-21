package relations;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.ExecutionMode;
import graphicalUserInterface.Program;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.org.apache.bcel.internal.generic.CPInstruction;

import translation.Messages;

public class RelationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9071668279819902914L;

	protected RelationDescription relation;

	protected JList objectives;
	protected DefaultListModel objectivesModel;
	protected JList meanings;
	protected DefaultListModel meaningsModel;
	protected DefaultComboBoxModel meaningList;

	protected ArrayList<Mean> currentMeanings;
	protected JButton addObj;
	protected JButton addMean;
	protected JButton changeObj;
	protected JButton changeMean;
	protected JButton deleteObj;
	protected JButton deleteMean;
	
	protected Vector<Goal> copiedGoals;
	protected Vector<ArrayList<Mean> > associatedMeanings;
	
	protected Vector<Mean> copiedMeans;
	
	public RelationPanel(DefaultComboBoxModel meaningList) {
		this.meaningList = meaningList;
		copiedMeans = null;
		associatedMeanings = null;
		copiedGoals = null;
		build();
	}

	protected void build() {
		final RelationPanel access = this;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel objPanel = new JPanel(new BorderLayout());
		objectivesModel = new DefaultListModel();
		objectives = new JList(objectivesModel);
		objectives.setEnabled(false);

		objectives.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				refreshMeanings();

			}
		});

		JPanel addObjPanel = new JPanel();
		addObjPanel.setLayout(new BorderLayout());
		final JTextField objName = new JTextField();
		addObjPanel.add(objName, BorderLayout.CENTER);
		addObj = new JButton(Messages.getString("RelationPanel.0")); //$NON-NLS-1$
		addObj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!objName.getText().trim().equals("")) //$NON-NLS-1$
				{
					addObjective(objName.getText());
					objName.setText(""); //$NON-NLS-1$
					objName.requestFocus();
				}
			}
		});

		deleteObj = new JButton(Messages.getString("RelationPanel.2")); //$NON-NLS-1$
		deleteObj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (objectives.getSelectedIndex() == -1) {
					DialogHandlerFrame
					.showErrorDialog(Messages.getString("RelationPanel.4")); //$NON-NLS-1$
					return;
				}

				if (JOptionPane.YES_OPTION == DialogHandlerFrame
						.showYesNoDialog(Messages.getString("RelationPanel.3"))) //$NON-NLS-1$
				{
					Goal obj = (Goal) objectives.getSelectedValue();
					relation.getPossibility().possibility.remove(obj);
					objectivesModel.removeElement(obj);
					if (objectivesModel.getSize() > 0)
						objectives.setSelectedValue(objectivesModel.get(0),true);
					else 
						objectives.clearSelection();

					objectives.repaint();
					refreshMeanings();
				} 

			}
		});

		changeObj= new JButton(Messages.getString("RelationPanel.8")); //$NON-NLS-1$
		changeObj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Goal goal = (Goal)objectives.getSelectedValue();

				if(goal != null) {
					String string =(String) JOptionPane.showInputDialog(Program.myMainFrame, Messages.getString("RelationPanel.12") + goal + "\" : ", Messages.getString("RelationPanel.16"), JOptionPane.QUESTION_MESSAGE, null, null, goal.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

					if(string != null && string.trim().compareTo("") !=0) { //$NON-NLS-1$

						//Goal existingGoal = Program.myMainFrame.datapack.findGoal(string);
						ArrayList<Mean> tempMeans = relation.getPossibility().getMap().get(goal);
						relation.getPossibility().getMap().remove(goal);
						goal.setName(string);
						relation.getPossibility().getMap().put(goal, tempMeans);

						loadRelation(relation);
					}
				}
			}
		});

		
		final JButton pasteGoals = new JButton("Coller");
		pasteGoals.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (copiedGoals == null || copiedGoals.size() == 0)
				{
					DialogHandlerFrame.showErrorDialog("Des objectifs doivent être copiés avant de pouvoir être collés.");
					return;
				}
				
				for (int i = 0; i < copiedGoals.size(); i++)
				{
					addGoalWithMeanings(copiedGoals.elementAt(i), associatedMeanings.elementAt(i));
				}
				refreshMeanings();
				repaint();
			}
		});


		JButton copyGoals = new JButton("Copier");
		copyGoals.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (copiedGoals == null)
				{
					copiedGoals = new Vector<Goal>();
					associatedMeanings = new Vector<ArrayList<Mean>>();
				}
				copiedGoals.clear();
				associatedMeanings.clear();
	
				if (objectives.getSelectedValues().length == 0)
				{
					DialogHandlerFrame.showErrorDialog("Vous devez selectionner des objectifs avant de pouvoir les copier\nCtrl et Maj permettent des sélections multiples");
					return;
				}
	String tooltip = "<html>";
				
				for (Object o :	objectives.getSelectedValues())
				{
					if (o instanceof Goal)
					{
						Goal g = (Goal)o;
						copiedGoals.add(g);
						associatedMeanings.add(relation.getPossibility().getMap().get(g));
						tooltip += g.toString()+"<br/>";
					}
				}
				tooltip += "</html>";
				pasteGoals.setToolTipText(tooltip);
			}
		});
		
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.add(addObj);
		buttonsPanel.add(changeObj);
		buttonsPanel.add(deleteObj);
		buttonsPanel.add(copyGoals);
		buttonsPanel.add(pasteGoals);
		if (ExecutionMode.isDebug())
		{
			JButton countObj = new JButton(Messages.getString("Version_0_9_9_9.RelationPanel.1")); //$NON-NLS-1$
			countObj.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					Goal goal = (Goal)objectives.getSelectedValue();
					if (goal != null)
					{
						int count = Program.myMainFrame.datapack.countGoalUsage(goal);
						DialogHandlerFrame.showErrorDialog(Messages.getString("Version_0_9_9_9.RelationPanel.14")+count); //$NON-NLS-1$
					}
				}
			});
			buttonsPanel.add(countObj);
		}


		addObjPanel.add(buttonsPanel, BorderLayout.SOUTH);
		addObjPanel.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("RelationPanel.5"))); //$NON-NLS-1$
		objPanel.add(addObjPanel, BorderLayout.SOUTH);
		objPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("RelationPanel.6"))); //$NON-NLS-1$
		objPanel.add(new JScrollPane(objectives), BorderLayout.CENTER);

		JPanel meanPanel = new JPanel(new BorderLayout());
		meaningsModel = new DefaultListModel();
		meanings = new JList(meaningsModel);
		meanings.setEnabled(false);
		
		JPanel addMeanPanel = new JPanel();
		addMeanPanel.setLayout(new BorderLayout());
		final JComboBox meanName = new JComboBox(meaningList);
		meanName.setEditable(true);
		meanName.setSelectedItem(""); //$NON-NLS-1$
		final JTextField textField = (JTextField)meanName.getEditor().getEditorComponent();
		textField.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				int index = -1;

				String begin = textField.getText();
				index = 0;
				for (; index < meaningList.getSize()-1; index++)
				{
					if (meaningList.getElementAt(index+1).toString().compareToIgnoreCase(begin) > 0)
					{
						break;
					}
				}
				if (index != meaningList.getSize()-1)
				{

					meanName.showPopup();

				}
				else
				{
					meanName.hidePopup();
				}

			}
		});





		addMeanPanel.add(meanName, BorderLayout.CENTER);
		addMean = new JButton(Messages.getString("RelationPanel.7")); //$NON-NLS-1$
		addMean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object newMeanObject = meanName.getSelectedItem();
				if (newMeanObject instanceof Mean)
				{
					Mean newMean = (Mean)newMeanObject;
					currentMeanings.add(newMean);
					meaningsModel.add(meaningsModel.size(),newMean);
				}
				else if (newMeanObject instanceof String)
				{
					String newMeanString = (String)newMeanObject;
					newMeanString = newMeanString.trim();
					Mean newMean = null;
					if (newMeanString.length() > 0)
					{
						for (int i = 0; i < meaningList.getSize() && newMean == null; i++)
						{
							if (meaningList.getElementAt(i).toString().equalsIgnoreCase(newMeanString))
							{
								newMean = (Mean)meaningList.getElementAt(i);
							}
						}
						if (newMean == null)
						{
							newMean = new Mean(newMeanString.trim());
							meaningList.addElement(newMean);
							currentMeanings.add(newMean);
							meaningsModel.add(meaningsModel.size(), newMean);

						}
						else
						{

							currentMeanings.add(newMean);
							meaningsModel.add(meaningsModel.size(), newMean);
						}
					}
				}
				refreshMeanings();
				meanName.setSelectedItem(""); //$NON-NLS-1$
				meanName.requestFocus();


			}
		});
		addMean.setEnabled(false);

		changeMean = new JButton(Messages.getString("RelationPanel.19")); //$NON-NLS-1$
		changeMean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Mean mean = (Mean)meanings.getSelectedValue();

				if(mean != null) {
					String string = (String)JOptionPane.showInputDialog(Program.myMainFrame, Messages.getString("RelationPanel.20") + mean + Messages.getString("RelationPanel.23"), Messages.getString("RelationPanel.22"), JOptionPane.QUESTION_MESSAGE,null, null, mean.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

					if(string != null && string.trim().compareTo("") !=0) { //$NON-NLS-1$
						mean.setName(string);
						access.repaint();
					}
				}
			}
		});

		deleteMean = new JButton(Messages.getString("RelationPanel.9")); //$NON-NLS-1$
		deleteMean.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (meanings.getSelectedValue() != null)
				{				
					Mean mean = (Mean) meanings.getSelectedValue();
					currentMeanings.remove(mean);
					meaningsModel.removeElement(mean);
					meanings.setSelectedIndex(0);
					refreshMeanings();
				}
			}
		});
		
		final JButton pasteMean = new JButton("Coller");
		pasteMean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (copiedMeans == null || copiedMeans.size() == 0)
				{
					DialogHandlerFrame.showErrorDialog("Des moyens doivent être copiés avant de pouvoir être collés.");
					return;
		
				}
				
				for (Mean m : copiedMeans)
				{
					if (!currentMeanings.contains(m))
					{
						currentMeanings.add(m);
						meaningsModel.addElement(m);
					}
				}
				meanings.repaint();
			}
		});
		
		JButton copyMean = new JButton("Copier");
		copyMean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (copiedMeans == null)
					copiedMeans = new Vector<Mean>();
				copiedMeans.clear();
				if (meanings.getSelectedValues().length == 0)
				{
					DialogHandlerFrame.showErrorDialog("Vous devez selectionner des moyens avant de pouvoir les copier\nCtrl et Maj permettent des sélections multiples");
					return;
				}
				String tooltip = "<html>";
				for (Object o : meanings.getSelectedValues())
				{
					if (o instanceof Mean)
					{
						Mean m = (Mean)o;
						copiedMeans.add(m);
						tooltip += m.toString()+"<br/>";
					}
				}
				tooltip += "</html>";
				pasteMean.setToolTipText(tooltip);
			}
		});


		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.add(addMean);
		buttonsPanel.add(changeMean);
		buttonsPanel.add(deleteMean);
		buttonsPanel.add(copyMean);
		buttonsPanel.add(pasteMean);

		if (ExecutionMode.isDebug())
		{
			JButton listMeans = new JButton(Messages.getString("Version_0_9_9_9.RelationPanel.17")); //$NON-NLS-1$
			listMeans.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (Mean m : currentMeanings)
					{
						System.out.println(m);
					}

				}
			});
			buttonsPanel.add(listMeans);
		}		
		addMeanPanel.add(buttonsPanel, BorderLayout.SOUTH);
		addMeanPanel.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("RelationPanel.10"))); //$NON-NLS-1$
		meanPanel.add(addMeanPanel, BorderLayout.SOUTH);
		meanPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("RelationPanel.11"))); //$NON-NLS-1$
		meanPanel.add(new JScrollPane(meanings), BorderLayout.CENTER);
		add(Box.createHorizontalGlue());
		add(objPanel);
		add(meanPanel);
		add(Box.createHorizontalGlue());

		loadRelation(null);
	}

	public void loadRelation(RelationDescription newRelation) {
		relation = newRelation;
		objectivesModel.removeAllElements();

		if(newRelation != null) {
			for (Goal s : relation.getPossibility().possibility.keySet()) {
				objectivesModel.addElement(s);
			}
			if (objectivesModel.size() > 0) {
				objectives.setSelectedIndex(0);
				refreshMeanings();
			} else {
				addMean.setEnabled(false);
				meanings.setEnabled(false);
				deleteMean.setEnabled(false);
			}
			addObj.setEnabled(true);
			objectives.setEnabled(true);
			deleteObj.setEnabled(true);
		} else {
			meaningsModel.removeAllElements();

			objectives.setEnabled(false);
			addObj.setEnabled(false);
			addMean.setEnabled(false);
			meanings.setEnabled(false);
			deleteMean.setEnabled(false);
			deleteObj.setEnabled(false);
		}
		validate();
		repaint();
	}

	protected void addObjective(String newObjective) {
		if (relation == null) {
			System.err.println("Relation null lors d'un ajout"); //$NON-NLS-1$
			return;
		}
		Goal newGoal = new Goal(newObjective);
		if (relation.getPossibility().possibility.containsKey(newGoal)) {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("RelationPanel.13")); //$NON-NLS-1$

			return;
		}

		if (newObjective != "") { //$NON-NLS-1$
			currentMeanings = new ArrayList<Mean>();
			relation.getPossibility().possibility.put(newGoal,
					currentMeanings);
			objectivesModel.addElement(newGoal);
			objectives.setSelectedValue(newGoal, true);
			refreshMeanings();

		} else {
			DialogHandlerFrame
			.showErrorDialog(Messages.getString("RelationPanel.15")); //$NON-NLS-1$
		}
	}

	protected void addGoalWithMeanings(Goal g, ArrayList<Mean> meanings)
	{
		ArrayList<Mean> current = relation.getPossibility().getMap().get(g);
		if (current == null)
		{
			current = new ArrayList<Mean>(meanings);
			relation.getPossibility().getMap().put(g, current);
			objectivesModel.addElement(g);
		}
		else
		{
			for(Mean m : meanings)
			{
				if (!current.contains(m))
					current.add(m);
			}
		}
	}

	protected void refreshMeanings() {
		if(relation != null) {
			if (objectives.getSelectedValue() != null)
				currentMeanings = relation.getPossibility().getMap().get(objectives.getSelectedValue());
			else
				currentMeanings = null;
			meaningsModel.removeAllElements();
			if (currentMeanings != null)
				for (Mean s : currentMeanings) {
					meaningsModel.addElement(s);
				}
			addMean.setEnabled(true);
			deleteMean.setEnabled(true);
			meanings.setEnabled(true);
		} 
		validate();
	}
}
