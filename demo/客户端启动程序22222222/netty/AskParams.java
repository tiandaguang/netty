package com.chat.server.netty;

import java.io.Serializable;

//请求类型参数
//必须实现序列化接口
public class AskParams implements Serializable {
  private static final long serialVersionUID = 1L;
  private String auth;

  public String getAuth() {
      return auth;
  }

  public void setAuth(String auth) {
      this.auth = auth;
  }
}