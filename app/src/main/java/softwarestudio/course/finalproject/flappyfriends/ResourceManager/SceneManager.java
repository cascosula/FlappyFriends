package softwarestudio.course.finalproject.flappyfriends.ResourceManager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by lusa on 2016/06/18.
 * Scene generation
 * Attach back ground to scene
 * No others functionality
 */
public class SceneManager {

    private static SimpleBaseGameActivity mContext = null;
    private static FontManager mFontManager = null;
    private static ImageManager mImgaeManager = null;
    private static ParallaxBackground mParallaxBackground = null;


    public SceneManager(
            SimpleBaseGameActivity context,
            FontManager fontManager,
            ImageManager imageManager,
            ParallaxBackground parallaxBackground
    ) {
        mContext = context;
        mFontManager = fontManager;
        mImgaeManager = imageManager;
        mParallaxBackground = parallaxBackground;
    }

    public Scene buildScene() {

        Scene scene = new Scene();

        mImgaeManager.ceterSprite(mImgaeManager.getBackGroundSprite());
        mParallaxBackground.attachParallaxEntity(
                new ParallaxBackground.ParallaxEntity(1, mImgaeManager.getBackGroundSprite()));
        scene.setBackground(mParallaxBackground);
        scene.setBackgroundEnabled(true);

        return scene;
    }
}
