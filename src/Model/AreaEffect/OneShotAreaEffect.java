package Model.AreaEffect;

import Model.Command.Command;
import Model.Entity.Entity;

public class OneShotAreaEffect implements AreaEffect {

    private Command command;
    private boolean hasNotFired;

    public OneShotAreaEffect(Command command) {
        this.command = command;
        hasNotFired = true;
    }

    public void trigger(Entity entity) {
        if(hasNotFired) {
            command.execute(entity);
            hasNotFired = false;
        }
    }
}
