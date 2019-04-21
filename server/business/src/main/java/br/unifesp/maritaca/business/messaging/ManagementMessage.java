package br.unifesp.maritaca.business.messaging;

import br.unifesp.maritaca.business.messaging.dto.EmailDTO;

public interface ManagementMessage {

	void sendMessage(EmailDTO emailDTO);	
}