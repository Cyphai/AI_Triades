package client;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import translation.Messages;

import models.Brick;
import models.Brick.BrickState;
import models.BrickVertex;
import models.BrickVertex.VerticeRank;
import client.export.ExportImageData;
import client.export.ExportModel;
import dataPack.Acteur;
import dataPack.Content;
import dataPack.DataPack;

public class Session implements Serializable {

	private static final long serialVersionUID = -8314347987491588105L;
	protected HashMap<Content, VerticeRank> actorsList;
	protected String name;
	protected transient SchemaGenerator schemaGenerator;
	protected transient NavigationTree navigationTree;
	protected transient ActorsSheetNavigationView actorsSheetView;
	protected transient DataPack datapack;
	protected Vector<Brick> Bricks;

	protected HashMap<Brick, Vector<ExportModel>> exports;
	protected HashMap<Content, ExportImageData> defaultImages;
	//	protected Hashtable<Object, String> actorNames;
	protected Hashtable<Brick, Integer> shownedLevels;
	protected Hashtable<Brick, StyledDocument> brickNotes;

	protected Hashtable<Acteur, ActorSheet> actorSheets;
	protected boolean isValid;

	protected Session() {
		actorsList = null;
		name = null;
		schemaGenerator = null;
		datapack = null;
		Bricks = null;
		exports = new HashMap<Brick, Vector<ExportModel>>();
		defaultImages = new HashMap<Content, ExportImageData>();
		//		actorNames = new Hashtable<Object, String>();
		actorSheets = new Hashtable<Acteur, ActorSheet>();
		shownedLevels = new Hashtable<Brick, Integer>();
		brickNotes = new Hashtable<Brick, StyledDocument>();
		isValid = false;
	}

	public Session(Map<Content, VerticeRank> actorsList, String name,
			DataPack datapack) {
		this();

		this.actorsList = new HashMap<Content, VerticeRank>(actorsList);

		this.name = name;
		this.datapack = datapack;
		isValid = false;
	}

