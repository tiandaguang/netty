package com.chat.server.netty;

public class ReplyMsg extends BaseMsg {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6632551097968442880L;

	public ReplyMsg() {
        super();
        setType(MsgType.REPLY);
    }
    private ReplyBody body;

    public ReplyBody getBody() {
        return body;
    }

    public void setBody(ReplyBody body) {
        this.body = body;
    }
}