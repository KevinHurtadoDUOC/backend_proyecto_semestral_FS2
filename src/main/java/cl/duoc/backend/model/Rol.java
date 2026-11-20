package cl.duoc.backend.model;

public enum Rol {
    ADMIN,
    OPERADOR,
    REPARTIDOR,
    CLIENTE,
    USER;

    public static Rol fromString(String rol) {
        if (rol == null) return CLIENTE;
        try {
            return Rol.valueOf(rol.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return CLIENTE;
        }
    }
}
