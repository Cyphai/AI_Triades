package client;

import graphicalUserInterface.Program;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import translation.Messages;
import client.stringTranslator.StringTranslator;
import client.stringTranslator.StringTranslator.StringType;

public class ActorSheetView extends JPanel {
	private static final long serialVersionUID = -4713305113855049799L;
	
	private final ActorSheetView access;
	private ActorSheet actorSheet;
	
	protected ArrayList<ActorSheetSelectionListener> dataListener;

	abstract class ListenerLabel extends JLabel implements ActorSheetSelectionListener {
		private static final long serialVersionUID = -8048108358182083783L;
	};
	
	abstract class ListenerPanel extends JPanel implements ActorSheetSelectionListener {
		private static final long serialVersionUID = -8034108357182093783L;
	};

	abstract class ListenerCheckBox extends JCheckBox implements ActorSheetSelectionListener {
		private static final long serialVersionUID = -8649477081670733830L;

		public ListenerCheckBox(String text) {
			super(text);
		}
	};

	abstract class ListenerTextField extends JTextField implements ActorSheetSelectionListener {
		private static final long serialVersionUID = 7905579699396427169L;
	}

	abstract class ListenerJSpinner extends JSpinner implements ActorSheetSelectionListener{
		private static final long serialVersionUID = -3180229401554302906L;

		public ListenerJSpinner()
		{
			super(new SpinnerNumberModel(1, 1, 30, 1));
		}
	}

	abstract class ListenerJCombotBox extends JComboBox implements ActorSheetSelectionListener {
		private static final long serialVersionUID = 8976617493377440512L;
	}
	
	abstract class ListenerJTextArea extends JTextArea implements ActorSheetSelectionListener {
		private static final long serialVersionUID = 8976617493377440512L;
	}
	
	public ActorSheetView() {
		super();
		access = this;
		dataListener = new ArrayList<ActorSheetSelectionListener>();
		
		setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		//Actor Label
		final ListenerLabel actorLabel = new ListenerLabel() {
			private static final long serialVersionUID = 5102718103225905464L;

			@Override
			public void updateSelection(ActorSheet newActorSheet) {
				setText(getActorLabel(newActorSheet));
			}
		};
		actorLabel.setFont(actorLabel.getFont().deriveFont(Font.BOLD, actorLabel.getFont().getSize() + 8));

		dataListener.add(actorLabel);
		
		//actor name
		final ListenerTextField nameTextField = new ListenerTextField() {
			private static final long serialVersionUID = 2807588137160424492L;

			@Override
			public void updateSelection(ActorSheet actorSheet) {
				setText(actorSheet.getActorName());
			}
		};
		
		dataListener.add(nameTextField);
		
		nameTextField.setPreferredSize(new Dimension(175, 25));
		nameTextField.setMinimumSize(new Dimension(175, 25));
		nameTextField.setMaximumSize(new Dimension(175, 25));
		nameTextField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(nameTextField.getText().trim().compareTo("") != 0) { //$NON-NLS-1$
					access.actorSheet.setActorName(nameTextField.getText().trim());
				} else {
					access.actorSheet.setActorName(null);
				}
				actorLabel.setText(getActorLabel(access.actorSheet));
				Program.myMainFrame.validate();
				Program.myMainFrame.repaint();
			}
		});
		
		JPanel namePanel = new JPanel(new GridLayout());

		ListenerLabel label = new ListenerLabel() {
			private static final long serialVersionUID = -7839568516116934868L;

			@Override
			public void updateSelection(ActorSheet newActorSheet) {
				setText(Messages.getString("ActorSheetView.1") + StringTranslator.getDataPackTranslatedString(newActorSheet.getActor(), StringType.actorType) + Messages.getString("ActorSheetView.2"));//$NON-NLS-1$ //$NON-NLS-2$
			}
		};
		
		dataListener.add(label);
		
		JPanel namePanelTemp = new JPanel();
		namePanelTemp.setLayout(new BoxLayout(namePanelTemp, BoxLayout.X_AXIS));
		namePanelTemp.setPreferredSize(new Dimension(600, 30));
		namePanelTemp.setMinimumSize(new Dimension(600, 30));
		namePanelTemp.setMaximumSize(new Dimension(600, 30));		
		namePanelTemp.add(label);
		namePanelTemp.add(Box.createHorizontalStrut(20));
		namePanelTemp.add(nameTextField);
		namePanelTemp.add(Box.createGlue());
		
		namePanel.add(namePanelTemp);
		
		JPanel firstTextPanel = new JPanel();
		firstTextPanel.setLayout(new BoxLayout(firstTextPanel, BoxLayout.X_AXIS));
		firstTextPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("ActorSheetView.3")), BorderFactory.createEmptyBorder(1, 10, 5, 10))); //$NON-NLS-1$

		final ListenerJTextArea firstTextArea = new ListenerJTextArea() {
			private static final long serialVersionUID = 385376422727287127L;

			@Override
			public void updateSelection(ActorSheet newActorSheet) {
				setText(access.actorSheet.getFirstText());
			}
		};
		
		firstTextArea.setEditable(true);
		firstTextArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(firstTextArea.getText().trim().compareTo("") != 0) { //$NON-NLS-1$
					access.actorSheet.setFirstText(firstTextArea.getText());
				} else {
					access.actorSheet.setFirstText(null);
				}
			}
		});
		
		dataListener.add(firstTextArea);
		
		JScrollPane firstTextScroll = new JScrollPane(firstTextArea);
		firstTextScroll.setPreferredSize(new Dimension(800, 200));
		firstTextPanel.add(Box.createHorizontalGlue());
		firstTextPanel.add(firstTextScroll);
		firstTextPanel.add(Box.createHorizontalGlue());
		
		JPanel secondTextPanel = new JPanel();
		secondTextPanel.setLayout(new BoxLayout(secondTextPanel, BoxLayout.X_AXIS));
		secondTextPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("ActorSheetView.5")), BorderFactory.createEmptyBorder(1, 10, 5, 10))); //$NON-NLS-1$

		final ListenerJTextArea secondTextArea = new ListenerJTextArea() {
			private static final long serialVersionUID = -4539263793300685791L;

			@Override
			public void updateSelection(ActorSheet newActorSheet) {
				setText(access.actorSheet.getSecondText());
			}
		};
		
		dataListener.add(secondTextArea);
		
		secondTextArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(secondTextArea.getText().trim().compareTo("") != 0) { //$NON-NLS-1$
					access.actorSheet.setSecondText(secondTextArea.getText());
				} else {
					access.actorSheet.setSecondText(null);
				}
			}
		});
		JScrollPane secondTextScroll = new JScrollPane(secondTextArea);
		secondTextScroll.setPreferredSize(new Dimension(800, 200));
		secondTextPanel.add(Box.createHorizontalGlue());
		secondTextPanel.add(secondTextScroll);
		secondTextPanel.add(Box.createHorizontalGlue());
		
		
		JPanel notePanel = new JPanel();
		notePanel.setLayout(new BoxLayout(notePanel, BoxLayout.X_AXIS));
		notePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("ActorSheetView.7")), BorderFactory.createEmptyBorder(1, 10, 5, 10))); //$NON-NLS-1$

		final ListenerJTextArea noteTextArea = new ListenerJTextArea() {
			private static final long serialVersionUID = -6445110934727317597L;

			@Override
			public void updateSelection(ActorSheet newActorSheet) {
				setText(newActorSheet.getNoteText());
			}
		};
		
		dataListener.add(noteTextArea);
		
