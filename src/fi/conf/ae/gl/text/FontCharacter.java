/* [LGPL] Copyright 2011 Irah, Gima

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
package fi.conf.ae.gl.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import fi.conf.ae.image.ImageUtils;
import fi.conf.ae.math.DistanceField;
import fi.conf.ae.math.Geometry;

/**
 * Retrieve font character as bitmap with metadata information for placement.
 *  
 * @see <a href="http://www.valvesoftware.com/publications/2007/SIGGRAPH2007_AlphaTestedMagnification.pdf">Chris Green. 2007. "Improved Alpha-Tested Magnification for Vector Textures and Special Effects." SIGGRAPH Course on Advanced Real-Time Rendering in 3D Graphics and Games.</a>
 */
public class FontCharacter {
	
	public static final FontRenderContext fontRenderingContext;

	private byte[] bytes;
	private int width;
	private int height;
	private float baseline;
	private float advanceBefore;
	private float advanceAfter;
	
	static {
		fontRenderingContext = new FontRenderContext(Geometry.identityMatrix, false, true);
	}
	
	private FontCharacter(byte[] image, int width, int height, float baseline, float advanceBefore, float advanceAfter) {
		this.bytes = image;
		this.width = width;
		this.height = height;
		this.baseline = baseline;
		this.advanceBefore = advanceBefore;
		this.advanceAfter = advanceAfter;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getBaseline() {
		return baseline;
	}
	
	public float getAdvanceBefore() {
		return advanceBefore;
	}
	
	public float getAdvanceAfter() {
		return advanceAfter;
	}
	
	@Override
	public String toString() {
		return String.format("[%s, bytes:%s, width:%d, height:%d, baseline:%f, advanceBefore:%f, advanceAfter:%f]",
				this.getClass().getName(),
				bytes,
				width,
				height,
				baseline,
				advanceBefore,
				advanceAfter
				);
	}
	
	public static FontCharacter getChar(Font font, String c) {
		GlyphVector gv = font.createGlyphVector(fontRenderingContext, c);
		Shape characterShape = gv.getOutline();
		
		Rectangle2D shapeBounds = characterShape.getBounds2D();
		Rectangle2D logicalBounds = gv.getLogicalBounds();
		Rectangle2D visualBounds = gv.getVisualBounds();
		
		BufferedImage characterImage = ImageUtils.createBufferedImage(
				ImageUtils.colorModel_8BPP,
				ImageUtils.createInterleavedRaster(
						(int) Math.round(shapeBounds.getWidth()) + 2,
						(int) Math.round(shapeBounds.getHeight()) + 2,
						1,
						ImageUtils.pixelOffsets_GRAY
						)
				);
		
		Graphics2D g = characterImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
		
		g.setColor(Color.WHITE);

		// move origin so character fits to the image
		// round + 1 because translate changes origin with decimals and
		// fill fills with shape's origin and decimals, thus pixels can shift
		g.translate(
				-Math.round(shapeBounds.getX()) + 1,
				-Math.round(visualBounds.getY()) + 1
				);
		g.fill(characterShape);
		
		g.dispose();
		
		// ImageUtils was directed to create BufferedImage with DataBufferByte 
		byte[] characterBytes = ((DataBufferByte) characterImage.getRaster().getDataBuffer()).getData();
		
//		byte[] characterBytes = new byte[characterImage.getWidth() * characterImage.getHeight()];
//		for (int y=0; y<characterImage.getHeight(); y++) {
//			for (int x=0; x<characterImage.getWidth(); x++) {
//				int characterByte = characterImage.getRGB(x, y);
//				characterBytes[y * characterImage.getWidth() + x] = (byte) characterByte;
//			}
//		}
		
		FontCharacter fontCharacter = new FontCharacter(
				characterBytes,
				characterImage.getWidth(),
				characterImage.getHeight(),
				(float) visualBounds.getY(),
				(float) visualBounds.getX(),
				(float) logicalBounds.getWidth()
				);
		
		characterImage.flush(); // this or gc or this&gc?
		
		return fontCharacter;
	}
	
	public static FontCharacter getDistanceFieldChar(FontCharacter fontCharacter, Font font, int newPtSize, float smoothAmount) {
		
		DistanceField distanceField = DistanceField.calculate(
				fontCharacter.getBytes(),
				fontCharacter.getWidth(),
				fontCharacter.getHeight(),
				(int) Math.ceil(smoothAmount*font.getSize2D()*0.336f), // smooth pixels length
				newPtSize / font.getSize2D() // new font size
				);
		
		float resizedPerc = (distanceField.getHeight() - 2 * distanceField.getBorder()) / (float) fontCharacter.getHeight();
		
		return new FontCharacter(
				distanceField.getBytes(),
				distanceField.getWidth(),
				distanceField.getHeight(),
				fontCharacter.getBaseline() * resizedPerc - distanceField.getBorder(),
				fontCharacter.getAdvanceBefore() * resizedPerc + distanceField.getBorder(),
				fontCharacter.getAdvanceAfter() * resizedPerc - distanceField.getBorder()
				);
	}
	
}
