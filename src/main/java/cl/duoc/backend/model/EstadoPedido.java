package cl.duoc.backend.model;

public enum EstadoPedido {
    PENDIENTE("pendiente"),
    ASIGNADO("asignado"),
    EN_CAMINO("en camino"),
    ENTREGADO("entregado");

    private final String valor;

    EstadoPedido(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static EstadoPedido fromString(String estado) {
        if (estado == null) return PENDIENTE;
        String normalizado = estado.trim().toLowerCase();
        for (EstadoPedido ep : values()) {
            if (ep.valor.equalsIgnoreCase(normalizado) || ep.name().equalsIgnoreCase(normalizado.replace(" ", "_"))) {
                return ep;
            }
        }
        return PENDIENTE;
    }
}
