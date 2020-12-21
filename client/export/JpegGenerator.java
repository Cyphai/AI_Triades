package client.export;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import translation.Messages;
import client.Session;
import dataPack.TreeListener;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class JpegGenerator {

	private static JpegGenerator singleton;
	private String path;

	protected JpegGenerator() {

	}

	static public JpegGenerator getSingleton() {
		if (singleton == null) {
			singleton = new JpegGenerator();
		}
		return singleton;
	}

	public void exportSession(Session session) {
		JFileChooser chooser = new JFileChooser(path);
		chooser.setDialogTitle(Messages.getString("JpegGenerator.0")); //$NON-NLS-1$
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = chooser.showSaveDialog(Program.myMainFrame);
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
			String folder = chooser.getSelectedFile().getPath();
			for (ExportModel export : session.getExports())
			{

				File file = new File(folder+export.getName()+".png"); //$NON-NLS-1$
				//TODO vérifier l'existence d'un fichier et demander si on remplace ou on renomme

				generateJpeg(new ExportView(export, new TreeListener(), null),
						file);

			}
		}

	}

	public void generateJpeg(ExportView view) {
		generateJpeg(view, null);
	}

	public void generateJpeg(ExportView view, File file)
	{
		String extension = "png"; //$NON-NLS-1$
		String unumberedFileName = ""; //$NON-NLS-1$
		
		ExportView eV = new ExportView(view.getExportModel(), view.getTreeListener(), null);
		eV.setCorrectSizeForBoundingBox();
		Vector<Integer> usefullSteps = scanForApparitionStep(eV.getExportModel());
		
		
		if (file == null)
		{
			boolean isAllowedToSave = false;
			String nomFichier;
			JFileChooser chooser = new JFileChooser(path);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					Messages.getString("JpegGenerator.4"), extension, "jpg", "bmp"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			chooser.setFileFilter(filter);
			while (!isAllowedToSave) {
				int returnVal = chooser.showSaveDialog(view);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					nomFichier = file.getPath();

					int pointIndex = nomFichier.lastIndexOf('.');
					if(pointIndex >= 0) {
						extension = nomFichier.substring(pointIndex+1, nomFichier.length());
						nomFichier = nomFichier.substring(0, pointIndex);
					}

					if (view.getExportModel().getApparitionStepCount() > 1)
					{

						nomFichier += Messages.getString("JpegGenerator.7"); //$NON-NLS-1$
						unumberedFileName = nomFichier;
						nomFichier += "1."+extension; //$NON-NLS-1$
					}
					else
					{
						nomFichier+= "."+extension; //$NON-NLS-1$
						System.out.println(Messages.getString("JpegGenerator.2")+nomFichier); //$NON-NLS-1$
					}
					file = new File(nomFichier); 

					if (file.exists()) {
						int returnV = DialogHandlerFrame
								.showYesNoDialog(
										chooser,
										Messages.getString("JpegGenerator.9") //$NON-NLS-1$
										+ file.getName()
										+ Messages.getString("JpegGenerator.10")); //$NON-NLS-1$
						if (returnV == JOptionPane.YES_OPTION) {
							isAllowedToSave = true;
						}
					} else {
						isAllowedToSave = true;
					}
				} else if (returnVal == JFileChooser.CANCEL_OPTION)
					return;

			}
		}		
		else if(usefullSteps.size() > 1)
		{
			int pointIndex = file.getPath().lastIndexOf('.');
			unumberedFileName = file.getPath().substring(0, pointIndex);
			unumberedFileName += Messages.getString("JpegGenerator.11"); //$NON-NLS-1$
			extension = file.getPath().substring(pointIndex+1);

		}
		path = file.getParent();
		if (usefullSteps.size() > 1)
		{
			int j = 0;
			for (Integer i : usefullSteps)
			{
				//Générer tous les exports qui vont bien

				VisualizationViewer vV = eV.getVisualizationViewer();
				eV.setCurrentApparitionStep(i.intValue());
				file = new File(unumberedFileName+ (++j) +"."+extension); //$NON-NLS-1$

				BufferedImage bI = new BufferedImage(vV.getWidth(), vV.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				// capture: create a BufferedImage
				// create the Graphics2D object that paints to it
				vV.paint( bI.getGraphics() );
				// and save out the BufferedImage
				try {
					ImageIO.write(bI, extension, file);
					System.out.println(Messages.getString("JpegGenerator.13") + file + " - " + extension); //$NON-NLS-1$ //$NON-NLS-2$
				} catch (IOException e) {
					DialogHandlerFrame
					.showErrorDialog(Messages.getString("JpegGenerator.15")); //$NON-NLS-1$
					e.printStackTrace();
				}
			}
		}
		else
		{
			VisualizationViewer vV = eV.getVisualizationViewer();/*new VisualizationViewer<ExportVertexData, ExportEdgeData>(*///.getModel());


			BufferedImage bI = new BufferedImage(vV.getWidth(), vV.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			// capture: create a BufferedImage
			// create the Graphics2D object that paints to it
			vV.paint( bI.getGraphics() );
			// and save out the BufferedImage
			try {
				ImageIO.write(bI, extension, file);
				System.out.println(Messages.getString("JpegGenerator.16") + file + " - " + extension); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (IOException e) {
				DialogHandlerFrame
				.showErrorDialog(Messages.getString("JpegGenerator.18")); //$NON-NLS-1$
				e.printStackTrace();
			}
		}

	}

	public static JPopupMenu getGenerationMenu(final ExportView view) {
		JPopupMenu result = new JPopupMenu();
		JMenuItem item = new JMenuItem(Messages.getString("JpegGenerator.19")); //$NON-NLS-1$
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getSingleton().generateJpeg(view);
			}
		});
		result.add(item);
		return result;
	}

	protected Vector<Integer> scanForApparitionStep(ExportModel model)
	{
		Vector<Integer> result = new Vector<Integer>();
		for (int i = 1; i <= model.apparitionStepCount; i++)
		{
			boolean usefullStep = false;
			for (ExportDataInterface data : model.getEdgeData())
			{
				if (data.getExportData().getApparitionStep() == i)
					usefullStep = true;
			}
			for (ExportDataInterface data : model.getVertexData())
			{
				if (data.getExportData().getApparitionStep() == i)
					usefullStep = true;
			}
			if (usefullStep)
				result.add(i);

		}
		return result;

	}


}
