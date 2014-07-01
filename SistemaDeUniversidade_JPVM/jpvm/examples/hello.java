package examples;

/* hello.java
 *
 * A simple test of jpvm message passing.
 *
 * Adam J Ferrari
 * Sun 05-26-1996
 *
 * Copyright (C) 1996  Adam J Ferrari
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 675 Mass Ave, Cambridge,
 * MA 02139, USA.
 */

import jpvm.*;

class hello {
	static int num_workers = 1;

	public static void main(String args[]) {
		try {
			// inicia o pvm...
			jpvmEnvironment jpvm = new jpvmEnvironment();

			// pega o meu id...
			jpvmTaskId mytid = jpvm.pvm_mytid();
			System.out.println("Task Id: " + mytid.toString());

			// distribui o trabalho...
			jpvmTaskId tids[] = new jpvmTaskId[num_workers];
			jpvm.pvm_spawn("examples.hello_other", num_workers, tids);

			System.out.println("Worker tasks: ");
			int i;
			for (i = 0; i < num_workers; i++)
				System.out.println("\t" + tids[i].toString());

			// recebe uma mensagem de cada processo...
			for (i = 0; i < num_workers; i++) {
				// recebe uma mensagem...
				jpvmMessage message = jpvm.pvm_recv();
				System.out.println("Got message tag " + message.messageTag + " from " + message.sourceTid.toString());

				// desempacota a mensagem...
				String str = message.buffer.upkstr();

				System.out.println("Received: " + str);
			}

			// sai da m�quina virtual...
			jpvm.pvm_exit();
		} catch (jpvmException jpe) {
			System.out.println("Error - jpvm exception");
		}
	}
};
