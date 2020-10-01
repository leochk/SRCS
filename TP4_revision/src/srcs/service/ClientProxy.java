package srcs.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class ClientProxy {
    private final String machine;
    private final int port;

    public ClientProxy(String machine, int port) {
        this.machine = machine;
        this.port = port;
    }

    public Object invokeService(String name, Object[] params) throws MyProtocolException {
        try (Socket s = new Socket(machine, port);
             ObjectInputStream is = new ObjectInputStream(s.getInputStream());
             ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream())){

            os.writeUTF(name);
            for (int i = 0; i < params.length; i++)
                os.writeObject(params[i]);

            Object o = is.readObject();
            if (o instanceof MyProtocolException)
                throw new MyProtocolException();
            else
                return o;
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
        throw new MyProtocolException();
    }

    public String getMachine() {
        return machine;
    }

    public int getPort() {
        return port;
    }
}
