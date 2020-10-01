import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.Socket;

public class PrintRequestHTTP implements IRequest {

    @Override
    public void execute(Socket connexion) throws IOException {
        BufferedReader is = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
        System.out.println(is.readLine());
    }
}
