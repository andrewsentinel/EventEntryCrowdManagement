import java.util.Scanner;
public class ConsoleMenu {
    private OccupancyMonitor monitor;
    private TicketValidator validator;
    private Gate vipGate;
    private Attendee attendee;
    private AreaAccessController vipArea;
    private AreaAccessController generalArea;
    private TicketAssistance assistance;

    public void start() {
        Scanner input = new Scanner(System.in);
        boolean finished = false;

        while (!finished) {
            if (!input.hasNextLine()) {
                System.out.println("No more input. Exiting the system.");
                break;
            }

            showMenu();
            String choice = input.nextLine().trim();

            if (choice.equals("1")) {
                runMainFlow();
                finished = true;
            } else if (choice.equals("0")) {
                System.out.println("Goodbye.");
                finished = true;
            } else {
                System.out.println("Invalid choice. Please enter 1 or 0.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n===== SMART EVENT ENTRY SYSTEM =====");
        System.out.println("1. Run the main flow once");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    private void runMainFlow() {
        setUpSystem();
        showTicket();
        boolean entered = scanAndEnter();

        if (entered) {
            checkAreaAccess();
            exitVenue();
        } else {
            requestAssistance();
        }

        System.out.println("\n=== Main flow completed ===");
    }

    private void setUpSystem() {
        printStep("Step 1: Set up the venue");
        monitor = new OccupancyMonitor(10000);
        validator = new TicketValidator();
        vipGate = new Gate(1, "VIP", "BOTH", validator, monitor);
        vipArea = new AreaAccessController("CP-VIP", "VIP");
        generalArea = new AreaAccessController("CP-GA", "GENERAL");
        assistance = new TicketAssistance("AST-001");

        Ticket ticket = new Ticket("TKT-0001", "VIP");
        ticket.generateTicket();

        if (!validator.registerTicket(ticket)) {
            throw new IllegalStateException("Ticket could not be registered.");
        }

        attendee = new Attendee(
                "ATT-001", "Juan Dela Cruz", "09171234567", ticket
        );

        System.out.println("Venue ready. Gate " + vipGate.getGateID()
                + " (" + vipGate.getGateCategory() + ", "
                + vipGate.getGateType() + ") is "
                + vipGate.getGateStatus() + ".");
        monitor.displayOccupancy();
    }

    private void showTicket() {
        printStep("Step 2: Attendee receives a digital ticket");
        Ticket ticket = attendee.viewTicket();
        System.out.println("Attendee : " + attendee.getName());
        System.out.println("Ticket   : " + ticket.getTicketID()
                + " [" + ticket.getTicketType() + "]");
        System.out.println("Status   : " + ticket.checkTicketStatus());
        System.out.println("Purchased: " + ticket.getPurchaseDate());
        System.out.println("Location : " + attendee.checkEntryStatus());
    }

    private boolean scanAndEnter() {
        printStep("Step 3: Scan the ticket at the VIP entrance");
        String ticketID = attendee.viewTicket().getTicketID();
        String result = vipGate.scanTicket(ticketID);
        System.out.println("Scan result: " + result);

        if (!vipGate.isOpen()) {
            return false;
        }

        boolean recorded = vipGate.recordEntry(ticketID, attendee);
        vipGate.closeGate();

        if (!recorded) {
            System.out.println("Entry could not be recorded.");
            return false;
        }

        System.out.println("Entry recorded successfully.");
        System.out.println("Ticket status: " + attendee.viewTicket().checkTicketStatus());
        System.out.println("Location     : " + attendee.checkEntryStatus());
        monitor.displayOccupancy();
        return true;
    }

    private void checkAreaAccess() {
        printStep("Step 4: Check area access");
        checkOneArea(generalArea);
        checkOneArea(vipArea);
    }

    private void checkOneArea(AreaAccessController area) {
        if (area.validateAreaAccess(attendee.viewTicket())) {
            area.grantAccess();
        } else {
            area.denyAccess();
        }
        System.out.println(area.getAreaName() + " area: " + area.getAccessStatus());
    }

    private void exitVenue() {
        printStep("Step 5: Attendee exits the venue");
        vipGate.openGate();
        boolean exited = vipGate.recordExit(attendee);
        vipGate.closeGate();

        if (exited) {
            System.out.println("Exit recorded successfully.");
        } else {
            System.out.println("Exit could not be recorded.");
        }

        System.out.println("Location     : " + attendee.checkEntryStatus());
        System.out.println("Ticket status: " + attendee.viewTicket().checkTicketStatus());
        monitor.displayOccupancy();
    }

    private void requestAssistance() {
        printStep("Entry rejected: ticket assistance");
        assistance.reportIssue("Attendee could not enter at the gate.");
        System.out.println("Assistance " + assistance.getAssistanceID()
                + ": " + assistance.getAssistanceStatus());
        System.out.println("Issue: " + assistance.getIssueDescription());
    }

    private void printStep(String title) {
        System.out.println("\n--- " + title + " ---");
    }
}