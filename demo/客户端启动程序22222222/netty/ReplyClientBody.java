package com.chat.server.netty;

public class ReplyClientBody extends ReplyBody {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5963041743376012387L;
	private String clientInfo;

    public ReplyClientBody(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }
}
