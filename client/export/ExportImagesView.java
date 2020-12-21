package client.export;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.IconDatabase;
import graphicalUserInterface.Program;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import tools.Config;
import tools.ConfigTriades;
import translation.Messages;

public class ExportImagesView extends JFrame {
	static private final long serialVersionUID = -7353684973355867933L;

	private static int borderSize = 3;

	static private final Dimension windowSize = new Dimension(800, 600);

	static private Border EmptyBorder = BorderFactory.createEmptyBorder(
			borderSize, borderSize, borderSize, borderSize);
	static private Border SelectedBorder = BorderFactory.createLineBorder(
			Color.GREEN, borderSize);
	static private Border OverlapingBorder = BorderFactory.createLineBorder(
			Color.BLUE, borderSize);

	static private int imagesSize = 48;
	
	static public final String imagesDirectoryPath =  Config.settingsDirectory + "pics"; //$NON-NLS-1$
	private boolean init;

	private JLabel selectedImage;
	private final ExportImageData exportImageData;

	private ExportVertexData exportedVertex;

	private JComponent mainView;

	private final ExportImagesView access = this;

	private boolean isGlobalImage;
	private JCheckBox globalCheckBox;

	DataSelectionListener dataSelectionListener;

	static private ExportImagesView singleton = null;

	private ExportImagesView() {
		super();

		exportImageData = new ExportImageData();
		dataSelectionListener = null;
		this.exportedVertex = null;

		setSize(windowSize);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		init();

		//setResizable(false);
		setVisible(true);
		validate();
	}

	private void init() {
		if (!init) {
			File imagesDirectoryFile = new File(imagesDirectoryPath);

			if (imagesDirectoryFile.isDirectory() == false) {
				DialogHandlerFrame
						.showInformationDialog(
								Program.myMainFrame,
								Messages.getString("ExportImagesView.1"), //$NON-NLS-1$
								Messages.getString("ExportImagesView.2") //$NON-NLS-1$
										+ imagesDirectoryPath
										+ Messages.getString("ExportImagesView.3"), //$NON-NLS-1$
								null);
				init = false;
				setVisible(false);
				return;
			}

			add(createView(imagesDirectoryFile));
			validate();

			init = true;
		}
	}

