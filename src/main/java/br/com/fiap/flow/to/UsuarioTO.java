package br.com.fiap.flow.to;

public class UsuarioTO {

    private Long codigo;
    private String nome;
    private String email;
    private String cargo;

    public UsuarioTO() {
    }

    public UsuarioTO(Long codigo, String nome, String email, String cargo) {
        this.codigo = codigo;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
