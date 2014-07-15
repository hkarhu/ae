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

public class Dimension2Df implements Cloneable {
	
	private float width;
	private float height;
	
	public Dimension2Df(float width, float height) {
		setWH(width, height);
	}
	
	public void setWH(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	@Override
	public Dimension2Df clone() {
		return new Dimension2Df(width, height);
	}
	
}
