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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fi.conf.ae.routines.S;

public class TestThreadWorkRecipe {
	
	public static ThreadWork work1 = new ThreadWork() {
		@SuppressWarnings("unused")
		public void call(ThreadWorkRecipe recipe, String carryData1, Number carryData2, Float carryData3) {
			
			S.printf("#1 TID:" + Thread.currentThread().getId());
			S.funcArgs(carryData1, carryData2, carryData3);
			
			recipe.nextWork(recipe, carryData1, carryData2, carryData3);
		}
	};
	
	public static ThreadWork work2 = new ThreadWork() {
		@SuppressWarnings("unused")
		public void call(ThreadWorkRecipe recipe, String carryData1, Number carryData2, Float carryData3) {
			S.printf("#2 TID:" + Thread.currentThread().getId());
			S.funcArgs(carryData1, carryData2, carryData3);
			
			recipe.nextWork(recipe, carryData1, carryData2, carryData3);
		}
	};
	
	
	public static ThreadWork work3 = new ThreadWork() {
		@SuppressWarnings("unused")
		public void call(ThreadWorkRecipe recipe, String carryData1, Object carryData2, Float carryData3, String additionalParam) {
			S.printf("#3 TID:" + Thread.currentThread().getId());
			S.funcArgs(carryData1, carryData2, carryData3, additionalParam);
			
			recipe.nextWork(recipe, carryData1, carryData2, carryData3);
		}
	};
	
	public static void main(String[] args) throws Exception {
		ThreadPoolExecutor workerPool1 = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		ThreadPoolExecutor workerPool2 = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		
		ThreadWorkRecipe recipe = new ThreadWorkRecipe();
		recipe.add(TestThreadWorkRecipe.work1, workerPool1);
		recipe.add(TestThreadWorkRecipe.work2, workerPool2);
		recipe.add(TestThreadWorkRecipe.work3, workerPool1, "additionalParameter");

		recipe.nextWork(recipe, "1.234", 5, 0.5f);
		
		try {
			recipe.getResultingCallParams();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("done");
		workerPool1.shutdown();
		workerPool2.shutdown();
	}
	
	public static ThreadWork testWork = new ThreadWork() {
		@SuppressWarnings("unused")
		public void call() {
			System.out.println("testWork");
		}
	};
	
}
