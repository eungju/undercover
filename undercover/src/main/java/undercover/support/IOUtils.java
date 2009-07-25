package undercover.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOUtils {
	public static void closeQuietly(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

	public static void closeQuietly(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

	public static void closeQuietly(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
	}

	public static void closeQuietly(Writer writer) {
        try {
            if (writer != null) {
            	writer.close();
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
