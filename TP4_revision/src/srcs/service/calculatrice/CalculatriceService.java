package srcs.service.calculatrice;

import srcs.service.MyProtocolException;
import srcs.service.SansEtat;
import srcs.service.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@SansEtat
public class CalculatriceService implements Service, Calculatrice {
    @Override
    public Integer add(Integer a, Integer b) {
        return new Integer(a + b);
    }

    @Override
    public Integer sous(Integer a, Integer b) {
        return new Integer(a - b);
    }

    @Override
    public Integer mult(Integer a, Integer b) {
        return new Integer(a * b);
    }

    @Override
    public ResDiv div(Integer a, Integer b) {
        return new ResDiv(a, b);
    }

    @Override
    public void execute(Socket connexion) {
        try (ObjectOutputStream os = new ObjectOutputStream(connexion.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(connexion.getInputStream())) {
            String name = is.readUTF();
            Integer a = is.readInt();
            Integer b = is.readInt();

            switch (name) {
                case "add" :
                    os.writeObject(add(a,b));
                    os.flush();
                    break;
                case "sous" :
                    os.writeObject(sous(a,b));
                    os.flush();
                    break;
                case "mult" :
                    os.writeObject(mult(a,b));
                    os.flush();
                    break;
                case "div" :
                    os.writeObject(div(a,b));
                    os.flush();
                    break;
                default:
                    os.writeObject(new MyProtocolException());
                    os.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
