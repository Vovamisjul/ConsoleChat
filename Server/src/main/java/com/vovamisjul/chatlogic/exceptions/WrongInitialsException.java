package com.vovamisjul.chatlogic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Wrong login or password")
public class WrongInitialsException extends RuntimeException {
}
