package fi.conf.ae.gl.shader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import fi.conf.ae.gl.texture.GLTextureManager;

public class GLShaderManager {

	private static GLTextureManager INSTANCE;	
	
	private HashMap<String, Integer> programMap = new HashMap<>();
	private int programID = 0;
	
	public static GLTextureManager getInstance() {
		return INSTANCE;
	}
	
	/*
	 * If the shader was setup succesfully, we use the shader. Otherwise
	 * we do drawing normally.
	 */
	public void use(String shaderKey){
		if(shaderKey == null || !programMap.containsKey(shaderKey)){
			ARBShaderObjects.glUseProgramObjectARB(0);
		} else {
			ARBShaderObjects.glUseProgramObjectARB(programMap.get(shaderKey));
		}
	}

    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
	
	/*
	 * With the exception of syntax, setting up vertex and fragment shaders
	 * is the same.
	 * @param the name and path to the vertex shader
	 */
	public void create(String filename, int shaderType) throws Exception {
		
		String shaderKey = filename.replaceFirst(".*/", "").replace(".shdr", "");
		
		int shader = 0;
		
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if(shader == 0) return;

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			
		} catch(Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}
		
		programID = ARBShaderObjects.glCreateProgramObjectARB();
    	
    	if(programID == 0) return;
        
        /*
        * if the vertex and fragment shaders setup sucessfully,
        * attach them to the shader program, link the sahder program
        * (into the GL context I suppose), and validate
        */
        ARBShaderObjects.glAttachObjectARB(programID, shader);
        ARBShaderObjects.glLinkProgramARB(programID);
        if (ARBShaderObjects.glGetObjectParameteriARB(programID, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(programID));
            return;
        }
        
        ARBShaderObjects.glValidateProgramARB(programID);
        if (ARBShaderObjects.glGetObjectParameteriARB(programID, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
        	System.err.println(getLogInfo(programID));
        	return;
        }
        
        programMap.put(shaderKey, programID);
		
	}


	private String readFileAsString(String filename) throws Exception {
		StringBuilder source = new StringBuilder();
		FileInputStream in = new FileInputStream(filename);
		Exception exception = null;
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			Exception innerExc= null;
			try {
				String line;
				while((line = reader.readLine()) != null){
					source.append(line).append('\n');
				}
			} catch(Exception exc) {
				exception = exc;
			} finally {
				try {
					reader.close();
				} catch(Exception exc) {
					if(innerExc == null){
						innerExc = exc;
					} else {
						exc.printStackTrace();
					}
				}
			}

			if(innerExc != null) throw innerExc;
			
		} catch(Exception exc) {
			exception = exc;
		} finally {
			try {
				in.close();
			} catch(Exception exc) {
				if(exception == null){
					exception = exc;
				} else {
					exc.printStackTrace();
				}
			}

			if(exception != null) throw exception;
		}

		return source.toString();
	}
}
