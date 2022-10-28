package model;

import java.sql.Date;
import java.sql.Time;

public class Carga {
    private Date data_carga;
    private Time hora_carga;
    private String responsavel;
    private String email;
    private String nome_arquivo;
    private Integer tipo_carga;

    public Date getData_carga() {
        return data_carga;
    }

    public void setData_carga(Date data_carga) {
        this.data_carga = data_carga;
    }

    public Time getHora_carga() {
        return hora_carga;
    }

    public void setHora_carga(Time hora_carga) {
        this.hora_carga = hora_carga;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome_arquivo() {
        return nome_arquivo;
    }

    public void setNome_arquivo(String nome_arquivo) {
        this.nome_arquivo = nome_arquivo;
    }

    public Integer getTipo_carga() {
        return tipo_carga;
    }

    public void setTipo_carga(Integer tipo_carga) {
        this.tipo_carga = tipo_carga;
    }
}
