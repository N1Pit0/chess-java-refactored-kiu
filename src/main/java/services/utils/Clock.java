package services.utils;

public class Clock {
    private int hours;
    private int minutes;
    private int seconds;

    public Clock(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public boolean isTimeUp() {
        return hours == 0 && minutes == 0 && seconds == 0;
    }

    public void decrementTime() {

        if (seconds > 0) {
            seconds--;
            return;
        }

        if (minutes > 0) {
            minutes--;
            seconds = 59;
            return;
        }

        if (hours > 0) {
            hours--;
            minutes = 59;
            seconds = 59;
        }

    }

    public String getFormattedTime() {
        String formattedHours = String.format("%02d", hours);
        String formattedMinutes = String.format("%02d", minutes);
        String formattedSeconds = String.format("%02d", seconds);
        return formattedHours + ":" + formattedMinutes + ":" + formattedSeconds;
    }
}