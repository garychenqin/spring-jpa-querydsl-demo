package com.baidu.bpit.demo.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ProtoStuff序列化与反序列化
 * Created by chenshouqin on 2017/7/8
 */
public class ProtoStuffSerializer {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

    /**
     * 返回缓存的schema
     *
     * @param clazz
     * @return
     */
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
        if (null == schema) {
            schema = RuntimeSchema.getSchema(clazz);
            cachedSchema.putIfAbsent(clazz, schema);
        }
        return schema;
    }

    /**
     * 序列化
     *
     * @param obj 待序列化的对象
     * @param <T>
     * @return
     */
    public static <T> byte[] serializer(T obj) throws Exception {
        Assert.notNull(obj);
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化
     *
     * @param data  待反序列化的数据
     * @param clazz 待转换的类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T deserializer(byte[] data, Class<?> clazz) throws Exception {
        Assert.notNull(data);
        Assert.notNull(clazz);
        T obj = (T) clazz.newInstance();
        Schema<T> schema = getSchema((Class<T>) clazz);
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }
}
