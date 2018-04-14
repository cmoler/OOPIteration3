package Model.AreaEffect;

import Model.Command.Command;
import Model.Entity.Entity;

public class OneShotAreaEffect implements AreaEffect {

    private Command command;
    private boolean hasFired;

    public OneShotAreaEffect(Command command) {
        this.command = command;
        hasFired = false;
    }

    public void trigger(Entity entity) {
        // TODO: implement
    }
}
