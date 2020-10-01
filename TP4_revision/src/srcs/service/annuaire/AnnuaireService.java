package srcs.service.annuaire;

import srcs.service.EtatGlobal;
import srcs.service.MyProtocolException;
import srcs.service.VoidResponse;
import srcs.service.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@EtatGlobal
public class AnnuaireService implements Service, Annuaire {
    private Map<String, String> map = new HashMap<>();

    @Override
    public String lookup(String nom) {
        String res;
        synchronized (map) {
            res = (map.containsKey(nom)) ? map.get(nom) : "";
        }
        return res;
    }

    @Override
    public void bind(String nom, String val) {
        synchronized (map) {
            map.put(nom, val);
        }
    }

    @Override
    public void unbind(String nom) {
        synchronized (map) {
            map.remove(nom);
        }
    }

    @Override
    public void execute(Socket connexion)  {
        try (ObjectInputStream is = new ObjectInputStream(connexion.getInputStream());
             ObjectOutputStream os = new ObjectOutputStream(connexion.getOutputStream())) {

            String name = is.readUTF();

            String nom, val;
            switch (name) {
                case "lookup":
                    nom = is.readUTF();
                    os.writeObject(lookup(nom));
                    os.flush();
                    break;
                case "bind":
                    nom = is.readUTF();
                    val = is.readUTF();
                    bind(nom, val);
                    os.writeObject(new VoidResponse());
                    os.flush();
                    break;
                case "unbind":
                    nom = is.readUTF();
                    unbind(nom);
                    os.writeObject(new VoidResponse());
                    os.flush();
                    break;
                default:
                    os.writeObject(new MyProtocolException());
                    os.flush();
                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
