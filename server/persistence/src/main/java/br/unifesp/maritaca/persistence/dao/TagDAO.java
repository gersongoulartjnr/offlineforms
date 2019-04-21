package br.unifesp.maritaca.persistence.dao;

import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.Tag;
import br.unifesp.maritaca.persistence.entity.UserFormTag;

public interface TagDAO {

	void save(Tag tag);

	Tag findByText(String tagText);

	Tag getTag(UUID key);

	void saveUFT(UserFormTag uft);

	boolean existUFT(UUID tagKey, UUID userKey, UUID formKey);

	void delete(UUID user, UUID tag, UUID form);
	
}
