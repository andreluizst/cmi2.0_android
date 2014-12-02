package br.com.android.cmi.model;

import java.io.Serializable;

public class Config implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;
	
	
	private long id;
	private String serverAddress;
	private boolean saveLogin;
	
	public Config() {
		
	}

	public Config(long id, String serverAddress, boolean saveLogin) {
		super();
		this.id = id;
		this.serverAddress = serverAddress;
		this.saveLogin = saveLogin;
	}
	
	public Config(String serverAddress, boolean saveLogin) {
		this(0, serverAddress, saveLogin);
	}
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public boolean isSaveLogin() {
		return saveLogin;
	}

	public void setSaveLogin(boolean saveLogin) {
		this.saveLogin = saveLogin;
	}

	@Override
	public String toString() {
		return "Config [id=" + id + ", serverAddress=" + serverAddress
				+ ", saveLogin=" + saveLogin + "]";
	}
	
	

}
