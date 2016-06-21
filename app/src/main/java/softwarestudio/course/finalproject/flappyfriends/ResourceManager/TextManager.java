package softwarestudio.course.finalproject.flappyfriends.ResourceManager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.align.HorizontalAlign;

import softwarestudio.course.finalproject.flappyfriends.GameActivity;
import softwarestudio.course.finalproject.flappyfriends.Utility;

/**
 * Created by lusa on 2016/06/21.
 */
public class TextManager {

    private Text bestscoreTitleText;
    private Text scoreTitleText;
    private Text bestscoreText;
    private Text scoreText;

    private SimpleBaseGameActivity context;
    private float y_AlignLine = GameActivity.getCameraHeight() * 3 / 2;
    private final static float SCOREBOARD_SCROLL_SPEED = -3.0f;

    public TextManager(
        SimpleBaseGameActivity context,
        Font font
    ) {
        this.context = context;
        bestscoreTitleText = buildText(context, font, "Best Score");
        scoreTitleText = buildText(context, font, "Your Score");
        bestscoreText = buildText(context, font, "              ");
        scoreText = buildText(context, font, "              ");
    }

    public void AttachToScene(Scene scene) {
        if (scene == null) return;
        scene.attachChild(bestscoreTitleText);
        scene.attachChild(bestscoreText);
        scene.attachChild(scoreTitleText);
        scene.attachChild(scoreText);
    }

    public void setScoreBoard(int score) {
        Utility.SetBestScore(context, score);
        setScoreText(score);
        setBestscoreText();
        AllignOnMiddleLine();
        bestscoreTitleText.setY(GameActivity.getCameraHeight() *7 /10);
        bestscoreText.setY(GameActivity.getCameraHeight() *6 /10);
        scoreTitleText.setY(GameActivity.getCameraHeight() *4 /10);
        scoreText.setY(GameActivity.getCameraHeight() *3 /10);
    }

    public void setScoreBoardReady(int score) {
        Utility.SetBestScore(context, score);
        setScoreText(score);
        setBestscoreText();
        AllignOnMiddleLine();
        y_AlignLine = GameActivity.getCameraHeight() * 3 /2;
        bestscoreTitleText.setY(GameActivity.getCameraHeight() *17 /10);
        bestscoreText.setY(GameActivity.getCameraHeight() *16 /10);
        scoreTitleText.setY(GameActivity.getCameraHeight() *14 /10);
        scoreText.setY(GameActivity.getCameraHeight() *13 /10);
    }

    public void AllignOnMiddleLine() {
        float middle = GameActivity.getCameraWidth() / 2;
        bestscoreTitleText.setX(middle);
        bestscoreText.setX(middle);
        scoreTitleText.setX(middle);
        scoreText.setX(middle);
    }

    public void moveScoreBoard() {
        if (y_AlignLine > GameActivity.getCameraHeight() / 2) {
            y_AlignLine += SCOREBOARD_SCROLL_SPEED;
            bestscoreTitleText.setY(
                    bestscoreTitleText.getY() + SCOREBOARD_SCROLL_SPEED
            );
            bestscoreText.setY(
                    bestscoreText.getY() + SCOREBOARD_SCROLL_SPEED
            );
            scoreTitleText.setY(
                    scoreTitleText.getY() + SCOREBOARD_SCROLL_SPEED
            );
            scoreText.setY(
                    scoreText.getY() + SCOREBOARD_SCROLL_SPEED
            );
        }
    }

    public boolean isAboveHalfScreen() {
        if (y_AlignLine > GameActivity.getCameraHeight() / 2)
            return true;
        return false;
    }

    public void setBestscoreText() {
        if (context == null) return;
        int bestscore = Utility.GetBestScore(context);
        if (bestscore >= 0 && bestscore <= Utility.MAX_SCORE)
            bestscoreText.setText(Integer.toString(bestscore));
    }

    public void setScoreText(int score) {
        if (scoreText == null) return;
        if (score >= 0 && score <= Utility.MAX_SCORE)
            scoreText.setText(Integer.toString(score));
    }

    public void setCurrentScoreBoard() {
        bestscoreTitleText.setY(GameActivity.getCameraHeight() *17 /10);
        bestscoreText.setY(GameActivity.getCameraHeight() *16 /10);
        scoreTitleText.setY(GameActivity.getCameraHeight() *14 /10);
        scoreText.setX(GameActivity.getCameraWidth() / 2);
        scoreText.setY(GameActivity.getCameraHeight() *3 /10);
    }

    public void showBestScoreOnly() {
        setBestscoreText();
        AllignOnMiddleLine();
        bestscoreTitleText.setY(GameActivity.getCameraHeight() *4 /10);
        bestscoreText.setY(GameActivity.getCameraHeight() *3 /10);
        scoreTitleText.setY(GameActivity.getCameraHeight() *14 /10);
        scoreText.setY(GameActivity.getCameraHeight() *13 /10);
    }

    public Text buildText(
            SimpleBaseGameActivity context,
            Font font,
            String content
    ) {
        Text text = new Text(
                GameActivity.getCameraWidth() / 2,
                GameActivity.getCameraHeight() * 4 / 3,
                font,
                content,
                new TextOptions(HorizontalAlign.CENTER),
                context.getVertexBufferObjectManager()
        );

        text.setZIndex(1);
        return text;
    }
}
