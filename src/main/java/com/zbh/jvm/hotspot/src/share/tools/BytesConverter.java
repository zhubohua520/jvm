package com.zbh.jvm.hotspot.src.share.tools;

import java.nio.ByteBuffer;

public class BytesConverter {

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();


    public static int toInt(byte[] bytes) throws Exception {
        return toInt(bytes, true);
    }

    public static float toFloat(byte[] bytes) {

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        return bb.getFloat();
    }

    //默认大端
    public static char toChar(byte[] bytes) {

        ByteBuffer bb = ByteBuffer.wrap(bytes);

        char c;

        if (bytes.length > 2) {
            c = bb.getChar(bytes.length - 2);
        } else if (bytes.length == 2) {
            c = bb.getChar();
        } else if (bytes.length == 1) {
            bb = ByteBuffer.wrap(new byte[]{0, bytes[0]});
            c = bb.getChar();
        } else {
            c = 0;
        }


        return c;
    }


    public static byte[] toBytes(float f) {
        return toBytes(f, true);
    }

    public static byte[] toBytes(float f, boolean isBig) {

        int castInt = Float.floatToIntBits(f);

        return toBytes(castInt, isBig);

    }

    public static byte[] toBytes(short s) {
        return toBytes(s, true);
    }

    public static byte[] toBytes(short s, boolean isBig) {

        byte[] bytes = new byte[2];


        if (isBig) {
            int max = 16 - 8;
            for (int i = 0; i < 2; i++) {
                bytes[i] = ((byte) ((s >> (max - i * 8)) & 0xFF));
            }
        } else {
            for (int i = 0; i < 2; i++) {
                bytes[i] = (byte) ((s >> i * 8) & 0xFF);
            }
        }


        return bytes;
    }

    public static byte[] toBytes(int i) {
        return toBytes(i, true);
    }

    public static byte[] toBytes(int sourceInt, boolean isBig) {
        byte[] bytes = new byte[4];


        if (isBig) {
            int max = 32 - 8;
            for (int i = 0; i < 4; i++) {
                bytes[i] = ((byte) ((sourceInt >> (max - i * 8)) & 0xFF));
            }
        } else {
            for (int i = 0; i < 4; i++) {
                bytes[i] = (byte) ((sourceInt >> i * 8) & 0xFF);
            }
        }


        return bytes;
    }

    //long 最大8个字节
    public static long toLong(byte[] bytes) {


        ByteBuffer bb = ByteBuffer.allocate(8);

        if (bytes.length < 8) {
            bb.put(new byte[8 - bytes.length]);
            bb.put(bytes);
            bb.position(0);
            return bb.getLong();
        } else if (bytes.length > 8) {
            bb.put(bytes, bytes.length - 8, 8);
            bb.position(0);
            return bb.getLong(bytes.length - 8);
        } else {
            bb.put(bytes);
            bb.position(0);
            return bb.getLong();
        }



    }

    public static double toDouble(byte[] bytes) {

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, 0, 8);
        return byteBuffer.getDouble();
    }


    public static int toInt(byte[] bytes, boolean isBig) throws Exception {
        if (bytes.length > 4) {
            throw new Exception("bytes length too long!");
        }

        ByteBuffer bb = ByteBuffer.allocate(4);

        if (isBig) {
            if (bytes.length < 4) {
                bb.put(new byte[4 - bytes.length]);
                bb.put(bytes);
                bb.position(0);
                return bb.getInt();
            } else if (bytes.length > 4) {
                bb.put(bytes, bytes.length - 4, 4);
                bb.position(0);
                return bb.getInt(bytes.length - 4);
            } else {
                bb.put(bytes);
                bb.position(0);
                return bb.getInt();
            }
        } else {
            throw new Exception("暂时不支持小端");
        }

    }


    public static int toInt(byte aByte) throws Exception {
        return toInt(new byte[]{aByte}, true);
    }

    public static short toShort(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        return bb.getShort();
    }

    public static boolean toBoolean(byte[] bytes) {

        return bytes[0] != 0;
    }

    public static int toUnsignedInt(byte aByte) {

        return Byte.toUnsignedInt(aByte);
    }

    //没试过，可能有问题
    public static int toUnsignedInt(byte[] bytes) {

        return (bytes[0] & 0xFF << 8) | (bytes[1] & 0xFF);
    }


    public static String toHexBinaryStr(byte[] bytes) {

        return toHexBinaryStr(bytes, true);
    }

    public static String toHexBinaryStr(byte[] bytes, boolean isBig) {

        StringBuilder sb = new StringBuilder(bytes.length * 2);

        for (byte aByte : bytes) {

            int i = (aByte >> 4) & 0xf;
            sb.append(hexCode[i]);
            i = aByte & 0xf;
            sb.append(hexCode[i]);
        }


        if (isBig) {
            return sb.toString();
        } else {
            return sb.reverse().toString();
        }
    }

    //默认大端
    public static byte[] toBytes(double d) {
        return toBytes(d, true);
    }

    public static byte[] toBytes(long l) {
        return toBytes(l, true);
    }

    public static byte[] toBytes(long l, boolean isBig) {


        byte[] bytes = new byte[8];

        if (isBig) {
            int max = 64 - 8;
            for (int i = 0; i < 8; i++) {
                bytes[i] = ((byte) ((l >> (max - i * 8)) & 0xFF));
            }
        } else {
            for (int i = 0; i < 8; i++) {
                bytes[i] = (byte) ((l >> i * 8) & 0xFF);
            }
        }

        return bytes;
    }

    public static byte[] toBytes(boolean b) {


        byte[] bytes = new byte[1];

        if (b) {
            bytes[0] = 1;
        } else {
            bytes[0] = 0;
        }

        return bytes;
    }

    public static byte[] toBytes(double d, boolean isBig) {

        long l = Double.doubleToLongBits(d);

        return toBytes(l, isBig);

    }


    public static void main(String[] args) throws Exception {
        byte[] bytes = new byte[]{0, 22};
        System.out.println(toInt(bytes));


        bytes = new byte[]{-54, -2, -70, -66};

        System.out.println(toHexBinaryStr(bytes));


    }
}
