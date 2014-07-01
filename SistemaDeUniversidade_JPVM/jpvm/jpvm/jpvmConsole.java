/* jpvmConsole.java
 *
 * A simple command line console for interacting with the local
 * jpvm daemon.
 *
 * Adam J Ferrari
 * Mon 05-27-1996
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

package jpvm;
import  jpvm.jpvmEnvironment;
import  jpvm.jpvmDaemonMessageTag;
import	java.io.*;

public 
class jpvmConsole {
	private static jpvmEnvironment	jpvm;
	public static BufferedReader 	user;

	public static void main(String args[]) {
	    try {
		jpvm  = new jpvmEnvironment("jpvm console");
		InputStreamReader userIn = new InputStreamReader(System.in);
		user = new BufferedReader(userIn);
		while(true) {
			System.out.print("jpvm> ");
			try {
				System.out.flush();
                    		String command = user.readLine();
				if(command.equalsIgnoreCase("quit") ||
				   command.equalsIgnoreCase("q") ) {
					Quit();
				}
				else if (command.equalsIgnoreCase("help") ||
					 command.equals("?")) {
					 Help();
				}
				else if (command.equalsIgnoreCase("conf")) {
					Conf();
				}
				else if (command.equalsIgnoreCase("halt")) {
					Halt();
				}
				else if (command.equalsIgnoreCase("add")) {
					Add();
				}
				else if (command.equalsIgnoreCase("ps")) {
					Ps();
				}
				else {
					System.out.println(command+
						": not found");
				}
			}
			catch (IOException ioe) {
				System.err.println("jpvm console: i/o " +
					"exception.");
				System.exit(1);
			}
		}
	    }
	    catch (jpvmException jpe) {
		perror("internal jpvm error - "+jpe.toString());
	    }
	}

	private static void Quit() throws jpvmException {
		System.out.println("jpvm still running.");
		jpvm.pvm_exit();
		System.exit(0);
	}

	private static void Help() throws jpvmException {
		System.out.println("Commands are:");
		System.out.println("  add\t- Add a host to the virtual "+
					"machine");
		System.out.println("  halt\t- Stop jpvm daemons");
		System.out.println("  help\t- Print helpful information " +
					"about commands");
		System.out.println("  ps\t- List tasks");
		System.out.println("  quit\t- Exit console");
	}

	private static void Conf() throws jpvmException {
		jpvmConfiguration conf = jpvm.pvm_config();
		System.out.println(""+conf.numHosts+" hosts:");
		for(int i=0;i<conf.numHosts;i++)
			System.out.println("\t"+conf.hostNames[i]);
	}

	private static void Ps() throws jpvmException {
		jpvmConfiguration conf = jpvm.pvm_config();
		for(int i=0;i<conf.numHosts;i++) {
			jpvmTaskStatus ps = jpvm.pvm_tasks(conf,i);
			System.out.println(ps.hostName+", "+ps.numTasks+
				" tasks:");
			for(int j=0;j<ps.numTasks;j++)
			    System.out.println("\t"+ps.taskNames[j]);
		}
	}

	private static void Halt() throws jpvmException {
		jpvm.pvm_halt();
		try {
                        Thread.sleep(2000);
                }
                catch (InterruptedException ie) {
                }
		System.exit(0);
	}

	private static void Add() {
		String host = null;
		int    port = 0;
		try {
			System.out.print("\tHost name   : ");
			System.out.flush();
			host = user.readLine();
		    	System.out.print("\tPort number : ");
		    	System.out.flush();
		    	String port_str = user.readLine();
		    try {
		    	port = Integer.valueOf(port_str).intValue();
		    }
		    catch (NumberFormatException nfe) {
			System.out.println("Bad port.");
			return;
		    }
		}
		catch (IOException e) {
		 	System.out.println("i/o exception");
			try {
				Quit();
			}
			catch (jpvmException jpe) {
				System.exit(0);
			}
		}
		jpvmTaskId tid = new jpvmTaskId(host,port);
		String h[] = new String[1];
		jpvmTaskId t[] = new jpvmTaskId[1];
		h[0] = host;
		t[0] = tid;
		try {
		  jpvm.pvm_addhosts(1,h,t);
		}
		catch (jpvmException jpe) {
		  perror("error - couldn't add host " + host);
		}
	}

 	private static void perror(String message) {
                System.err.println("jpvm console: "+ message);
                System.err.flush();
        }
};
