/* [LGPL] Copyright 2011 Gima, Irah

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
package fi.conf.ae.gl;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GLMousePoint{
	
	private Vector2f point2D;
	private Vector2f delta2D;
	private Vector3f point3D;
	
	public GLMousePoint() {
		this(new Vector2f(), new Vector2f(), new Vector3f());
	}
	
	public GLMousePoint(Vector2f point2D, Vector2f delta2D, Vector3f point3D) {
		this.point2D = point2D;
		this.delta2D = delta2D;
		this.point3D = point3D;
	}

	public Vector2f getPoint2D() {
		return point2D;
	}
	
	public Vector2f getDelta2D() {
		return delta2D;
	}
	
	public Vector3f getPoint3D() {
		return point3D;
	}

}
