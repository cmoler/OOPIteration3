package Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand;

import Model.Command.Command;

public abstract class ToggleableCommand implements Command {

    private boolean hasFired;

    protected ToggleableCommand() {
        this.hasFired = false;
    }

    protected ToggleableCommand(boolean hasFired) {
        this.hasFired = hasFired;
    }

    protected boolean hasFired() {
        return hasFired;
    }

    protected void toggleHasFired() {
        hasFired = !hasFired;
    }
}
