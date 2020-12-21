package main;

public enum ObjectifObjetObjet implements Objectif
{
		permetRea("Permet la réalisation"),fiabiliseRea("Fiabilise la réalisation"),controleRea("Controle la réalisation"),transforme("Transformer"),controleEtat("Contrôler l'état"),eval("Evaluation");

		public String nom;
		
		private ObjectifObjetObjet(String _nom)
		{
			nom = _nom;
		}
		
		public String toString()
		{
			return nom;
		}
	
	
	
}
