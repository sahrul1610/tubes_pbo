package com.cheeselevel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Screen;

public class StarfishLevel implements Screen{
	private Stage mainStage;
	private Stage uiStage;
	private AnimatedActor turtle;
	private BaseActor starfish;
	private BaseActor floor;
	private BaseActor rock;
	private BaseActor rock1;
	private BaseActor rock2;
	private BaseActor winText;
	private boolean win;
	private float timeElapsed;
	private Label timeLabel;

	// game world dimensions
	final int mapWidth = 800;
	final int mapHeight = 570;

	// window dimensions
	final int viewWidth = 640;
	final int viewHeight = 480;

	public Game game;
	public StarfishLevel(Game g)
	{
		game = g;
		create();
	}

	public void create()
	{
		mainStage = new Stage();
		uiStage = new Stage();
		timeElapsed = 0;

		floor = new BaseActor();
		floor.setTexture( new
				Texture(Gdx.files.internal("water-border.jpg")) );
		floor.setPosition( 0, -20 );
		mainStage.addActor( floor );

		starfish = new BaseActor();
		starfish.setTexture( new
				Texture(Gdx.files.internal("starfish.png")) );
		starfish.setPosition( 400, 300 );
		starfish.setOrigin( starfish.getWidth()/2,
				starfish.getHeight()/2 );

		rock = new BaseActor();
		rock.setTexture( new
				Texture(Gdx.files.internal("rock.png")) );
		rock.setPosition( 200, 400 );
		rock.setOrigin( rock.getWidth()/2,
				rock.getHeight()/2 );

		rock1 = new BaseActor();
		rock1.setTexture( new
				Texture(Gdx.files.internal("rock1.png")) );
		rock1.setPosition( 100, 400 );
		rock1.setOrigin( rock1.getWidth()/2,
				rock1.getHeight()/2 );

		rock2 = new BaseActor();
		rock2.setTexture( new
				Texture(Gdx.files.internal("rock2.png")) );
		rock2.setPosition( 250, 300 );
		rock2.setOrigin( rock2.getWidth()/2,
				rock2.getHeight()/2 );



		mainStage.addActor( starfish );
		mainStage.addActor( rock );
		mainStage.addActor( rock1 );
		mainStage.addActor( rock2 );
		turtle = new AnimatedActor();

		TextureRegion[] frames = new TextureRegion[4];
		for (int n = 0; n < 4; n++)
		{
			String fileName = "turtle" + n + ".png";
			Texture tex = new
					Texture(Gdx.files.internal(fileName));
			tex.setFilter(TextureFilter.Linear,
					TextureFilter.Linear);
			frames[n] = new TextureRegion( tex );
		}
		Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);

		Animation anim = new Animation(0.1f, framesArray,
				Animation.PlayMode.LOOP_PINGPONG);

		turtle.setAnimation( anim );
		turtle.setOrigin( turtle.getWidth()/2, turtle.getHeight()/2 );
		turtle.setPosition( 20, 20 );
		mainStage.addActor(turtle);

		winText = new BaseActor();
		winText.setTexture( new Texture(Gdx.files.internal("you-win.png")) );
		winText.setPosition( 100, 60 );
		winText.setVisible( false );
		uiStage.addActor( winText );



		BitmapFont font = new BitmapFont();
		String text = "Time: 0";
		LabelStyle style = new LabelStyle( font, Color.NAVY );
		timeLabel = new Label( text, style );
		timeLabel.setFontScale(2);
		timeLabel.setPosition(500,440); // sets bottom left(baseline) corner?
		uiStage.addActor( timeLabel );

		win = false;
	}

	public void render(float dt){
		// process input
		turtle.velocityX = 0;
		turtle.velocityY = 0;

		if (Gdx.input.isKeyPressed(Keys.LEFT))
			turtle.velocityX -= 300;
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			turtle.velocityX += 300;;
		if (Gdx.input.isKeyPressed(Keys.UP))
			turtle.velocityY += 300;
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			turtle.velocityY -= 300;
		if (Gdx.input.isKeyPressed(Keys.M))
			game.setScreen( new StarfishMenu(game) );

		// update
		mainStage.act(dt);
		uiStage.act(dt);

		// bound mouse to the rectangle defined by mapWidth, mapHeight
		turtle.setX( MathUtils.clamp( turtle.getX(),
				0, mapWidth - turtle.getWidth() ));
		turtle.setY( MathUtils.clamp( turtle.getY(),
				0, mapHeight - turtle.getHeight() ));

		// check win condition: mouse must be overlapping cheese
		Rectangle starfishRectangle = starfish.getBoundingRectangle();
		Rectangle rockRectangle = rock.getBoundingRectangle();
		Rectangle turtleRectangle = turtle.getBoundingRectangle();

		if ( !win && turtleRectangle.overlaps( starfishRectangle))
		{
			win = true;
			winText.addAction( Actions.sequence(
					Actions.alpha(0),
					Actions.show(),
					Actions.fadeIn(2),
					Actions.forever( Actions.sequence(
							Actions.color( new Color(1,0,0,1), 1 ),
							Actions.color( new Color(0,0,1,1), 1 )
					))
			));

			starfish.addAction( Actions.parallel(
					Actions.alpha(1),
					Actions.rotateBy(360f, 1),
					Actions.scaleTo(0,0, 2), // xAmt, yAmt,duration
					Actions.fadeOut(1)));
		}

		if (!win) {
			timeElapsed += dt;
			timeLabel.setText( "Time: " + (int)timeElapsed );
		}

		// draw graphics
		Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// camera adjustment
		Camera cam = mainStage.getCamera();

		// center camera on player
		cam.position.set( turtle.getX() + turtle.getOriginX(),
				turtle.getY() + turtle.getOriginY(), 0 );

		// bound camera to layout
		cam.position.x = MathUtils.clamp(cam.position.x,
				viewWidth/2, mapWidth - viewWidth/2);
		cam.position.y = MathUtils.clamp(cam.position.y,
				viewHeight/2, mapHeight - viewHeight/2);
		cam.update();

		mainStage.draw();
		uiStage.draw();
	}

	public void resize(int width, int height) { }
	public void pause() { }
	public void resume() { }
	public void dispose() { }
	public void show() { }
	public void hide() { }
}

