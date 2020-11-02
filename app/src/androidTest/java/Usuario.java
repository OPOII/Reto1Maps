public class Usuario {
    private String nombre;
    private String correo;
    private String contrasena;
    private int id;

    public Usuario(){

    }
    public Usuario(String name, String email, String password, int identi){
        nombre=name;
        correo=email;
        contrasena=password;
        id=identi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
