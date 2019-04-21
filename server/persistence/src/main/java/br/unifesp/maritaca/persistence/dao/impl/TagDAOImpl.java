package br.unifesp.maritaca.persistence.dao.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.TagDAO;
import br.unifesp.maritaca.persistence.entity.Tag;
import br.unifesp.maritaca.persistence.entity.UserFormTag;

@Service("tagDAO")
public class TagDAOImpl extends AbstractDAO implements TagDAO {

	@Override
	public void save(Tag tag) {
		emHector.persist(tag);
	}

	@Override
	public Tag findByText(String tagText) {
		List<Tag> result = emHector.cQuery(Tag.class, "text", tagText);
		return result.size() == 1 ? result.get(0) : null;
	}

	@Override
	public Tag getTag(UUID key) {
		return emHector.find(Tag.class, key);
	}

	@Override
	public void saveUFT(UserFormTag uft) {
		emHector.persist(uft);
	}

	@Override
	public boolean existUFT(UUID tagKey, UUID userKey, UUID formKey) {
		List<UserFormTag> result = emHector.cQuery(UserFormTag.class, "tag", tagKey.toString());
		for (UserFormTag uft : result) {
			if (uft.getUser().equals(userKey) && uft.getForm().equals(formKey)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void delete(UUID user, UUID tag, UUID form) {
		List<UserFormTag> result = emHector.cQuery(UserFormTag.class, "tag", tag.toString());
		for (UserFormTag uft : result) {
			if (uft.getUser().equals(user) && uft.getForm().equals(form)) {
				emHector.delete(UserFormTag.class, uft.getKey());
			}
		}
	}

}
