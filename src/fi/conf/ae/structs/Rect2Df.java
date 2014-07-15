/* [LGPL] Copyright 2011 Gima

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
package fi.conf.ae.structs;

public class Rect2Df implements Cloneable {

	private float x;
	private float y;
	private float w;
	private float h;
	
	public Rect2Df(float x, float y, float w, float h) {
		setXYWH(x, y, w, h);
	}
	
	public void setXYWH(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}
	
//	public void setRectCenter(float newCX, float newCY) {
//		setX(newCX - (getW()/2));
//		setY(newCY - (getH()/2));
//	}
	
	public void copyValuesFrom(Rect2Df rect) {
		this.x = rect.x;
		this.y = rect.y;
		this.w = rect.w;
		this.h = rect.h;
	}

	@Override
	public Rect2Df clone() {
		return new Rect2Df(x, y, w, h);
	}
	
}
