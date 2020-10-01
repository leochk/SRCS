import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Interpreteur i = null;

        try {
            i = new Interpreteur(br, "savefileAdd.txt");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

       //  i = new Interpreteur(br);

        while (true) {
            try {
                i.run();
            } catch (IOException | IllegalArgumentException |ReflectiveOperationException e) {
                e.printStackTrace();
            } catch (ExceptionTerminated exceptionTerminated) {
                exceptionTerminated.printStackTrace();
                break;
            }
        }

    }
}
