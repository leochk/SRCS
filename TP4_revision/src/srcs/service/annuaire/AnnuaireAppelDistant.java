package srcs.service.annuaire;

import srcs.service.EtatGlobal;
import srcs.service.intefaces.AppelDistant;

import java.util.HashMap;
import java.util.Map;

@EtatGlobal
public class AnnuaireAppelDistant implements AppelDistant, Annuaire {
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
}
