package Model.AreaEffect;

import Model.Command.Command;
import Model.Entity.Entity;

public class InfiniteAreaEffect implements AreaEffect {

    private Command command;

    public InfiniteAreaEffect(Command command) {
        this.command = command;
    }

    public void trigger(Entity entity) {
        // TODO: implement
    }
}
