package relations;

import java.io.Serializable;

import client.stringTranslator.StringTranslator;

/**
 * Décrit l'ensemble des acteurs pouvant intervenir dans une relation.
 * @author babcool
 *
 */
public class RelationDescription implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8819964772447156207L;
	protected boolean content[][];
	protected String name;
	protected RelationPossibility possibility;
	protected boolean onlyRealRelation;

	protected RelationDescription(String name) {
		this.name = name;
		// Taille du tableau : 18 statuts différents + 4 moyens + 1 objet = 23

		content = new boolean[23][23];
		possibility = new RelationPossibility();
	}




	public boolean allow(Integer a, Integer b, boolean realRelation) {
		return (realRelation || !this.onlyRealRelation)
				&& content[a.intValue()][b.intValue()];

	}

	public void set(int a, int b, boolean value) {
		content[a][b] = value;
		//		System.out.println("Nouvelle valeur pour " + a + " et " + b + " vers " + value);
	}

	public void setRect(int minA, int maxA, int minB, int maxB, boolean value) {
		for(int a = minA ; a <= maxA ; a++) {
			for(int b = minB ; b <= maxB ; b++) {
				set(a, b, value);
			}
		}
	}


	public void setTriangleInf(int a, int minB, int maxB, boolean value) {
		for(int i = 0 ; i < maxB - minB ; i++) {
			for(int j = 0 ; j < maxB - minB - i ; j++) {
				set(a+i, minB+j, value);
			}
		}
	}

	public void setTriangleSup(int a, int minB, int maxB, boolean value) {
		for(int i = 0 ; i < maxB - minB ; i++) {
			for(int j = 0 ; j < i ; j++) {
				set(a+j, minB+i, value);
			}
		}
	}

	public RelationPossibility getPossibility() {
		return possibility;
	}

	public void setRealRelation(boolean realRelation) {
		onlyRealRelation = realRelation;
	}

	public boolean isRealRelation() {
		return onlyRealRelation;
	}

	public String getNoTranslatedString() {
		return name;
	}

	@Override
	public String toString() {
		return StringTranslator.getTranslatedString(this, StringTranslator.StringType.groupType);
	}

	public void setName(String text) {
		if (name != "") //$NON-NLS-1$
			name = text;
	}

	public boolean relationCategory(int source, int destination)
	{

		// Taille du tableau : 18 statuts différents + 4 moyens + 1 objet = 23
		boolean sourceB = false;
		boolean targetB = false;
		boolean result = false;
		if (source == 0)
		{
			for (int i = 0; i < 23 && !sourceB; i++)
			{
				for (int j = 0; j < 18 && !sourceB; j++)
				{
					if (content[j][i])
					{
						sourceB = true;
					}
				}
			}
		}
		else if (source == 1)
		{
			for (int i = 0; i < 23 && !sourceB; i++)
			{
				for (int j = 18; j < 22 && !sourceB; j++)
				{
					if (content[j][i])
					{
						sourceB = true;
					}
				}
			}
		}
		else if (source == 2)
		{
			for (int i = 0; i < 23 && !sourceB; i++)
			{

				if (content[22][i])
				{
					sourceB = true;
				}
			}
		}
		
		if (destination == 0)
		{
			for (int i = 0; i < 23 && !targetB; i++)
			{
				for (int j = 0; j < 18 && !targetB; j++)
				{
					if (content[i][j])
					{
						targetB = true;
					}
				}
			}
		}
		else if (destination == 1)
		{
			for (int i = 0; i < 23 && !targetB; i++)
			{
				for (int j = 18; j < 22 && !targetB; j++)
				{
					if (content[i][j])
					{
						targetB = true;
					}
				}
			}
		}
		else if (destination == 2)
		{
			for (int i = 0; i < 23 && !targetB; i++)
			{

				if (content[i][22])
				{
					targetB = true;
				}
			}
		}
		return sourceB && targetB;
	}
}