	private JComponent createView(File imagesDirectoryFile) {
		JButton okButton = new JButton(Messages.getString("ExportImagesView.4")); //$NON-NLS-1$
		okButton.setSize(300, 35);
		okButton.setVisible(true);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				access.onButtonOk();
			}
		});

		JButton cancelButton = new JButton(Messages.getString("ExportImagesView.5")); //$NON-NLS-1$
		cancelButton.setSize(300, 35);
		cancelButton.setVisible(true);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				access.hideView();
			}
		});

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);

		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

		globalCheckBox = new JCheckBox(Messages.getString("ExportImagesView.6")); //$NON-NLS-1$
		globalCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				isGlobalImage = (e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		JPanel view = new JPanel();
		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

		addImagesViewFromVector(IconDatabase.vectorExportedIcons, Messages.getString("ExportImagesView.7"), //$NON-NLS-1$
				view);
		addImagesViewFromFile(imagesDirectoryFile, view);

		JScrollPane scrollPaneView = new JScrollPane(view);
		scrollPaneView.getVerticalScrollBar().setUnitIncrement(20);
		scrollPaneView.setVisible(true);

		JPanel globalView = new JPanel();
		globalView.setLayout(new BoxLayout(globalView, BoxLayout.Y_AXIS));
		globalView.add(scrollPaneView);
		globalView.add(globalCheckBox);
		globalView.add(buttonsPanel);

		return globalView;
	}

	protected void onButtonOk() {
		if(exportImageData.getIcon() != null) {
			ConfigTriades.getInstance().getLastImages()
					.addLastObject(new ExportImageData(exportImageData));
	
			if (dataSelectionListener != null) {
				dataSelectionListener.updateSelection(null);
			}
	
			if (isGlobalImage) {
				Program.myMainFrame
						.getDataPack()
						.getCurrentSession()
						.setExportedImageData(exportImageData,
								exportedVertex.getContent());
				exportedVertex.setImageData(new ExportImageData());
			} else {
				exportedVertex.setImageData(exportImageData);
			}
		}
		
		hideView();
	}

	private void addImagesViewFromFile(File file, JComponent view) {
		File files[] = file.listFiles();
		Vector<JLabel> iconsVector = new Vector<JLabel>();
		int maxWidth = -1;

		if(files == null) {
			return;
		}
		
		for (int i = 0; i < files.length; i++) {
			final File subFile = files[i];

			if (subFile.isHidden()) {
				continue;
			}

			if (subFile.isDirectory()) {
				addImagesViewFromFile(subFile, view);
			} else if (subFile.isFile()) {
			   ImageIcon imageIcon = IconDatabase.getResizeIcon(subFile.getPath());

				if (imageIcon == null || (imageIcon.getIconHeight() + imageIcon.getIconWidth()) <= 0) {
					continue;
				}

				final JLabel image = new JLabel(imageIcon);
				image.setBorder(EmptyBorder);
				image.addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {
					}

					@Override
					public void mousePressed(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent e) {
						if (selectedImage == image) {
							image.setBorder(SelectedBorder);
						} else {
							image.setBorder(EmptyBorder);
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						image.setBorder(OverlapingBorder);
					}

					@Override
					public void mouseClicked(MouseEvent e) {
						access.setSelectedPath(subFile.getPath(), image);
						
						if(e.getClickCount() > 1) {
							access.onButtonOk();
						}
					}
				});

				maxWidth = Math.max(maxWidth, imageIcon.getIconWidth());

				iconsVector.add(image);
			}
		}

		if (!iconsVector.isEmpty()) {
			view.add(createImagesView(iconsVector, maxWidth, file.getName()));
		}
	}

	private void addImagesViewFromVector(Vector<ImageIcon> icons, String name,
			JComponent view) {
		Vector<JLabel> images = new Vector<JLabel>();
		int maxWidth = -1;

		for (int i = 0; i < icons.size(); i++) {
			final int index = i;
			final JLabel image = new JLabel(icons.elementAt(i));
			image.setBorder(EmptyBorder);

			image.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (selectedImage == image) {
						image.setBorder(SelectedBorder);
					} else {
						image.setBorder(EmptyBorder);
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					image.setBorder(OverlapingBorder);
				}

				@Override
				public void mouseClicked(MouseEvent arg0) {
					access.setSelectedIndex(index, image);

					if(arg0.getClickCount() > 1) {
						access.onButtonOk();
					}
				}
			});

			maxWidth = Math.max(maxWidth, icons.elementAt(i).getIconWidth());
			images.add(image);
		}

		if (!images.isEmpty()) {
			view.add(createImagesView(images, maxWidth, name));
		}
	}

	private Component createImagesView(Vector<JLabel> iconsVector,
			int maxWidth, String name) {
		if (iconsVector.isEmpty()) {
			throw new RuntimeException("Empty vector !"); //$NON-NLS-1$
		}

		JPanel directoryView = new JPanel();
		directoryView.setLayout(new BoxLayout(directoryView, BoxLayout.Y_AXIS));

		int colsCount = windowSize.width / (maxWidth + 2 * borderSize + 10) - 1;
		JPanel imagesView = new JPanel(new GridLayout(0, colsCount));

		for (JComponent image : iconsVector) {
			imagesView.add(image);
		}
		
		directoryView.add(new JLabel(name));
		directoryView.add(imagesView);

		return directoryView;
	}

	protected void hideView() {
		if (selectedImage != null) {
			selectedImage.setBorder(EmptyBorder);
		}

		selectedImage = null;
		exportedVertex = null;

		setVisible(false);
		setEnabled(false);
		// view.validate();
		mainView.repaint();
	}

	public void display(ExportVertexData exportedVertex, JComponent mainView, DataSelectionListener dataListener) {
		this.exportedVertex = exportedVertex;
		this.mainView = mainView;
		exportImageData.clear();
		isGlobalImage = false;
		globalCheckBox.setSelected(isGlobalImage);
		dataSelectionListener = dataListener;

		setEnabled(true);
		setVisible(true);
	}

	protected void setSelectedIcon(JLabel image) {
		if (selectedImage != null) {
			selectedImage.setBorder(BorderFactory.createEmptyBorder(borderSize,
					borderSize, borderSize, borderSize));
		}

		selectedImage = image;
		selectedImage.setBorder(SelectedBorder);
	}

	protected void setSelectedIndex(int index, JLabel image) {
		exportImageData.setImageIndex(index);
		setSelectedIcon(image);
	}

	protected void setSelectedPath(String path, JLabel image) {
		exportImageData.setImagePath(path);
		setSelectedIcon(image);
	}

	static public ExportImagesView getSingleton() {
		if (singleton == null) {
			singleton = new ExportImagesView();
		}

		singleton.init();

		return singleton;
	}
	
	public static void createFiles() {
		File file = new File(imagesDirectoryPath);
		file.mkdirs();
	}
}
