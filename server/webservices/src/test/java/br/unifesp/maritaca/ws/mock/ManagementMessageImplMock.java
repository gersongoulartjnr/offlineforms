package br.unifesp.maritaca.ws.mock;

import br.unifesp.maritaca.business.messaging.ManagementMessage;
import br.unifesp.maritaca.business.messaging.dto.EmailDTO;

public class ManagementMessageImplMock implements ManagementMessage {

	@SuppressWarnings("unused")
	private EmailDTO emailDTO;
	
	@Override
	public void sendMessage(EmailDTO emailDTO) {
		this.emailDTO = emailDTO;		
	}
}