package game.shay.manager;

import game.shay.activity.GameActivity;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;


import android.graphics.Color;

public class ResourcesManager {
	
	private static ResourcesManager instance;
	
	public Engine engine;
    public GameActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;
    public Font font;
    public Font font2;
    
    private BitmapTextureAtlas splashTextureAtlas;
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    private BuildableBitmapTextureAtlas gameTextureAtlas;
    private BuildableBitmapTextureAtlas finalTextureAtlas;
    
    //Splash
    public ITextureRegion splash_region;
    //Menu
    public ITextureRegion menuBackground_region;
    public ITextureRegion veryeasy_region, easy_region, medium_region, hard_region, veryhard_region, howtoplay_region;
    public ITiledTextureRegion sound_region;
    public ITextureRegion howtoplay_popup_region;
    public ITextureRegion close_region;
    //Game
    public ITextureRegion gameBackground_region;
    public ITextureRegion popup_region;
    public ITiledTextureRegion menu_region;
    public ITiledTextureRegion play_region;
    public ITiledTextureRegion button1_region,button2_region,button3_region,button4_region,button5_region,button6_region,button7_region,button8_region;
    public Sound sound1,sound2,sound3,sound4,sound5,sound6,sound7,sound8;
    
	public static ResourcesManager getInstance() {
		if(instance == null) {
			instance = new ResourcesManager();
		}
        return instance;
    }
	
	public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom) {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
	
    public void loadMenuResources() {
    	loadMenuGraphics();	
    }
    
    public void loadGameResources() {
    	loadGameFonts();
    	loadGameSounds();
        loadGameGraphics();
    }
    
    public void loadFinalResources() {
    	loadFinalGraphics();
    }
    
    private void loadMenuGraphics() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1800, 1800, TextureOptions.BILINEAR);
    	
    	menuBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.jpg");
    	veryeasy_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "veryeasy.png");
    	easy_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "easy.png");
    	medium_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "medium.png");
    	hard_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "hard.png");
    	veryhard_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "veryhard.png");
    	howtoplay_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "howtoplay.png");
    	sound_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(menuTextureAtlas, activity, "sound.png", 2, 1);
    	howtoplay_popup_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "howtoplay_popup.jpg");
    	close_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "close.png");
    	
    	try 
    	{
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.menuTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    		Debug.e(e);
    	}
    }
    
    private void loadGameFonts() {
    	FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 526, 526, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        final ITexture mainFontTexture2 = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "RAVIE.TTF", 48, true, Color.WHITE, 2, Color.BLACK);
        font2 = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture2, activity.getAssets(), "ARIAL.TTF", 33, true, Color.WHITE, 2, Color.WHITE);
        
        try {
        	font.load();
        	font2.load();
        } catch (final Exception e) {
        	Debug.e(e);
        }
       
        
    }
    
    private void loadGameSounds() {
    	SoundFactory.setAssetBasePath("mfx/");
    	try  {
    		sound1 = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "sound1.ogg");
    		sound2 = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "sound2.ogg");
    		sound3 = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "sound3.ogg");
    		sound4 = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "sound4.ogg");
    		sound5 = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "sound5.ogg");
    		sound6 = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "sound6.ogg");
    		sound7 = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "sound7.ogg");
    		sound8 = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "sound8.ogg");
        } 
        catch (final IOException e) {
        	Debug.e(e);
        }
    }
    
    private void loadGameGraphics() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
    	gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1500, 1500, TextureOptions.BILINEAR);
    	
    	gameBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "game_background.jpg");

    	button1_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "TiledButton1.png", 2, 1);
		button2_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "TiledButton2.png", 2, 1);
		button3_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "TiledButton3.png", 2, 1);
		button4_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "TiledButton4.png", 2, 1);
		
		button5_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "TiledButton5.png", 2, 1);
		button6_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "TiledButton6.png", 2, 1);
		button7_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "TiledButton7.png", 2, 1);
		button8_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "TiledButton8.png", 2, 1);
    	
    	try 
    	{
    	    this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.gameTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    		Debug.e(e);
    	}
    }
   
    private void loadFinalGraphics() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/final/");
    	finalTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
    	
    	popup_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(finalTextureAtlas, activity, "popup.jpg");
    	play_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(finalTextureAtlas, activity, "play.png", 2, 1);
    	menu_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(finalTextureAtlas, activity, "menu.png", 2, 1);
    	
    	try 
    	{
    	    this.finalTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.finalTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    		Debug.e(e);
    	}
    }
    
    public void loadSplashScreen()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
    	splashTextureAtlas.load();
    }
    
    public void unloadSplashScreen() {
    	splashTextureAtlas.unload();
    	splash_region = null;
    }
  
}
