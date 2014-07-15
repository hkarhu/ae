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
package fi.conf.ae.routines;

/**
 * Differential timer.
 */
public class Timer {
	
	private long lastTick;

	public Timer() {
		lastTick = -1;
	}
	
	/**
	 * Retrieve the difference from last reset.
	 * 
	 * @return Difference in milliseconds since last reset. If last call wasn't made, returns 0
	 */
	public long diff() {
		
		long retDiff;
		
		if (lastTick == -1) {
			retDiff = 0;
		}
		else {
			retDiff = System.currentTimeMillis() - lastTick;
		}
		
		return retDiff;
	}
	
	/**
	 * Retrieve the difference from last reset and then reset.
	 * 
	 * @return Difference in milliseconds since last reset. If last call wasn't made, returns 0
	 */
	public long diffAndReset() {
		long retDiff = diff();
		reset();
		return retDiff;
	}
	
	/**
	 * Reset to current timer.
	 */
	public void reset() {
		lastTick = System.currentTimeMillis();
	}
	
}
