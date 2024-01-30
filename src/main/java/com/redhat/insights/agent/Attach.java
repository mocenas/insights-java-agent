/* Copyright (C) Red Hat 2023 */
package com.redhat.insights.agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import java.io.IOException;
import java.net.URL;

/** Dynamic attachment class for use on apps that are already running. */
public class Attach {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Need args: pid, options");
      System.exit(1);
    }
    URL jarUrl = AgentMain.class.getProtectionDomain().getCodeSource().getLocation();
    //    System.out.println("Trying to attach: " + jarUrl);
    String agentJar = jarUrl.toExternalForm().replaceFirst("^file:", "");

    String pid = args[0];
    String options = args[1];

    try {
      VirtualMachine vm = VirtualMachine.attach(pid);
      vm.loadAgent(agentJar, options);
      vm.detach();
    } catch (AgentLoadException | IOException e) {
      //show exceptions
      throw new RuntimeException(e);
      // Probable Java version mismatch, ignore
    } catch (AttachNotSupportedException | AgentInitializationException e) {
      throw new RuntimeException(e);
    }
  }
}
