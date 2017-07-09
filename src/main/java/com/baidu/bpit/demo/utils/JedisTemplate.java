package com.baidu.bpit.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作工具类
 * Created by chenshouqin on 2017/7/8
 */
public class JedisTemplate {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RedisTemplate<byte[], byte[]> redisTemplate;

    public JedisTemplate(RedisTemplate<byte[], byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * redis set值
     *
     * @param key    key键
     * @param value  value值
     * @param expire 过期时间
     * @param <T>
     */
    public <T> void set(String key, T value, int expire) {
        if (!(value instanceof Serializable)) {
            throw new UnsupportedOperationException("value must implements serializable.");
        }
        try {
            byte[] binKey = ProtoStuffSerializer.serializer(key);
            byte[] binValue = ProtoStuffSerializer.serializer(value);
            if (expire > 0) {
                redisTemplate.opsForValue().set(binKey, binValue, expire, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(binKey, binValue);
            }
        } catch (Exception e) {
            logger.error("redis set data failed.", e);
        }
    }

    /**
     * redis 获取数据
     *
     * @param key   key
     * @param clazz 获取的类型
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<?> clazz) {
        if (!(clazz.isAssignableFrom(Serializable.class))) {
            throw new UnsupportedOperationException("value must implements serializable.");
        }
        try {
            byte[] binKey = ProtoStuffSerializer.serializer(key);
            byte[] data = redisTemplate.opsForValue().get(binKey);
            if (null == data) {
                return null;
            }
            return ProtoStuffSerializer.deserializer(data, clazz);
        } catch (Exception e) {
            logger.error("redis get data failed.", e);
            return null;
        }
    }

    /**
     * 删除某个Key
     *
     * @param key 待删除的Key
     */
    public void del(String key) {
        try {
            byte[] binKey = ProtoStuffSerializer.serializer(key);
            redisTemplate.delete(binKey);
        } catch (Exception e) {
            logger.error("delete redis key failed.", e);
        }
    }

    /**
     * 整体添加Hash表数据
     *
     * @param key
     * @param data
     * @param expire
     */
    public <K, V> void hmset(String key, Map<K, V> data, int expire) {
        try {
            Assert.notNull(data);
            Map<byte[], byte[]> convertData = new HashMap<byte[], byte[]>();
            Set<K> dataKeys = data.keySet();
            for (Object dataKey : dataKeys) {
                convertData.put(ProtoStuffSerializer.serializer(dataKey),
                        ProtoStuffSerializer.serializer(data.get(dataKey)));
            }
            byte[] binKey = ProtoStuffSerializer.serializer(key);
            redisTemplate.opsForHash().putAll(binKey, convertData);
            if (expire > 0) {
                redisTemplate.expire(binKey, expire, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error("redis hmset failed.", e);
        }
    }

    /**
     * 获取整个Hash表
     *
     * @param key      hash表的Key
     * @param keyClass hash表中每个Key的类型
     * @param valClass hash表中每个Key对应值的类型
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> Map<K, V> hmget(String key, Class<?> keyClass, Class<?> valClass) {
        try {
            Map<K, V> result = new HashMap<K, V>();
            byte[] binKey = ProtoStuffSerializer.serializer(key);
            Map<Object, Object> data = redisTemplate.opsForHash().entries(binKey);
            if (null == data) {
                return null;
            }
            Set<Object> dataKeys = data.keySet();
            for (Object dataKey : dataKeys) {
                K hashKey = ProtoStuffSerializer.deserializer((byte[]) dataKey, keyClass);
                V hashValue = ProtoStuffSerializer.deserializer((byte[]) data.get(dataKey), valClass);
                result.put(hashKey, hashValue);
            }
            return result;
        } catch (Exception e) {
            logger.error("redis hmget failed.", e);
            return null;
        }
    }

    /**
     * hash表中设置某一个值
     *
     * @param key     hash表的值
     * @param hashKey hash表的某一个Key
     * @param value   待设置的值
     */
    public <K, V> void hset(String key, K hashKey, V value) {
        try {
            byte[] binKey = ProtoStuffSerializer.serializer(key);
            byte[] binHashKey = ProtoStuffSerializer.serializer(hashKey);
            byte[] binValue = ProtoStuffSerializer.serializer(value);
            redisTemplate.opsForHash().put(binKey, binHashKey, binValue);
        } catch (Exception e) {
            logger.error("redis hset failed.", e);
        }
    }

    /**
     * Hash表中获取某一个值
     *
     * @param key
     * @param hashKey
     * @param clazz
     * @param <T>
     * @param <K>
     * @return
     */
    public <T, K> T hget(String key, K hashKey, Class<?> clazz) {
        try {
            byte[] binKey = ProtoStuffSerializer.serializer(key);
            byte[] binHashKey = ProtoStuffSerializer.serializer(hashKey);
            byte[] value = (byte[]) redisTemplate.opsForHash().get(binKey, binHashKey);
            if (null == value) {
                return null;
            }
            return ProtoStuffSerializer.deserializer(value, clazz);
        } catch (Exception e) {
            logger.error("redis hget failed.", e);
            return null;
        }
    }
}
