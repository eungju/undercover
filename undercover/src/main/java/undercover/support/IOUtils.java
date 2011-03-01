package undercover.support;

import java.io.*;

public class IOUtils {
	public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

	public static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[4 * 1024];
		while (true) {
			int n = input.read(buffer, 0, buffer.length);
			if (n == -1) {
				break;
			}
			output.write(buffer, 0, n);
		}
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		copy(input, buffer);
		return buffer.toByteArray();
	}
}
