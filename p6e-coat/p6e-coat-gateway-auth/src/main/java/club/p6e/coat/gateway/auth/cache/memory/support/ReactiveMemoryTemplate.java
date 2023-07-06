package club.p6e.coat.gateway.auth.cache.memory.support;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ReactiveMemoryTemplate {

    protected final Map<String, Model> CACHE = new ConcurrentHashMap<>();

    @Data
    @Accessors(chain = true)
    public static class Model implements Serializable {
        private byte[] bytes;
        private long expire;
        private long date;

        public Model(byte[] bytes) {
            this.bytes = bytes;
            this.expire = -1;
            this.date = System.currentTimeMillis();
        }

        public Model(byte[] bytes, long expire) {
            this.bytes = bytes;
            this.expire = expire;
            this.date = System.currentTimeMillis();
        }
    }

    public boolean set(String key, Object value) {
        CACHE.put(key, new Model(serialize(value), -1));
        return true;
    }

    public boolean set(String key, Object value, long expire) {
        CACHE.put(key, new Model(serialize(value), expire));
        return true;
    }

    @SuppressWarnings("ALL")
    public <T> T get(String key, Class<T> clazz) {
        final Model model = CACHE.get(key);
        if (model == null) {
            return null;
        } else {
            if (model.getExpire() > 0
                    && System.currentTimeMillis() - model.getDate() > model.getExpire() * 1000) {
                del(key);
                return null;
            } else {
                return (T) deserialize(model.getBytes());
            }
        }
    }

    public long del(String key) {
        final Model model = CACHE.remove(key);
        if (model != null
                && model.getExpire() > 0
                && System.currentTimeMillis() - model.getDate() <= model.getExpire() * 1000) {
            return 1;
        } else {
            return 0;
        }
    }

    private static byte[] serialize(Object o) {
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(objectOutputStream);
            close(byteArrayOutputStream);
        }
        return new byte[0];
    }

    private static Object deserialize(byte[] bytes) {
        ObjectInputStream objectInputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(objectInputStream);
            close(byteArrayInputStream);
        }
        return new byte[0];
    }

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
