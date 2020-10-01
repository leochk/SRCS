package srcs.banque;

import srcs.persistance.Sauvegardable;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Banque implements Sauvegardable {

	private final Set<Client> clients;
	
	public Banque() {
		clients=new HashSet<>();
	}

	public Banque(InputStream is) throws IOException {
		DataInputStream dos = new DataInputStream(is);
		clients=new HashSet<>();
		int len = dos.readInt();
		for (int i = 0; i < len; i++) clients.add(new Client(dos));
	}

	public int nbClients() {
		return clients.size();
	}
	
	public int nbComptes() {
		Set<Compte> comptes = new HashSet<>();
		for(Client c : clients) {
			comptes.add(c.getCompte());
		}
		return comptes.size();
	}
	
	public Client getClient(String nom) {
		for(Client c : clients) {
			if(c.getNom().equals(nom)) return c;
		}
		return null;
	}
	
	public boolean addNewClient(Client c) {
		return clients.add(c);
	}


	@Override
	public void save(OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeInt(clients.size());
		for (Client c: clients) c.save(dos);
	}
}
