package command;

import java.util.List;

@DontSave(reason = "command from TP2bis project")
public class Add implements ICommand {
    private final int a;
    private final int b;

    public Add(List<String> args) {
        if (args.size() < 3) {
            throw new IllegalArgumentException("usage add : <operande1> <operande2>");
        }
        this.a = Integer.parseInt(args.get(1));
        this.b = Integer.parseInt(args.get(2));
    }

    @Override
    public void execute() {
        System.out.println(a+b);
    }
}
