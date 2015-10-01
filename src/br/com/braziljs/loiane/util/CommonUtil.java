package br.com.braziljs.loiane.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


public class CommonUtil {

	   private static long VALID_TIME ;//= Long.valueOf(TestUtil.getMIFSProperty("ios.nonce.valid.time"));
	 
	 
	   static {
		   setValidTime();
	   }
	   private static void setValidTime(){
		   VALID_TIME = Long.valueOf(TestUtil.getMIFSProperty("ios.nonce.valid.time"));
	   }
	   public static void print(){
		   System.out.println("VALID_TIME = "+VALID_TIME);
	   }
	
	 	
	 /*	
	 	public static void main (String[] args) {
	 		try {
	 			print(); 
	 		} catch(Exception e) {
	 			e.printStackTrace();
	 		}
	 	}*/
	
	/*
	
	public boolean mount(String cmd) throws MIConfigException {
		boolean response = false;
		StringBuilder command = new StringBuilder();
		command.append(MICSCommands.MOUNT_CMD_WITH_SUDO + " ").append(cmd);
		LOG.debug("About to attempt mount: " + cmd);

		CommandExecutor cmdExe = new CommandExecutor();
		int status = -1;
		try {
			status = cmdExe.runCommand(command.toString());
		} catch (Exception e) {
			LOG.error("mount " + cmd + " is failed", e);
			throw new MIConfigException(IErrorCodes.MOUNT_FAILED, e.getMessage());
		}
		if (status == 0) {
			response = true;
		} else {
		    String server = null;
		    if(StringUtils.contains(cmd, "nfs")){
		        server = "nfs";
		    }else if (StringUtils.contains(cmd, "cifs")){
		        server = "cifs";
		    }
			if (StringUtils.isNotBlank(cmdExe.getCommandError())) {
				throw new MIConfigException(IErrorCodes.MOUNT_FAILED, "Mount to "+ server + " failed (" + cmdExe.getCommandError() + ")");
			} else {
			    throw new MIConfigException(IErrorCodes.MOUNT_FAILED, "Mount to "+ server + " failed due to invalid parameters.");
			}
		}

		return response;
	}  */
}
