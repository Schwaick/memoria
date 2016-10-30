package game.shay.scenes;

import game.shay.manager.ResourcesManager;
import game.shay.manager.SceneManager;
import game.shay.manager.SceneManager.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;


public class FinalScene extends BaseScene {

	private int cron = 1;
	
	private int difficultyID;
	private int maxTouches;
	private float interval;
	
	@Override
	public void createScene() {
		createBackground();
		createText();
		createButtons();
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_FINAL;
	}

	@Override
	public void disposeScene() {
		
	}

	private void createBackground() {
		attachChild(new Sprite(400, 240, resourcesManager.popup_region, vbom) {

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

		});
	}

	private void createText() {
		//Gambiarra pra funcionar
		int message = SceneManager.getInstance().message;
		Text text = new Text(400, 400, ResourcesManager.getInstance().font,(message==1)? "Você Acertou" : "Você Errou" , vbom);
		text.setColor(message==1?0f:1f, message==1?1f:0f, 0f);
    	attachChild(text);
	}

	private void createButtons() {
		new Thread(new Runnable() {
    		public void run() {
    			engine.registerUpdateHandler(new TimerHandler(0.4f, true, new ITimerCallback() 
    			{
    				public void onTimePassed(final TimerHandler pTimerHandler) 
    				{
    					cron--;
    					if(cron == 0) {
    						engine.unregisterUpdateHandler(pTimerHandler);
    					} 

    					pTimerHandler.reset();
    				}
    			}));
    		}
    	}).start();
    	
    	TiledSprite play = new TiledSprite(400, 275, ResourcesManager.getInstance().play_region, vbom){
    		@Override
    		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
    			switch(pAreaTouchEvent.getAction()){
    			case TouchEvent.ACTION_DOWN:
    				if(cron == 0)
    					this.setCurrentTileIndex(1);
    				break;
    			case TouchEvent.ACTION_UP:
    				if(cron == 0) {
    					this.setCurrentTileIndex(0);
    					SceneManager.getInstance().loadGameScene(engine, difficultyID, interval, maxTouches);
    				}
    				break;
    			}
    			return true;
    		}
    	};
    	
    	TiledSprite menu = new TiledSprite(400, 165, ResourcesManager.getInstance().menu_region, vbom){
    		@Override
    		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
    			switch(pAreaTouchEvent.getAction()){
    			case TouchEvent.ACTION_DOWN:
    				if(cron == 0)
    					this.setCurrentTileIndex(1);
    				break;
    			case TouchEvent.ACTION_UP:
    				if(cron == 0) {
	    				this.setCurrentTileIndex(0);
	    				SceneManager.getInstance().loadMenuScene(engine);
    				}
    				break;
    			}
    			return true;
    		}
    	};
    	
    	registerTouchArea(play);
    	registerTouchArea(menu);
    	
    	attachChild(play);
    	attachChild(menu);
	}

	public int getDifficultyID() {
		return difficultyID;
	}

	public void setDifficultyID(int difficultyID) {
		this.difficultyID = difficultyID;
	}

	public int getMaxTouches() {
		return maxTouches;
	}

	public void setMaxTouches(int maxTouches) {
		this.maxTouches = maxTouches;
	}

	public float getInterval() {
		return interval;
	}

	public void setInterval(float interval) {
		this.interval = interval;
	}
	
}
