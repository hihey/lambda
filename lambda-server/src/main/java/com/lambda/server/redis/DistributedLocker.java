package com.lambda.server.redis;

import java.util.concurrent.TimeUnit;

import com.lambda.core.base.UnableToAquireLockException;

/**
 * 获取锁管理类
 */
public interface DistributedLocker {
 
    /**
     * 获取锁
     * @param lockKey 锁标识
     * @param worker 获取锁后的处理类
     * @param <T>
     * @return 处理完具体的业务逻辑要返回的数据
     * @throws UnableToAquireLockException
     * @throws Exception
     */
    <T> T lock(String lockKey, AquiredLockWorker<T> worker) throws UnableToAquireLockException,Exception;
 
    /**
     * @param lockKey 锁标识
     * @param worker 获取锁后的处理类
     * @param lockTime 指定加锁的时间。超过这个时间后锁便自动解开了。
     * @return 处理完具体的业务逻辑要返回的数据
     * @throws UnableToAquireLockException
     * @throws Exception
     */
    <T> T lock(String lockKey, AquiredLockWorker<T> worker, int lockTime) throws UnableToAquireLockException,Exception;
    
    /**
     * 加锁
     * @param lockKey 锁标识
     */
    void lock(String lockKey);

    /**
     * 加锁，超时解锁（单位：SECONDS）
     * @param lockKey 锁标识
     * @param timeout 指定加锁的时间。超过这个时间后锁便自动解开了。
     */
    void lock(String lockKey,int timeout);
    
    /**
     * 加锁，超时解锁
     * @param lockKey 锁标识
     * @param unit 时间单位
     * @param timeout 指定加锁的时间。超过这个时间后锁便自动解开了。
     */
    void lock(String lockKey,TimeUnit unit,int timeout);
    
    /**
     * 解锁
     * @param lockKey 锁标识
     */
    void unlock(String lockKey);
}