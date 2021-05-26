package com.zbh.jvm.hotspot.src.share.tools;

public class BytesConverter {

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();


    public static int toInt(byte[] bytes) throws Exception {
        return toInt(bytes, true);
    }

    public static int toInt(byte[] bytes, boolean isBig) throws Exception {
        if (bytes.length > 4) {
            throw new Exception("bytes length too long!");
        }

        int result = 0;

        int cursor = 0;

        if (isBig) {
            for (int i = bytes.length - 1; i >= 0; i--) {
                byte aByte = bytes[i];
                result |= aByte << cursor;
                cursor += 8;
            }
        } else {
            for (byte aByte : bytes) {
                result |= aByte << cursor;
                cursor += 8;
            }
        }


        return result;
    }


    public static int toInt(byte aByte) throws Exception {
        return toInt(new byte[]{aByte}, true);
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

    public static void main(String[] args) throws Exception {
        byte[] bytes = new byte[]{0, 22};
        System.out.println(toInt(bytes));


        bytes = new byte[]{-54, -2, -70, -66};

        System.out.println(toHexBinaryStr(bytes));


    }
}
