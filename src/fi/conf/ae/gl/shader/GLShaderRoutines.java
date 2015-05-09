package fi.conf.ae.gl.shader;

import java.nio.ByteOrder;
import java.nio.IntBuffer;

import fi.conf.ae.sys.DirectBuffers;

public class GLShaderRoutines {
	
	private static IntBuffer shaderIDbuffer;
	
	static {
		allocateNewTextureIDBuffer(4);
	}
	
	private static void allocateNewTextureIDBuffer(int size) {
		if (shaderIDbuffer != null) DirectBuffers.freeNativeBufferMemory(shaderIDbuffer);
		shaderIDbuffer = DirectBuffers.allocateByteBuffer(size << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
	}
	
}
