package softwarestudio.course.finalproject.flappyfriends;

import android.support.v7.widget.LinearLayoutCompat;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;

import softwarestudio.course.finalproject.flappyfriends.Creature.Bird;
import softwarestudio.course.finalproject.flappyfriends.Creature.Pipe;
import softwarestudio.course.finalproject.flappyfriends.Creature.PipePair;
import softwarestudio.course.finalproject.flappyfriends.Receiver.ReceiveDataStorage;
import softwarestudio.course.finalproject.flappyfriends.ResourceManager.AudioManager;
import softwarestudio.course.finalproject.flappyfriends.ResourceManager.BirdManager;
import softwarestudio.course.finalproject.flappyfriends.ResourceManager.FontManager;
import softwarestudio.course.finalproject.flappyfriends.ResourceManager.ImageManager;
import softwarestudio.course.finalproject.flappyfriends.ResourceManager.PipeManager;
import softwarestudio.course.finalproject.flappyfriends.ResourceManager.SceneManager;
import softwarestudio.course.finalproject.flappyfriends.ResourceManager.SoundManager;
import softwarestudio.course.finalproject.flappyfriends.ResourceManager.TextManager;

/**
 * Created by lusa on 2016/06/18.
 */
public class GameActivity extends SimpleBaseGameActivity {

    private static float CAMERA_HEIGHT = 800;
    private static float CAMERA_WIDTH = 485;

    private Camera mCamera = null;

    private SoundManager mSoundManager = null;
    private AudioManager mAudioManager = null;
    private FontManager mFontManager = null;
    private TextManager mTextManager = null;
    private ImageManager mImageManager = null;
    private PipeManager mPipeManager = null;
    private BirdManager mBirdManager = null;

    private SceneManager mSceneManager = null;
    private Scene mScene = null;
    private Background mBackGround = null;

    private TimerHandler mTimerHandler = null;

    private static final float SCROLL_SPEED = 4.5f;	// game speed

    private float mCurrentWorldPosition = 0;

    @Override
    protected void onCreateResources() throws IOException {
        CAMERA_HEIGHT = 800;
        CAMERA_WIDTH = Utility.calculateScreenWidth(this, CAMERA_HEIGHT);

        //mSoundManager = new SoundManager(this);
        mAudioManager = new AudioManager(this);
        mFontManager = new FontManager(this);
        mTextManager = new TextManager(this, mFontManager.getFont());
        mImageManager = new ImageManager(this);
        mPipeManager = new PipeManager(this, mImageManager, Utility.MAX_PIPEPAIRS);
        mBirdManager = new BirdManager(
                this,
                mImageManager,
                ReceiveDataStorage.getPlayerNum()
        );
    }

