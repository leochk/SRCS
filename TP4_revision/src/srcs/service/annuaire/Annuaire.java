package srcs.service.annuaire;

import srcs.service.MyProtocolException;

public interface Annuaire {
    public String lookup(String nom);
    public void bind(String nom, String val);
    public void unbind(String nom);
}
