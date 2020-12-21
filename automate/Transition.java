package automate;

import java.util.*;

public interface Transition {

		public boolean evalTransition(Hashtable info);
		public int getWeight();
}
