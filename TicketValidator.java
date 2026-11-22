import java.util.ArrayList;

public class TicketValidator {
    private String validationStatus;
    private String validationMessage;
    private ArrayList<Ticket> tickets;

    public TicketValidator() {
        tickets = new ArrayList<Ticket>();
        validationStatus = "NONE";
        validationMessage = "No ticket checked yet.";
    }

    public boolean registerTicket(Ticket ticket) {
        if (ticket == null || ticket.getTicketID() == null
                || ticket.getTicketID().trim().isEmpty()
                || !ticket.checkTicketStatus().equals("VALID")
                || findTicket(ticket.getTicketID()) != null) {
            return false;
        }
        tickets.add(ticket);
        return true;
    }

    public Ticket findTicket(String ticketID) {
        if (ticketID == null) {
            return null;
        }
        for (int i = 0; i < tickets.size(); i++) {
            if (ticketID.equals(tickets.get(i).getTicketID())) {
                return tickets.get(i);
            }
        }
        return null;
    }

    public boolean validateTicket(String ticketID) {
        Ticket ticket = findTicket(ticketID);
        if (ticket == null) {
            return reject("Ticket ID cannot be found.");
        }
        if (!ticket.checkTicketStatus().equals("VALID")) {
            return reject("Ticket is not valid or has not been issued.");
        }
        validationStatus = "PASSED";
        validationMessage = "Ticket exists and is valid.";
        return true;
    }

    public boolean checkDuplicateTicket(String ticketID) {
        Ticket ticket = findTicket(ticketID);
        if (ticket != null && ticket.isUsed()) {
            reject("Ticket has already been used.");
            return true;
        }
        validationStatus = "PASSED";
        validationMessage = "Ticket has not been used.";
        return false;
    }

    public boolean checkTicketAvailability(OccupancyMonitor monitor) {
        if (monitor == null || !monitor.checkCapacity()) {
            return reject("Venue is at maximum capacity.");
        }
        validationStatus = "PASSED";
        validationMessage = "Venue has available capacity.";
        return true;
    }

    public boolean validateEntranceGate(String ticketID, String gateCategory, String gateType) {
        Ticket ticket = findTicket(ticketID);
        String category = gateCategory == null ? "" : gateCategory.trim().toUpperCase();
        String type = gateType == null ? "" : gateType.trim().toUpperCase();

        if (!category.equals("GENERAL") && !category.equals("VIP")) {
            return reject("Invalid entrance category.");
        }
        if (ticket == null) {
            return reject("Ticket ID cannot be found.");
        }
        if (!type.equals("ENTRY") && !type.equals("BOTH")) {
            return reject("This gate cannot be used for entry.");
        }
        if (!ticket.getTicketType().equals(category)) {
            return reject("Ticket does not match this entrance category.");
        }
        validationStatus = "PASSED";
        validationMessage = "Ticket matches this entrance.";
        return true;
    }

    private boolean reject(String message) {
        validationStatus = "REJECTED";
        validationMessage = message;
        return false;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public String getValidationMessage() {
        return validationMessage;
    }
}