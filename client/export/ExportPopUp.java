package client.export;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.PopUpView;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;

import main.RelationComplete;
import tools.ConfigTriades;
import translation.Messages;
import dataPack.Activite;

public class ExportPopUp extends PopUpView {
	private static final long serialVersionUID = -4896702758779246432L;

	protected JPanel edgePanel;
	protected JPanel vertexPanel;
	protected JPanel textPanel;
	protected JPanel forAllPanel;

	protected ExportModel model;
	protected ExportView view;

	protected ExportDataInterface currentData;

	protected ArrayList<DataSelectionListener> edgeListeners;
	protected ArrayList<DataSelectionListener> vertexListeners;
	protected ArrayList<DataSelectionListener> textListeners;
	protected ArrayList<DataSelectionListener> forAllListeners;

	protected JPanel contentPanel;
	protected CardLayout contentLayout;

	final protected JPanel mainPanel;

	protected final ExportPopUp access;


	public ExportPopUp(ExportModel model, JPanel mainPanel) {

		access = this;
		this.mainPanel = mainPanel;
		this.model = model;

		edgeListeners = new ArrayList<DataSelectionListener>();
		vertexListeners = new ArrayList<DataSelectionListener>();
		textListeners = new ArrayList<DataSelectionListener>();
		forAllListeners = new ArrayList<DataSelectionListener>();

		edgePanel = buildEdgePanel();
		vertexPanel = buildVertexPanel();
		textPanel = buildTextPanel();
		forAllPanel = buildForAllPanel();

		contentPanel = new JPanel();
		contentLayout = new CardLayout();
		contentPanel.setLayout(contentLayout);

		currentData = null;

		contentPanel.add(edgePanel, "edge"); //$NON-NLS-1$
		contentPanel.add(vertexPanel, "vertex"); //$NON-NLS-1$
		contentPanel.add(textPanel, "text"); //$NON-NLS-1$

		panelMax.add(contentPanel, BorderLayout.CENTER);
		panelMax.add(forAllPanel, BorderLayout.NORTH);
		JButton masquer = new JButton(Messages.getString("ExportPopUp.3")); //$NON-NLS-1$
		masquer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				access.panelMax.setVisible(false);
				access.panelMin.setVisible(true);
				access.validate();
			}
		});

		panelMax.add(masquer, BorderLayout.SOUTH);
	}

	public void setView(ExportView theView)
	{
		view = theView;
	}

	public void selectNewObject(ExportDataInterface newSelection)
	{
		currentData = newSelection;


		if (newSelection != null) {
			for (DataSelectionListener allListener : forAllListeners) {
				allListener.updateSelection(newSelection);
			}

			if (newSelection instanceof ExportEdgeData) {
				for (DataSelectionListener listener : edgeListeners)
					listener.updateSelection(newSelection);
				contentLayout.show(contentPanel, "edge"); //$NON-NLS-1$
			} else if (newSelection instanceof ExportVertexData) {
				for (DataSelectionListener listener : vertexListeners)
					listener.updateSelection(newSelection);
				contentLayout.show(contentPanel, "vertex"); //$NON-NLS-1$
			} else if (newSelection instanceof ExportTextData) {
				for (DataSelectionListener listener : textListeners)
					listener.updateSelection(newSelection);
				contentLayout.show(contentPanel, "text"); //$NON-NLS-1$
			} else {
				panelMax.setVisible(false);
				return;
			}
			panelMax.setVisible(true);
			panelMin.setVisible(false);
			this.validate();

		} else {
			panelMax.setVisible(false);
			panelMin.setVisible(false);
		}
	}

	public void unselectVertex()
	{
		if (currentData != null && currentData instanceof ExportVertexData)
			selectNewObject(null);
	}

	public void unselectEdge()
	{
		if (currentData != null && currentData instanceof ExportEdgeData)
			selectNewObject(null);
	}

	abstract class ListenerLabel extends JLabel implements DataSelectionListener {
		private static final long serialVersionUID = -8048108358182093783L;
	};

	abstract class ListenerPanel extends JPanel implements DataSelectionListener {
		private static final long serialVersionUID = -8034108358182093783L;
	};

	abstract class ListenerCheckBox extends JCheckBox implements DataSelectionListener {
		private static final long serialVersionUID = -8649477080670733830L;

		public ListenerCheckBox(String text) {
			super(text);
		}
	};

	abstract class ListenerTextField extends JTextField implements DataSelectionListener {
		private static final long serialVersionUID = 7905579799396427169L;
	}

	abstract class ListenerJSpinner extends JSpinner implements DataSelectionListener{
		private static final long serialVersionUID = -3180229401654302906L;

		public ListenerJSpinner()
		{
			super(new SpinnerNumberModel(1, 1, 30, 1));
		}
	}

	abstract class ListenerButton extends JButton implements DataSelectionListener {
		private static final long serialVersionUID = -6954182510056530751L;
	}

	abstract class ListenerJCombotBox extends JComboBox implements DataSelectionListener {
		private static final long serialVersionUID = 8976617494377440512L;
	}

	private JPanel buildEdgePanel() {
		JPanel result = new JPanel();
		result.setLayout(new BorderLayout());

		final ListenerLabel currentEdgeColor = new ListenerLabel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void updateSelection(ExportDataInterface object) {
				if(access.currentData instanceof ExportEdgeData) {
					ExportEdgeData edgeData = (ExportEdgeData)access.currentData;
					Color color = edgeData.getColor();
					if(color == null) {
						int state = edgeData.getCompleteRelation().getState();
						switch (state) {
						case RelationComplete.RELATION_OK:
							color = Color.GREEN;
							break;
						case RelationComplete.RELATION_INCOMPLETE:
							color = Color.BLUE;
							break;
						case RelationComplete.RELATION_ECART_OBJECTIF:
							color = Color.RED;
							break;
						case RelationComplete.RELATION_ECART_MOYEN:
							color = Color.getHSBColor(0.0f, 1.0f, 0.41f);
							break;
						}
					}

					setBackground(color);
					setOpaque(true);
					setBorder(access.getBorder(false, false, color));
				}
			}
		};

		ActionListener imageListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				access.chooseExportedImage(currentEdgeColor);
			}
		};

		currentEdgeColor.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				currentEdgeColor.setBorder(getBorder(false, false, currentEdgeColor.getBackground()));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				currentEdgeColor.setBorder(getBorder(true, true, currentEdgeColor.getBackground()));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				currentEdgeColor.setBorder(getBorder(false, false, currentEdgeColor.getBackground()));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				currentEdgeColor.setBorder(getBorder(true, false, currentEdgeColor.getBackground()));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				access.chooseExportedImage(currentEdgeColor);
			}
		});

		JButton edgeColorButton = new JButton(Messages.getString("ExportPopUp.5")); //$NON-NLS-1$
		edgeColorButton.addActionListener(imageListener);

		currentEdgeColor.setMaximumSize(new Dimension(20, 20));
		currentEdgeColor.setMinimumSize(new Dimension(20, 20));
		currentEdgeColor.setPreferredSize(new Dimension(20, 20));

		JPanel colorPanel = new JPanel();
		colorPanel.add(edgeColorButton);
		colorPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("ExportPopUp.6"))); //$NON-NLS-1$
		colorPanel.add(Box.createHorizontalGlue());
		colorPanel.add(currentEdgeColor);

		edgeListeners.add(currentEdgeColor);

		result.add(colorPanel, BorderLayout.NORTH);

		return result;
	}

	private Border getBorder(boolean overlap, boolean click, Color color) {
		Color lightColor = color.brighter().brighter().brighter();
		Color darckColor = color.darker().darker().darker();

		if(overlap) {
			lightColor = lightColor.brighter().brighter();
			darckColor = darckColor.darker().darker();
		}

		return BorderFactory.createBevelBorder(click ? BevelBorder.LOWERED: BevelBorder.RAISED, lightColor, darckColor);
	}

	protected void chooseExportedImage(final JComponent currentEdgeColor){
		if(access.currentData instanceof ExportEdgeData) {
			ExportEdgeData edgeData = (ExportEdgeData)access.currentData;
			Color edgeColor = JColorChooser.showDialog(access, Messages.getString("ExportPopUp.11"), edgeData.getColor()); //$NON-NLS-1$
			if(edgeColor != null) {
				edgeData.setEdgeColor(edgeColor);
				currentEdgeColor.setBackground(edgeColor);
				mainPanel.repaint();
			}
		}
	}

	private JPanel buildVertexPanel() {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));





		final ListenerJCombotBox lastImages = new ListenerJCombotBox() {
			private static final long serialVersionUID = -3476259768214720516L;

			@Override
			public void updateSelection(ExportDataInterface object) {
				if(ConfigTriades.getInstance().getLastImages().getLastObjects().size() <= 0) {
					setVisible(false);
					return;
				}

				setVisible(true);

				Vector<Icon> images = new Vector<Icon>();
				for (ExportImageData imageData : ConfigTriades.getInstance().getLastImages().getLastObjects()) {
					if (imageData.getIcon() != null)
						images.add(imageData.getIcon());
				}

				setModel(new DefaultComboBoxModel(images));
				if (object instanceof ExportVertexData)
				{
					ExportVertexData data = (ExportVertexData)object;

					setSelectedItem(data.getIcon());
				}

				mainPanel.repaint();
			}

			@Override
			protected void fireItemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.DESELECTED || getSelectedItem() == null) {
					return;
				}


				int index = getSelectedIndex();

				ExportImageData imageData = ConfigTriades.getInstance().getLastImages().getLastObjects().elementAt(index);
				((ExportVertexData)access.currentData).setImageData(imageData);
				ConfigTriades.getInstance().getLastImages().addLastObject(new ExportImageData(imageData));

				Vector<Icon> images = new Vector<Icon>();
				for (ExportImageData imgData : ConfigTriades.getInstance().getLastImages().getLastObjects()) {
					if (imgData.getIcon() != null)
						images.add(imgData.getIcon());
				}
				setModel(new DefaultComboBoxModel(images));


				repaint();

				mainPanel.repaint();
				super.fireItemStateChanged(e);

			}
		};


		vertexListeners.add(lastImages);

		JButton selectImageButton = new JButton(Messages.getString("ExportPopUp.8")); //$NON-NLS-1$

		selectImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ExportImagesView.getSingleton().display((ExportVertexData)access.currentData, mainPanel, lastImages);
			}
		});

		JPanel imagePanel = new JPanel();
		imagePanel.setToolTipText(Messages.getString("ExportPopUp.0")); //$NON-NLS-1$
		imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
		imagePanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("ExportPopUp.9"))); //$NON-NLS-1$

		imagePanel.add(selectImageButton, BorderLayout.CENTER);
		imagePanel.add(lastImages, BorderLayout.CENTER);

		result.add(imagePanel);

		return result;
	}

	private JPanel buildTextPanel() {
		JPanel result = new JPanel();

		return result;
	}

	private JPanel buildForAllPanel() {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));

		ListenerCheckBox lcheckBox = new ListenerCheckBox(Messages.getString("ExportPopUp.10")) { //$NON-NLS-1$
			private static final long serialVersionUID = 8305867850610117203L;

			@Override
			public void updateSelection(ExportDataInterface object) {

				if (access.currentData != null) {
					setSelected(access.currentData.getExportData().isVisible());
					if(object instanceof ExportVertexData && ((ExportVertexData)object).getContent() instanceof Activite)
						setEnabled(false);
					else 
						setEnabled(true);
				} else 
					setEnabled(false);

			}

			@Override
			protected void fireItemStateChanged(ItemEvent e) {
				if (access.currentData != null) {
					access.currentData.getExportData()
					.setVisible(e.getStateChange() == ItemEvent.SELECTED);
					mainPanel.repaint();
				}
				super.fireItemStateChanged(e);

			}
		};

		ListenerTextField nameField = new ListenerTextField() {

			private static final long serialVersionUID = 1L;


			@Override
			public void updateSelection(ExportDataInterface object) {
				if (access.currentData != null
						&& access.currentData.getExportData().getText() != null) {
					setText(access.currentData.getExportData().getText());
				} else {
					setText(""); //$NON-NLS-1$
				}
			}

			@Override
			protected void fireCaretUpdate(CaretEvent e)
			{
				if (access.currentData != null) {
					access.currentData.getExportData().setText(getText());
					mainPanel.repaint();
				}
				super.fireCaretUpdate(e);
			}

		};

		ListenerJSpinner textSizeField = new ListenerJSpinner() {
			private static final long serialVersionUID = 1L;

			@Override
			public void updateSelection(ExportDataInterface object) {
				if(access.currentData != null) {
					if (access.currentData.getExportData().getTextSize() > 0)
						setValue(access.currentData.getExportData().getTextSize());
					else
					{
						if (currentData instanceof ExportVertexData)
							setValue(model.getExportInformations().getVertexLabelSize());
						else if (currentData instanceof ExportEdgeData)
							setValue(model.getExportInformations().getEdgeLabelSize());
					}
				}
			}

			@Override
			protected void fireStateChanged()
			{
				if (access.currentData != null) {
					if((Integer)getValue() > 1) {
						int defaultValue = -1;
						if (currentData instanceof ExportVertexData)
							defaultValue = model.getExportInformations().getVertexLabelSize();
						else if (currentData instanceof ExportEdgeData)
							defaultValue = model.getExportInformations().getEdgeLabelSize();
						if (defaultValue == -1)
						{
							System.err.println("A JSpinner is used in the ExportPopup for something different than a ExportVertexData or an ExportEdgeData"); //$NON-NLS-1$
							super.fireStateChanged();
							return;
						}

						if (((Integer)getValue()).intValue() != defaultValue)
						{
							access.currentData.getExportData().setTextSize((Integer)getValue());
						}
						mainPanel.repaint();
					}
				}
				super.fireStateChanged();
			}
		};

		JPanel textSizePanel = new JPanel();
		textSizePanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("ExportPopUp.13"))); //$NON-NLS-1$
		textSizePanel.add(textSizeField);

		nameField.setBorder(BorderFactory.createTitledBorder(Messages.getString("ExportPopUp.1"))); //$NON-NLS-1$
		nameField.setToolTipText(Messages.getString("ExportPopUp.2")); //$NON-NLS-1$



		final ListenerJSpinner spinner = new ListenerJSpinner() {

			private static final long serialVersionUID = -529891602022228620L;

			@Override
			public void updateSelection(ExportDataInterface object) {

				setValue(object.getExportData().getApparitionStep());

			}

			@Override
			protected void fireStateChanged()
			{
				if (access.currentData != null)
				{
					access.currentData.getExportData().setApparitionStep(((Integer)getValue()).intValue());

					if (((Integer)getValue()).intValue() > model.getApparitionStepCount())
						model.setApparitionStepCount(((Integer)getValue()).intValue());
					mainPanel.repaint();
				}	

				super.fireStateChanged();
			}
		};
		spinner.setBorder(BorderFactory.createTitledBorder(Messages.getString("ExportPopUp.7"))); //$NON-NLS-1$


		forAllListeners.add(spinner);

		forAllListeners.add(nameField);
		forAllListeners.add(lcheckBox);
		forAllListeners.add(textSizeField);

		result.add(nameField);
		result.add(textSizePanel);
		result.add(lcheckBox);
		result.add(spinner);

		JPanel stepControl = new JPanel();
		stepControl.setBorder(BorderFactory.createTitledBorder(Messages.getString("ExportPopUp.12"))); //$NON-NLS-1$


		stepControl.setLayout(new BoxLayout(stepControl, BoxLayout.X_AXIS));

		JButton minus = new JButton(" - "); //$NON-NLS-1$
		JButton plus = new JButton(" + "); //$NON-NLS-1$
		final JTextField count = new JTextField(2);
		count.setText(model.getApparitionStepCount()+""); //$NON-NLS-1$

		minus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (view.getCurrentApparitionStep() > 1)
					view.setCurrentApparitionStep(view.getCurrentApparitionStep()-1);
				else
					DialogHandlerFrame.showErrorDialog(Messages.getString("ExportPopUp.16")); //$NON-NLS-1$
				count.setText(view.getCurrentApparitionStep()+""); //$NON-NLS-1$
				count.repaint();
				view.repaint();
			}
		});

		plus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (view.getCurrentApparitionStep() < model.getApparitionStepCount())
					view.setCurrentApparitionStep(view.getCurrentApparitionStep()+1);
				else
					DialogHandlerFrame.showErrorDialog(Messages.getString("ExportPopUp.18")); //$NON-NLS-1$
				count.setText(view.getCurrentApparitionStep()+""); //$NON-NLS-1$
				count.repaint();
				view.repaint();
			}
		});

		stepControl.setToolTipText(Messages.getString("ExportPopUp.4")); //$NON-NLS-1$
		stepControl.add(Box.createGlue());
		stepControl.add(minus);
		stepControl.add(count);
		stepControl.add(plus);
		stepControl.add(Box.createGlue());
		result.add(stepControl);

		return result;
	}

}

