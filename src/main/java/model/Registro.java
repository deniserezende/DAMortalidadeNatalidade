package model;

public class Registro {
    private Integer id_registro;
    private String tipo_registro;
    private Integer ano_registro;

    public Integer getId_registro() {
        return id_registro;
    }

    public void setId_registro(Integer id_registro) {
        this.id_registro = id_registro;
    }

    public String getTipo_registro() {
        return tipo_registro;
    }

    public void setTipo_registro(String tipo_registro) {
        this.tipo_registro = tipo_registro;
    }

    public Integer getAno_registro() {
        return ano_registro;
    }

    public void setAno_registro(Integer ano_registro) {
        this.ano_registro = ano_registro;
    }
}
