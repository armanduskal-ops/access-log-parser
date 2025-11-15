import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String logLine) {
        this.ipAddr = parseIpAddress(logLine);
        this.time = parseDateTime(logLine);
        this.method = parseMethod(logLine);
        this.path = parsePath(logLine);
        this.responseCode = parseResponseCode(logLine);
        this.responseSize = parseDataSize(logLine);
        this.referer = parseReferer(logLine);
        this.userAgent = new UserAgent(parseUserAgentString(logLine));
    }

    // Геттеры
    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    // Методы парсинга (остаются без изменений)
    private String parseIpAddress(String logLine) {
        String[] parts = logLine.split("\\s+");
        return parts.length > 0 ? parts[0] : "0.0.0.0";
    }

    private LocalDateTime parseDateTime(String logLine) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            String dateTimeStr = matcher.group(1);
            String[] dateParts = dateTimeStr.split("\\s+");
            if (dateParts.length > 0) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
                try {
                    String normalizedDate = dateParts[0]
                            .replace("Jan", "01")
                            .replace("Feb", "02")
                            .replace("Mar", "03")
                            .replace("Apr", "04")
                            .replace("May", "05")
                            .replace("Jun", "06")
                            .replace("Jul", "07")
                            .replace("Aug", "08")
                            .replace("Sep", "09")
                            .replace("Oct", "10")
                            .replace("Nov", "11")
                            .replace("Dec", "12");

                    return LocalDateTime.parse(normalizedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss"));
                } catch (Exception e) {
                    return LocalDateTime.now();
                }
            }
        }
        return LocalDateTime.now();
    }

    private HttpMethod parseMethod(String logLine) {
        Pattern pattern = Pattern.compile("\"([A-Z]+)\\s");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            try {
                return HttpMethod.valueOf(matcher.group(1));
            } catch (IllegalArgumentException e) {
                return HttpMethod.GET;
            }
        }
        return HttpMethod.GET;
    }

    private String parsePath(String logLine) {
        Pattern pattern = Pattern.compile("\"[A-Z]+\\s([^\\s?]+)");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "/";
    }

    private int parseResponseCode(String logLine) {
        Pattern pattern = Pattern.compile("\"\\s(\\d{3})\\s");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {

                return 200;
            }
        }
        return 200;
    }

    private int parseDataSize(String logLine) {
        Pattern pattern = Pattern.compile("\"\\s\\d{3}\\s(\\d+)");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private String parseReferer(String logLine) {
        Pattern pattern = Pattern.compile("\"\\s\\d{3}\\s\\d+\\s\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            String referer = matcher.group(1);
            return "-".equals(referer) ? "" : referer;
        }
        return "";
    }

    private String parseUserAgentString(String logLine) {
        Pattern pattern = Pattern.compile("\"\\s\"[^\"]*\"\\s\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
