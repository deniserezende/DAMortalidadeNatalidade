package model;

import java.sql.Time;

public class Nascimento {
    private Registro registro;
    private Time hora_nascimento;
    private Integer idade_mae;
    private Integer peso_nascido_vivo;
    private Integer cod_tipo_parto; // FOREIGN KEY
    private Integer cod_raca_cor_mae; // FOREIGN KEY
    private Integer cod_estado_civil_mae; // FOREIGN KEY

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public Time getHora_nascimento() {
        return hora_nascimento;
    }

    public void setHora_nascimento(Time hora_nascimento) {
        this.hora_nascimento = hora_nascimento;
    }

    public Integer getIdade_mae() {
        return idade_mae;
    }

    public void setIdade_mae(Integer idade_mae) {
        this.idade_mae = idade_mae;
    }

    public Integer getPeso_nascido_vivo() {
        return peso_nascido_vivo;
    }

    public void setPeso_nascido_vivo(Integer peso_nascido_vivo) {
        this.peso_nascido_vivo = peso_nascido_vivo;
    }

    public Integer getCod_tipo_parto() {
        return cod_tipo_parto;
    }

    public void setCod_tipo_parto(Integer cod_tipo_parto) {
        this.cod_tipo_parto = cod_tipo_parto;
    }

    public Integer getCod_raca_cor_mae() {
        return cod_raca_cor_mae;
    }

    public void setCod_raca_cor_mae(Integer cod_raca_cor_mae) {
        this.cod_raca_cor_mae = cod_raca_cor_mae;
    }

    public Integer getCod_estado_civil_mae() {
        return cod_estado_civil_mae;
    }

    public void setCod_estado_civil_mae(Integer cod_estado_civil_mae) {
        this.cod_estado_civil_mae = cod_estado_civil_mae;
    }
}