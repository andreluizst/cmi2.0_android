package br.com.android.cmi.ws;

public class WSMessage {
	
	public static final int USER_NOT_FOUND = 1;
    public static final int INVALID_USER_NAME_OR_PASSWORD = 2;
    public static final int BUSINESS_ROLES_EXCEPTION = 3;
    public static final int USER_NAME_MUST_CONTAIN_5_TO_15_CARACTERS = 4;
    public static final int USER_PASSWORD_MUST_CONTAIN_5_TO_15_CARACTERS = 5;
    public static final int USER_EXISTS = 6;
    
    public static final int OK = 200;
	
	private Integer code;
	private Integer serverCode;
	private String message;
	
	
	public WSMessage() {
	}
	
	public WSMessage(Integer code, Integer serverCode, String message) {
		super();
		this.code = code;
		this.serverCode = serverCode;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getServerCode() {
		return serverCode;
	}

	public void setServerCode(Integer serverCode) {
		this.serverCode = serverCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	
	@Override
	public String toString() {
		return "code=" + code + "\n serverCode=" + serverCode
				+ "\n message=" + message;
	}
	
	
}
