package com.cooperate.wxh5.wx.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteSerializer {

    private final static Logger serializerLogger = LoggerFactory.getLogger(ByteSerializer.class);

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bis = null;
        ObjectOutputStream os = null;
        byte[] byteArray = (byte[]) null;
        try {
            bis = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bis);
            os.writeObject(obj);
            os.flush();
            byteArray = bis.toByteArray();
        } catch (IOException e) {
            CommonLogUtil.error(e, serializerLogger, "Object convert to bytes array fail:object={0}", obj);
            throw e;
        } finally {
            closeOutputStream(os, bis);
        }
        return byteArray;
    }

    public static Object deSerialize(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bos = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            bos = new ByteArrayInputStream(byteArray);
            ois = new ObjectInputStream(bos);
            obj = ois.readObject();
        } catch (IOException e) {
            CommonLogUtil.error(e, serializerLogger, "bytes array convert to object fail:byteArray={0}", byteArray);
            throw e;
        } catch (ClassNotFoundException e) {
            CommonLogUtil.error(e, serializerLogger, "bytes array convert to object fail:byteArray={0}", byteArray);
            throw e;
        } finally {
            closeInputStream(bos, ois);
        }
        return obj;
    }

    private static void closeOutputStream(ObjectOutputStream os, ByteArrayOutputStream bis) throws IOException {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                CommonLogUtil.error(e, serializerLogger, "close objectOutputStream fail:object={0}", os);
                throw e;
            }
        }
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                CommonLogUtil.error(e, serializerLogger, "close byteArrayOutputStream fail:object={0}", os);
                throw e;
            }
        }
    }

    private static void closeInputStream(ByteArrayInputStream bos, ObjectInputStream ois) throws IOException {
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e) {
                CommonLogUtil.error(e, serializerLogger, "close objectInputStream fail:object={0}", bos);
                throw e;
            }
        }
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                CommonLogUtil.error(e, serializerLogger, "close byteArrayInputStream fail:object={0}", ois);
                throw e;
            }
        }
    }

}
