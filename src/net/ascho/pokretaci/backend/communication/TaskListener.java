package net.ascho.pokretaci.backend.communication;

import net.ascho.pokretaci.backend.beans.ServerResponseObject;

public interface TaskListener {
	
	public void onResponse(ServerResponseObject taskResponse);
	
}
