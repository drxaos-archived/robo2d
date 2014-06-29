package graphics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import slick2d.NativeLoader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class Main {

	private UnicodeFont fpsFont;

	private float lightAmbient[] = { 0.0f, 0.0f, 0.0f, 1.0f };  // Ambient Light Values ( NEW )
	private float lightDiffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };      // Diffuse Light Values ( NEW )
	private float lightPosition[] = { 0.0f, 0.0f, 1000.0f, 1.0f };
	private float fogColour[] = {0.0f,0.0f,0.1f,1.0f };
	private float material_shinyness = 80f;

	boolean firstPersonView = false;

	long lastTime,fps;

	public void start() {

		//init display
		try {
			Display.setDisplayMode(new DisplayMode(Support.SCREEN_WIDTH, Support.SCREEN_HEIGHT));
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		//init gl
		GL11.glEnable(GL11.GL_TEXTURE_2D);               
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);          

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective (60.0f,800f/600f, 1f, 1500.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glLoadIdentity();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_NORMALIZE); 

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		//enable lightning
		GL11.glEnable(GL11.GL_LIGHTING);

		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());

		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(lightDiffuse).flip());
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS,(int)material_shinyness);


		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,(FloatBuffer)temp.asFloatBuffer().put(lightPosition).flip());         // Position The Light
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(lightDiffuse).flip());
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT,(FloatBuffer)temp.asFloatBuffer().put(lightAmbient).flip());

		GL11.glEnable(GL11.GL_LIGHT1);

		GL11.glLightModeli(GL12.GL_LIGHT_MODEL_COLOR_CONTROL, GL12.GL_SEPARATE_SPECULAR_COLOR);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK,GL11.GL_AMBIENT_AND_DIFFUSE);


//		GL11.glEnable(GL11.GL_FOG);
//		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
//		GL11.glFog(GL11.GL_FOG_COLOR,(FloatBuffer)temp.asFloatBuffer().put(fogColour).flip());
//		GL11.glFogf(GL11.GL_FOG_DENSITY, 0.3f);
//		GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_NICEST);
//		GL11.glFogf(GL11.GL_FOG_START, 400f);
//		GL11.glFogf(GL11.GL_FOG_END, 1000f);

		//load textures and font
		try {
			//font
			String fontPath = "fonts/arial.ttf";
			fpsFont = new UnicodeFont(fontPath, 25, true, false);
			fpsFont.addAsciiGlyphs();
			fpsFont.addGlyphs(400, 600);
			fpsFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
			fpsFont.loadGlyphs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//initialize ground
//		Ground.initGround();

		lastTime = getTime();
		//render
		render();

		Display.destroy();
	}

	private void render()
	{
		int counter=0;
		while (!Display.isCloseRequested()) {

				// set up the projection matrix
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GLU.gluPerspective (60.0f,800f/600f, 1f, 1500.0f);

				//position the camera
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();

				//first person view or not?
					GLU.gluLookAt(0, -50f, 400f, 0, 230.0f, 0.0f, 0f,0f,1f);

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.5f);
				GL11.glDepthMask(true);

				ByteBuffer temp = ByteBuffer.allocateDirect(16);
				temp.order(ByteOrder.nativeOrder());
				GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,(FloatBuffer)temp.asFloatBuffer().put(lightPosition).flip());  

//				Ground.drawGround();

				pollInput();

				//display overlay
				displayOverlay();

				//sync the display to provide 60fps
				Display.sync(30);
				//update the display
				Display.update();

				updateFPS();
		}
	}

	public long getTime() {
		return System.nanoTime() / 1000000;
	}
	public void updateFPS() {
		if (getTime() - lastTime > 1000) {
			Display.setTitle("FPS: " + fps); 
			fps = 0; //reset the FPS counter
			lastTime += 1000; //add one second
		}
		fps++;
	}

	public void pollInput() {

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				//allow movement only if player hasn't failed yet
				{
					//flip view
					if (Keyboard.getEventKey() == Keyboard.KEY_V) {
						firstPersonView=!firstPersonView;
					}


					if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
//						left=true;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
//						right=true;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
						//shooting

						/*
						 * and yes, this means that there will be one bullet fired for every key press
						 * but that's intented and that is my method of limiting the rate of fire
						 * play the game to see why - there's hundreds of space invaders coming, you need to be able to fire that fast!
						 * you can't fire by holding the button down,so I am limiting the speed of shooting
						 */
//						entities.add(new Bullet(player.getX(),player.getY()+player.getScale()));
					}
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
				{
//					quit=true;
				}
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
//					left=false;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
//					right=false;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
					System.out.println("D Key Released");
				}
			}
		}
	}

	public void displayOverlay()
	{
		//display overlay

		//display the red bar

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPushMatrix();
		GL11.glColor3f(1.0f, 0, 0);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(0, Support.SCREEN_HEIGHT*0.38f);
		GL11.glVertex2f(Support.SCREEN_WIDTH, Support.SCREEN_HEIGHT*0.38f);
		GL11.glEnd();
		GL11.glPopMatrix();

		//change the projection matrix to ortographic
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Support.SCREEN_WIDTH, 0, Support.SCREEN_HEIGHT, 1, -1);

		//back to modelview
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		//draw black bar at the bottom
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glColor3f(0.0f, 0.0f, 0.0f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(Support.SCREEN_WIDTH, 0,0.1f);
		GL11.glVertex3f(Support.SCREEN_WIDTH, fpsFont.getHeight("0")+10,0.1f);
		GL11.glVertex3f(0, fpsFont.getHeight("0")+10,0.1f);
		GL11.glVertex3f(0, 0,0.1f);
		GL11.glEnd();

		//draw blue line at the top
		GL11.glColor3f(0.0f, 0.0f, 1.0f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(Support.SCREEN_WIDTH, fpsFont.getHeight("0")+10,0.1f);
		GL11.glVertex3f(0, fpsFont.getHeight("0")+10,0.1f);
		GL11.glEnd();


		//draw strings
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		//enemies left string
		String string = "Enemies left: ";

		GL11.glPushMatrix();
		GL11.glTranslatef(5f, fpsFont.getHeight(string)+5f, 0f);
		GL11.glScalef(1.0f, -1.0f, 1.0f);
		fpsFont.drawString(0, 0,  string);
		GL11.glPopMatrix();


		//lives left string
		string = "Lives left: " + 0;

		GL11.glPushMatrix();
		GL11.glTranslatef(Support.SCREEN_WIDTH-fpsFont.getWidth(string)-5f, fpsFont.getHeight(string)+5f, 0f);
		GL11.glScalef(1.0f, -1.0f, 1.0f);
		fpsFont.drawString(0, 0,  string);
		GL11.glPopMatrix();


		//game over screen
		{
			string = "Game Over!";

			GL11.glPushMatrix();
			GL11.glTranslatef(Support.SCREEN_WIDTH/2-(fpsFont.getWidth(string)/2), Support.SCREEN_HEIGHT/2f, 0f);
			GL11.glScalef(1.0f, -1.0f, 1.0f);
			fpsFont.drawString(0, 0,  string);
			GL11.glPopMatrix();
		}

		GL11.glEnable(GL11.GL_LIGHTING);
	}

	public static void main(String[] argv) {
        NativeLoader.load("build/natives");
        Main main = new Main();
		main.start();
	}
}
