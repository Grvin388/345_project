package src;

public class EmailNotification implements Notification {
    @Override
    public void send(String to, String message) {
        // TODO integrate email provider
        System.out.println("[EMAIL to " + to + "] " + message);
    }
}
