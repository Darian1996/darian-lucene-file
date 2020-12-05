package com.darian.darianlucenefile.utils;



import java.io.*;
import java.util.List;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/4/13  1:29
 */
public class CustomerFileUtils {



    public static String readFileToString(File file, String encoding) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return CustomerIOUtils.toString(in, encoding);
        } finally {
            CustomerIOUtils.closeQuietly(in);
        }
    }

    public static byte[] readFileToByteArray(final File file) throws IOException {
        try (InputStream in = openInputStream(file)) {
            final long fileLength = file.length();
            // file.length() may return 0 for system-dependent entities, treat 0 as unknown length - see IO-453
            return fileLength > 0 ? CustomerIOUtils.toByteArray(in, fileLength) : CustomerIOUtils.toByteArray(in);
        }
    }



    public static List readLines(File file, String encoding) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return CustomerIOUtils.readLines(in, encoding);
        } finally {
            CustomerIOUtils.closeQuietly(in);
        }
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }
}
