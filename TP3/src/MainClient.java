import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainClient {
    public static void main(String[] args) throws IOException, ReflectiveOperationException {
        new Serveur(4343, ProcessRequestPut.class).listen();


    }
}
