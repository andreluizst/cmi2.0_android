package br.com.android.cmi.ws;

public class ResultAction {
	
	public static final boolean SHOW_MSG = true;
	public static final boolean NOT_SHOW_MSG = false;
	
	private Integer codResult;
	private Object obj;
	private boolean canShowMsg;

	public ResultAction() {
		canShowMsg = true;
	}
	
	public ResultAction(Integer codResult, Object obj){
		this();
		this.codResult = codResult;
		this.obj = obj;
	}
	
	public ResultAction(Integer codResult, Object obj, boolean canShowMsg){
		this.codResult = codResult;
		this.obj = obj;
		this.canShowMsg = canShowMsg;
	}
	
	public ResultAction(Integer codResult){
		this(codResult, null);
	}
	

	
	public Integer getCodResult() {
		return codResult;
	}

	public Object getObj() {
		return obj;
	}
	
	public boolean isCanShowMsg(){
		return canShowMsg;
	}

}
