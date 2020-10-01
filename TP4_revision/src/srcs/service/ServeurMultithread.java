package srcs.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServeurMultithread {
    private final int port;
    private final Class<? extends Service> service;
    private Map<String, Service> map = new HashMap<>();

    public ServeurMultithread(int port, Class<? extends Service> service) {
        this.port = port;
        this.service = service;

        if (service.getAnnotation(EtatGlobal.class) != null
                && !map.containsKey(service.getCanonicalName())) {
            try {
                map.put(service.getCanonicalName(), service.getConstructor().newInstance());
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IllegalArgumentException {
        try (ServerSocket ss = new ServerSocket(port)) {
            while (true) {
                Socket s =      ss.accept();
                Service serv =  getService();

                new Thread(() -> {
                    serv.execute(s);
                }).start();
            }
        } catch (ReflectiveOperationException | IOException e) {
            e.printStackTrace();
        }
    }

    private Service getService() throws IllegalStateException, ReflectiveOperationException {
        if (service.getAnnotation(SansEtat.class) != null)
            return service.getConstructor().newInstance();

        else if (service.getAnnotation(EtatGlobal.class) != null)
            return map.get(service.getCanonicalName());

        throw new IllegalStateException("Pas d'annotation");
    }
}
