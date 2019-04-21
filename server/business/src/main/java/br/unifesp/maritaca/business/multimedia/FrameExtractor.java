package br.unifesp.maritaca.business.multimedia;

import java.util.ArrayList;
import java.util.List;

import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

public class FrameExtractor extends MultimediaTreater {

	public FrameExtractor(String data, String filename) {
		this.setTmpDir(UtilsPersistence.retrieveConfigFile().getTmpDir());
		this.setFfmpegPath(UtilsPersistence.retrieveConfigFile().getFfmpegPath());
		
		this.setInputFilename(getTmpDir() + filename + "." + MediaFormat._3GPP);
		this.setOutputFilename(getTmpDir() + filename + "." + MediaFormat.JPEG);
		this.setData(data);
		this.setType(ComponentType.VIDEO);
	}
	
	public FrameExtractor(byte[] byteArray, String filename) {
		this.setTmpDir(UtilsPersistence.retrieveConfigFile().getTmpDir());
		this.setFfmpegPath(UtilsPersistence.retrieveConfigFile().getFfmpegPath());
		
		this.setInputFilename(getTmpDir() + filename + "." + MediaFormat._3GPP);
		this.setOutputFilename(getTmpDir() + filename + "." + MediaFormat.JPEG);
		this.setByteArray(byteArray);
		this.setType(ComponentType.VIDEO);
	}
	
	@Override
	List<String> loadFFmpegCommand() {
		List<String> command = new ArrayList<String>();
		command.add(getFfmpegPath()); 		// command name
		command.add("-itsoffset");			// Set the input time offset in seconds.
		command.add("-2");
		command.add("-i");					// input file name
		command.add(getInputFilename()); 	
		command.add("-vcodec");				// Force video codec to codec
		command.add("mjpeg");
		command.add("-vframes");			// Set the number of video frames to record
		command.add("1");
		command.add("-an");					// Disable audio recording
		command.add("-f");					// Force format
		command.add("rawvideo");
		command.add(getOutputFilename());	// output filename format
		return command;		
	}
}