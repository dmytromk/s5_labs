package dmytromk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientServerTest {

    @Test
    public void testClientServerCommunication() {
        Thread serverThread = new Thread(() -> {
            Server.main(null);
        });
        serverThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Client.main(null);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        serverThread.interrupt();

        assertEquals("Hello from client!", Server.getReceivedObject().getMessage());
    }
}


