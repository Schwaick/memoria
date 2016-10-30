package game.shay.scenes;

import game.shay.database.DBHelper;
import game.shay.manager.ResourcesManager;
import game.shay.manager.SceneManager;
import game.shay.manager.SceneManager.SceneType;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.align.HorizontalAlign;


public class GameScene extends BaseScene {
	
	private HUD gameHUD;
	private Text state;
	private Text touch;
	private TiledSprite button1, button2, button3, button4, button5, button6, button7, button8;

	private boolean start = false;
	private boolean waitExecuted = false;
	private boolean goExecuted = false;
	private boolean permission = false;
	
	//Controladores dificuldade, velocidade e contador de toques
	private int difficultyID;
	private int maxTouches;
	private float interval;

	//Controladores delay do start();
	private boolean delayExecuted = false;
	private int cron = 2;
	
	//Número de toques, e armazenamento de toques
	private int computerCounter = 0;
	private int playerCounter = 0;
	private ArrayList<Integer> computerTouches = new ArrayList<Integer>();
	private ArrayList<Integer> playerTouches = new ArrayList<Integer>();

	@Override
	public void createScene() {		
		createBackground();
		createButtons();
		createHUD();
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		listenState();
		start();
		listenTouches();
	}
	
	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		gameHUD = null;
		state = null;
	}
	
	private void createBackground() {
		attachChild(new Sprite(400, 240, resourcesManager.gameBackground_region, vbom) {

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

		});
	}
	
	private void createButtons() {
		button1 = getTiledSprite(110, 280, ResourcesManager.getInstance().button4_region, resourcesManager.sound1, 1);
		button2 = getTiledSprite(310, 280, ResourcesManager.getInstance().button8_region, resourcesManager.sound2, 2);
		button3 = getTiledSprite(510, 280, ResourcesManager.getInstance().button2_region, resourcesManager.sound3, 3);
		button4 = getTiledSprite(710, 280, ResourcesManager.getInstance().button5_region, resourcesManager.sound4, 4);
		button5 = getTiledSprite(110, 130, ResourcesManager.getInstance().button3_region, resourcesManager.sound5, 5);
		button6 = getTiledSprite(310, 130, ResourcesManager.getInstance().button6_region, resourcesManager.sound6, 6);
		button7 = getTiledSprite(510, 130, ResourcesManager.getInstance().button7_region, resourcesManager.sound7, 7);
		button8 = getTiledSprite(710, 130, ResourcesManager.getInstance().button1_region, resourcesManager.sound8, 8);

		attachChild(button1);
		attachChild(button2);
		attachChild(button3);
		attachChild(button4);
		attachChild(button5);
		attachChild(button6);
		attachChild(button7);
		attachChild(button8);
		
		registerTouchArea(button1);
		registerTouchArea(button2);
		registerTouchArea(button3);
		registerTouchArea(button4);
		registerTouchArea(button5);
		registerTouchArea(button6);
		registerTouchArea(button7);
		registerTouchArea(button8);
	}
	
	private TiledSprite getTiledSprite(float pX, float pY, ITiledTextureRegion region, final Sound sound, final int value) {
		TiledSprite sprite = new TiledSprite(pX, pY, region, vbom) {
			@Override
    		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
				
    			switch(pAreaTouchEvent.getAction()){
	    			case TouchEvent.ACTION_DOWN:
	    				if(computerCounter > maxTouches && start) {
	    					if(pTouchAreaLocalX < (this.getWidth()/2)-5 + 60 && pTouchAreaLocalX > (this.getWidth()/2)-5 - 60 
	    							&& pTouchAreaLocalY < this.getHeight()/2 + 60 && pTouchAreaLocalY > this.getHeight()/2 - 60) {
	    						
		    					this.setCurrentTileIndex(1);
		    					if(isSoundEnabled())
		    						sound.play();
		    					
			    				playerTouches.add(value);
				    			playerCounter++;
		    					
			    				addTouch();
	    					}
	
	    				}
	    				break;
	    			case TouchEvent.ACTION_UP:
	    				if(computerCounter > maxTouches) {
		    				this.setCurrentTileIndex(0);
		    				
		    				if(playerCounter == computerCounter)
		    					permission = true;
	    				}
	    				break;
	    			case TouchEvent.ACTION_MOVE:
	    				if(this.getCurrentTileIndex() == 1) {
	    					if(pTouchAreaLocalX > (this.getWidth()/2)-5 + 60 || pTouchAreaLocalX < (this.getWidth()/2)-5 - 60 
	    							|| pTouchAreaLocalY > this.getHeight()/2 + 60 || pTouchAreaLocalY < this.getHeight()/2 - 60) {
	    						
	    						this.setCurrentTileIndex(0);
	    						//playerTouches.add(value);
	    	    				//playerCounter++;
	    					}
	    				}
	    				break;
    			}
    			
    			return true;
    		}
		};
		
		return sprite;
	}
	
	private void createHUD() {
		
		gameHUD = new HUD();
		
		state = new Text(400,400,resourcesManager.font,"Espere..",new TextOptions(HorizontalAlign.LEFT),vbom);
		gameHUD.attachChild(state);
		
		touch = new Text(700,400,resourcesManager.font,"0123456789/0123456789",new TextOptions(HorizontalAlign.LEFT),vbom);
		touch.setText("0/0");
		gameHUD.attachChild(touch);
		
		camera.setHUD(gameHUD);

	}
	
	private void listenState() {
		
		LoopEntityModifier blinkModifier = new LoopEntityModifier(
				new SequenceEntityModifier(new FadeOutModifier(0.5f), new FadeInModifier(0.5f)));
		
		if(computerCounter <= maxTouches && waitExecuted == false) {
			state.registerEntityModifier(blinkModifier);
			state.setText("Espere..");
			state.setColor(1f, 0, 0);
			waitExecuted = true;
		} 
		
		if(computerCounter > maxTouches && goExecuted == false && start) {
			state.unregisterEntityModifier(blinkModifier);
			state.setText("Vai!!");
			state.setColor(0,1f,0,1f);
			state.setIgnoreUpdate(true); //Problema aqui
			goExecuted = true;
		}
	}
	
	private void start() {
		if(delayExecuted == false) {
			delayExecuted = true;
			engine.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {
				public void onTimePassed(final TimerHandler pTimerHandler) {
					cron--;
					if(cron == 0) {
						start = true;
						engine.unregisterUpdateHandler(pTimerHandler);
					} 
					
					pTimerHandler.reset();
				}
			}));
		}
		
		if(start && computerCounter <= maxTouches && delayExecuted) {
			Random rand = new Random();
			int randNumber = rand.nextInt(8) + 1;
			switch (randNumber) {
			case 1:
				touchButton(button1, resourcesManager.sound1, 1);
				break;
			case 2:
				touchButton(button2, resourcesManager.sound2, 2);
				break;
			case 3:
				touchButton(button3, resourcesManager.sound3, 3);
				break;
			case 4:
				touchButton(button4, resourcesManager.sound4, 4);
				break;
			case 5:
				touchButton(button5, resourcesManager.sound5, 5);
				break;
			case 6:
				touchButton(button6, resourcesManager.sound6, 6);
				break;
			case 7:
				touchButton(button7, resourcesManager.sound7, 7);
				break;
			case 8:
				touchButton(button8, resourcesManager.sound8, 8);
				break;
				
			default:
				break;
			}
			
		}
	}

	private void touchButton(final TiledSprite sprite, Sound sound, final int value) {
		start = false;
		sprite.setCurrentTileIndex(1);
		
		if(isSoundEnabled())
			sound.play();
		
		computerTouches.add(value);
		computerCounter++;
		addTouch();
		
		engine.registerUpdateHandler(new TimerHandler(interval, new ITimerCallback() {
			public void onTimePassed(final TimerHandler pTimerHandler) {
				engine.unregisterUpdateHandler(pTimerHandler);
				sprite.setCurrentTileIndex(0);
				
				start = true;
			}
		}));
	}
	
	private void listenTouches() {
		//É 5 porque, quando computerCounter=4, computerCounter++ será executado. Depois disso computerCounter será sempre 5.
		if(playerCounter == computerCounter && permission) {
			int message;
			
			if(arrayListCompare(playerTouches, computerTouches)) {
				message = 1;
				addHits(difficultyID);
			} else {
				message = 0;
			}
			
			addTotal(difficultyID);

			SceneManager.getInstance().loadFinalScene(getDifficultyID(), getInterval(), getMaxTouches(), message);
			
		}
	}
	
	private boolean arrayListCompare(ArrayList<Integer> arrayA, ArrayList<Integer> arrayB) {
		for(int i=0;i<arrayB.size();i++) {
			if(arrayA.get(i) != arrayB.get(i)) {
				return false;
			}
		}
		return true;
	}
	
	private void addTouch() {
		touch.setText(playerCounter+"/"+computerCounter);
	}
	
	private boolean isSoundEnabled() {
		DBHelper dbHelper = new DBHelper(activity);
		return dbHelper.isSoundEnabled();
	}

	private void addHits(int id) {
		DBHelper dbHelper = new DBHelper(activity);
		dbHelper.addHits(id);
	}

	private void addTotal(int id) {
		DBHelper dbHelper = new DBHelper(activity);
		dbHelper.addTotal(id);
	}

	public int getDifficultyID() {
		return difficultyID;
	}

	public void setDifficultyID(int difficultyID) {
		this.difficultyID = difficultyID;
	}

	public float getInterval() {
		return interval;
	}

	public void setInterval(float interval) {
		this.interval = interval;
	}

	public int getMaxTouches() {
		return maxTouches;
	}

	public void setMaxTouches(int maxTouches) {
		this.maxTouches = maxTouches;
	}
	
}
