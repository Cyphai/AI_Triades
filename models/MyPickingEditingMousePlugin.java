package models;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class MyPickingEditingMousePlugin<V extends BrickVertex, E extends BrickEdge>
extends PickingGraphMousePlugin<V, E> {

	public MyPickingEditingMousePlugin(int selectionModifiers,
			int addToSelectionModifiers) {
		super(selectionModifiers, addToSelectionModifiers);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mouseDragged(MouseEvent e) {
		if (locked == false) {
			VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
			.getSource();
			if (vertex != null) {



				Layout<V, E> layout = vv
				.getGraphLayout();
				PickedState<V> ps = vv.getPickedVertexState();

				Point2D newLocation = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint());
				layout.setLocation(vertex, newLocation);

				vertex.setLocation(newLocation);						

				e.consume();
				


			} 
		}
	}

}
