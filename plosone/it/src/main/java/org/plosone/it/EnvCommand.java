/* $HeadURL::                                                                                     $
 * $Id$
 *
 * Copyright (c) 2007 by Topaz, Inc.
 * http://topazproject.org
 *
 * Licensed under the Educational Community License version 1.0
 * http://opensource.org/licenses/ecl1.php
 */
package org.plosone.it;

/**
 * A command line wrapper for Env.groovy
 *
 * @author Pradeep Krishnan
 */
public class EnvCommand {
  /**
   * Execute the command
   *
   * @param args install-location, command
   *
   * @throws Exception on an error
   */
  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Usage: EnvCommand <install-location> [start/stop/install/restore] " + 
          "[data-artifact (eg. org.plosone:plosone-it-data:07)]");
      return;
    }

    String cmd = (args.length > 1) ? args[1].toLowerCase() : "start";
    String data = (args.length > 2) ? args[2] : null;
    // simple space seperated for now
    String cmdArgs[] = ((args.length > 3) && (args[3] != null)) ? args[3].split(" ") : new String[0];

    Env    env = new Env(args[0], data);
    env.invokeMethod(cmd, cmdArgs);

    if (cmd.equals("start")) {
      Object block = new Object();

      while (true) {
        synchronized (block) {
          block.wait();
        }
      }
    }
  }
}