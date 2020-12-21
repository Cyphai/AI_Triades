package relations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import main.Statut;
import main.StatutObjet;
import translation.Messages;

public class DescriptionBuilder extends JPanel{

	private enum SelectedMode {SimpleMode, AddMode, OnlyMode, InversMode};

	private static final long serialVersionUID = 2656852414355161636L;

	private final Vector<JCheckBox> descriptions; // ligne les une a la suite des autre (debut des index a 0)
	private final Vector<JLabel> descriptionsName;

	private final RelationDescription relationDescription;

	private SelectedMode selectingMode = SelectedMode.SimpleMode;
	private int hierarchiqueMode = 0;
	private boolean isLineMode = true;
	private boolean isObjectifMode = false;
	private boolean isActivitiesMode = false;
	private boolean isPartnerMode = false;
	private boolean isAllMode = false;
	
	private final JPanel complexFilter;

	private final Color lineColor = Color.RED;
	private final Color columnColor = Color.YELLOW;

	private final DescriptionBuilder descriptionBuilder;

	private final int descriptionSize = 23;

	public DescriptionBuilder(RelationDescription relationDescription) {
		descriptionBuilder = this;

		descriptions = new Vector<JCheckBox>(descriptionSize*descriptionSize);
		descriptionsName = new Vector<JLabel>(descriptionSize);

		this.relationDescription = relationDescription;
		
		complexFilter = new JPanel();

		buildView();
	}

	private void buildView() {
		this.setVisible(true);
		this.setLayout(new BorderLayout());

		this.add(createNameView(), BorderLayout.WEST);
		this.add(createCheckBoxView(), BorderLayout.CENTER);
		this.add(new JScrollPane(createFilterView()), BorderLayout.NORTH);

		this.validate();
	}

	private JPanel createNameView() {
		JPanel view = new JPanel(new GridLayout(0, 1));

		for (int i = 0 ; i < descriptionSize ; i++) {
			String label;

			if(i < 18) {
				label = Statut.values()[i].toString();
			} else if (i < 22) {
				label = StatutObjet.values()[i - 18].toString();
			} else {
				label = Messages.getString("DescriptionBuilder.0"); //$NON-NLS-1$
			}

			JLabel textLabel = new JLabel(label);
			descriptionsName.add(textLabel);
			textLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
			view.add(textLabel);
		}

		return view;
	}

