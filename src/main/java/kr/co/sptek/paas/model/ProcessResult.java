package kr.co.sptek.paas.model;

public class ProcessResult {
	private int exitCode;	
	private String message;
	
	public int getExitCode() {
		return exitCode;
	}
	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
