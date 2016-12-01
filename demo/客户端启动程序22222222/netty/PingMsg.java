package com.chat.server.netty;

public class PingMsg extends BaseMsg {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3108797784678355973L;

	public PingMsg() {
        super();
        setType(MsgType.PING);
    }
}