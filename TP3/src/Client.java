import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4343);
        StringBuilder sb = new StringBuilder();
        BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String toWrite = "<html>\n" +
                "<head> <title>Coucou</title> </head>\n" +
                "<body> Test !<br/> </body>\n" +
                "</html>\n";

        sb.append("PUT /test.html HTTP/1.1\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Content-Length: "+toWrite.getBytes().length+ "\r\n");

        sb.append("\r\n");
        sb.append(toWrite);

        os.write(sb.toString());
        os.flush();
        os.close();
        socket.close();
        System.out.println(sb.toString());
    }
}
