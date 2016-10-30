package game.shay.scenes;

import game.shay.manager.ResourcesManager;
import game.shay.manager.SceneManager.SceneType;

import org.andengine.entity.scene.Scene;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;


public abstract class BaseScene extends Scene{
	
	protected Engine engine;
    protected BaseGameActivity activity;
    protected ResourcesManager resourcesManager;
    protected VertexBufferObjectManager vbom;
    protected Camera camera;
    
    public BaseScene()
    {
        this.resourcesManager = ResourcesManager.getInstance();
        this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        createScene();
    }
    
    public abstract void createScene();
    public abstract void onBackKeyPressed();    
    public abstract SceneType getSceneType();
    public abstract void disposeScene();
}