    @Override
    protected Scene onCreateScene() {

        mBackGround = new ParallaxBackground(82/255f, 190/255f, 206/255f) {

            private float prevX = 0;
            private float parallaxValueOffset = 0;

            @Override
            public void onUpdate(float pSecondsElapsed) {
                switch(ReceiveDataStorage.getGameState()){
                    case Utility.GAMESTATE_ONOPERATE:
                        final float cameraCurrentX = mCurrentWorldPosition;//mCamera.getCenterX();

                        if (prevX != cameraCurrentX) {

                            parallaxValueOffset +=  cameraCurrentX - prevX;
                            this.setParallaxValue(parallaxValueOffset);
                            prevX = cameraCurrentX;
                        }
                        break;
                    default: break;
                }

                super.onUpdate(pSecondsElapsed);
            }
        };

        mSceneManager = new SceneManager(
                this,
                mFontManager,
                mImageManager,
                (ParallaxBackground) mBackGround
        );
        mScene = mSceneManager.buildScene();
        mPipeManager.AttachToScene(mScene);
        mBirdManager.AttachToScene(mScene);
        mTextManager.AttachToScene(mScene);
        mScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.isActionDown()) {
                    switch (ReceiveDataStorage.getGameState()) {
                        case Utility.GAMESTATE_ONIDLE:
                            if (ReceiveDataStorage.getPlayerLabel() == Utility.TARGET_HOST) {
                                ReceiveDataStorage.setGameActivation(true);
                                ReceiveDataStorage.setGameState(Utility.GAMESTATE_ONPREPARE);
                            }
                            break;
                        case Utility.GAMESTATE_ONOPERATE:
                            /*
                            if (ReceiveDataStorage.getGameActivation()) {
                                mBirdManager.SendCommand();
                                mAudioManager.playAudio(
                                        AudioManager.AUDIOLABEL_JUMP
                                );
                            }
                            */
                            mBirdManager.SendCommand();
                            mAudioManager.playAudio(
                                    AudioManager.AUDIOLABEL_JUMP);
                            break;
                        case Utility.GAMESTATE_ONSTOP:
                            /*
                            if (ReceiveDataStorage.getPlayerLabel() == Utility.TARGET_HOST)
                                ReceiveDataStorage.setGameState(Utility.GAMESTATE_ONIDLE);
                            */
                            break;
                        default: break;
                    }
                }
                return false;
            }
        });
        return mScene;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        CAMERA_HEIGHT = 800;
        CAMERA_WIDTH = Utility.calculateScreenWidth(this, CAMERA_HEIGHT);
        mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT) {

            @Override
            public void onUpdate(float pSecondsElapsed) {
                switch (ReceiveDataStorage.getGameState()) {
                    case Utility.GAMESTATE_ONIDLE:
                        onIdle();
                        break;
                    case Utility.GAMESTATE_ONPREPARE:
                        onPrepare();
                        break;
                    case Utility.GAMESTATE_ONOPERATE:
                        onOperate();
                        break;
                    case Utility.GAMESTATE_ONSTOP:
                        onStop();
                        break;
                    default:
                        ReceiveDataStorage
                                .setGameState(Utility.GAMESTATE_ONIDLE);
                        break;
                }
                super.onUpdate(pSecondsElapsed);
            }


            private void onIdle() {
                ReceiveDataStorage.setMyscoreZero();
                mTextManager.showBestScoreBoard();
                mPipeManager.setReadyPosition();
                mBirdManager.setAtVerticalMiddle();
            }

            private void onPrepare() {
                mBirdManager.setReadyPosition();
                // if only one player(non-multi-player mode)
                // game starts as screen touched
                // else starts after 3s
                if (ReceiveDataStorage.getPlayerLabel() == Utility.TARGET_HOST) {
                    ReceiveDataStorage.setGameState(Utility.GAMESTATE_ONOPERATE);
                }
            }

            private void onOperate() {
                mAudioManager.playAudio(AudioManager.AUDIOLABEL_BGM);

                mTextManager.showCountingScoreBoard();

                mCurrentWorldPosition -= SCROLL_SPEED;

                mPipeManager.receiveCommand();

                mBirdManager.FetchCommand();

                if (mBirdManager.checkSelfBirdPassPipePair(mPipeManager)) {
                    ReceiveDataStorage.MyscoreIncremnet();
                    mAudioManager.playAudio(AudioManager.AUDIOLABEL_SCOREUP);
                }
                if (mBirdManager.checkSelfBirdCollision(mPipeManager)) {
                    ReceiveDataStorage.setGameActivation(false);
                    mAudioManager.playAudio(AudioManager.AUDIOLABEL_GAMEOVER);
                }
                if (!ReceiveDataStorage.getGameActivation()
                        && ReceiveDataStorage.getPlayerLabel() == Utility.TARGET_HOST) {
                    ReceiveDataStorage.setGameState(Utility.GAMESTATE_ONSTOP);
                    mTextManager.setFullScoreBoardReady();
                    //mPipeManager.setReadyPosition();
                }
            }

            private void onStop() {
                mAudioManager.pauseAudio(AudioManager.AUDIOLABEL_BGM);

                // let the bird falls down to the floor
                mBirdManager.FetchCommand();

                if (ReceiveDataStorage.getConnection()) {

                } else {
                    if (!mTextManager.isAboveHorizontalMiddle()) {
                        mTimerHandler = new TimerHandler(
                                1.6f,
                                false,
                                new ITimerCallback() {
                                    @Override
                                    public void onTimePassed(TimerHandler pTimerHandler) {
                                        //mTextManager.setScoreBoard(curScore);
                                        ReceiveDataStorage.setGameState(Utility.GAMESTATE_ONIDLE);
                                        mScene.unregisterUpdateHandler(mTimerHandler);
                                    }
                                }
                        );
                        mScene.registerUpdateHandler(mTimerHandler);
                    } else {
                        mTextManager.FullScoreBoardDescendAnimation();
                    }
                }
            }
        };

        EngineOptions engineOptions = new EngineOptions(
                true,
                ScreenOrientation.PORTRAIT_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
                mCamera
        );

        engineOptions.getAudioOptions().setNeedsSound(true);
        engineOptions.getAudioOptions().setNeedsMusic(true);

        return engineOptions;
    }

    @Override
    public void onPause() {
        super.onPause();
        mAudioManager.pauseAudio(AudioManager.AUDIOLABEL_BGM);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static float getCameraHeight() {
        return CAMERA_HEIGHT;
    }

    public static float getCameraWidth() {
        return CAMERA_WIDTH;
    }
}
