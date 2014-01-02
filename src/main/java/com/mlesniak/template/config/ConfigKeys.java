package com.mlesniak.template.config;

/**
 * Default globlaly ussed configuration keys.
 */
public class ConfigKeys {
    public final String ALLOW_SUBMIT = "allowSubmit";
    public final String SHOW_DEFAILT_OPTIONS = "showDefaultOptions";

    // Database configuration.
    public final String DATABASE_DRIVER = "database.driver";
    public final String DATABASE_URL = "database.url";
    public final String DATABASE_USER = "database.user";
    public final String DATABASE_PASSWORD = "database.password";
    public final String DATABASE_GENERATION = "database.generation";

    // Default admin user.
    public final String ADMIN_USERNAME = "database.admin.username";
    public final String ADMIN_PASSWORD = "database.admin.password";
    public final String ADMIN_EMAIL = "database.admin.email";
    public final String ADMIN_ROLES = "database.admin.roles";
    public final String ADMIN_LANGUAGE = "database.admin.language";

    // SMTP-server configuration.
    // This is currently quite GMail-centric.
    public final String EMAIL_FROM = "email.from";
    public final String EMAIL_HOST = "email.host";
    public final String EMAIL_PORT = "email.port";
    public final String EMAIL_USERNAME = "email.username";
    public final String EMAIL_PASSWORD = "email.password";
}
