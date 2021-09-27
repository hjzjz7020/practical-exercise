package com.example.server.exception;

import org.springframework.http.HttpStatus;

/** @author Jingze Zheng */
public class InternalException extends Exception {
  private final HttpStatus httpStatus;

  public InternalException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return this.httpStatus;
  }
}
