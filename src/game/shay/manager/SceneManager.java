package game.shay.manager;

import game.shay.scenes.BaseScene;
import game.shay.scenes.FinalScene;
import game.shay.scenes.GameScene;
import game.shay.scenes.LoadingScene;
import game.shay.scenes.MainMenuScene;
import game.shay.scenes.SplashScene;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;


public class SceneManager {

	private static SceneManager instance;
	
	//---------------------------------------------
    // SCENES
    //---------------------------------------------
	
	private SplashScene splashScene;
    private MainMenuScene menuScene;
    private GameScene gameScene;
    private LoadingScene loadingScene;
    private FinalScene finalScene;
    
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    private BaseScene currentScene;
    private Engine engine = ResourcesManager.getInstance().engine;
    private float loadingTime = 1.5f;
    
    public int message = 0;
    
    public enum SceneType
    {
        SCENE_SPLASH, SCENE_MENU, SCENE_GAME, SCENE_LOADING, SCENE_FINAL 
    }
    
    //---------------------------------------------
    // SINGLETON
    //---------------------------------------------
    
    public static SceneManager getInstance()
    {
    	if(instance == null) {
    		instance = new SceneManager();
    	}
        return instance;
    }
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    public void setScene(BaseScene scene)
    {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    
    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
	        case SCENE_SPLASH:
	            setScene(splashScene);
	            break;
	        case SCENE_MENU:
	            setScene(menuScene);
	            break;
	        case SCENE_GAME:
	            setScene(gameScene);
	            break;
	        case SCENE_LOADING:
	            setScene(loadingScene);
	            break;
	        case SCENE_FINAL:
	            setScene(finalScene);
	            break;
            default:
                break;
        }
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    
    private void disposeSplashScene()
    {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }
    
    public void createMenuScene()
    {
    	ResourcesManager.getInstance().loadMenuResources();
    	ResourcesManager.getInstance().loadGameResources();
    	ResourcesManager.getInstance().loadFinalResources();
    	
    	menuScene = new MainMenuScene();
    	loadingScene = new LoadingScene();
    	
    	currentScene = menuScene;
        setScene(menuScene);
        disposeSplashScene();
    }
    
    public void loadMenuScene(final Engine mEngine) {
    	setScene(loadingScene);
    	gameScene.disposeScene();
    	
    	mEngine.registerUpdateHandler(new TimerHandler(loadingTime, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                menuScene = new MainMenuScene();
                currentScene = menuScene;
                setScene(menuScene);
            }
        }));
    	
    }

    public void loadGameScene(final Engine mEngine, final int difficultyID, final float interval, final int maxTouches) {
    	setScene(loadingScene);
    	//menuScene.disposeScene();
    	
    	mEngine.registerUpdateHandler(new TimerHandler(loadingTime, new ITimerCallback()  {
            public void onTimePassed(final TimerHandler pTimerHandler)  {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                gameScene = new GameScene();
                gameScene.setDifficultyID(difficultyID);
                gameScene.setInterval(interval);
                gameScene.setMaxTouches(maxTouches);
            	currentScene = gameScene;
                setScene(gameScene); 
            }
        }));	
        
    }
    
    public void loadFinalScene(final int difficultyID, final float interval, final int maxTouches, int message) {
    	this.message = message;
    	setScene(loadingScene);
    	gameScene.disposeScene();

    	finalScene = new FinalScene();
    	finalScene.setDifficultyID(difficultyID);
    	finalScene.setInterval(interval);
    	finalScene.setMaxTouches(maxTouches);
    	currentScene = finalScene;
    	setScene(finalScene); 

    }
    
    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }
    
    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
	
}
