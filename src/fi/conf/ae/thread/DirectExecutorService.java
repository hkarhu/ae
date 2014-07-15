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
package fi.conf.ae.thread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executes all submitted commands immediately in the calling thread.
 */
public class DirectExecutorService extends AbstractExecutorService {

	/**
	 * Cannot be shut down.
	 * @return False, always
	 */
	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return false;
	}

	/**
	 * Cannot be shut down.
	 */
	@Override
	public boolean isShutdown() {
		return false;
	}

	/**
	 * Cannot be shut down.
	 */
	@Override
	public boolean isTerminated() {
		return false;
	}

	/**
	 * Cannot be shut down.
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Cannot be shut down
	 * @return Empty list, always.
	 */
	@Override
	public List<Runnable> shutdownNow() {
		return new LinkedList<>();
	}

	/**
	 * Run the given command immediately in the calling thread.
	 */
	@Override
	public void execute(Runnable command) {
		command.run();
	}
	
}
