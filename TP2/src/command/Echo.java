package command;

import java.util.List;

public class Echo implements ICommand {

    List<String> args;

    public Echo(List<String> args) {
        if (args.size() < 1) throw new IllegalArgumentException();
        this.args = args;
    }

    @Override
    public void execute() {
        for (int i = 1; i < args.size(); i++)
            System.out.print(args.get(i) + " ");
        System.out.println("\n");
    }
}
