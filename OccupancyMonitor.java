public class OccupancyMonitor {
    private int currentOccupancy;
    private int maximumCapacity;
    private int availableCapacity;
    private String occupancyStatus;

    public OccupancyMonitor(int maximumCapacity) {
        if (maximumCapacity <= 0) {
            throw new IllegalArgumentException("Maximum capacity must be greater than zero.");
        }
        this.maximumCapacity = maximumCapacity;
        this.currentOccupancy = 0;
        updateOccupancy();
    }

    public void updateOccupancy() {
        availableCapacity = maximumCapacity - currentOccupancy;
        occupancyStatus = availableCapacity > 0
                ? "ENTRY AVAILABLE" : "ENTRY CLOSED - VENUE FULL";
    }

    public void increaseOccupancy() {
        if (checkCapacity()) {
            currentOccupancy++;
        }
        updateOccupancy();
    }

    public void decreaseOccupancy() {
        if (currentOccupancy > 0) {
            currentOccupancy--;
        }
        updateOccupancy();
    }

    public boolean checkCapacity() {
        return currentOccupancy < maximumCapacity;
    }

    public void displayOccupancy() {
        updateOccupancy();
        System.out.println("===== CONCERT VENUE MONITOR =====");
        System.out.println("Current occupancy: " + currentOccupancy);
        System.out.println("Maximum capacity: " + maximumCapacity);
        System.out.println("Available capacity: " + availableCapacity);
        System.out.println("Entry status: " + occupancyStatus);
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public String getOccupancyStatus() {
        return occupancyStatus;
    }
}