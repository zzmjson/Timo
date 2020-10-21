//package com.linln.modules.system.service.impl;
//
//
//import com.linln.modules.system.service.RedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.*;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.stereotype.Service;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class RedisServiceImpl implements RedisService {
//
//    private final RedisTemplate redisTemplate;
//
//    @Autowired
//    public RedisServiceImpl(RedisTemplate redisTemplate) {
//        RedisSerializer stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setValueSerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setHashValueSerializer(stringSerializer);
//        this.redisTemplate = redisTemplate;
//    }
//
//    /**
//     * 写入缓存
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    @Override
//    public boolean set(String key, Object value) {
//        boolean result = false;
//        try {
//            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//            operations.set(key, value);
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 写入缓存设置时效时间
//     *
//     * @param key
//     * @param value
//     * @param expireTime
//     * @return
//     */
//    @Override
//    public boolean set(String key, Object value, Long expireTime) {
//        boolean result = false;
//        try {
//            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//            operations.set(key, value);
//            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 指定缓存失效时间
//     *
//     * @param key  键
//     * @param time 时间(秒)
//     * @return
//     */
//    @Override
//    public boolean expire(String key, long time) {
//        try {
//            if (time > 0) {
//                redisTemplate.expire(key, time, TimeUnit.SECONDS);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 批量删除对应的value
//     *
//     * @param keys
//     */
//    @Override
//    public void remove(final String... keys) {
//        for (String key : keys) {
//            remove(key);
//        }
//    }
//
//    /**
//     * 批量删除key
//     *
//     * @param pattern
//     */
//    @Override
//    public void removePattern(final String pattern) {
//        Set<Serializable> keys = redisTemplate.keys(pattern);
//        if (keys.size() > 0) {
//            redisTemplate.delete(keys);
//        }
//    }
//
//    /**
//     * 删除对应的value
//     *
//     * @param key
//     */
//    @Override
//    public void remove(final String key) {
//        if (exists(key)) {
//            redisTemplate.delete(key);
//        }
//    }
//
//    /**
//     * 判断缓存中是否有对应的value
//     *
//     * @param key
//     * @return
//     */
//    @Override
//    public boolean exists(final String key) {
//        return redisTemplate.hasKey(key);
//    }
//
//    /**
//     * 读取缓存
//     *
//     * @param key
//     * @return
//     */
//    @Override
//    public Object get(final String key) {
//        Object result = null;
//        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//        result = operations.get(key);
//        return result;
//    }
//
//    /**
//     * 哈希 添加
//     *
//     * @param key
//     * @param hashKey
//     * @param value
//     */
//    @Override
//    public void hmSet(String key, Object hashKey, Object value) {
//        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
//        hash.put(key, hashKey, value);
//    }
//
//    /**
//     * 哈希获取数据
//     *
//     * @param key
//     * @param hashKey
//     * @return
//     */
//    @Override
//    public Object hmGet(String key, Object hashKey) {
//        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
//        return hash.get(key, hashKey);
//    }
//
//    /**
//     * 列表添加
//     *
//     * @param k
//     * @param v
//     */
//    @Override
//    public void lPush(String k, Object v) {
//        ListOperations<String, Object> list = redisTemplate.opsForList();
//        list.rightPush(k, v);
//    }
//
//    /**
//     * 列表获取
//     *
//     * @param k
//     * @param l
//     * @param l1
//     * @return
//     */
//    @Override
//    public List<Object> lRange(String k, long l, long l1) {
//        ListOperations<String, Object> list = redisTemplate.opsForList();
//        return list.range(k, l, l1);
//    }
//
//    /**
//     * 集合添加
//     *
//     * @param key
//     * @param value
//     */
//    @Override
//    public void add(String key, Object value) {
//        SetOperations<String, Object> set = redisTemplate.opsForSet();
//        set.add(key, value);
//    }
//
//    /**
//     * 集合获取
//     *
//     * @param key
//     * @return
//     */
//    @Override
//    public Set<Object> setMembers(String key) {
//        SetOperations<String, Object> set = redisTemplate.opsForSet();
//        return set.members(key);
//    }
//
//    /**
//     * 有序集合添加
//     *
//     * @param key
//     * @param value
//     * @param scoure
//     */
//    @Override
//    public void zAdd(String key, Object value, double scoure) {
//        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
//        zset.add(key, value, scoure);
//    }
//
//    /**
//     * 有序集合获取
//     *
//     * @param key
//     * @param scoure
//     * @param scoure1
//     * @return
//     */
//    @Override
//    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
//        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
//        return zset.rangeByScore(key, scoure, scoure1);
//    }
//
//    /**
//     * 将list放入缓存
//     *
//     * @param key   键
//     * @param value 值
//     * @return
//     */
//    @Override
//    public boolean lSet(String key, List<Object> value) {
//        try {
//            redisTemplate.opsForList().rightPushAll(key, value);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 将list放入缓存
//     *
//     * @param key   键
//     * @param value 值
//     * @param time  时间(秒)
//     * @return
//     */
//    @Override
//    public boolean lSet(String key, List<Object> value, long time) {
//        try {
//            redisTemplate.opsForList().rightPushAll(key, value);
//            if (time > 0) {
//                expire(key, time);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}