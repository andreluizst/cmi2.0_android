package br.com.android.cmi.model;

import java.io.Serializable;

public class MainVisibleOptions implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 130L;
	
	public Boolean settings = true;
	public Boolean changePassword = null;
	public Boolean seekDoctors = null;
	public Boolean searchScheduledAppointments = null;
}