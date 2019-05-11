package br.unifesp.offlineforms.mobile.dataaccess.model;

import java.io.Serializable;

public class MaritacaSetting implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean syncActive;
	private Integer syncInterval;
	private boolean collectaDataList;
	private boolean sendingAuto;
	
	public MaritacaSetting(boolean syncActive, Integer syncInterval,
			boolean collectaDataList, boolean sendingAuto) {
		this.syncActive = syncActive;
		this.syncInterval = syncInterval;
		this.setCollectaDataList(collectaDataList);
		this.sendingAuto = sendingAuto;
	}

	public boolean isSyncActive() {
		return syncActive;
	}
	public void setSyncActive(boolean syncActive) {
		this.syncActive = syncActive;
	}

	public Integer getSyncInterval() {
		return syncInterval;
	}
	public void setSyncInterval(Integer syncInterval) {
		this.syncInterval = syncInterval;
	}

	public boolean isCollectaDataList() {
		return collectaDataList;
	}
	public void setCollectaDataList(boolean collectaDataList) {
		this.collectaDataList = collectaDataList;
	}
	
	public boolean isSendingAuto() {
		return sendingAuto;
	}
	public void setSendingAuto(boolean sendingAuto) {
		this.sendingAuto = sendingAuto;
	}
}