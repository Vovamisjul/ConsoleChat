package com.vovamisjul.chatlogic.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason="You dont have authorities to do it")
public class ForbiddenException extends RuntimeException {
}
