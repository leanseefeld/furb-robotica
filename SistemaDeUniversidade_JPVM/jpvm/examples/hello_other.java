package examples;

/* hello_other.java
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

import javax.swing.JOptionPane;

import jpvm.*;

class hello_other {
	static int num_workers = 1;

	public static void main(String args[]) {
		try {
			JOptionPane.showMessageDialog(null, "hello_other.main()");
			// inicia o jpvm...
			jpvmEnvironment jpvm = new jpvmEnvironment();

			// pega o id do meu pai...
			jpvmTaskId parent = jpvm.pvm_parent();

			// envia mensagem para meu pai...
			jpvmBuffer buf = new jpvmBuffer();
			buf.pack("Hello from jpvm task, id: " + jpvm.pvm_mytid().toString());
			jpvm.pvm_send(buf, parent, 12345);

			// sai do jpvm
			jpvm.pvm_exit();
		} catch (jpvmException jpe) {
			System.out.println("Error - jpvm exception");
		}
	}
};
