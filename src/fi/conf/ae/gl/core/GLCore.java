/* [LGPL] Copyright 2010, 2011 Gima, Irah

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.conf.ae.gl.core;

import java.awt.Canvas;
import java.awt.RenderingHints.Key;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import fi.conf.ae.gl.GLMousePoint;
import fi.conf.ae.gl.GLValues;
import fi.conf.ae.routines.ListenerManager;
import fi.conf.ae.routines.S;
import fi.conf.ae.thread.DrainableExecutorService;

public abstract class GLCore {
	
	private boolean requestClose;
	private boolean lastFocusedState;
	
	private String windowTitle;
	private ByteBuffer[] windowIcon;
	
	public ListenerManager<GLKeyboardListener> keyboardListeners;
	public ListenerManager<GLMouseListener> mouseListeners;
	
	private GLMousePoint mousePoint;
	public enum StereoMode { off, oculus };
	private StereoMode stereoMode = StereoMode.off;
	
	private final static DrainableExecutorService drainableExecutorService = new DrainableExecutorService();
	
	public GLCore() {
		requestClose = false;
		lastFocusedState = true;

		windowTitle = "untitled window wodniw deltitnu";
		windowIcon = null;
		
		keyboardListeners = new ListenerManager<>();
		mouseListeners = new ListenerManager<>();
		
		mousePoint = new GLMousePoint();
	}
	
	public DrainableExecutorService getExecutorService() {
		return drainableExecutorService;
	}

	public void startGL(String title) {
		this.windowTitle = title;
		startGL();		
	}

	public void startGL(String title, ByteBuffer[] icon) {
		this.windowIcon = icon;
		startGL(windowTitle);
	}
	
	public void startGL(String title, StereoMode stereoMode, ByteBuffer[] icon) {
		this.stereoMode = stereoMode;
		startGL(windowTitle, icon);
	}
	
	public void startGL() {
		
		requestClose = false;
		lastFocusedState = true;
		
		Keyboard.enableRepeatEvents(false);
		Display.setTitle(windowTitle);
		Display.setIcon(windowIcon);
		
		contemplateDisplayMode();
		
		if (glInit()) {
			
			//Do stereo mode-init if any
//			switch(stereoMode){
//			
//				case interlaced: interlacedStereoInit(); break;
//				case oculus: oculusStereoInit(); break;
//					
//			}
			
			while (internalLoop()) {
				Display.sync(60);
			}
			glTerminate();
		}

		Display.destroy();
	}
	
	public void startGL(Canvas destination){
		
		requestClose = false;
		lastFocusedState = true;
		
		Keyboard.enableRepeatEvents(false);
		
		try {
			Display.setParent(destination);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			return;
		}
		
		if (!glInit()) throw new Error("GL Initialization failed.");

		startGL();
		
	}
	
	public void requestClose() {
		requestClose = true;
	}
	
	/**
	 * Sleep at most the specified amount milliseconds. 
	 */
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void swapBuffers() {
		try {
			Display.swapBuffers();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	private void contemplateDisplayMode() {
		
		DisplayModePack displayModePack; 
		
		try {
			// request subclass to pick a display mode
			displayModePack = glPickDisplayMode();
			if (displayModePack == null) {
				// subclass specifically requested to let the user choose
				displayModeChooserDialog();
				return;
			}
		}
		catch (Exception e) {
			throw new Error("User class failed to pick a display mode.", e);
		}
		
		try {
			// try to use the returned display mode
			useDisplayModePack(displayModePack);
			return;
		}
		catch (LWJGLException e) {
			S.eprintf("Requested display mode could not be set. Offering the user a choice to select one of the available display modes.", e);
		}
		
		// let the user choose a display mode
		displayModeChooserDialog();
		
	}

	private void displayModeChooserDialog() {
		DisplayModePack dmpUserChosen = DisplayModeChooserDialog.dialogChooseDisplayMode();
		try {
			useDisplayModePack(dmpUserChosen);
		}
		catch (LWJGLException e) {
			throw new Error("No display mode could not be set. Exiting.");
		}
	}

	private void useDisplayModePack(DisplayModePack displayModePack) throws LWJGLException {
		
		if (displayModePack == null) throw new NullPointerException("DisplayModePack must not be null.");
		
		Display.setDisplayMode(displayModePack.getDisplayMode());
		Display.setFullscreen(displayModePack.isFullscreen());
		
		GLValues.screenWidth = displayModePack.getDisplayMode().getWidth();
		GLValues.screenHeight = displayModePack.getDisplayMode().getHeight();
		GLValues.calculateRatios();
		
		if (displayModePack.getPixelFormat() == null) {
			Display.create();
		}
		else {
			Display.create(displayModePack.getPixelFormat());
		}
		
	}
	
	private boolean internalLoop() {
		
		handleKeyboardEvents();
		handleMouseEvents();
		handleFocusChange();
		
//		switch(stereoMode){
//		
//			case interlaced: interlacedStereoDraw(true); glLoop(); interlacedStereoDraw(false); glLoop(); break;
//			case oculus: oculusStereoDraw(true); glLoop(); oculusStereoDraw(false); glLoop(); break; 
//		
//			default: glLoop(); swapBuffers(); break;
//		}
		
		 glLoop(); swapBuffers();
		
		Display.processMessages();

		drainableExecutorService.executePending();
		
		return (!(Display.isCloseRequested() || requestClose));
		
	}
	
	public void renderToTexture(int textureID, int width, int height){
		GL11.glViewport(0, 0, width, height);
		glLoop();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);	// Bind and copy texture
		GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 0, 0, width, height, 0);
		GL11.glViewport(0, 0, GLValues.screenWidth, GLValues.screenHeight);
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);		// Clear The Screen And Depth Buffer
	}
	
	private void handleFocusChange() {
		
		boolean isFocused = Display.isActive();
		
		if (lastFocusedState != isFocused) {
			glFocusChanged(isFocused);
			lastFocusedState = isFocused;
		}
	}
	
	private void handleKeyboardEvents() {
		while (Keyboard.next()) {

			if (Keyboard.getEventKeyState() == true) {
				// key changed to down
				for (GLKeyboardListener listener : keyboardListeners) {
					listener.glKeyDown(Keyboard.getEventKey(), Keyboard.getEventCharacter());
				}
				
			}
			else {
				// key changed to up
				for (GLKeyboardListener listener : keyboardListeners) {
					listener.glKeyUp(Keyboard.getEventKey(), Keyboard.getEventCharacter());
				}
			}
			
		} // while
	}
	
	private void handleMouseEvents() {
		while (Mouse.next()) {

			if ((Mouse.getDX() != 0) || (Mouse.getDY() != 0)) {
				// mouse has moved
				float x = (Mouse.getX() / (float) Display.getDisplayMode().getWidth());
				float y = (Mouse.getY() / (float) Display.getDisplayMode().getHeight());
				
				// lwjgl is having a blast at making left-bottom 0,0 "because opengl"
				// (Don't whine Tommi, it's just a matter of perspective!)
				y = 1-y;
				
				mousePoint.getPointNormal2D().set(x, y);
				mousePoint.getPointGL2D().set(x*GLValues.glWidth, y*GLValues.glHeight);
				mousePoint.getDelta2D().set(Mouse.getDX(), Mouse.getDY());
				
				//Unproject 2D point to get the point in 3D space
				IntBuffer viewport = BufferUtils.createIntBuffer(16);
				FloatBuffer z =  BufferUtils.createFloatBuffer(1);
				FloatBuffer modelviewMatrix = BufferUtils.createFloatBuffer(16);
				FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
				FloatBuffer mouse_pos = FloatBuffer.allocate(3);
				
				GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelviewMatrix );
				GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projMatrix);
				GL11.glGetInteger( GL11.GL_VIEWPORT, viewport);
				
			    GL11.glReadPixels(Mouse.getX(), Mouse.getY(), 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, z);
				GLU.gluUnProject(Mouse.getX(), Mouse.getY(), z.get(), modelviewMatrix, projMatrix, viewport, mouse_pos);
				
				mousePoint.getPoint3D().set(mouse_pos.get(0), mouse_pos.get(1), mouse_pos.get(2));	
				
				for (GLMouseListener listener : mouseListeners) {
					listener.glMouseMoved(mousePoint);
				}
			}
			
			if (Mouse.getEventButton() == -1) {
				// no buttons changed
				continue;
			}
			
			if (Mouse.getEventButtonState() == true) {
				// button changed to down
				for (GLMouseListener listener : mouseListeners) {
					listener.glMouseButtonDown(mousePoint, Mouse.getEventButton());
				}
			}
			else {
				// button changed to up
				for (GLMouseListener listener : mouseListeners) {
					listener.glMouseButtonUp(mousePoint, Mouse.getEventButton());
				}
			}
	
		} // while
	}

	private void interlacedStereoInit(){
		
	}
	
	private void interlacedStereoDraw(boolean lor){
		
	}
	
	private void oculusStereoInit(){
		
	}
	
	private void oculusStereoDraw(boolean lor){
		
	}
	
	public abstract boolean glInit();
	public abstract DisplayModePack glPickDisplayMode() throws Exception;
	public abstract void glLoop();
	public abstract void glFocusChanged(boolean isFocused);
	public abstract void glTerminate();

}
