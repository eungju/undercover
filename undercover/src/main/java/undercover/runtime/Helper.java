package undercover.runtime;

import java.io.Closeable;
import java.io.IOException;

public class Helper {
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
