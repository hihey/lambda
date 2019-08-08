package com.lambda.core.base;

/**
 * 异常类（分布式锁）
 */
public class UnableToAquireLockException extends RuntimeException {
 
	private static final long serialVersionUID = 9030148257792355718L;

	public UnableToAquireLockException() {
    }
 
    public UnableToAquireLockException(String message) {
        super(message);
    }
 
    public UnableToAquireLockException(String message, Throwable cause) {
        super(message, cause);
    }
}