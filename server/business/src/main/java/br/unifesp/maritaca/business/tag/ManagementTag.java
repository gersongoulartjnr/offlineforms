package br.unifesp.maritaca.business.tag;

import java.util.List;
import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.Tag;

public interface ManagementTag {

	List<UUID> retrieveTagKeysList(String tags);
	
	TagsWrapper retrieveTagsWrapper(String tags);

	String retriveTagTextList(List<UUID> tags);
	
	void saveUserFormTag(List<UUID> tags, UUID userKey, UUID formKey);

	void checkTagModifications(Form originalForm, List<UUID> tagKeys);

	/**
	 * Get a tag by the tag key
	 * @param tagKey
	 * @return A Tag entity
	 */
	public Tag getTagsByTagKey(UUID tagKey);
}