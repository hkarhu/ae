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

import java.awt.geom.AffineTransform;

public class Geometry {
	
	public static final AffineTransform identityMatrix = new AffineTransform();
	
	public static float normalize(float value, float min, float max) {
		if (value < min) value = min;
		if (value > max) value = max;
		
		return (value - min)/(max-min);
	}
	
	public static float cartesianDistance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(
				Math.pow(x2-x1,2) + Math.pow(y2-y1,2)
				);
	}
	
	public static float angleFromGroundPlane(float x1, float y1, float x2, float y2) {
		//int quadrant = 0;
		//if (a > 0 && b > 0) quadrant = 0;
		//if (a > 0 && b < 0) quadrant = 1;
		//if (a < 0 && b < 0) quadrant = 2;
		//if (a < 0 && b > 0) quadrant = 3;

		float a = x2-x1;
		float b = y2-y1;
		
		
		float radians = (float) Math.atan(b / a);
		float angle = normalize(radians, (float) (-Math.PI / 2.0f), (float) (Math.PI / 2.0f));
		
		if (a < 0) {
			// left side
			angle += 1;
		}
		
		angle /= 2;
		return angle * 360;
	}
	
	public static boolean pointInRect(float pointX, float pointY, float rectX, float rectY, float rectW, float rectH) {
		if (pointX > rectX && pointX < (rectX + rectW) &&
			pointY > rectY && pointY < (rectY + rectH)
		) return true;
		else return false;
	}

	public static float getHypotenuseLength(float side1, float side2) {
		return (float) Math.sqrt((Math.pow(side1, 2) + Math.pow(side1, 2)));
	}
	
}
