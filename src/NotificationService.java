package src;

public class NotificationService {
    private final Notification emailNotifier;
    private final Notification smsNotifier;

    public NotificationService(Notification emailNotifier, Notification smsNotifier) {
        this.emailNotifier = emailNotifier;
        this.smsNotifier = smsNotifier;
    }

    public void sendConfirmation(User user, Reservation res) {
        String msg = "Reservation confirmed: " + res.getReservationId()
                + " for event " + res.getEventId()
                + " (qty=" + res.getQuantity() + ")";
        notifyUser(user, msg);
    }

    public void sendCancellation(User user, Reservation res) {
        String msg = "Reservation cancelled: " + res.getReservationId()
                + " for event " + res.getEventId();
        notifyUser(user, msg);
    }

    private void notifyUser(User user, String message) {
        // send to whichever contact info exists
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            emailNotifier.send(user.getEmail(), message);
        }
        if (user.getPhone() != null && !user.getPhone().isBlank()) {
            smsNotifier.send(user.getPhone(), message);
        }
    }
}