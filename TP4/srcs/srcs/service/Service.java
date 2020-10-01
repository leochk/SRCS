package srcs.service;

import java.io.IOException;
import java.net.Socket;

public interface Service {
    public void execute (Socket connexion);
}
