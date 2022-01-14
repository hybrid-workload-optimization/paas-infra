package kr.co.sptek.paas.model;

import io.swagger.annotations.ApiModelProperty;

public class ProcessResult {
	
	@ApiModelProperty(value="작업에 대한 결과 코드 0=성공, 그외=error")
	private int exitCode;	
	
	@ApiModelProperty(value="Error에 대한 메세지.")
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
	
	public static ProcessResult errorMessage(String errorMsg) {
		ProcessResult result = new ProcessResult();
		result.setExitCode(-1);
		result.setMessage(errorMsg);
		return result;
	}
	
	
	public static ProcessResult successMessage() {
		ProcessResult result = new ProcessResult();
		result.setExitCode(0);
		return result;
	}
}
