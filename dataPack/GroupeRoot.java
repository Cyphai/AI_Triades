package dataPack;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;

import translation.Messages;
import graphicalUserInterface.DialogHandlerFrame;

public class GroupeRoot extends Groupe {

	private static final long serialVersionUID = -6533221997848227528L;

	GroupeRoot(String nNom) {
		super(nNom);
	}

	protected GroupeRoot(String name, Integer idGroup, Integer idFather)
	{
		super(name,idGroup, idFather);
	}

	@Override
	public void changeStringValue(String newString) {
		DialogHandlerFrame
		.showErrorDialog(Messages.getString("GroupeRoot.0")); //$NON-NLS-1$
	}

	protected Object writeReplace()
	{
		return new GroupProxy(this, true);
	}

	protected void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();
	}

	@Override
	public void validateObject() throws InvalidObjectException {
		if (!addedToGroupList)
		{

			super.validateObject();
		}
		else
		{
		//	DataPack.getLastLoadedDatapack().getActorsModel().getRoot().add(this);

		}
	}


}
