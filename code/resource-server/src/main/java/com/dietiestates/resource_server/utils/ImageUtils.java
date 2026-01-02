package com.dietiestates.resource_server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {

    public static byte[] compressImage(byte[] data) throws IOException {
        try (Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {

            deflater.setInput(data);
            deflater.finish();

            byte[] tmp = new byte[4 * 1024];
            while (!deflater.finished()) {
                int size = deflater.deflate(tmp);
                outputStream.write(tmp, 0, size);
            }
            return outputStream.toByteArray();
        }
    }

    public static byte[] decompressImage(byte[] data) {
        try (Inflater inflater = new Inflater();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {

            inflater.setInput(data);

            byte[] tmp = new byte[4 * 1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);

                if (count == 0) {
                    if (inflater.needsDictionary()) {
                        throw new IllegalArgumentException("Compressed data requires a preset dictionary");
                    }
                    if (inflater.needsInput()) {
                        throw new IllegalArgumentException("Compressed data is truncated or corrupted");
                    }
                }

                outputStream.write(tmp, 0, count);
            }
            return outputStream.toByteArray();

        } catch (DataFormatException | IOException e) {
            throw new IllegalArgumentException("Compressed data is not in a valid zlib format", e);
        }
    }
}
