package beans;

import javax.ejb.Remote;

import org.entreprise.Employe;

@Remote
public interface DRHRemote {
	boolean recrutement(String nom, double v);
	double getSalaire(String nom);
	void promotion(String nom, double v);
	Employe[] list_personnel();
	void tu_es_vire(String nom);
}
