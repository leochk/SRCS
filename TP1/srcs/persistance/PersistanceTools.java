package srcs.persistance;

import srcs.banque.Compte;

import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class PersistanceTools {
  public static void saveArrayInt(String f, int[] tab) throws FileNotFoundException, IOException {
    DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
    dos.writeInt(tab.length);

    for (int i = 0; i < tab.length; i++) {
      dos.writeInt(tab[i]);
    }
  }

  public static int[] loadArrayInt(String f) throws IOException {
    DataInputStream dis = new DataInputStream(new FileInputStream((f)));
    int[] res;

    int size = dis.readInt();
    res = new int[size];
    for (int i = 0; i < size; i++) {
      res[i] = dis.readInt();
    }
    return res;
  }

  public static void saveCompte(String f, Compte e) throws IOException {
    e.save(new FileOutputStream(f));
  }

  public static Compte loadCompte(String f) throws IOException {
    return new Compte(new FileInputStream(f));
  }

  public static void save(String fichier, Sauvegardable s) throws IOException {
    DataOutputStream dos = new DataOutputStream(new FileOutputStream(fichier));
    dos.writeUTF(s.getClass().getCanonicalName());
    s.save(dos);
  }

  public static Sauvegardable load(String fichier) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    DataInputStream dos = new DataInputStream(new FileInputStream(fichier));
    String c = dos.readUTF();

    Class<? extends Sauvegardable> classe = Class.forName(c).asSubclass(Sauvegardable.class);
    return classe.getConstructor(InputStream.class).newInstance(dos);
  }

}

