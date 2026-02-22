public class SMSNotification implements Notification {
    @Override
    public void send(String to, String message) {
        // TODO integrate SMS provider
        System.out.println("[SMS to " + to + "] " + message);
    }
}
