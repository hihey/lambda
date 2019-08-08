package com.lambda.server.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lambda.core.base.UnableToAquireLockException;

@Component
public class RedisLocker implements DistributedLocker{
 
    private final static String LOCKER_PREFIX = "lock:";
 
    @Autowired
    RedissonConnector redissonConnector;
    
    @Override
    public <T> T lock(String lockKey, AquiredLockWorker<T> worker) throws InterruptedException, UnableToAquireLockException, Exception {
        return lock(lockKey, worker, 100);
    }
    
    @Override
    public <T> T lock(String lockKey, AquiredLockWorker<T> worker, int lockTime) throws UnableToAquireLockException, Exception {
        RedissonClient redisson = redissonConnector.getClient();
        RLock lock = redisson.getLock(LOCKER_PREFIX + lockKey);
        
        boolean success = lock.tryLock(100, lockTime, TimeUnit.SECONDS);//Wait for 100 seconds seconds and automatically unlock it after lockTime seconds
        if (success) {
            try {
                return worker.invokeAfterLockAquire();
            } finally {
                lock.unlock();
            }
        }
        throw new UnableToAquireLockException();
    }
    
    @Override
    public void lock(String lockKey) {
    	RedissonClient redisson = redissonConnector.getClient();
        RLock lock = redisson.getLock(LOCKER_PREFIX + lockKey);
        lock.lock();
    }

    @Override
    public void lock(String lockKey,int leaseTime) {
    	RedissonClient redisson = redissonConnector.getClient();
        RLock lock = redisson.getLock(LOCKER_PREFIX + lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
    }
    
    @Override
    public void lock(String lockKey,TimeUnit unit,int timeout) {
    	RedissonClient redisson = redissonConnector.getClient();
        RLock lock = redisson.getLock(LOCKER_PREFIX + lockKey);
        lock.lock(timeout, unit);
    }
    
    @Override
    public void unlock(String lockKey) {
    	RedissonClient redisson = redissonConnector.getClient();
        RLock lock = redisson.getLock(LOCKER_PREFIX + lockKey);
        lock.unlock();
    }
}