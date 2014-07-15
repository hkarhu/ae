/* [LGPL] Copyright 2010, 2011 Gima

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

import fi.conf.ae.gl.GLMousePoint;

public interface GLMouseListener {

	/**
	 * Left-top corner @ 0,0. Values in range 0..1f
	 */
	void glMouseMoved(GLMousePoint mousePoint);
	/**
	 * Left-top corner @ 0,0. Values in range 0..1f
	 */
	void glMouseButtonDown(GLMousePoint mousePoint, int mouseButton);
	/**
	 * Left-top corner @ 0,0. Values in range 0..1f
	 */
	void glMouseButtonUp(GLMousePoint mousePoint, int mouseButton);

}
