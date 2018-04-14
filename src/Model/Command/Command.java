package Model.Command;

import Model.Entity.Entity;

public interface Command {
    void execute(Entity entity);
}
