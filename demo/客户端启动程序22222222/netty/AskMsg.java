package com.chat.server.netty;


public class AskMsg extends BaseMsg {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4290671570410372300L;

	public AskMsg() {
        super();
        setType(MsgType.ASK);
    }
    private AskParams params;

    public AskParams getParams() {
        return params;
    }

    public void setParams(AskParams params) {
        this.params = params;
    }
}
