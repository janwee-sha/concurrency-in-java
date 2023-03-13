package interrupting;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * cancel non-interruptable blocking operation by altering interrupt method
 */
public class CancelByAlteringInterrupt {
    class ReaderThread extends Thread {
        private static final int BUFSZ = 1024;
        private final Socket socket;
        private final InputStream in;

        ReaderThread(Socket socket) throws IOException {
            this.socket = socket;
            this.in = socket.getInputStream();
        }

        public void interrupt() {
            try {
                socket.close();
            } catch (IOException e) {
                super.interrupt();
            }
        }

        public void run() {
            //...
        }
    }
}
