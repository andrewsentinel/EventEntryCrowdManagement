public class TicketAssistance {
    private String assistanceID;
    private String issueDescription;
    private String assistanceStatus;

    public TicketAssistance(String assistanceID) {
        this.assistanceID = assistanceID;
        this.issueDescription = "None";
        this.assistanceStatus = "NO ISSUE";
    }

    public boolean checkTicket(String ticketID, TicketValidator validator) {
        return validator != null && validator.findTicket(ticketID) != null;
    }

    public void reportIssue(String issueDescription) {
        this.issueDescription = issueDescription;
        this.assistanceStatus = "OPEN";
    }

    public void resolveIssue() {
        if (assistanceStatus.equals("OPEN")) {
            assistanceStatus = "RESOLVED";
        }
    }

    public String getAssistanceID() {
        return assistanceID;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getAssistanceStatus() {
        return assistanceStatus;
    }
}
