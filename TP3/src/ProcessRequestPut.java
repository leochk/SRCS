import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ProcessRequestPut implements IRequest{


    @Override
    public void execute(Socket connexion) throws IOException {
        try (
                BufferedReader is = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(connexion.getOutputStream()))
        ) {

            StringBuilder sb = new StringBuilder();
            boolean error = false;

            // requete
            String request = is.readLine();
            if (request == null) {
                error = true;
                sb.append("HTTP/1.1 400 BadRequest\r\n");
                System.out.println("Should not appened\n");
            }

            String[] splitedrequest = request.split(" ");
            if (!error && !splitedrequest[0].equals("PUT")) {
                error = true;
                sb.append("HTTP/1.1 400 BadRequest\r\n");
                System.out.println("Bad request\n");
            }

            if (error) {
                os.write(sb.toString());
                os.flush();
                return;
            }

            String pathdir = System.getProperty("user.dir");
            File f = new File(pathdir + splitedrequest[1]);
            if (!f.exists()) f.createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()));

            // entete
            List<String> entetes = new ArrayList<>();
            String line;
            while ((line = is.readLine()) != null) {
                if (line.isEmpty()) break;
                entetes.add(line);

            }

            sb.append("HTTP/1.1 200 OK\r\n");
            for (String s : entetes)
                sb.append(s + "\r\n");

            sb.append("\r\n\r\n");
            while ((line = is.readLine()) != null) {
                sb.append(line+"\n");
                bw.write(line+"\n");
            }
            bw.close();
            System.out.println(sb.toString());

            os.write(sb.toString());
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
