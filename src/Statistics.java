import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
    }

    public void addEntry(LogEntry entry) {
        // Добавляем трафик
        this.totalTraffic += entry.getResponseSize();

        // Обновляем minTime и maxTime
        LocalDateTime entryTime = entry.getTime();
        if (this.minTime == null || entryTime.isBefore(this.minTime)) {
            this.minTime = entryTime;
        }
        if (this.maxTime == null || entryTime.isAfter(this.maxTime)) {
            this.maxTime = entryTime;
        }
    }

    public double getTrafficRate() {
        if (minTime == null || minTime.equals(maxTime)) {
            return 0.0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        double hours = duration.toMinutes() / 60.0;

        if (hours < 1.0) {
            hours = 1.0;
        }

        return totalTraffic / hours;
    }

    // Геттеры
    public int getTotalTraffic() {
        return totalTraffic;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }
}