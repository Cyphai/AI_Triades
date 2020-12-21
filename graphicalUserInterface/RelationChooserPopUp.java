package graphicalUserInterface;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;

import models.BrickEdge;
import models.BrickVertex;
import models.TriadeEditingMousePlugin;
import relations.RelationPossibility;
import translation.Messages;

public class RelationChooserPopUp
extends PopUpView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6942148785999726898L;

	protected RelationChooserView relationChooser;
	protected BrickEdge editedModelEdge;

	public int showRelationChooserView(BrickEdge edge,
			TriadeEditingMousePlugin<BrickVertex, BrickEdge> mousePlugin,
			String title)
	{
		return showRelationChooserView(edge, mousePlugin, title, true);
	}

	public int showRelationChooserView(BrickEdge edge,
			TriadeEditingMousePlugin<BrickVertex, BrickEdge> mousePlugin,
			String title, boolean checkForSaving) {
		boolean reelle = Program.isTriades();
		labelTitle.setText(title);
		if (editedModelEdge == null || !edge.equals(editedModelEdge)) {
			if (relationChooser != null) {

				if (checkForSaving)
				{
					int choice = DialogHandlerFrame
							.showYesNoCancelDialog(
									Program.myMainFrame,
									Messages.getString("RelationChooserPopUp.0")); //$NON-NLS-1$
					switch (choice) {
					case JOptionPane.YES_OPTION:
						relationChooser.saveState();
					case JOptionPane.NO_OPTION:
						removeRelationChooserView(mousePlugin);
						break;
					default:
						return -1;
					}
				}

				removeRelationChooserView();
			}

			BrickVertex source = edge.getSource();
			BrickVertex destination = edge.getDestination();



			editedModelEdge = edge;
			if (!reelle)
				relationChooser = new RelationChooserView(edge,
						mousePlugin, Program.myMainFrame.datapack
						.getRelations().getRelationPossibility(
								source.getContent(),
								destination.getContent(),
								false),
								RelationPossibility.RELATIONSTRUCTURELLE, this);
			else
				relationChooser = new RelationChooserView(edge,
						mousePlugin, Program.myMainFrame.datapack
						.getRelations()
						.getRelationPossibility(source.getContent(),
								destination.getContent(),
								true),
								RelationPossibility.RELATIONREELLE, this);

			contentPane.add(relationChooser, BorderLayout.CENTER);
			panelMax.setVisible(true);
			this.validate();
			return 0;
		}
		return -1;
	}

	protected void removeRelationChooserView() {
		removeRelationChooserView(null);
	}

	protected void removeRelationChooserView(
			TriadeEditingMousePlugin<BrickVertex, BrickEdge> mousePlugin) {
		removeRelationChooserView(mousePlugin, editedModelEdge);
	}

	protected void removeRelationChooserView(
			TriadeEditingMousePlugin<BrickVertex, BrickEdge> mousePlugin,
			BrickEdge modelEdge) {
		if (relationChooser != null && editedModelEdge == modelEdge) {
			contentPane.remove(relationChooser);
			if (mousePlugin != null
					&& editedModelEdge.getCompleteRelation().isEmpty(
							RelationPossibility.RELATIONSTRUCTURELLE)) {
				mousePlugin.getEditedAbstractSchema().removeModelEdge(
						editedModelEdge);
			}
			relationChooser = null;
			editedModelEdge = null;
		}
		panelMax.setVisible(false);
		panelMin.setVisible(false);
	}

}
