package client.export;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.ExecutionMode;
import graphicalUserInterface.Program;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.ActionTime;
import translation.Messages;

public class ExportInformationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7164690073690013L;

	protected ExportInformations content;
	//	protected JScrollPane scrollPanel;
	protected JPanel contentPanel;
	protected ExportView mainView;


	public ExportInformationPanel(ExportInformations content, ExportView view)
	{
		super();
		this.content = content;
		mainView = view;
		buildPanel();

	}

	protected void buildPanel()
	{
		setLayout(new BorderLayout());

		contentPanel = new JPanel();
		//scrollPanel = new JScrollPane(contentPanel);
		//scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setMaximumSize(new Dimension(400,400));
		setPreferredSize(new Dimension(320,300));
		setMinimumSize(new Dimension(250, 270));
		//Title bar
		JLabel titleLabel = new JLabel(Messages.getString("Version_1_0_1.ExportInformationPanel.0")); //$NON-NLS-1$
		titleLabel.setOpaque(false);

		JPanel titleBar = new JPanel();
		titleBar.setLayout(new BorderLayout());
		titleBar.setForeground(Color.lightGray);
		titleBar.add(titleLabel, BorderLayout.WEST);
		titleBar.add(new JSeparator(), BorderLayout.SOUTH);

		//ContentPanel
		final JTextArea exportTitleField = new JTextArea(content.getTitle(),3,45);
		exportTitleField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				content.setTitle(exportTitleField.getText());
				mainView.modifyTitle(exportTitleField.getText());
				mainView.repaint();
			}
		});

		exportTitleField.setToolTipText(Messages.getString("Version_1_0_1.ExportInformationPanel.1")); //$NON-NLS-1$

		final JComboBox edgeLabelModeCB = new JComboBox();
		edgeLabelModeCB.addItem(Messages.getString("Version_1_0_1.ExportInformationPanel.2")); //$NON-NLS-1$
		edgeLabelModeCB.addItem(Messages.getString("Version_1_0_1.ExportInformationPanel.3")); //$NON-NLS-1$
		edgeLabelModeCB.addItem(Messages.getString("Version_1_0_1.ExportInformationPanel.4")); //$NON-NLS-1$
		for (ActionTime aT : Program.myMainFrame.getDataPack().getActionTimeList())
		{
			edgeLabelModeCB.addItem(Messages.getString("Version_1_0_1.ExportInformationPanel.5")+aT.toString()); //$NON-NLS-1$
		}
		edgeLabelModeCB.setSelectedIndex(content.getEdgeLabelMode()+1);
		edgeLabelModeCB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					content.setEdgeLabelMode(edgeLabelModeCB.getSelectedIndex() - 1);
					mainView.repaint();
					// Le -1 permet d'avoir -1 comme valeur pour pas d'étiquette et de respecter celles utilisées dans le reste du programme pour le mode des étiquettes.
				}
			}
		});
		edgeLabelModeCB.setBorder(BorderFactory.createTitledBorder(Messages.getString("Version_1_0_1.ExportInformationPanel.6"))); //$NON-NLS-1$

		final SpinnerNumberModel edgeSpinnerModel = new SpinnerNumberModel(content.getEdgeLabelSize(), 5,35,1);
		final JSpinner edgeSizeSpinner = new JSpinner(edgeSpinnerModel);
		edgeSizeSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int newVal = edgeSpinnerModel.getNumber().intValue();
				if (newVal > 1 && newVal < 50)
				{
					content.setEdgeLabelSize(newVal);
					mainView.repaint();
				}
				else
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_1.ExportInformationPanel.7")); //$NON-NLS-1$
			}
		});
		edgeSizeSpinner.setBorder(BorderFactory.createTitledBorder(Messages.getString("Version_1_0_1.ExportInformationPanel.8"))); //$NON-NLS-1$

		final SpinnerNumberModel vertexSpinnerModel = new SpinnerNumberModel(content.getVertexLabelSize(), 5,35,1);
		final JSpinner vertexSizeSpinner = new JSpinner(vertexSpinnerModel);
		vertexSizeSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int newVal = vertexSpinnerModel.getNumber().intValue();
				if (newVal > 1 && newVal < 50)
				{
					content.setVertexLabelSize(newVal);
					mainView.repaint();
				}
				else
					DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_1.ExportInformationPanel.9")); //$NON-NLS-1$
			}
		});
		vertexSizeSpinner.setBorder(BorderFactory.createTitledBorder(Messages.getString("Version_1_0_1.ExportInformationPanel.10"))); //$NON-NLS-1$

		contentPanel.setLayout(new BorderLayout());

		JScrollPane scrollTitle = new JScrollPane(exportTitleField);
		scrollTitle.setBorder(BorderFactory.createTitledBorder(Messages.getString("Version_1_0_1.ExportInformationPanel.11"))); //$NON-NLS-1$
		contentPanel.add(scrollTitle, BorderLayout.CENTER);

		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		fieldPanel.add(edgeLabelModeCB);
		fieldPanel.add(edgeSizeSpinner);
		fieldPanel.add(vertexSizeSpinner);
		contentPanel.add(fieldPanel, BorderLayout.SOUTH);

		if (ExecutionMode.isIntern())
		{
			JButton deleteExport = new JButton(Messages.getString("Version_1_0_2.ExportInformationPanel.0")); //$NON-NLS-1$
			deleteExport.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (DialogHandlerFrame.showYesNoCancelDialog(contentPanel, Messages.getString("Version_1_0_2.ExportInformationPanel.1")) == JOptionPane.YES_OPTION) //$NON-NLS-1$
					{
						ExportModel export = mainView.getExportModel();
						Program.myMainFrame.getDataPack().getCurrentSession().removeExport(export.getBaseSchema(), export);
					}
				}
			});
			contentPanel.add(deleteExport, BorderLayout.NORTH);

		}


		add(contentPanel, BorderLayout.CENTER);

	}

}
