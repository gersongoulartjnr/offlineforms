package br.unifesp.maritaca.business.form;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.business.util.FileDataManager;
import br.unifesp.maritaca.business.util.UtilsBusiness;
import br.unifesp.maritaca.persistence.dao.FormDAO;
import br.unifesp.maritaca.persistence.entity.Form;
import br.unifesp.maritaca.persistence.util.ConfigurationFile;
import br.unifesp.maritaca.persistence.util.ConstantsPersistence;

@Service
@Scope("prototype")
public class ApkBuilder implements Runnable {
	
	private static Logger logger = Logger.getLogger(ApkBuilder.class);
	
	private FormDTO formDTO;
	private ConfigurationFile configFileDTO;
	
	private String pathFilename;
	
	@Autowired
	private FormDAO formDAO;
	
	public ApkBuilder() { }
	
	public ApkBuilder(FormDTO formDTO, ConfigurationFile configFileDTO) {
		if (formDTO == null || configFileDTO == null) {
			throw new RuntimeException("ApkBuilder: formDTO or configFileDTO shouldn't have null value.");
		}
		this.setFormDTO(formDTO);
		this.setConfigFileDTO(configFileDTO);
	}

	@Override
	public void run() {
		logger.info("Building APK for: " + getFormDTO().getTitle());
		this.buildNewApk(getFormDTO(), getConfigFileDTO());
	}
	
	private void buildNewApk(FormDTO formDTO, ConfigurationFile configFileDTO) {		
		try {
			Form form = formDAO.getFormByKey(formDTO.getKey(), false);
			form.setBuildingApk(true);
			formDAO.saveForm(form);
			
			String scriptLocation	= configFileDTO.getScriptLocation();
			String maritacaPath 	= configFileDTO.getMaritacaMobileFolder();
			String projectsPath 	= configFileDTO.getMaritacaMobileProjects();
			String androidSDKPath 	= configFileDTO.getAndroidSdkPath();
			String uriServer		= configFileDTO.getUriServer();
			String releaseScript	= configFileDTO.getReleaseScriptLocation();
			String packageName = ConstantsBusiness.PREFIX_FORM_NAME+formDTO.getUrl();

			// delete file from file system
			pathFilename = configFileDTO.getFilesystemPath() + File.separator + UtilsBusiness.buildApkPathFS(formDTO.getUrl());
			FileDataManager.delete(pathFilename);
			
			List<String> commands = new ArrayList<String>();
			commands.add(scriptLocation);		
			commands.add(packageName);					// PACKAGE_NAME 
			commands.add(formDTO.getTitle());			// APP_NAME
			commands.add(maritacaPath);					// MARITACA_MOBILE_PATH
			commands.add(projectsPath);					// MARITACA_MOBILE_APPS
			commands.add(uriServer);					// URI_SERVER
			commands.add(formDTO.getDescription()!=null ? formDTO.getDescription() : "");	// APP_DESCRIPTION
			commands.add(formDTO.getKey().toString());	// FORM_ID
			ProcessBuilder processBuilder = new ProcessBuilder(commands);
			Process process = processBuilder.start();
	
			process.waitFor();
			//Execute release script
			String pathFilenme = maritacaPath + ConstantsPersistence.MARITACA_MOBILE_PROJECTS + packageName + ConstantsBusiness.MOB_FORM_XML_PATH;
			FileDataManager.saveInLocalFromBase64(Base64.encodeBase64String(formDTO.getXml().getBytes()), pathFilenme);
			this.createAppLogo(formDTO.getImage(), maritacaPath+ConstantsPersistence.MARITACA_MOBILE_PROJECTS+packageName+ConstantsBusiness.MOB_FORM_LOGO_PATH);
			
			commands = new ArrayList<String>(1);
			commands.add(releaseScript);
			commands.add(projectsPath);
			commands.add(packageName);
			commands.add(androidSDKPath);
			processBuilder = new ProcessBuilder(commands);
			process = processBuilder.start();
			
			String s = "";
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((s = stdInput.readLine()) != null || (s = stdError.readLine()) != null) {
				logger.info(s);
			}
			
			process.waitFor();			
			this.saveApkInFileSystem(formDTO.getUrl(), configFileDTO);
			
			form = formDAO.getFormByKey(formDTO.getKey(), false);
			form.setBuildingApk(false);
			formDAO.saveForm(form);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void createAppLogo(String imgBase64, String appLogoPath) {
		try {
			if(imgBase64 != null && !"".equals(imgBase64)) {
				File icon = new File(appLogoPath);
				OutputStream out = new FileOutputStream(icon);
				out.write(Base64.decodeBase64(imgBase64));
				out.close();
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	private void saveApkInFileSystem(String formUrl, ConfigurationFile configFileDTO) {
		logger.info("saving apk: formUrl=" + formUrl + "\tpathFS=" + configFileDTO.getFilesystemPath());
		String packageName	= ConstantsBusiness.PREFIX_FORM_NAME + formUrl;
		String projectsPath = configFileDTO.getMaritacaMobileProjects();
		String filePath 	= projectsPath + packageName + ConstantsBusiness.MOB_BIN_PATH + ConstantsBusiness.APK_NAME;
		File file = new File(filePath);
		if(file.isFile()) {
			String apkInBase64 = FileDataManager.read(filePath);
			FileDataManager.saveInLocalFromBase64(apkInBase64, pathFilename);
			logger.info("thread: job finished");
		} else {
			throw new RuntimeException("error in APK building process");
		}
	}

	public FormDTO getFormDTO() {
		return formDTO;
	}

	public void setFormDTO(FormDTO formDTO) {
		this.formDTO = formDTO;
	}

	public ConfigurationFile getConfigFileDTO() {
		return configFileDTO;
	}

	public void setConfigFileDTO(ConfigurationFile configFileDTO) {
		this.configFileDTO = configFileDTO;
	}
}