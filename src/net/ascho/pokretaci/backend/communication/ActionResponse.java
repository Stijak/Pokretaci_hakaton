package net.ascho.pokretaci.backend.communication;

/**
 * Response u slucaju interakcije sa Goal-s ili Comments.
 * @author Bojan
 *
 */
public class ActionResponse {
	public boolean success;
	public String message;
	
	public boolean isSuccessful() {
		return success;
	}
	
	public String getMessage() {
		return message;
	}
}
