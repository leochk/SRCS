package command;

import java.util.List;

public class Exit implements ICommand {

    List<String> args;
    int i;

    public Exit(List<String> args) {
        switch (args.size()) {
            case 1 :
                i = 1;
                break;
            case 2:
                try {
                    i = Integer.parseInt(args.get(1));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException();
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.args = args;
    }

    @Override
    public void execute() {
        System.out.println("Fin.");
        System.exit(i);
    }
}
