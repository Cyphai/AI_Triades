package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import translation.Messages;

import dataPack.Activite;

import models.Brick;

public class BrickParameterPopUp extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1782702240360015741L;

	protected Brick brick;
	protected JTextField name;
	protected JComboBox step;
	protected JComboBox activity;
	protected JPanel mainPanel;

	public BrickParameterPopUp(Brick theBrick)
	{
		brick = theBrick;
		buildPanel(this);
		setVisible(false); 
	}
	private void buildPanel(JPanel result)
	{
		final BrickParameterPopUp access = this;		

		result.setLayout(new BorderLayout());
		result.setBorder(BorderFactory.createRaisedBevelBorder());

		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel,BoxLayout.Y_AXIS));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));



		name = new JTextField(35);
		if (brick != null)
			name.setText(brick.getName());
		name.setBorder(BorderFactory.createTitledBorder(Messages.getString("BrickParameterPopUp.0"))); //$NON-NLS-1$
		//name.setAlignmentX(LEFT_ALIGNMENT);
		name.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (name.getText().trim().length() == 0)

					name.setText(brick.getName());
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});

		step = new JComboBox(Program.myMainFrame.getDataPack().getSteps());
		if (brick != null)
			step.setSelectedItem(brick.getNoTranslatedStep());
		step.setBorder(BorderFactory.createTitledBorder(Messages.getString("BrickParameterPopUp.1"))); //$NON-NLS-1$
		//	step.setAlignmentX(LEFT_ALIGNMENT);
		activity = new JComboBox(Program.myMainFrame.getDataPack().getActivities().getActivities());
		activity.setBorder(BorderFactory.createTitledBorder(Messages.getString("BrickParameterPopUp.2"))); //$NON-NLS-1$
		


		JPanel tempNamePanel = new JPanel();
		tempNamePanel.setLayout(new FlowLayout());
		tempNamePanel.add(name);


		fieldPanel.add(Box.createVerticalGlue());
		fieldPanel.add(tempNamePanel);
		fieldPanel.add(Box.createRigidArea(new Dimension(250,5)));
		fieldPanel.add(step);
		fieldPanel.add(activity);
		fieldPanel.add(Box.createGlue());

		JButton ok = new JButton(Messages.getString("BrickParameterPopUp.3")); //$NON-NLS-1$
		JButton cancel = new JButton(Messages.getString("BrickParameterPopUp.4")); //$NON-NLS-1$

		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean change = false;
				String oldName = brick.toString();
				String newTitle = name.getText().trim();
				if (newTitle.length() > 0)
				{
					if (!newTitle.equals(brick.getName()))
					{
						brick.setName(newTitle);
						change = true;
					}
				}
				else
				{
					name.setText(brick.getName());

				}

				if (!step.getSelectedItem().toString().equals(brick.getNoTranslatedStep()))				
				{
					brick.setStep(step.getSelectedItem().toString());
					change = true;
				}
				
				if (!activity.getSelectedItem().equals(brick.getActivity()))
				{
					brick.switchActivity(brick.getActivity(), (Activite) activity.getSelectedItem());
					change = true;
				}
				
				if (change)
				{
					if (Program.myMainFrame.closeTab(oldName))
					{
						Program.myMainFrame.addTab(brick);
						Program.myMainFrame.showMainTab();
					}

				}
				
				
				access.setVisible(false);

			}
		});

		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				access.setVisible(false);
			}
		});

		buttonPanel.add(Box.createGlue());
		buttonPanel.add(cancel);
		buttonPanel.add(ok);
		buttonPanel.add(Box.createGlue());

		result.add(fieldPanel,BorderLayout.CENTER);
		result.add(buttonPanel, BorderLayout.SOUTH);

		

	}

	public void showMeTheBrick(Brick theBrick)
	{
		if (step.getItemCount() != theBrick.getDatapack().getSteps().size())
		{
			step.removeAllItems();
			for (String s : theBrick.getDatapack().getSteps())
			{
				step.addItem(s);
			}
		}
		
		
		
		brick = theBrick;
		name.setText(brick.getName());
		step.setSelectedItem(brick.getNoTranslatedStep());
		activity.setSelectedItem(brick.getActivity());
		setVisible(true);
	}

}
