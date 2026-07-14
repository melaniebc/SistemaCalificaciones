package usuarios;

//Clase abstracta base para todos los usuarios del sistema.

public abstract class Usuario {
    private String id;
    private String nombre;
    private String correo;
    private String password;

    public Usuario(String id, String nombre, String correo, String password) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    /**
     * Método abstracto: cada rol debe devolver su propio perfil.
     */
    public abstract String obtenerPerfil();

    public boolean login(String correoIngresado, String passwordIngresada) {
        return correo != null
                && password != null
                && correo.equalsIgnoreCase(correoIngresado)
                && password.equals(passwordIngresada);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
    public String getPassword() {return password;}

    @Override
    public String toString() {
        return nombre;
    }
}
