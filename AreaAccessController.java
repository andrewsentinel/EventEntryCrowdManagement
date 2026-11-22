public class AreaAccessController {
    private String checkpointID;
    private String areaName;
    private String accessStatus;

    public AreaAccessController(String checkpointID, String areaName) {
        this.checkpointID = checkpointID;
        this.areaName = areaName == null ? "" : areaName.trim().toUpperCase();
        this.accessStatus = "NOT CHECKED";
    }

    public boolean validateAreaAccess(Ticket ticket) {
        if (ticket == null || !ticket.checkTicketStatus().equals("USED")) {
            return false;
        }
        if (ticket.getTicketType() == null) {
            return false;
        }
        if (areaName.equals("GENERAL")) {
            return ticket.getTicketType().equals("GENERAL")
                    || ticket.getTicketType().equals("VIP");
        }
        if (areaName.equals("VIP")) {
            return ticket.getTicketType().equals("VIP");
        }
        return false;
    }

    public void grantAccess() {
        accessStatus = "GRANTED";
    }

    public void denyAccess() {
        accessStatus = "DENIED";
    }

    public String getCheckpointID() {
        return checkpointID;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getAccessStatus() {
        return accessStatus;
    }
}