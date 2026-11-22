import java.util.Date;

public class Ticket {
    private String ticketID;
    private String ticketStatus;
    private boolean isUsed;
    private Date purchaseDate;
    private String ticketType;

    public Ticket(String ticketID, String ticketType) {
        this.ticketID = ticketID;
        this.ticketType = ticketType == null ? "" : ticketType.trim().toUpperCase();
        this.ticketStatus = "NOT ISSUED";
        this.isUsed = false;
        this.purchaseDate = null;
    }

    public void generateTicket() {
        if (ticketID != null && !ticketID.trim().isEmpty()
                && (ticketType.equals("GENERAL") || ticketType.equals("VIP"))) {
            ticketStatus = "VALID";
            purchaseDate = new Date();
        }
    }

    /** A used ticket stays used even after its attendee exits. */
    public void markAsUsed() {
        if (ticketStatus.equals("VALID") && !isUsed) {
            isUsed = true;
            ticketStatus = "USED";
        }
    }

    public String checkTicketStatus() {
        return ticketStatus;
    }

    public String getTicketID() {
        return ticketID;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public String getTicketType() {
        return ticketType;
    }
}