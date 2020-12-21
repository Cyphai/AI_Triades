package dataPack;

import graphicalUserInterface.Program;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;

public abstract class MyDefaultMutableTreeNode extends DefaultMutableTreeNode implements Content, ObjectInputValidation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5695300567627123042L;

	public enum MyTreeNodeType {
		ActorType, GroupeType, ActiviteType, MoyenType, BaseType, CorpsType, BrickType
	};

	public abstract JLabel getJComponent(boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus);

	public abstract Icon getIcon(boolean bigIcon);
	public abstract Integer getId();
	public abstract MyTreeNodeType getType();
	
	public abstract void changeStringValue(String newString);

	public JComponent getJcomComponent() {
		return getJComponent(false, false, false, 0, false);
	}

	static public boolean isGroupe(MyDefaultMutableTreeNode myNode) {
		return myNode.getType() == MyTreeNodeType.GroupeType;
	}

	static public boolean isActor(MyDefaultMutableTreeNode myNode) {
		return myNode.getType() == MyTreeNodeType.ActorType;
	}

	static public boolean isActivite(MyDefaultMutableTreeNode myNode) {
		return myNode.getType() == MyTreeNodeType.ActiviteType;
	}

	static public boolean isMoyen(MyDefaultMutableTreeNode myNode) {
		return myNode.getType() == MyTreeNodeType.MoyenType;
	}

	static public boolean isBrick(MyDefaultMutableTreeNode myNode) {
		return myNode.getType() == MyTreeNodeType.BrickType;
	}

	static public boolean isBase(MyDefaultMutableTreeNode myNode) {
		return myNode.getType() == MyTreeNodeType.BaseType;
	}

	static public boolean isCorps(MyDefaultMutableTreeNode myNode) {
		return myNode.getType() == MyTreeNodeType.CorpsType;
	}

	static public ActeurBase isBaseCorps(MyDefaultMutableTreeNode myNode) {
		if (isBase(myNode) || isCorps(myNode)) {
			return (ActeurBase) myNode;
		} else {
			return null;
		}
	}

	static public ActeurBase isBaseCorps(Integer idActor) {
		return isBaseCorps(Program.myMainFrame.getDataPack().getActorsModel().getActorById(idActor));
	}
	
	protected void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();
	}
	
}