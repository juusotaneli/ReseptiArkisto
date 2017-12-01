/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author Juuso
 */
public class AnnosRaakaAine {

    private Integer annos_id;
    private Integer raakaAineId;
    private Integer jarjestysNumero;
    private String maara;
    private String ohje;

    public AnnosRaakaAine(Integer annos_id, Integer RaakaAineId, Integer jarjestysNumero, String maara, String ohje) {
        
        this.annos_id = annos_id;
        this.raakaAineId = RaakaAineId;
        this.jarjestysNumero = jarjestysNumero;
        this.maara = maara;
        this.ohje = ohje;
    }

    public Integer getAnnos_id() {
        return annos_id;
    }

    public void setAnnos_id(Integer annos_id) {
        this.annos_id = annos_id;
    }

    public Integer getRaakaAineId() {
        return raakaAineId;
    }

    public void setRaakaAineId(Integer raakaAineId) {
        this.raakaAineId = raakaAineId;
    }

    public Integer getJarjestysNumero() {
        return jarjestysNumero;
    }

    public void setJarjestysNumero(Integer jarjestysNumero) {
        this.jarjestysNumero = jarjestysNumero;
    }

    public String getMaara() {
        return maara;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }

}