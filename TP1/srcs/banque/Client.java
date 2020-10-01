package srcs.banque;

import srcs.persistance.Sauvegardable;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class Client implements Sauvegardable {

	private final String nom;
	private final Compte compte;

	public static HashMap<String, Compte> hashSet = new HashMap<String, Compte>();

	public Client(String nom, Compte compte) {
		this.nom=nom;
		this.compte=compte;

		if (!hashSet.containsKey(compte.getId())) {
			hashSet.put(compte.getId(), compte);
		} else {
			compte = hashSet.get(compte.getId());
		}
	}

	public Client(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);
		this.nom = dis.readUTF();
		Compte test = new Compte(dis);

		if (!hashSet.containsKey(test.getId())) {
			hashSet.put(test.getId(), test);
			this.compte = test;
		} else {
			this.compte = hashSet.get(test.getId());
		}


	}

	public String getNom() {
		return nom;
	}


	public Compte getCompte() {
		return compte;
	}

	@Override
	public boolean equals(Object o) {
		if(o==this) return true;
		if(o==null) return false;
		if(!(o instanceof Compte)) return false;
		Client other= (Client) o;
		return other.nom.equals(nom);
	}

	@Override
	public void save(OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeUTF(nom);
		compte.save(os);
	}
}
