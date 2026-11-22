public class Attendee {
    private String attendeeID;
    private String name;
    private String contactNumber;
    private boolean isInside;
    private Ticket ticket;

    public Attendee(String attendeeID, String name, String contactNumber, Ticket ticket) {
        this.attendeeID = attendeeID;
        this.name = name;
        this.contactNumber = contactNumber;
        this.ticket = ticket;
        this.isInside = false;
    }

    public Ticket viewTicket() {
        return ticket;
    }

    public String checkEntryStatus() {
        return isInside ? "INSIDE the venue" : "OUTSIDE the venue";
    }

    public void setInside(boolean inside) {
        isInside = inside;
    }

    public String getAttendeeID() {
        return attendeeID;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
