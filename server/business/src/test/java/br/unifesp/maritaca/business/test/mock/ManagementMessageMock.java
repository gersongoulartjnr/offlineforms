package br.unifesp.maritaca.business.test.mock;

import br.unifesp.maritaca.business.messaging.ManagementMessage;
import br.unifesp.maritaca.business.messaging.dto.EmailDTO;

public class ManagementMessageMock implements ManagementMessage {

	@Override
	public void sendMessage(EmailDTO emailDTO) {
		return;		
	}

}
