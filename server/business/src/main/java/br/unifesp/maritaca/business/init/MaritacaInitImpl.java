package br.unifesp.maritaca.business.init;

import java.io.File;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.business.util.FileDataManager;
import br.unifesp.maritaca.persistence.dao.impl.MaritacaInitDAO;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

@Service
public class MaritacaInitImpl implements MaritacaInit {

	private static final String TEMP_FILENAME = "test.tmp";
	
	@Autowired private MaritacaInitDAO maritacaInitDAO;
	
	@Override
	public void createAllEntities() {
		maritacaInitDAO.createAllEntities();
	}

	@Override
	public void createDirectories() {
		String fsPath = UtilsPersistence.retrieveConfigFile().getFilesystemPath();
		if (fsPath == null || fsPath.equals("")) {
			throw new RuntimeException("filesystem_path in configuration.properties is invalid!!");
		}
		checkPermissions(fsPath);
		maritacaInitDAO.createDirectory(fsPath, ComponentType.AUDIO.getValue());
		maritacaInitDAO.createDirectory(fsPath, ComponentType.VIDEO.getValue());
		maritacaInitDAO.createDirectory(fsPath, ComponentType.PICTURE.getValue());
		maritacaInitDAO.createDirectory(fsPath, ConstantsBusiness.APK_DIRNAME);
	}

	@Override
	public void close() {
		maritacaInitDAO.close();
	}
	
	private void checkPermissions(String fsPath) {
		String testFilename = fsPath + File.separator + TEMP_FILENAME;
		String testContent  = Base64.encodeBase64String("tmp".getBytes());
		FileDataManager.saveInLocalFromBase64(testContent, testFilename);
		FileDataManager.delete(testFilename);
	}
}