public class Gate {
    private int gateID;
    private String gateStatus;
    private String gateCategory;
    private String gateType;
    private TicketValidator validator;
    private OccupancyMonitor monitor;
    private String lastScannedTicketID;

    public Gate(int gateID, String gateCategory, String gateType,
                TicketValidator validator, OccupancyMonitor monitor) {
        if (gateID < 1 || gateID > 10) {
            throw new IllegalArgumentException("Gate ID must be between 1 and 10.");
        }
        String category = gateCategory == null ? "" : gateCategory.trim().toUpperCase();
        String type = gateType == null ? "" : gateType.trim().toUpperCase();
        if (!category.equals("GENERAL") && !category.equals("VIP")) {
            throw new IllegalArgumentException("Gate category must be GENERAL or VIP.");
        }
        if (!type.equals("ENTRY") && !type.equals("EXIT") && !type.equals("BOTH")) {
            throw new IllegalArgumentException("Gate type must be ENTRY, EXIT, or BOTH.");
        }
        if (validator == null || monitor == null) {
            throw new IllegalArgumentException("Gate requires a validator and occupancy monitor.");
        }
        this.gateID = gateID;
        this.gateCategory = category;
        this.gateType = type;
        this.validator = validator;
        this.monitor = monitor;
        this.gateStatus = "CLOSED";
        this.lastScannedTicketID = null;
    }

    public String scanTicket(String ticketID) {
        lastScannedTicketID = null;
        if (ticketID == null || ticketID.trim().isEmpty()) {
            closeGate();
            return "ENTRY REJECTED: Ticket ID is required.";
        }
        if (!gateType.equals("ENTRY") && !gateType.equals("BOTH")) {
            closeGate();
            return "ENTRY REJECTED: This gate cannot be used for entry.";
        }
        if (validator.checkDuplicateTicket(ticketID)
                || !validator.validateTicket(ticketID)
                || !validator.validateEntranceGate(ticketID, gateCategory, gateType)
                || !validator.checkTicketAvailability(monitor)) {
            closeGate();
            return "ENTRY REJECTED: " + validator.getValidationMessage();
        }

        lastScannedTicketID = ticketID;
        openGate();
        return "ENTRY APPROVED: Ticket is valid.";
    }

    public void openGate() {
        gateStatus = "OPEN";
    }

    public void closeGate() {
        gateStatus = "CLOSED";
        lastScannedTicketID = null;
    }

    public boolean recordEntry(String ticketID, Attendee attendee) {
        if (!isOpen() || attendee == null || ticketID == null
                || lastScannedTicketID == null
                || !lastScannedTicketID.equals(ticketID)
                || attendee.viewTicket() == null
                || !ticketID.equals(attendee.viewTicket().getTicketID())
                || attendee.checkEntryStatus().startsWith("INSIDE")
                || (!gateType.equals("ENTRY") && !gateType.equals("BOTH"))) {
            return false;
        }

        Ticket ticket = validator.findTicket(ticketID);
        if (ticket == null || ticket != attendee.viewTicket()
                || ticket.isUsed() || !ticket.checkTicketStatus().equals("VALID")
                || !ticket.getTicketType().equals(gateCategory)
                || !monitor.checkCapacity()) {
            closeGate();
            return false;
        }

        ticket.markAsUsed();
        if (!ticket.isUsed()) {
            closeGate();
            return false;
        }
        monitor.increaseOccupancy();
        attendee.setInside(true);
        lastScannedTicketID = null;
        return true;
    }

    public boolean recordExit(Attendee attendee) {
        if (!isOpen() || attendee == null
                || (!gateType.equals("EXIT") && !gateType.equals("BOTH"))
                || !attendee.checkEntryStatus().startsWith("INSIDE")) {
            return false;
        }

        if (monitor.getCurrentOccupancy() <= 0) {
            return false;
        }
        monitor.decreaseOccupancy();
        attendee.setInside(false);
        return true;
    }

    public boolean isOpen() {
        return gateStatus.equals("OPEN");
    }

    public int getGateID() { return gateID; }
    public String getGateStatus() { return gateStatus; }
    public String getGateCategory() { return gateCategory; }
    public String getGateType() { return gateType; }
}