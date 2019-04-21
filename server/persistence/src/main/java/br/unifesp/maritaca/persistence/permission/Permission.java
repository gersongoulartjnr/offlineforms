package br.unifesp.maritaca.persistence.permission;


public class Permission {
	
	private Boolean read;
	private Boolean update;
	private Boolean remove;
	private Boolean share;
	
	public Permission(Operation... operations) {
		this.read   = false;
		this.update = false;
		this.remove = false;
		this.share  = false;
		for(Operation op : operations) {
			if(op == Operation.READ) 			{ this.read = true; }
			else if(op == Operation.UPDATE) 	{ this.update = true; }
			else if(op == Operation.DELETE) 	{ this.remove = true; }
			else if(op == Operation.SHARE) 		{ this.share = true; }			
		}
	}
	
	public static Permission defaultPermissions(){
		return new Permission(Operation.READ, Operation.UPDATE, Operation.DELETE, Operation.SHARE);
	}

	public Boolean getRead() {
		return read;
	}
	
	public void setRead(Boolean read) {
		this.read = read;
	}
	
	public Boolean getUpdate() {
		return update;
	}
	
	public void setUpdate(Boolean update) {
		this.update = update;
	}
	
	public Boolean getRemove() {
		return remove;
	}
	
	public void setRemove(Boolean remove) {
		this.remove = remove;
	}
	
	public Boolean getShare() {
		return share;
	}
	
	public void setShare(Boolean share) {
		this.share = share;
	}

	@Override
	public String toString() {
		return "Permission [read=" + read + ", update=" + update + ", remove="
				+ remove + ", share=" + share + "]";
	}
}