	private JPanel createCheckBoxView() {
		JPanel view = new JPanel(new GridLayout(descriptionSize, descriptionSize));

		int descriptionSizeSqrt = descriptionSize*descriptionSize;
		for (int i = 0 ; i < descriptionSizeSqrt ; i++) {
			final int a = i/descriptionSize;
			final int b = i%descriptionSize;

			JCheckBox checkBox = new JCheckBox();

			boolean borderRight = b == 17 || b == 7 || b == 21;
			boolean borderBottom = a == 17 || a == 7 || a == 21;

			checkBox.setBorder(BorderFactory.createEmptyBorder());
			checkBox.setSelected(relationDescription.allow(a, b, relationDescription.isRealRelation()));
			checkBox.removeMouseListener(checkBox.getMouseListeners()[0]);

			JPanel container = new JPanel();
			container.setLayout(new GridBagLayout());

			int borderRightSize = b == 7 ? 1 : 2;
			int borderBottomSize = a == 7 ? 1 : 2;

			if(borderBottom) {
				if(borderRight) 
					container.setBorder(BorderFactory.createMatteBorder(0, 0, borderBottomSize, borderRightSize, Color.BLACK));
				else 
					container.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, borderBottomSize, 0, Color.BLACK),BorderFactory.createEmptyBorder(0, 0, borderBottomSize, 0)));
			} else if (borderRight) {
				container.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, borderRightSize, Color.BLACK),BorderFactory.createEmptyBorder(0, 0, 0, borderRightSize)));
			}

			container.add(checkBox);

			checkBox.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					relationDescription.set(a, b, e.getStateChange() == ItemEvent.SELECTED);
				}
			});

			MouseListener mouseListner = new MouseListener() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if(a == b) {
						descriptionsName.get(a).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(lineColor, 1), BorderFactory.createLineBorder(columnColor, 1)));
					} else {
						descriptionsName.get(a).setBorder(BorderFactory.createLineBorder(lineColor, 2));
						descriptionsName.get(b).setBorder(BorderFactory.createLineBorder(columnColor, 2));
					}

					if(selectingMode == SelectedMode.SimpleMode) {
						try {
							((JPanel)e.getSource()).setBackground(Color.LIGHT_GRAY);
						} catch (Exception e1) {
							((JCheckBox)e.getSource()).getParent().setBackground(Color.LIGHT_GRAY);
						}
					} else {
						descriptionBuilder.setPaternVisible(a, b, true);
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					descriptionsName.get(a).setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
					descriptionsName.get(b).setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));

					if(selectingMode == SelectedMode.SimpleMode) {
						try {
							((JPanel)e.getSource()).setBackground(null);
						} catch (Exception e1) {
							((JCheckBox)e.getSource()).getParent().setBackground(null);
						}
					} else {
						descriptionBuilder.setPaternVisible(a, b, false);
					}				
				}

				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}				
				@Override
				public void mouseClicked(MouseEvent e) {
					descriptionBuilder.applyPatern(a, b);
				}
			};

			container.addMouseListener(mouseListner);
			checkBox.addMouseListener(mouseListner);

			view.add(container);
			view.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

			descriptions.add(checkBox);
		}

		return view;
	}

	private JPanel createFilterView() {
		JPanel view = new JPanel();
		view.setLayout(new BoxLayout(view, BoxLayout.X_AXIS));

		String legendText = Messages.getString("DescriptionBuilder.16"); //$NON-NLS-1$
		JTextArea legend = new JTextArea(legendText);
		legend.setBorder(BorderFactory.createTitledBorder(Messages.getString("DescriptionBuilder.17"))); //$NON-NLS-1$
		legend.setOpaque(false);
		legend.setEditable(false);
		view.add(legend);
		
		JPanel nameAndRealPanel = new JPanel(new BorderLayout());
		final JTextField nameField = new JTextField(relationDescription
				.toString(), 25);
		nameField.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent arg0) {
				relationDescription.setName(nameField.getText());
			}
		});
		JPanel embeddedNamePanel = new JPanel(new GridBagLayout());
		embeddedNamePanel.add(nameField);

		nameAndRealPanel.add(embeddedNamePanel, BorderLayout.CENTER);
		JCheckBox realRelations = new JCheckBox(Messages.getString("DescriptionBuilder.1")); //$NON-NLS-1$
		realRelations.setSelected(relationDescription.isRealRelation());		
		nameAndRealPanel.add(realRelations, BorderLayout.SOUTH);

		nameAndRealPanel.setBorder(BorderFactory
				.createTitledBorder(Messages.getString("DescriptionBuilder.2"))); //$NON-NLS-1$

		view.add(nameAndRealPanel);
		
		String[] logics = {Messages.getString("DescriptionBuilder.3"), Messages.getString("DescriptionBuilder.4"), Messages.getString("DescriptionBuilder.5"), Messages.getString("DescriptionBuilder.6")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		final JComboBox logicFilter = new JComboBox(logics);
		logicFilter.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionBuilder.selectingMode = SelectedMode.values()[logicFilter.getSelectedIndex()];
				setEnableRecursively(complexFilter, descriptionBuilder.selectingMode != SelectedMode.SimpleMode);
			}
		});


		JPanel embeddingLogicPanel = new JPanel(new GridBagLayout());
		embeddingLogicPanel.add(logicFilter);
		view.add(embeddingLogicPanel);

		complexFilter.setLayout(new BoxLayout(complexFilter, BoxLayout.X_AXIS));

		JCheckBox allBox = new JCheckBox(Messages.getString("DescriptionBuilder.7")); //$NON-NLS-1$
		allBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionBuilder.isAllMode = e.getStateChange() == ItemEvent.SELECTED;
			}
		});
		complexFilter.add(allBox);
		
		JPanel hierarchiqueView = new JPanel();
		hierarchiqueView.setLayout(new BoxLayout(hierarchiqueView, BoxLayout.Y_AXIS));

		JCheckBox hierarchiqueBox = new JCheckBox(Messages.getString("DescriptionBuilder.8")); //$NON-NLS-1$
		hierarchiqueBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionBuilder.hierarchiqueMode += e.getStateChange() == ItemEvent.SELECTED ? 1 : -1;
			}
		});

		hierarchiqueView.add(hierarchiqueBox);

		JCheckBox supHierarchiqueBox = new JCheckBox(Messages.getString("DescriptionBuilder.9")); //$NON-NLS-1$
		supHierarchiqueBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionBuilder.hierarchiqueMode += e.getStateChange() == ItemEvent.SELECTED ? 2 : -2;
			}
		});
		hierarchiqueView.add(supHierarchiqueBox);

		JCheckBox infHierarchiqueBox = new JCheckBox(Messages.getString("DescriptionBuilder.10")); //$NON-NLS-1$
		infHierarchiqueBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionBuilder.hierarchiqueMode += e.getStateChange() == ItemEvent.SELECTED ? 4 : -4;
			}
		});
		hierarchiqueView.add(infHierarchiqueBox);

		complexFilter.add(hierarchiqueView);

		JCheckBox partnerBox = new JCheckBox(Messages.getString("DescriptionBuilder.11")); //$NON-NLS-1$
		partnerBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionBuilder.isPartnerMode = e.getStateChange() == ItemEvent.SELECTED;
			}
		});
		complexFilter.add(partnerBox);
		
		JCheckBox objectifBox = new JCheckBox(Messages.getString("DescriptionBuilder.12")); //$NON-NLS-1$
		objectifBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionBuilder.isObjectifMode = e.getStateChange() == ItemEvent.SELECTED;
			}
		});
		complexFilter.add(objectifBox);

		JCheckBox activiteBox = new JCheckBox(Messages.getString("DescriptionBuilder.13")); //$NON-NLS-1$
		activiteBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionBuilder.isActivitiesMode = e.getStateChange() == ItemEvent.SELECTED;
			}
		});
		complexFilter.add(activiteBox);

		String[] lineColone = {Messages.getString("DescriptionBuilder.14"), Messages.getString("DescriptionBuilder.15")}; //$NON-NLS-1$ //$NON-NLS-2$

		final JComboBox lineColoneBox = new JComboBox(lineColone);
		lineColoneBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				descriptionBuilder.isLineMode = lineColoneBox.getSelectedIndex() == 0;
			}
		});
		JPanel embeddingLineColumnPanel = new JPanel(new GridBagLayout());
		embeddingLineColumnPanel.add(lineColoneBox);
		complexFilter.add(embeddingLineColumnPanel);

		setEnableRecursively(complexFilter, false);
		
		view.add(complexFilter);

		JPanel filterView = new JPanel(new GridBagLayout());
		filterView.add(view);
		return view;
	}

	public void setValue(int line, int column, boolean value) {
		descriptions.get(getIndex(line, column)).setSelected(value);
	}

	public boolean getValue(int line, int column) {
		return descriptions.get(getIndex(line, column)).isSelected();
	}

	public String getName(int lineOrColumn) {
		return descriptionsName.get(lineOrColumn).getText();
	}

	private int getIndex(int line, int column) {
		return line + column*descriptionSize;
	}

	public Vector<JCheckBox> getCheckBoxSelected(int a, int b) {
		if(selectingMode == SelectedMode.SimpleMode) {
			return new Vector<JCheckBox>();
		} 

		Vector<JCheckBox> selectedBoxes = new Vector<JCheckBox>();
		Vector<JCheckBox> columLineBoxes = isLineMode ? getLine(a) : getColumn(b);
		int hierarchiqueLevel = isLineMode ? a : b;

		if(isAllMode) {
			return columLineBoxes;
		}
		
		for(int i = 0 ; i < descriptionSize ; i++) {
			JCheckBox currentCheckBox = columLineBoxes.get(i);

			if(i<8) {
				if((hierarchiqueMode & 1) == 1 || (hierarchiqueMode !=0 && hierarchiqueLevel > 7)) {
					selectedBoxes.add(currentCheckBox);
				} else {
					if ((hierarchiqueMode & 2) == 2) 
						if(isLineMode ? i<hierarchiqueLevel : i<hierarchiqueLevel) 
							selectedBoxes.add(currentCheckBox);

					if ((hierarchiqueMode & 4) == 4)
						if(isLineMode ? i>hierarchiqueLevel : i>hierarchiqueLevel) 
							selectedBoxes.add(currentCheckBox);
					
					if((hierarchiqueMode & 6) == 6 && i == hierarchiqueLevel) 
						selectedBoxes.add(currentCheckBox);
				}
			} else if (i<18) {
				if(isPartnerMode) {
					selectedBoxes.add(currentCheckBox);
				}
			} else if(i<22) {
				if(isObjectifMode) {
					selectedBoxes.add(currentCheckBox);
				}
			} else if (isActivitiesMode) {
				selectedBoxes.add(currentCheckBox);
			}
		}
		
		return selectedBoxes;
	}

	public void setPaternVisible(int a, int b, boolean visible) {
		for(JCheckBox checkBox : getCheckBoxSelected(a, b)) {
			if(visible) {
				checkBox.getParent().setBackground(isLineMode ? lineColor : columnColor);
			} else {
				checkBox.getParent().setBackground(null);
			}

			checkBox.validate();
		}
	}

	public void applyPatern(int a, int b) {
		if(selectingMode == SelectedMode.SimpleMode) {
			descriptions.get(a*descriptionSize + b).setSelected(!descriptions.get(a*descriptionSize + b).isSelected());
		} else {
			if(selectingMode == SelectedMode.OnlyMode) {
				//décoche toute la ligne ou colonne
				for(JCheckBox checkBox : isLineMode ? descriptionBuilder.getLine(a) : descriptionBuilder.getColumn(b)) {
					checkBox.setSelected(false);
				}
			}

			for(JCheckBox checkBox : getCheckBoxSelected(a, b)) {
				checkBox.setSelected(selectingMode == SelectedMode.InversMode ? (!checkBox.isSelected()) : true);
			}
		}
	}

	public Vector<JCheckBox> getLine(int index) {
		Vector<JCheckBox> line = new Vector<JCheckBox>();

		for(int i = 0 ; i < descriptionSize ; i++) {
			line.add(descriptions.get(i + descriptionSize*index));
		}

		return line;
	}

	public Vector<JCheckBox> getColumn(int index) {
		Vector<JCheckBox> column = new Vector<JCheckBox>();

		for(int i = 0 ; i < descriptionSize ; i++) {
			column.add(descriptions.get(i*descriptionSize + index));
		}

		return column;		
	}
	
	public void setEnableRecursively(JComponent component, boolean enable) {
		try {
		for(Component childComponent : component.getComponents()) {
			setEnableRecursively((JComponent)childComponent, enable);
		}
		} catch (Exception e) {
			
		}
		
		component.setEnabled(enable);
	}
	
//	public static void main(String[] args) {
//		JFrame window = new JFrame();
//
//		RelationDescription relationTest = new RelationDescription("Test relation description");
//		relationTest.setRealRelation(true);
//		//relationTest.set(10, 10, true);
//
//		window.add(new DescriptionBuilder(relationTest));
//		window.setSize(800, 600);
//		window.setVisible(true);
//		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		window.pack();
//	}
}
