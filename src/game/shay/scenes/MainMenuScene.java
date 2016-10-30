package game.shay.scenes;

import game.shay.database.DBHelper;
import game.shay.manager.ResourcesManager;
import game.shay.manager.SceneManager;
import game.shay.manager.SceneManager.SceneType;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;
import org.andengine.engine.camera.Camera;
import org.andengine.input.touch.TouchEvent;


public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menuChildScene;
	private Sprite background;
	private TiledSprite sound;
	private IMenuItem veryeasyMenuItem, easyMenuItem, mediumMenuItem, hardMenuItem, veryhardMenuItem, howtoplayMenuItem;
	private final int MENU_VERYEASY = 1, MENU_EASY = 2,MENU_MEDIUM = 3, MENU_HARD = 4, MENU_VERYHARD = 5, MENU_HOWTOPLAY = 6;
	
	@Override
	public void createScene() {
		createBackground();
		createSound();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		menuChildScene.dispose();
		menuChildScene = null;
	}
	
	private void createBackground() {
		background = new Sprite(400, 240, resourcesManager.menuBackground_region, vbom) {

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

		};
		
		attachChild(background);
	}
	
	private void createSound() {
		sound = new TiledSprite(400, 355, ResourcesManager.getInstance().sound_region, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					int index = this.getCurrentTileIndex() == 0 ? 1 : 0;
					this.setCurrentTileIndex(index);
					updateSound(index);
				}
				return true;
			}
		};
		
		if(isSoundEnabled()) {
			sound.setCurrentTileIndex(0);
		} else {
			sound.setCurrentTileIndex(1);
		}
		
		attachChild(sound);
		registerTouchArea(sound);
	}
	
	private void createMenuChildScene() {
		menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(400, 240);
	    
	    veryeasyMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_VERYEASY, resourcesManager.veryeasy_region, vbom), 1.1f, 1);
	    easyMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_EASY, resourcesManager.easy_region, vbom), 1.1f, 1);
	    mediumMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_MEDIUM, resourcesManager.medium_region, vbom), 1.1f, 1);
	    hardMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_HARD, resourcesManager.hard_region, vbom), 1.1f, 1);
	    veryhardMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_VERYHARD, resourcesManager.veryhard_region, vbom), 1.1f, 1);
	    howtoplayMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_HOWTOPLAY, resourcesManager.howtoplay_region, vbom), 1.1f, 1);
	    
	    attachText();
	    
	    menuChildScene.addMenuItem(veryeasyMenuItem);
	    menuChildScene.addMenuItem(easyMenuItem);
	    menuChildScene.addMenuItem(mediumMenuItem);
	    menuChildScene.addMenuItem(hardMenuItem);
	    menuChildScene.addMenuItem(veryhardMenuItem);
	    menuChildScene.addMenuItem(howtoplayMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	    veryeasyMenuItem.setPosition(-258, 15);
	    easyMenuItem.setPosition(-258, -120);
	    mediumMenuItem.setPosition(0, 15);
	    hardMenuItem.setPosition(0, -120);
	    veryhardMenuItem.setPosition(258, 15);
	    howtoplayMenuItem.setPosition(258, -94);
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	}
	
	private void attachText() {
		int formatNumber;
		Text text;
		
		formatNumber = getFormattedNumber(1);
		text = new Text(127, 34, resourcesManager.font2, "Acertos:"+formatNumber+"%", vbom);
		text.setColor(formatNumber>=70?0.13f:0.85f, formatNumber>=70?0.54f:0f, formatNumber>=70?0.13f:0f);
		veryeasyMenuItem.attachChild(text);
		
		formatNumber = getFormattedNumber(2);
		text = new Text(127, 34, resourcesManager.font2, "Acertos:"+formatNumber+"%", vbom);
		text.setColor(formatNumber>=70?0.13f:0.85f, formatNumber>=70?0.54f:0f, formatNumber>=70?0.13f:0f);
	    easyMenuItem.attachChild(text);
	    
		formatNumber = getFormattedNumber(3);
		text = new Text(127, 34, resourcesManager.font2, "Acertos:"+formatNumber+"%", vbom);
		text.setColor(formatNumber>=70?0.13f:0.85f, formatNumber>=70?0.54f:0f, formatNumber>=70?0.13f:0f);
	    mediumMenuItem.attachChild(text);
	    
		formatNumber = getFormattedNumber(4);
		text = new Text(127, 34, resourcesManager.font2, "Acertos:"+formatNumber+"%", vbom);
		text.setColor(formatNumber>=70?0.13f:0.85f, formatNumber>=70?0.54f:0f, formatNumber>=70?0.13f:0f);
	    hardMenuItem.attachChild(text);
	    
		formatNumber = getFormattedNumber(5);
		text = new Text(127, 34, resourcesManager.font2, "Acertos:"+formatNumber+"%", vbom);
		text.setColor(formatNumber>=70?0.13f:0.85f, formatNumber>=70?0.54f:0f, formatNumber>=70?0.13f:0f);
	    veryhardMenuItem.attachChild(text);
	}
	
	private int getFormattedNumber(int id) {
		float formatNumber;
		float total;
		float hits;
		
		total = (float) getTotal(id);
		hits = (float) getHits(id);
		
		if(total != 0 && hits != 0 && hits != total)
			formatNumber = (hits/total) * 100;
		else if(total != 0 && hits != 0 && hits == total)
			formatNumber = 100;
		else
			formatNumber = 0;	
		
		return (int) formatNumber;
	}
	
	private void updateSound(int currentTileIndex) {
		DBHelper dbHelper = new DBHelper(activity);
		dbHelper.updateSound(currentTileIndex);
	}
	
	private boolean isSoundEnabled() {
		DBHelper dbHelper = new DBHelper(activity);
		return dbHelper.isSoundEnabled();
	}
	
	private int getHits(int id) {
		DBHelper dbHelper = new DBHelper(activity);
		return dbHelper.getHits(id);
	}
	
	private int getTotal(int id) {
		DBHelper dbHelper = new DBHelper(activity);
		return dbHelper.getTotal(id);
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		
		switch(pMenuItem.getID()) {
			case MENU_VERYEASY:
				SceneManager.getInstance().loadGameScene(engine, MENU_VERYEASY, 2f, 2); //Computador toca 3 vezes
				return true;
			case MENU_EASY:
	            SceneManager.getInstance().loadGameScene(engine, MENU_EASY, 1.5f, 4); //Computador toca 5 vezes
				return true;
			case MENU_MEDIUM:
				SceneManager.getInstance().loadGameScene(engine, MENU_MEDIUM, 1f, 6); //Computador toca 7 vezes
				return true;
			case MENU_HARD:
				SceneManager.getInstance().loadGameScene(engine, MENU_HARD, 0.8f, 8); //Computador toca 9 vezes
				return true;
			case MENU_VERYHARD:
				SceneManager.getInstance().loadGameScene(engine, MENU_VERYHARD, 0.7f, 10); //Computador toca 11 vezes
				return true;
			case MENU_HOWTOPLAY:
				final Sprite popup = new Sprite(400, 240, resourcesManager.howtoplay_popup_region, vbom);
				final Sprite close = new Sprite(600, 352, resourcesManager.close_region, vbom) {
					@Override
					public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
						unregisterTouchArea(this);
						popup.detachSelf();
						popup.dispose();
						
						//background.setAlpha(1f);
						createMenuChildScene();
						registerTouchArea(sound);
						
						return true;
					}
				};
				
				menuChildScene.closeMenuScene();
				
				unregisterTouchArea(sound);
				//background.setAlpha(0f);
				
				registerTouchArea(close);
				popup.attachChild(close);
				attachChild(popup);
				return true;
				
			default:
				return false;
			
		}
	}

}
