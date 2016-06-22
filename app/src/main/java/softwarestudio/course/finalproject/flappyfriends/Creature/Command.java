package softwarestudio.course.finalproject.flappyfriends.Creature;

import softwarestudio.course.finalproject.flappyfriends.Utility;

/**
 * Created by lusa on 2016/06/21.
 */
public class Command {

    private int commandTarget = Utility.TARGET_HOST;

    public Command(int target) {
        setCommandTarget(target);
    }

    public void setCommandTarget(int commandTarget) {
        if (commandTarget < Utility.MAX_PLAYERS)
            this.commandTarget = commandTarget;
        else
            this.commandTarget = Utility.TARGET_NULL;
    }

    public int getCommandTarget() { return commandTarget; }
}
