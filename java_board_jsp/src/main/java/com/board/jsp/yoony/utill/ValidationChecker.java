package com.board.jsp.yoony.utill;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ValidationChecker {

  private Logger logger = LogManager.getLogger(ValidationChecker.class);
  private static ValidationChecker validationChecker = new ValidationChecker();

  private ValidationChecker() {
  }

  public static ValidationChecker getInstance() {
    return validationChecker;
  }

  public boolean CheckStringIsNullOrEmpty(String targetString){
    return targetString == null || "".equals(targetString) || targetString.isEmpty();
  }
  public boolean CheckStringIsNullOrEmpty(Object targetObject){
    return targetObject == null || "".equals(targetObject);
  }
}
