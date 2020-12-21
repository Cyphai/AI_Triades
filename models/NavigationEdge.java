package models;

import java.util.HashSet;
import java.util.Set;

import translation.Messages;

import dataPack.Content;

public class NavigationEdge extends BrickEdge {

	private static final long serialVersionUID = 9007768621574379344L;
	protected Set<Content> sharedContent;

	public NavigationEdge(BrickVertex source, BrickVertex destination,
			Set<Content> actorIntersection) {
		super(source, destination);
		sharedContent = new HashSet<Content>(actorIntersection);

	}

	@Override
	public String getStringDescription() {
		int size = sharedContent.size();
		if (size == 1) {
			return Messages.getString("NavigationEdge.0") + sharedContent.iterator().next().toString(); //$NON-NLS-1$
		} else {
			String result = "<html>" + size + Messages.getString("NavigationEdge.2"); //$NON-NLS-1$ //$NON-NLS-2$
			for (Content id : sharedContent) {
				result += "<br/>- " + id.toString(); //$NON-NLS-1$
			}
			return result + "</html>"; //$NON-NLS-1$
		}
	}

	public Set<Content> getSharedContent() {
		return sharedContent;
	}
	
	
}
