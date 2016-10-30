package game.shay.activity;

import game.shay.manager.ResourcesManager;
import game.shay.manager.SceneManager;
import game.shay.mestresdamemoria.R;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.LayoutGameActivity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class GameActivity extends LayoutGameActivity{

	private static final int WIDTH = 800;
	private static final int HEIGHT = 480;
	
	private Camera camera;
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
	    
		adView = (AdView) this.findViewById(R.id.adViewId);

		AdRequest adRequest = new AdRequest.Builder()
		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		.build();

		adView.loadAd(adRequest);
 
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, WIDTH, HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(WIDTH, HEIGHT), this.camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
	    pOnCreateResourcesCallback.onCreateResourcesFinished();
	    
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
		
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	            public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	            	mEngine.unregisterUpdateHandler(pTimerHandler);
	                SceneManager.getInstance().createMenuScene();
	               
	            }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}
	
	@Override
	protected synchronized void onResume() {
		super.onResume();
		adView.resume();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		adView.pause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		adView.destroy();
	    System.exit(0);	
	}
	
	@Override
	protected int getLayoutID() {
		return R.layout.game_layout;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.SurfaceViewId;
	}
	
}	