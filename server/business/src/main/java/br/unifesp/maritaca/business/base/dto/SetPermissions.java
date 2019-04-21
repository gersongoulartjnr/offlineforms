package br.unifesp.maritaca.business.base.dto;

import br.unifesp.maritaca.persistence.permission.Permission;

public class SetPermissions {
	private Permission aswPermission;
	
	private Permission formPermission;
	
	private Permission repoPermission;
	
	public Permission getAswPermission() {
		return aswPermission;
	}

	public void setAswPermission(Permission aswPermission) {
		this.aswPermission = aswPermission;
	}

	public Permission getFormPermission() {
		return formPermission;
	}

	public void setFormPermission(Permission formPermission) {
		this.formPermission = formPermission;
	}

	public Permission getRepoPermission() {
		return repoPermission;
	}

	public void setRepoPermission(Permission repoPermission) {
		this.repoPermission = repoPermission;
	}
}
