package br.com.hustik.magmageo.model;

/**
 * @SATTRA - Sistemas de Automação e Tecnologia para Terminais e Recintos Alfandegados
 * @author Rômulo Goelzer Portolann
 */
public class TipoOcorrencia {

    private int ID;
    private String descricao;

    public TipoOcorrencia(int ID, String descricao) {
        this.ID = ID;
        this.descricao = descricao;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
}
