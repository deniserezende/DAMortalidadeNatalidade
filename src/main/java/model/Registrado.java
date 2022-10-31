package model;

import java.sql.Date;

public class Registrado {
    private Integer id_registrado;
    private Date data_nascimento;
    private Obito obito = null;
    private Nascimento nascimento = null;
    private Integer cod_municipio_nasc; // FOREIGN KEY
    private Integer cod_raca_cor; // FOREIGN KEY
    private Integer cod_sexo; // FOREIGN KEY

    public Integer getId_registrado() {
        return id_registrado;
    }

    public void setId_registrado(Integer id_registrado) {
        this.id_registrado = id_registrado;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public Obito getObito() {
        return obito;
    }

    public void setObito(Obito obito) {
        this.obito = obito;
    }

    public Nascimento getNascimento() {
        return nascimento;
    }

    public void setNascimento(Nascimento nascimento) {
        this.nascimento = nascimento;
    }

    public Integer getCod_municipio_nasc() {
        return cod_municipio_nasc;
    }

    public void setCod_municipio_nasc(Integer cod_municipio_nasc) {
        this.cod_municipio_nasc = cod_municipio_nasc;
    }

    public Integer getCod_raca_cor() {
        return cod_raca_cor;
    }

    public void setCod_raca_cor(Integer cod_raca_cor) {
        this.cod_raca_cor = cod_raca_cor;
    }

    public Integer getCod_sexo() {
        return cod_sexo;
    }

    public void setCod_sexo(Integer cod_sexo) {
        this.cod_sexo = cod_sexo;
    }
}
