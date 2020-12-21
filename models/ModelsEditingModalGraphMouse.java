package models;

import java.awt.event.InputEvent;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;

public class ModelsEditingModalGraphMouse<V extends BrickVertex, E extends BrickEdge>
		extends EditingModalGraphMouse<V, E> {

	protected TriadeEditingMousePlugin<V, E> mousePlugin;

	protected ModelsEditingModalGraphMouse(
			TriadeEditingMousePlugin<V, E> _mousePlugin,
			RenderContext<V, E> renderContext) {
		super(renderContext, null, null);
		in = 1.1f;
		out = 1 / 1.1f;
		mousePlugin = _mousePlugin;
		loadPlugins();

	}

	@Override
	protected void loadPlugins() {
		pickingPlugin = new MyPickingEditingMousePlugin<BrickVertex, BrickEdge>(
				InputEvent.BUTTON1_MASK
				| InputEvent.SHIFT_MASK, 0);
		translatingPlugin = new TranslatingGraphMousePlugin(
				InputEvent.BUTTON1_MASK | InputEvent.CTRL_MASK);
		scalingPlugin = new ScalingGraphMousePlugin(
				new CrossoverScalingControl(), 0, in, out);
		editingPlugin = mousePlugin;

		setMode(Mode.EDITING);
	}

	@Override
	protected void setEditingMode() {
		add(pickingPlugin);
		remove(animatedPickingPlugin);
		add(translatingPlugin);
		remove(rotatingPlugin);
		remove(shearingPlugin);
		add(editingPlugin);
	}

}
