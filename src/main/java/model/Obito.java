package model;

import java.sql.Date;
import java.sql.Time;

public class Obito {
    private Registro registro = null;
    private Integer cod_est_civ_falecido; // FOREIGN KEY
    private Integer cod_municipio_obito; // FOREIGN KEY
    private Integer cod_tipo_obito; // FOREIGN KEY
    private Integer cod_circ_obito; // FOREIGN KEY
    private Integer cod_local_obito; // FOREIGN KEY
    private Integer idade_falecido;
    private Date data_obito;
    private Time hora_obito;

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public Integer getCod_est_civ_falecido() {
        return cod_est_civ_falecido;
    }

    public void setCod_est_civ_falecido(Integer cod_est_civ_falecido) {
        this.cod_est_civ_falecido = cod_est_civ_falecido;
    }

    public Integer getCod_municipio_obito() {
        return cod_municipio_obito;
    }

    public void setCod_municipio_obito(Integer cod_municipio_obito) {
        this.cod_municipio_obito = cod_municipio_obito;
    }

    public Integer getCod_tipo_obito() {
        return cod_tipo_obito;
    }

    public void setCod_tipo_obito(Integer cod_tipo_obito) {
        this.cod_tipo_obito = cod_tipo_obito;
    }

    public Integer getCod_circ_obito() {
        return cod_circ_obito;
    }

    public void setCod_circ_obito(Integer cod_circ_obito) {
        this.cod_circ_obito = cod_circ_obito;
    }

    public Integer getCod_local_obito() {
        return cod_local_obito;
    }

    public void setCod_local_obito(Integer cod_local_obito) {
        this.cod_local_obito = cod_local_obito;
    }

    public Integer getIdade_falecido() {
        return idade_falecido;
    }

    public void setIdade_falecido(Integer idade_falecido) {
        this.idade_falecido = idade_falecido;
    }

    public Date getData_obito() {
        return data_obito;
    }

    public void setData_obito(Date data_obito) {
        this.data_obito = data_obito;
    }

    public Time getHora_obito() {
        return hora_obito;
    }

    public void setHora_obito(Time hora_obito) {
        this.hora_obito = hora_obito;
    }
}