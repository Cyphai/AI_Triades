package relations;

import graphicalUserInterface.DataPackView;
import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import translation.Messages;


public class RelationBrowser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5902621915174739377L;


	protected RelationPanel relationPanel;

	protected JPanel mainPanel;
	protected EnsembleRelation relationSet;
	protected JList relationList;
	protected JButton switchButton;
	protected JPanel descriptionPanel;
	protected JPanel cardPanel;
	protected CardLayout cardLayout;
	protected boolean contentMode;
	protected DefaultListModel relationListModel;


	public RelationBrowser(final EnsembleRelation relationSet) {

		mainPanel = new JPanel(new BorderLayout());

		this.relationSet = relationSet;
		relationPanel = new RelationPanel(getMeaningList());
		contentMode = true;
		setLayout(new BorderLayout());

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		mainPanel.add(relationPanel,BorderLayout.CENTER);

		JPanel listPanel = new JPanel(new BorderLayout());
		relationListModel = new DefaultListModel();
		Vector<RelationDescription> relationVector = new Vector<RelationDescription>();
		
		for (RelationDescription relation : relationSet.table) {
			relationVector.addElement(relation);
		}
		Collections.sort(relationVector, new Comparator<RelationDescription>() {

			@Override
			public int compare(RelationDescription o1, RelationDescription o2) {
				
				return Collator.getInstance().compare(o1.toString(), o2.toString());
			}
		});
		for (RelationDescription rD : relationVector)
			relationListModel.addElement(rD);
		
		relationList = new JList(relationListModel);

		relationList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				openRelationDescription((RelationDescription) relationList
						.getSelectedValue());
			}
		});

		listPanel.add(new JScrollPane(relationList), BorderLayout.CENTER);
		switchButton = new JButton(Messages.getString("RelationBrowser.0")); //$NON-NLS-1$
		switchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				switchShownPanel();

			}

		});
		switchButton.setEnabled(false);
		JPanel addPanel = new JPanel();
		addPanel.setLayout(new BoxLayout(addPanel,BoxLayout.X_AXIS));
		final JTextField name = new JTextField(
				Messages.getString("RelationBrowser.1")); //$NON-NLS-1$
		name
		.addFocusListener(DataPackView
				.generateMouseListener(Messages.getString("RelationBrowser.2"))); //$NON-NLS-1$
		JButton add = new JButton(Messages.getString("RelationBrowser.3")); //$NON-NLS-1$
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!name.getText().equals(Messages.getString("RelationBrowser.4")) && name.getText().trim().length() > 0) //$NON-NLS-1$
				{
				RelationDescription newRelation = new RelationDescription(name.getText().trim());
				relationListModel.addElement(newRelation);

				relationSet.addRelationDescription(newRelation);
				name.setText(Messages.getString("RelationBrowser.4")); //$NON-NLS-1$
				relationList
				.setSelectedIndex(relationList.getModel().getSize() - 1);
				openRelationDescription((RelationDescription) relationList
						.getSelectedValue());
				}
			}
		});
		addPanel.add(name);
	
		JPanel delPanel = new JPanel();
		delPanel.setLayout(new BoxLayout(delPanel, BoxLayout.X_AXIS));

		JButton delRelation = new JButton(Messages.getString("RelationBrowser.5")); //$NON-NLS-1$
		delRelation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (DialogHandlerFrame
						.showYesNoDialog(Messages.getString("RelationBrowser.6")) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
					relationSet.table.remove(relationList.getSelectedValue());
					relationListModel.removeElement(relationList
							.getSelectedValue());
					relationPanel.loadRelation(null);
				}

			}
		});

		JButton renameRelation = new JButton(Messages.getString("RelationBrowser.7")); //$NON-NLS-1$
		renameRelation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RelationDescription relation = (RelationDescription)relationList.getSelectedValue();
				
				if(relation != null) {
					String string = JOptionPane.showInputDialog(Program.myMainFrame, Messages.getString("RelationBrowser.8") + relation + "\" : ", Messages.getString("RelationBrowser.12"), JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					
					if(string != null && string.trim().compareTo("") !=0) { //$NON-NLS-1$
						relation.setName(string);
						relationList.repaint();
					}
				}
			}
		});
		
		delPanel.add(Box.createGlue());
		delPanel.add(add);
		delPanel.add(renameRelation);
		delPanel.add(delRelation);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(addPanel, BorderLayout.CENTER);
		buttonPanel.add(delPanel, BorderLayout.SOUTH);

		listPanel.add(buttonPanel, BorderLayout.SOUTH);
		JPanel compressPanel = new JPanel(new BorderLayout());
		compressPanel.setLayout(new BoxLayout(compressPanel, BoxLayout.X_AXIS));
		compressPanel.add(Box.createGlue());
		compressPanel.add(listPanel);
		compressPanel.add(Box.createGlue());

		mainPanel.add(compressPanel, BorderLayout.NORTH);
		cardPanel.add(mainPanel,"main"); //$NON-NLS-1$
		add(switchButton, BorderLayout.SOUTH);

		if (relationListModel.size() > 0)
		{
			relationList.setSelectedIndex(0);
			openRelationDescription((RelationDescription) relationList
					.getSelectedValue());
		}
		add(cardPanel, BorderLayout.CENTER);

	}

	protected void openRelationDescription(RelationDescription newRelation)
	{
		if (newRelation != null) {
			if (!contentMode)
				switchShownPanel();

			if (descriptionPanel != null)
				cardPanel.remove(descriptionPanel);

			relationPanel.loadRelation(newRelation);
			descriptionPanel = new DescriptionBuilder(newRelation);

			cardPanel.add(descriptionPanel,"description"); //$NON-NLS-1$

			switchButton.setEnabled(true);
			validate();
		} else {
			relationPanel.loadRelation(null);
		}
	}



	protected void switchShownPanel() {
		if (contentMode) 
			switchButton.setText(Messages.getString("RelationBrowser.9")); //$NON-NLS-1$
		else 
			switchButton.setText(Messages.getString("RelationBrowser.10")); //$NON-NLS-1$

		cardLayout.next(cardPanel);
		contentMode = !contentMode;

		cardPanel.validate();
	}

	public void refreshList()
	{
		relationListModel.clear();
		for (RelationDescription relation : relationSet.table) {
			relationListModel.addElement(relation);
		}

		relationList.validate();
	}
	
	protected DefaultComboBoxModel getMeaningList()
	{
		Vector<Mean> result = new Vector<Mean>();
		for (RelationDescription rD : relationSet.getRelationDescriptions())
		{
			for (Entry<Goal,ArrayList<Mean>> entry : rD.getPossibility().getMap().entrySet())
			{
				if (entry.getValue() == null)
					System.err.println("#### Goal avec arrayList null : "+entry.getKey());  //$NON-NLS-1$
				
				
				for (int j = 0; j < entry.getValue().size(); j++)
				{
					boolean ok = false;
					for (int i = 0;i < result.size(); i++)
					{
						if (result.elementAt(i).equals(entry.getValue().get(j)))
						{
							ok = true;
							entry.getValue().set(j, result.elementAt(i));
							break;
						}
					}
					if (!ok)
					{
						result.addElement(entry.getValue().get(j));
					}
				}
			}
		}
		Collections.sort(result, new Comparator<Mean>() {
			@Override
			public int compare(Mean o1, Mean o2) {
				return Collator.getInstance().compare(o1.toString(), o2.toString());
			}
		});
		return new DefaultComboBoxModel(result);
	}

}
