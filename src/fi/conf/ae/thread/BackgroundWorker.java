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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fi.conf.ae.routines.SynchronizedListenerManager;

public abstract class BackgroundWorker<T> {
	
	public final SynchronizedListenerManager<T> listeners;
	private final ExecutorService workerExecutorService;
	private final ExecutorService listenerExecutorService;
	
	protected BackgroundWorker() {
		this(Executors.newSingleThreadExecutor(), new DirectExecutorService());
	}
	
	protected BackgroundWorker(ExecutorService workerExecutorService, ExecutorService listenerExecutorService) {
		this.workerExecutorService = workerExecutorService;
		this.listenerExecutorService = listenerExecutorService;
		listeners = new SynchronizedListenerManager<>();
	}
	
	protected ExecutorService getWorkerExecutorService() {
		return workerExecutorService;
	}
	
	protected ExecutorService getListenerExecutorService() {
		return listenerExecutorService;
	}

}
