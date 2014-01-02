package com.mlesniak.template.config;

/**
 * Default globlaly ussed configuration keys.
 */
public class ConfigKeys {
    public static final String ALLOW_SUBMIT = "allowSubmit";
    public static final String SHOW_DEFAULT_OPTIONS = "showDefaultOptions";

    // Database configuration.
    public static final String DATABASE_DRIVER = "database.driver";
    public static final String DATABASE_URL = "database.url";
    public static final String DATABASE_USER = "database.user";
    public static final String DATABASE_PASSWORD = "database.password";
    public static final String DATABASE_GENERATION = "database.generation";

    // Default admin user.
    public static final String ADMIN_USERNAME = "database.admin.username";
    public static final String ADMIN_PASSWORD = "database.admin.password";
    public static final String ADMIN_EMAIL = "database.admin.email";
    public static final String ADMIN_ROLES = "database.admin.roles";
    public static final String ADMIN_LANGUAGE = "database.admin.language";

    // SMTP-server configuration.
    // This is currently quite GMail-centric.
    public static final String EMAIL_FROM = "email.from";
    public static final String EMAIL_HOST = "email.host";
    public static final String EMAIL_PORT = "email.port";
    public static final String EMAIL_USERNAME = "email.username";
    public static final String EMAIL_PASSWORD = "email.password";

    // Reset everything from file?
    public static final String RESET = "reset";
}
