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
package fi.conf.ae.math;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import fi.conf.ae.image.ImageUtils;
import fi.conf.ae.math.EdgeResult.EdgeSearchFrom;

/**
 * Calculate distance field from black (byte) 0 and white (byte) 255 images only, where black represents non-interesting background.
 */ 
public class DistanceField {
	
	public static DistanceField calculate(byte[] srcBytes, int srcWidth, int srcHeight, int pxSmoothLength, float outSizePerc) {

		int srcBorder = pxSmoothLength;
		int srcPaddedWidth = srcWidth + (2 * srcBorder);
		int srcPaddedHeight = srcHeight + (2 * srcBorder);
		
		int dfBorder = (int) (pxSmoothLength * outSizePerc);
		int dfPaddedWidth = (int) (srcWidth * outSizePerc) + (2 * dfBorder);
		int dfPaddedHeight = (int) (srcHeight * outSizePerc) + (2 * dfBorder);
		
//		int dfBorder = (int) (srcBorder * newSize);
//		int dfPaddedWidth = (int) (srcPaddedWidth * newSize);
//		int dfPaddedHeight = (int) (srcPaddedHeight * newSize);

		BufferedImage dfImage = ImageUtils.createBufferedImage(
				ImageUtils.colorModel_8BPP,
				ImageUtils.createInterleavedRaster(
						dfPaddedWidth,
						dfPaddedHeight,
						1,
						ImageUtils.pixelOffsets_GRAY
						)
				);
		
		byte[] dfBytes = ((DataBufferByte) dfImage.getRaster().getDataBuffer()).getData();
		
		float maxDiagonalSearchDistance = Geometry.getHypotenuseLength(pxSmoothLength, pxSmoothLength);
		EdgeResult edgeResult = new EdgeResult();

		for (int y=0; y<dfImage.getHeight(); y++) {
			float normY = (float) y / dfImage.getHeight();
			int srcY = (int) (normY * srcPaddedHeight) - srcBorder;
			
			for (int x=0; x<dfImage.getWidth(); x++) {
				float normX = (float) x / dfImage.getWidth();
				int srcX = (int) (normX * srcPaddedWidth) - srcBorder;
				
				float distance = DistanceField.getDistanceToEdge(
						srcBytes,
						srcWidth, srcHeight,
						srcBorder,
						srcX, srcY,
						pxSmoothLength,
						edgeResult
						);
				
				int dfByteIdx = y * dfImage.getWidth() + x;

				if (edgeResult.from == EdgeSearchFrom.OUTSIDE) {
					// edge FROM BLACK pixel
					if (distance == Float.MAX_VALUE) {
						// result is over maxSearchDistance
						dfBytes[dfByteIdx] = (byte) 0;
					}
					else {
						// result is within maxSearchDistance
						dfBytes[dfByteIdx] = (byte) (127 - Geometry.normalize(distance, 0, maxDiagonalSearchDistance) * 127);
					}
				}
				else {
					// (edgeResult.from == EdgeSearchFrom.OUTSIDE)
					// edge FROM WHITE pixel
					if (distance == Float.MAX_VALUE) {
						// result is over maxSearchDistance
						dfBytes[dfByteIdx] = (byte) 255;
					}
					else {
						// result is within maxSearchDistance
						dfBytes[dfByteIdx] = (byte) (127 + Geometry.normalize(distance, 0, maxDiagonalSearchDistance) * 127);
					}
				}
				
			} // for
		} // for
		
		return new DistanceField(
				dfImage,
				dfBytes,
				dfImage.getWidth(),
				dfImage.getHeight(),
				dfBorder
				);
	}
	
	private static float getDistanceToEdge(byte[] imageBytes, int imageWidth, int imageHeight, int borderLength, int sampleX, int sampleY, int maxSearchDistance, EdgeResult edgeResult) {
		
		float smallestDist = Float.MAX_VALUE;
		
		int sampleByte;
		if (sampleX < 0 || sampleX >= imageWidth || sampleY < 0 || sampleY >= imageHeight) {
			sampleByte = 0;
		}
		else {
			sampleByte = imageBytes[sampleY * imageWidth + sampleX] & 0xff;
		}
		
		final int startX = sampleX - maxSearchDistance;
		final int startY = sampleY - maxSearchDistance;
		final int stopX = sampleX + maxSearchDistance;
		final int stopY = sampleY + maxSearchDistance;
		
		for (int y=startY; y<=stopY; y++) {
			for (int x=startX; x<=stopX; x++) {
				
				int currentByte;
				
				if (y < 0 || y >= imageHeight || x < 0 || x >= imageWidth) {
					currentByte = 0;
				}
				else {
					currentByte = imageBytes[y * imageWidth + x] & 0xff;
				}
				
				if (currentByte != sampleByte) {
					float dist = Geometry.cartesianDistance(sampleX, sampleY, x, y);
					if (dist < smallestDist) smallestDist = dist;
				}
				
			} // for
		} // for
		
		if (sampleByte == 0) {
			edgeResult.from = EdgeSearchFrom.OUTSIDE;
		}
		else {
			edgeResult.from = EdgeSearchFrom.INSIDE;
		}
		
		return smallestDist;
	}
	
	// "data structure" that holds the resulting distance field
	
	private final byte[] imageBytes;
	private final int imageWidth;
	private final int imageHeight;
	private final int outBorder;
	
	private DistanceField(BufferedImage bufferedImage, byte[] imageBytes, int imageWidth, int imageHeight, int border) {
		this.imageBytes = imageBytes;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.outBorder = border;
	}
	
	public byte[] getBytes() {
		return imageBytes;
	}
	
	public int getWidth() {
		return imageWidth;
	}
	
	public int getHeight() {
		return imageHeight;
	}
	
	public int getBorder() {
		return outBorder;
	};
	
}

class EdgeResult {

	enum EdgeSearchFrom {
		INSIDE,
		OUTSIDE
	}
	
	public EdgeSearchFrom from;
}

