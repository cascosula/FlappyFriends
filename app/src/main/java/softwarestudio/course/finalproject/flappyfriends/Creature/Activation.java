package softwarestudio.course.finalproject.flappyfriends.Creature;

/**
 * Created by lusa on 2016/06/23.
 */
public class Activation
{
    private boolean isactive = false;

    public Activation() {};
    public Activation(boolean isactive) { this.isactive = isactive; }

    public void ReplaceData(boolean isactive) { this.isactive = isactive; }

    public boolean getActivaion() { return isactive; }
}
