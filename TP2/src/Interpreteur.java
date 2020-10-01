import com.sun.org.apache.bcel.internal.generic.ICONST;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Annotated;
import command.DontSave;
import command.Echo;
import command.Exit;
import command.ICommand;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;


public class Interpreteur {
    BufferedReader br;
    HashMap<String, Class<? extends ICommand>> commandMap = new HashMap<>();

    public Interpreteur(BufferedReader br) {
        this.br = br;
        commandMap.put("deploy", Deploy.class);
        commandMap.put("undeploy", Undeploy.class);
        commandMap.put("save", Save.class);
        commandMap.put("echo", Echo.class);
        commandMap.put("exit", Exit.class);
       // ("echo", "src/", "command.Echo");
       // ("exit", "src/", "command.Exit");
       // ("add", "/home/check-leo/a_SAR_S2/SRCS/TP2bis/out/production/TP2bis/","command.Add");
    }

    public Interpreteur(BufferedReader br, String file) throws IOException, ClassNotFoundException {
        this.br = br;
        ObjectInputStream ois = new CustomClassLoader(new FileInputStream(file));
        this.commandMap = (HashMap<String, Class<? extends ICommand>>) ois.readObject();
    }

    public void run() throws IOException, ReflectiveOperationException, IllegalArgumentException, InvocationTargetException, ExceptionTerminated {
        String str;
        while ((str = this.br.readLine()) != null) {
            // Input testing
            String[] split;
            if (str.equals("") || (split = str.split(" ")).length == 0) continue;

            // lowercase command
            String cmd = split[0].toLowerCase();

            // if command exists
            if (commandMap.containsKey(cmd)) {
                // get the respective Class in commandMap
                Class<? extends ICommand> classe = commandMap.get(cmd);
                // inner class test
                boolean isInner = false;
                // forEach inner Class of Interpreteur
                for (Class<?> c : this.getClass().getDeclaredClasses()) {
                    // if the passed command correspond to an inner Class of Interpreteur (i.e deploy/undeploy)
                    if (classe.toString().equals(c.toString())) {
                        // build an instance and execute the command
                            classe.getConstructor(Interpreteur.class, List.class)
                                    .newInstance(this, Arrays.asList(split))
                                    .execute();
                            // set isInner at true
                            isInner = true;

                    }
                }
                // if we did deploy/undeploy, continue
                if (isInner) continue;

                // else, cmd corresponds to an "extern" command
                classe.getConstructor(List.class)
                        .newInstance(Arrays.asList(split))
                        .execute();
            } else {
                throw new IllegalArgumentException("Command " + cmd + " does not exist");
            }
        }
        throw new ExceptionTerminated();
    }


    private class Deploy implements ICommand {
        private List<String> args;

        public Deploy(List<String> args) {
            if (args.size() != 4) throw new IllegalArgumentException("usage deploy : <cmd> <pathClass> <absClassName>");
            this.args = args;
        }

        @Override
        public void execute() {
            HashMap<String, Class<? extends ICommand>> commandMap = Interpreteur.this.commandMap;
            String cmd = args.get(1);
            String path = args.get(2);
            String absoluteName = args.get(3);

            if (commandMap.containsKey(cmd)) throw new IllegalArgumentException(cmd+" already exists in Interpreteur");
            try {
                URL url = new File(path).toURI().toURL();
                URLClassLoader ucl = URLClassLoader.newInstance(new URL[]{url});

                Class<? extends ICommand> classe = ucl.loadClass(absoluteName).asSubclass(ICommand.class);
                commandMap.put(cmd, classe);
                System.out.println(args.get(1) + " deployed");
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(path+" does not exist");
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(absoluteName+" does not exist in "+path);
            }
        }
    }

    private class Undeploy implements ICommand {
        List<String> args;

        public Undeploy(List<String> args) {
            if (args.size() != 2) throw new IllegalArgumentException("usage deploy : <cmd>");
            this.args = args;
        }

        @Override
        public void execute() {
            if (Interpreteur.this.commandMap.containsKey(args.get(1))) {
                Interpreteur.this.commandMap.remove(args.get(1));
                System.out.println(args.get(1) + " undeployed");
            } else {
                throw new IllegalArgumentException(args.get(1) + " does not exist");
            }

        }
    }

    private class Save implements ICommand {
        List<String> args;

        public Save(List<String> args) {
            if (args.size() != 2) throw new IllegalArgumentException("usage deploy : <file>");
            this.args = args;
        }

        @Override
        public void execute() {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(args.get(1)));
                HashMap<String, Class<? extends ICommand>> commandMap = new HashMap<>();
                /*
                for (String k : Interpreteur.this.commandMap.keySet()) {
                    Class<? extends ICommand> c = Interpreteur.this.commandMap.get(k);
                    DontSave a;
                    if ((a = c.getAnnotation(DontSave.class)) != null) {
                        System.out.println(c.getName()+" can not be saved : "+ a.reason());
                    } else {
                        commandMap.put(c.getName(), c);
                        System.out.println(c.getName() + " saved");
                    }
                }
                */
                oos.writeObject(Interpreteur.this.commandMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
