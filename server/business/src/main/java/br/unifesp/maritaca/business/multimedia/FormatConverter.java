package br.unifesp.maritaca.business.multimedia;

import java.util.ArrayList;
import java.util.List;

import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

public class FormatConverter extends MultimediaTreater {

	private static final String MP3_CODEC_NAME		= "libmp3lame";
	private static final String OGG_CODEC_LIBVORBIS = "libvorbis";
	private static final String OGG_CODEC_LIBTHEORA = "libtheora";
	private static final String CODEC_LIBX264		= "libx264";
	private static final String CODEC_LIBFDK_AAC	= "libfdk_aac";
	
	private MediaFormat target;

	public FormatConverter(byte[] data, ComponentType type, String filename, MediaFormat source, MediaFormat target) {
		this.initVariables(type, filename, source, target);
		setByteArray(data);
	}
	
	public FormatConverter(String data, ComponentType type, String filename, MediaFormat source, MediaFormat target) {
		this.initVariables(type, filename, source, target);
		setData(data);
	}

	private void initVariables(ComponentType type, String filename, MediaFormat source, MediaFormat target){
		validateFormats(source, target);

		setTmpDir(UtilsPersistence.retrieveConfigFile().getTmpDir());
		setFfmpegPath(UtilsPersistence.retrieveConfigFile().getFfmpegPath());
		
		setInputFilename(getTmpDir() + buildFilename(filename, source.getValue()));
		setOutputFilename(getTmpDir() + buildFilename(filename, target.getValue()));
		setType(type);
		this.setTarget(target);
	}
	
	@Override
	List<String> loadFFmpegCommand() {
		List<String> command;
		switch (getType()) {
			case AUDIO:
				command = audioCommand();
				break;
			case VIDEO:
				command = videoCommand();
				break;
			default:
				command = null;
				break;
		}
		return command;
	}

	private List<String> videoCommand() {
		List<String> command = new ArrayList<String>();
		command.add(getFfmpegPath()); // command name
		command.add("-i");
		command.add(getInputFilename()); // input filename 3gpp format
		command.add("-f");
		if(target == MediaFormat.OGG){
			command.add(MediaFormat.OGG.getValue());
			command.add("-vcodec");
			command.add(OGG_CODEC_LIBTHEORA);
		} else if(target == MediaFormat.MP4){
			command.add(MediaFormat.MP4.getValue());
			command.add("-vcodec");
			command.add(CODEC_LIBX264);
		} else {
			throw new RuntimeException("Unsupported video format");
		}
		command.add("-r");
		command.add("25");
		command.add("-b");
		command.add("1024k");
		
		command.add("-ab");
		command.add("128k");
		command.add("-ac");
		command.add("2");
		command.add("-async");
		command.add("1");		
		
		command.add("-acodec");
		if(target == MediaFormat.OGG){
			command.add(OGG_CODEC_LIBVORBIS);
		} else{ //if(target == MediaFormat.MP4){
			command.add(CODEC_LIBFDK_AAC);
		}
		command.add(getOutputFilename()); // output filename format
		return command;
	}

	private List<String> audioCommand() {
		List<String> command = new ArrayList<String>();
		command.add(getFfmpegPath()); // command name
		command.add("-i");
		command.add(getInputFilename()); // input filename 3gpp format
		command.add("-c:a");
		command.add(selectAudioCodec()); // lib codec name
		command.add("-q:a");
		command.add("100"); // quality 100%
		command.add(getOutputFilename()); // output filename format
		return command;
	}

	private String selectAudioCodec() {
		String codecName;
		if (target == MediaFormat.OGG) {
			codecName = OGG_CODEC_LIBVORBIS;
		} else if (target == MediaFormat.MP3) {
			codecName = MP3_CODEC_NAME;
		} else {
			throw new RuntimeException("Unsupported audio format");
		}
		return codecName;
	}

	private void validateFormats(MediaFormat source, MediaFormat target) {
		if (source != MediaFormat._3GPP) {
			throw new RuntimeException("Source format must be 3GPP format");
		}
		if (target == MediaFormat._3GPP || target == MediaFormat.JPEG) {
			throw new RuntimeException("Target format is invalid");
		}
	}

	private String buildFilename(String filename, String extension) {
		return filename + "." + extension;
	}

	public MediaFormat getTarget() {
		return target;
	}

	public void setTarget(MediaFormat target) {
		this.target = target;
	}
}
