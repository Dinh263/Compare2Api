package com.dinhexam.compareAPI.libs;

public class ResponseObject {
	
	private String content;
	private String formatType;
	
	public ResponseObject() {
		
	}
	public ResponseObject(String content, String formatType) {
		super();
		this.content = content;
		this.formatType = formatType;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFormatType() {
		return formatType;
	}
	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}
	
	
}
