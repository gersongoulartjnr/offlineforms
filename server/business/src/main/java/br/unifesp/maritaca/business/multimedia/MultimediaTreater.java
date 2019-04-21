package br.unifesp.maritaca.business.multimedia;

import java.util.List;

import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.business.util.FFmpegConnector;
import br.unifesp.maritaca.business.util.FileDataManager;

public abstract class MultimediaTreater {
	
	private String data;
	private byte[] byteArray;
	private ComponentType type;
	private String inputFilename;
	private String outputFilename;
	
	private String ffmpegPath;
	private String tmpDir;
	
	public String executeFromByteArray(){
		FileDataManager.saveInLocalFromByteArray(getByteArray(), getInputFilename());
		FFmpegConnector.execute(loadFFmpegCommand());
		String outputFile = FileDataManager.read(getOutputFilename());
		FileDataManager.delete(getInputFilename());
		FileDataManager.delete(getOutputFilename());
		return outputFile;
	}
	
	abstract List<String> loadFFmpegCommand();
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
	
	public void setInputFilename(String inputFilename) {
		this.inputFilename = inputFilename;
	}
	
	public String getInputFilename() {
		return inputFilename;
	}
	
	public void setOutputFilename(String outputFilename) {
		this.outputFilename = outputFilename;
	}
	
	public String getOutputFilename() {
		return outputFilename;
	}

	public ComponentType getType() {
		return type;
	}

	public void setType(ComponentType type) {
		this.type = type;
	}
	
	public String getFfmpegPath() {
		return ffmpegPath;
	}
	
	public void setFfmpegPath(String ffmpegPath) {
		this.ffmpegPath = ffmpegPath;
	}
	
	public String getTmpDir() {
		return tmpDir;
	}
	
	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}
}