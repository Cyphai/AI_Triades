package tools;

import translation.Messages;

public class StringCreator {
	static public String createVersionString(short majorVersion, short mediumVersion, short minorVersion) {
		return majorVersion+"."+mediumVersion+"."+minorVersion; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
