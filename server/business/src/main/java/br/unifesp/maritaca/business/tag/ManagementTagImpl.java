package br.unifesp.maritaca.business.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.persistence.dao.TagDAO;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.entity.Tag;
import br.unifesp.maritaca.persistence.entity.UserFormTag;

@Service("managementTag")
public class ManagementTagImpl extends AbstractBusiness implements ManagementTag {

	@Autowired private TagDAO tagDAO;
	
	@Override
	public List<UUID> retrieveTagKeysList(String tags) {
		Tag tag;
		List<UUID> result = new ArrayList<UUID>();
		String[] _tags = "".equals(tags) ? new String[0] : tags.split(",");
		for(String tagText : _tags) {
			tag = tagDAO.findByText(tagText);
			if (tag == null) {
				tag = new Tag(tagText);
				tagDAO.save(tag);
			}
			result.add(tag.getKey());
		}
		return result;
	}
	
	@Override
	public TagsWrapper retrieveTagsWrapper(String tags) {
		TagsWrapper wrapper = new TagsWrapper();
		List<UUID> keys = new ArrayList<UUID>();
		List<String> texts = new ArrayList<String>();
		
		Tag tag;
		String[] _tags = "".equals(tags) ? new String[0] : tags.split(",");
		for(String tagText : _tags) {
			tag = tagDAO.findByText(tagText);
			if (tag == null) {
				tag = new Tag(tagText);
				tagDAO.save(tag);
			}
			keys.add(tag.getKey());
			texts.add(tag.getText());
		}
		wrapper.setKeys(keys);
		wrapper.setTexts(texts);
		return wrapper;
	}

	@Override
	public String retriveTagTextList(List<UUID> tags) {
		String result = "";
		Tag tag;
		int counter = tags.size()-1;	
		for (UUID key : tags) {
			tag = tagDAO.getTag(key);
			if (counter == 0) {
				result += tag.getText();
			} else {
				result += tag.getText() + ",";
				counter--;
			}
		}
		return result;
	}

	@Override
	public void saveUserFormTag(List<UUID> tags, UUID userKey, UUID formKey) {
		for (UUID tagKey : tags) {
			if (tagDAO.existUFT(tagKey, userKey, formKey)) {
				continue;
			}
			tagDAO.saveUFT(new UserFormTag(tagKey, userKey, formKey));
		}
	}

	@Override
	public void checkTagModifications(Form originalForm, List<UUID> newTagsKeys) {
		List<UUID> tags = new ArrayList<UUID>();
		// checking and saving new tags in UserFormTag
		for (UUID newTagKey : newTagsKeys) {
			if (originalForm.getTags().contains(newTagKey)) {
				continue;
			} else {
				tags.add(newTagKey);
			}
		}
		if (tags.size() > 0) {
			this.saveUserFormTag(tags, originalForm.getUser(), originalForm.getKey());
		}
		
		// checking and deleting tags
		if (originalForm.getTags() != null) {
			for (UUID oldTag : originalForm.getTags()) {
				if (newTagsKeys.contains(oldTag)) {
					continue;
				} else {
					this.deleteUserTagForm(originalForm.getUser(), oldTag, originalForm.getKey());
				}
			}
		}
	}
	
	private void deleteUserTagForm(UUID user, UUID tag, UUID form) {
		tagDAO.delete(user, tag, form);
	}

	@Override
	public Tag getTagsByTagKey(UUID tagKey) {
		return tagDAO.getTag(tagKey);
	}	
}