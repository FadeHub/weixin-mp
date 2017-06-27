package com.cooperate.wxh5.wx.redis.serializer;

import com.cooperate.wxh5.wx.redis.RedisException;
import com.cooperate.wxh5.wx.util.ByteSerializer;

public class CommonRedisJavaSerializer implements IRedisSerializer<Object> {

    static final byte[] EMPTY_ARRAY = new byte[0];

    @Override
    public byte[] serialize(Object t) {

        if (t == null) {
            return EMPTY_ARRAY;
        }
        try {
            return ByteSerializer.serialize(t);
        } catch (Exception ex) {
            throw new RedisException(-1, "Cannot serialize", ex);
        }
    }	

    @Override
    public Object deserialize(byte[] bytes) {

        if ((bytes == null || bytes.length == 0)) {
            return null;
        }

        try {
            return ByteSerializer.deSerialize(bytes);
        } catch (Exception ex) {
            throw new RedisException(-1, "Cannot serialize", ex);
        }
    }
}