	public Session(String name, List<Session> sessionsToMerge)
	{
		this();
		this.actorsList = new HashMap<Content, BrickVertex.VerticeRank>();
		this.name = name;
		this.datapack = sessionsToMerge.get(0).getDatapack();
		isValid = true;
		Bricks = new Vector<Brick>();
		
		HashMap<String, Brick> mainBricks = new HashMap<String, Brick>();


		for (String step : datapack.getSteps()) {
			Brick stepBrick = new Brick(step, Messages.getString("SchemaGenerator.0") //$NON-NLS-1$
					+ step, datapack, null);
			mainBricks.put(step, stepBrick);
			Bricks.add(stepBrick);
		}
		
		for (Session s : sessionsToMerge)
		{
			for (Map.Entry<Brick, StyledDocument> entry : s.brickNotes.entrySet())
			{
				if (!brickNotes.containsKey(entry.getKey()))
				{
					DefaultStyledDocument doc = new DefaultStyledDocument();
					try {
						doc.insertString(0, entry.getValue().getText(0, entry.getValue().getLength()), new SimpleAttributeSet());
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					brickNotes.put(entry.getKey(), doc);
				}
				else if (entry.getValue().getLength() > 0)
				{
					StyledDocument localDoc = brickNotes.get(entry.getKey());
					try {
						localDoc.insertString(localDoc.getLength(), "\n**********\n"+entry.getValue().getText(0, entry.getValue().getLength()), new SimpleAttributeSet());
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}



			actorSheets.putAll(s.actorSheets);
			exports.putAll(s.exports);

			for (Map.Entry<Content, VerticeRank> entry : s.getActorsList().entrySet())
			{
				if (actorsList.containsKey(entry.getKey()))
				{
					if (actorsList.get(entry.getKey()).getValue() > entry.getValue().getValue())
					{
						actorsList.put(entry.getKey(), entry.getValue());
					}
				}
				else
				{
					actorsList.put(entry.getKey(), entry.getValue());
				}
			}

		

			for (Brick b : s.getBrickList())
			{
				if (b.isNavigationBrick())
					continue;

				if (!Bricks.contains(b))
					Bricks.add(b);
				else
				{
					Brick localB = Bricks.get(Bricks.indexOf(b));
					if (localB.getState() == BrickState.EMPTY)
					{
						Bricks.remove(localB);
						Bricks.add(b);
					}
					else if (b.getState() != BrickState.EMPTY)
					{
						if (DialogHandlerFrame.showYesNoDialog("Au moins deux sessions contiennent des relations complétées dans la brique "+b.toString()+"\nVoulez vous garder celle de la session : "+s.getName()) == JOptionPane.YES_OPTION)
						{
							Bricks.remove(localB);
							Bricks.add(b);
						}

					}
				}
			}


		}

		for (Brick b: Bricks)
		{
			if (b.isNavigationBrick())
				continue;
			VerticeRank maxRank = VerticeRank.undefined;
			for (BrickVertex vertex : b.getVertices()) {
				if (actorsList.containsKey(vertex.getContent())) {
					if (vertex.getRank() != null && maxRank.getValue() > vertex.getRank().getValue())
					{
						maxRank = vertex.getRank();
					}
				}
			}
		
			String step = b.getNoTranslatedStep();
			BrickVertex newVertex = mainBricks.get(step).addBrickVertex(b);
			newVertex.setLocation(new Point2D.Double(Bricks.size() * 5,
					Bricks.size() * 5));
			newVertex.setRank(maxRank);
		}

	}

	public void initSession() {
		isValid = true;
		schemaGenerator = new SchemaGenerator(getDatapack(), actorsList);
	}

	public boolean isValid() {
		return isValid;
	}

	public Vector<Brick> getBrickList() {
		if (Bricks == null) {
			schemaGenerator = new SchemaGenerator(getDatapack(), actorsList);
			Bricks = schemaGenerator.generateBricks();

		}
		return Bricks;
	}

	public ExportModel addExport(Brick baseBrick, ExportModel newExport) {
		Vector<ExportModel> vertexExports;
		if ((vertexExports = exports.get(baseBrick)) == null) {
			vertexExports = new Vector<ExportModel>();
			exports.put(baseBrick, vertexExports);
		}
		vertexExports.add(newExport);

		return newExport;
	}

	public Vector<ExportModel> getExports() {
		Vector<ExportModel> result = new Vector<ExportModel>();

		for(Vector<ExportModel> vertexExports : exports.values())
			result.addAll(vertexExports);

		return result;
	}

	public Vector<ExportModel> getExports(Brick baseBrick) {
		return exports.get(baseBrick);
	}

	public HashMap<Content, VerticeRank> getActorsList() {
		return actorsList;
	}

	public void setActorsList(HashMap<Content, VerticeRank> actorsList) {
		this.actorsList = actorsList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNavigationTree(NavigationTree navigationTree) {
		this.navigationTree = navigationTree;
	}

	public NavigationTree getNavigationTree() {
		return navigationTree;
	}

	public Icon getDefaultImage(Content content) {

		ExportImageData data = defaultImages.get(content);

		if(data != null) {
			return data.getIcon();
		}

		return null;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setExportedImageData(ExportImageData exportImageData, Content content) {
		defaultImages.put(content, new ExportImageData(exportImageData));
	}

	public String getSessionActorName(Object object) {
		if(object instanceof Acteur ? actorSheets.get(object) != null : actorSheets.get(object) != null) {
			return actorSheets.get(object).getActorName();
		}

		return null;
	}

	public ActorSheet getActorSheet(Acteur actor) {
		if(actorSheets.get(actor) == null) {
			actorSheets.put(actor, new ActorSheet(actor));
		}

		ActorSheet actorSheet = actorSheets.get(actor);
		actorSheet.setActor(actor);
		return actorSheets.get(actor);
	}

	public void setActorSheets(Hashtable<Acteur, ActorSheet> sheets) {
		actorSheets = sheets;
	}

	public void showActorSheet(Acteur actor) {
		actorsSheetView = new ActorsSheetNavigationView(this, actor);
		Program.myMainFrame.addTab(actorsSheetView);
	}

	public Integer getBrickShownedLevel(Brick brick)
	{

		if (shownedLevels == null)
			shownedLevels = new Hashtable<Brick, Integer>();

		Integer result = null;
		result = shownedLevels.get(brick);
		if (result == null)
		{
			result = new Integer(1);
		}
		return result;
	}

	public void setBrickShownedLevel(Brick brick, Integer newLevel)
	{
		if (shownedLevels == null)
			shownedLevels = new Hashtable<Brick, Integer>();
		shownedLevels.put(brick, newLevel);
	}

	public void setDatapack(DataPack datapack) {
		this.datapack = datapack;
	}

	public DataPack getDatapack()
	{
		if (datapack == null)
			datapack = Program.myMainFrame.getDataPack();
		return datapack;
	}

	public ActorsSheetNavigationView getActorsSheetView() {
		return actorsSheetView;
	}

	public StyledDocument getNoteForBrick(Brick brick) {
		if (brickNotes == null)
			brickNotes = new Hashtable<Brick, StyledDocument>();

		StyledDocument result = brickNotes.get(brick);
		if (result == null)
		{
			result = new DefaultStyledDocument();
			brickNotes.put(brick, result);
		}
		return result;
	}

	public void removeExport(Brick baseSchema, ExportModel export) {
		getExports(baseSchema).remove(export);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Session))
			return false;
		Session other = (Session) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	


}
