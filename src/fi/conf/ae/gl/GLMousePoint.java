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
	
	private Vector2f pointGL2D;
	private Vector2f pointNormal2D;
	private Vector2f delta2D;
	private Vector3f point3D;
	
	public GLMousePoint() {
		this(new Vector2f(), new Vector2f(), new Vector3f(), new Vector2f());
	}
	
	public GLMousePoint(Vector2f pointGL2D, Vector2f delta2D, Vector3f point3D, Vector2f pointNormal2D) {
		this.pointGL2D = pointGL2D;
		this.pointNormal2D = pointNormal2D;
		this.delta2D = delta2D;
		this.point3D = point3D;
	}

	public Vector2f getPointNormal2D() {
		return pointNormal2D;
	}
	
	public Vector2f getPointGL2D() {
		return pointGL2D;
	}
	
	public Vector2f getDelta2D() {
		return delta2D;
	}
	
	public Vector3f getPoint3D() {
		return point3D;
	}

}
