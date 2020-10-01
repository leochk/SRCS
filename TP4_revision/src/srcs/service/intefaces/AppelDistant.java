package srcs.service.intefaces;

import srcs.service.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public interface AppelDistant extends Service {
    default public void execute (Socket connexion) {
        try (ObjectOutputStream os = new ObjectOutputStream(connexion.getOutputStream());
             ObjectInputStream is = new ObjectInputStream( connexion.getInputStream())) {

            String name = is.readUTF();
            Object[] params;

            for (Method m : this.getClass().getMethods()) {
                if (m.getName().equals(name)) {
                    params = new Object[m.getParameterCount()];
                    for (int i = 0; i < params.length; i++) {
                        params[i] = is.readObject();
                    }
                    os.writeObject(m.invoke(this, params));
                    break;
                }
            }
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
