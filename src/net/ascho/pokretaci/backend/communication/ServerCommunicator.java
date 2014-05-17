package net.ascho.pokretaci.backend.communication;

import java.io.IOException;

public abstract class ServerCommunicator {

	public static final String SERVER_NOT_RESPONDING_MESSAGE="Server not responding error. ";
	
	private int mDelay;
	private int mMaxAttemptCnt;
	
	public ServerCommunicator(int delay, int maxAttemptCnt) {
		mDelay=delay;
		mMaxAttemptCnt=maxAttemptCnt;
	}
	
	public Object communicate(Object... data) throws IOException, Exception {
		int attempts=0;
		int delay=mDelay;
		
		while (attempts++ < mMaxAttemptCnt) {
			try {
				return work(data);
				
			} catch (IOException e) {
				if (attempts==mMaxAttemptCnt) {//ako vise necemo pokusavati onda ne treba ni cekati
					throw new IOException(SERVER_NOT_RESPONDING_MESSAGE);
				}
					//sacekaj malo pre sledeceg pokusaja
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e1) {}
				delay*=2;

			}
		}
		
		return null;
	}
	
	public abstract Object work(Object... data) throws IOException, Exception;
	
	
	
	
}
