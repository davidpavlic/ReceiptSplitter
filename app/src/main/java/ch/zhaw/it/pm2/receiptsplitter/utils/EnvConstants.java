package ch.zhaw.it.pm2.receiptsplitter.utils;

public enum EnvConstants {
    SMTP_USERNAME("SMTP_USERNAME"),
    SMTP_API_KEY("SMTP_API_KEY");

    private final String key;

    EnvConstants(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public static boolean areAllSet() {
        for (EnvConstants constant : EnvConstants.values()) {
            String envVarValue = System.getProperty(constant.getKey());
            if (envVarValue == null || envVarValue.equals("null") || envVarValue.isEmpty() || envVarValue.equals("ChangeMe")) {
                return false;
            }
        }
        return true;
    }
}