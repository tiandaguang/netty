package com.chat.server.netty;

public class ReplyServerBody extends ReplyBody {
   
	private static final long serialVersionUID = -8925568469288009798L;
	private String serverInfo;
    public ReplyServerBody(String serverInfo) {
        this.serverInfo = serverInfo;
    }
    public String getServerInfo() {
        return serverInfo;
    }
    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }
}