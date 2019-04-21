package br.unifesp.maritaca.business.multimedia;

public enum MediaFormat {
	_3GPP("3gpp"), // Mobile format	
	MP4("mp4"), // Video
	OGG("ogg"),	// Video and Audio
	MP3("mp3"), // Audio
	JPEG("jpeg"); // Picture (thumbnail)
	
	private String value;
	
	private MediaFormat(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static MediaFormat getMediaFormat(String value) {
		for (MediaFormat format : MediaFormat.values()) {
			if(format.value.equals(value))
				return format;
		}
		throw new RuntimeException("Undefined MediaFormat");
	}
}
