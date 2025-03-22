package it.aredegalli.auctoritas.enums;

public enum AuditEventTypeEnum {
    APPLICATION_GET,
    APPLICATION_CREATE,
    APPLICATION_UPDATE,
    APPLICATION_DISABLE,
    APPLICATION_ENABLE,

    ROLE_GET,
    ROLE_CREATE,
    ROLE_UPDATE,
    ROLE_DELETE,

    PERMISSION_GET,
    PERMISSION_CREATE,
    PERMISSION_UPDATE,
    PERMISSION_DELETE,

    PERMISSION_ROLE_GET,
    PERMISSION_ROLE_CREATE,
    PERMISSION_ROLE_DELETE,

    USER_ROLE_GET,
    USER_ROLE_CREATE,
    USER_ROLE_DELETE,

    APPLICATION_ROLE_GET,
    APPLICATION_ROLE_CREATE,
    APPLICATION_ROLE_DELETE,

    AUTHENTICATOR_ACTIVE,
    AUTHENTICATOR_GET,
    AUTHENTICATOR_CREATE,
    AUTHENTICATOR_UPDATE,
    AUTHENTICATOR_DELETE,

    USER_AUTHENTICATOR_MAPPING_GET,
    USER_AUTHENTICATOR_MAPPING_CREATE,
    USER_AUTHENTICATOR_MAPPING_DELETE,

    APPLICATION_AUTHENTICATOR_ACTIVE,
    APPLICATION_AUTHENTICATOR_GET,
    APPLICATION_AUTHENTICATOR_CREATE,
    APPLICATION_AUTHENTICATOR_DELETE,

}
