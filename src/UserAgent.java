public class UserAgent {
    private final String operatingSystem;
    private final String browser;

    public UserAgent(String userAgentString) {
        this.operatingSystem = parseOperatingSystem(userAgentString);
        this.browser = parseBrowser(userAgentString);
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getBrowser() {
        return browser;
    }

    private String parseOperatingSystem(String userAgentString) {
        if (userAgentString == null || userAgentString.isEmpty()) {
            return "Unknown";
        }

        String ua = userAgentString.toLowerCase();

        if (ua.contains("windows")) {
            return "Windows";
        } else if (ua.contains("mac os") || ua.contains("macos")) {
            return "macOS";
        } else if (ua.contains("linux")) {
            return "Linux";
        } else if (ua.contains("android")) {
            return "Android";
        } else if (ua.contains("ios")) {
            return "iOS";
        } else {
            return "Other";
        }
    }

    private String parseBrowser(String userAgentString) {
        if (userAgentString == null || userAgentString.isEmpty()) {
            return "Unknown";
        }

        String ua = userAgentString.toLowerCase();

        if (ua.contains("edg/") || ua.contains("edge/")) {
            return "Edge";
        } else if (ua.contains("firefox") || ua.contains("fxios")) {
            return "Firefox";
        } else if (ua.contains("chrome") && !ua.contains("edg/") && !ua.contains("edge/")) {
            return "Chrome";
        } else if (ua.contains("safari") && !ua.contains("chrome")) {
            return "Safari";
        } else if (ua.contains("opera") || ua.contains("opr/")) {
            return "Opera";
        } else {
            return "Other";
        }
    }
}