//		final JTextArea noteTextArea = new JTextArea(actorSheet.getNoteText());
		final JScrollPane noteTextScroll = new JScrollPane(noteTextArea);
		noteTextScroll.setPreferredSize(new Dimension(800, 200));
		noteTextArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(noteTextArea.getText().trim().compareTo("") != 0) { //$NON-NLS-1$
					access.actorSheet.setNoteText(noteTextArea.getText());
				} else {
					access.actorSheet.setNoteText(null);
				}
			}
		});
		notePanel.add(Box.createHorizontalGlue());
		notePanel.add(noteTextScroll);
		notePanel.add(Box.createHorizontalGlue());
		notePanel.setAutoscrolls(true);
		centerPanel.add(Box.createVerticalStrut(15));
		centerPanel.add(namePanel);
		centerPanel.add(Box.createVerticalStrut(15));
		centerPanel.add(firstTextPanel);
		centerPanel.add(Box.createVerticalStrut(15));
		centerPanel.add(secondTextPanel);
		centerPanel.add(Box.createVerticalStrut(15));
		centerPanel.add(notePanel);
		Session session = Program.myMainFrame.getDataPack().getCurrentSession();
		if (session != null && session.isValid())
		{
			centerPanel.add(Box.createVerticalStrut(15));
			ActorSummary aS = new ActorSummary();
			aS.setBorder(BorderFactory.createTitledBorder(Messages.getString("V_0_9_9_9.ActorSheetView.0"))); //$NON-NLS-1$
			dataListener.add(aS);
			centerPanel.add(aS);
		}
		centerPanel.add(Box.createGlue());
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.add(Box.createGlue());
		titlePanel.add(actorLabel);
		titlePanel.add(Box.createGlue());
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.add(Box.createVerticalStrut(15));
		northPanel.add(titlePanel);
		northPanel.add(Box.createVerticalStrut(25));
		
		JScrollPane scrollCenterPanel = new JScrollPane(centerPanel);
		scrollCenterPanel.getVerticalScrollBar().setUnitIncrement(20);
		
//		JButton okButton = new JButton(Messages.getString("ActorSheetView.9")); //$NON-NLS-1$
//		
//		JPanel southPanel = new JPanel();
//		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
//		southPanel.add(Box.createGlue());
//		southPanel.add(okButton);
//		southPanel.add(Box.createGlue());
		
		add(northPanel, BorderLayout.NORTH);
		add(scrollCenterPanel, BorderLayout.CENTER);
//		add(southPanel,BorderLayout.SOUTH);
		
		setActorSheet(actorSheet);
	}
	
	static public String getActorLabel(ActorSheet actorSheet) {
		return Messages.getString("ActorSheetView.10") + (actorSheet.getActorName() == null ? (actorSheet.getActor().toString()) : (actorSheet.getActorName() + " (" + StringTranslator.getDataPackTranslatedString(actorSheet.getActor(), StringType.actorType) + ")")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	public void setActorSheet(ActorSheet newActorSheet) {
		if(newActorSheet != null) {
			this.actorSheet = newActorSheet;
			for(ActorSheetSelectionListener listener : dataListener) {
				listener.updateSelection(newActorSheet);
			}
			setVisible(true);
		} else {
			setVisible(false);
		}
	}
}
