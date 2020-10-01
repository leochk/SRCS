import java.io.IOException;
import java.net.Socket;

public interface IRequest {
    public void execute (Socket connexion) throws IOException;
}
