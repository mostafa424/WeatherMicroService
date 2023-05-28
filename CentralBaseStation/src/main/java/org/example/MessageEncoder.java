package org.example;

import java.nio.ByteBuffer;

public class MessageEncoder {

    public byte[] encodeClose() {
        byte[] methodBytes = ByteBuffer.allocate(Character.SIZE / Byte.SIZE).putChar((char)3).array();
        return methodBytes;
    }

    public byte[] encodeGet(int key) {
        byte[] methodBytes = ByteBuffer.allocate(Character.SIZE / Byte.SIZE).putChar((char)1).array();
        byte[] keyBytes = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(key).array();
        byte[] bytes = new byte[methodBytes.length + keyBytes.length];
        int ctr = 0;
        for(int i = 0; i < methodBytes.length; i++) {
            bytes[ctr++] = methodBytes[i];
        }
        for(int i = 0; i < keyBytes.length; i++) {
            bytes[ctr++] = keyBytes[i];
        }
        return bytes;
    }

    public byte[] encodePut(int key, Status status) {
        byte[] methodBytes = ByteBuffer.allocate(Character.SIZE / Byte.SIZE).putChar((char)2).array();
        byte[] keyBytes = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(key).array();
        byte[] statusBytes = StatusUtil.getBytes(status);
        byte[] statusLenBytes = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(statusBytes.length).array();
        byte[] bytes = new byte[statusBytes.length + keyBytes.length + methodBytes.length + statusLenBytes.length];
        int ctr = 0;
        for(int i = 0; i < methodBytes.length; i++) {
            bytes[ctr++] = methodBytes[i];
        }
        for(int i = 0; i < keyBytes.length; i++) {
            bytes[ctr++] = keyBytes[i];
        }
        for(int i = 0; i < statusLenBytes.length; i++) {
            bytes[ctr++] = statusLenBytes[i];
        }
        for(int i = 0; i < statusBytes.length; i++) {
            bytes[ctr++] = statusBytes[i];
        }
        return bytes;
    }

    public byte[] encodeOpen(String path) throws Exception {
        byte[] methodBytes = ByteBuffer.allocate(Character.SIZE / Byte.SIZE).putChar((char)0).array();
        byte[] pathBytes = path.getBytes("UTF-8");
        byte[] pathLenBytes = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(pathBytes.length).array();
        byte[] bytes = new byte[methodBytes.length + pathLenBytes.length + pathBytes.length];
        int ctr = 0;
        for(int i = 0; i < methodBytes.length; i++) {
            bytes[ctr++] = methodBytes[i];
        }
        for(int i = 0; i < pathLenBytes.length; i++) {
            bytes[ctr++] = pathLenBytes[i];
        }
        for(int i = 0; i < pathBytes.length; i++) {
            bytes[ctr++] = pathBytes[i];
        }
        return bytes;
    }

    
}
