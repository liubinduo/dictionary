package com.v1ok.dictionary.db.service.impl.support;

import lombok.Data;

@Data
public class AMAPData {

  private String status;
  private String info;
  private String infocode;
  private String count;
  private Districts[] districts;

}
