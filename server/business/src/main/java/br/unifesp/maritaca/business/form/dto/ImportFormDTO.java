package br.unifesp.maritaca.business.form.dto;

public class ImportFormDTO {
	
	private String fileName;
	private String fileContent;
	private String url;
	private boolean useFile;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isUseFile() {
		return useFile;
	}
	public void setUseFile(boolean useFile) {
		this.useFile = useFile;
	}
}