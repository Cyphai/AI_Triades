package automate;

import java.util.*;

public class KeywordTransition extends EpsilonTransition {

	public static String key = "keyword";
	protected String keyword;
	
	public KeywordTransition(String _keyword, int w)
	{
		super(w);
		keyword = _keyword;
	}
	
	public boolean evalTransition(Hashtable info) {
		Set<String> keywordSet = (Set<String>)info.get(key);
		if (keywordSet != null)
		{
			return keywordSet.contains(keyword);
		}
		return false;
	}

}
