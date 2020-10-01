import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    private final int port;
    private final Class<? extends  IRequest> classRequest;

    public Serveur(int port, Class<? extends IRequest> classRequest) {
        this.port = port;
        this.classRequest = classRequest;
    }

    public void listen() throws IOException, ReflectiveOperationException {
        ServerSocket sc = new ServerSocket(port);
        while (true) {
            Socket c = sc.accept();
            classRequest.getConstructor().newInstance().execute(c);
            c.close();
        }
    }

}
