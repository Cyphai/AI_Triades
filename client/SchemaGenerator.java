package client;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import client.stringTranslator.StringTranslator;

import models.Brick;
import models.BrickVertex;
import models.BrickVertex.VerticeRank;
import translation.Messages;
import dataPack.Content;
import dataPack.DataPack;

public class SchemaGenerator {

	protected DataPack datapack;
	protected Map<Content, VerticeRank> actors;

	public SchemaGenerator(DataPack _datapack, Map<Content, VerticeRank> _actors) {
		datapack = _datapack;
		actors = _actors;
	}

	public Vector<Brick> generateBricks() {
		Vector<Brick> result = new Vector<Brick>();
		HashMap<String, Brick> mainBricks = new HashMap<String, Brick>();
		for (String step : datapack.getSteps()) {
			Brick stepBrick = new Brick(step, Messages.getString("SchemaGenerator.0") //$NON-NLS-1$
					+ StringTranslator.getTranslatedString(step, StringTranslator.StringType.stepsType), datapack, null);
			mainBricks.put(step, stepBrick);
			result.add(stepBrick);
		}

		Vector<Brick> usedBrick = new Vector<Brick>();
		Vector<Vector<Content>> brickActors = new Vector<Vector<Content>>();

		for (Brick brick : datapack.getBrickList()) {
			if (!brick.isGeneric()) {
				Vector<Content> brickSet = new Vector<Content>();
				VerticeRank maxRank = VerticeRank.undefined;
				for (BrickVertex vertex : brick.getVertices()) {
					if (actors.containsKey(vertex.getContent())) {
						brickSet.add(vertex.getContent());
						if (vertex.getRank() != null && maxRank.getValue() > vertex.getRank().getValue())
						{
							maxRank = vertex.getRank();
						}
					}
				}

				if (brickSet.size() > 0) {
					brick = new Brick(brick);
					usedBrick.add(brick);
					brickActors.add(brickSet);
					String step = brick.getNoTranslatedStep();
					BrickVertex newVertex = mainBricks.get(step)
					.addBrickVertex(brick);
					newVertex.setLocation(new Point2D.Double(usedBrick.size() * 5,
							usedBrick.size() * 5));
					newVertex.setRank(maxRank);
				}
			}
		}

//		for (int i = 0; i < usedBrick.size(); i++) {
//			for (int j = i + 1; j < usedBrick.size(); j++) {
//				if (usedBrick.elementAt(i).getStep().equals(
//						usedBrick.elementAt(j).getStep())) {
//					Brick stepBrick = mainBricks.get(usedBrick.elementAt(i)
//							.getStep());
//					Set<Content> intersection = new HashSet<Content>();
//					intersection.addAll(brickActors.elementAt(i));
//					intersection.retainAll(brickActors.elementAt(j));
//
//					if (intersection.size() > 0) {
//
//
//						BrickVertex source = stepBrick
//						.getBrickVertexByContent(usedBrick.elementAt(i));
//						BrickVertex dest = stepBrick
//						.getBrickVertexByContent(usedBrick.elementAt(j));
//
//						stepBrick.addEdge(new NavigationEdge(source, dest,
//								intersection));
//					}
//				}
//			}
//		}

		for (Brick brick : usedBrick) {

			result.add(brick);
		}

		return result;
	}
}
