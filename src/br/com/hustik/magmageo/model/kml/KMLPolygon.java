package br.com.hustik.magmageo.model.kml;

/**
 * @SATTRA - Sistemas de Automação e Tecnologia para Terminais e Recintos Alfandegados
 * @author Rômulo Goelzer Portolann
 */
public class KMLPolygon {
    
    private final String KML;
    private final String bairro;
    private final float percentual;
    
    public KMLPolygon(String kml, String bairro, float percentual) {
        this.KML = kml;
        this.bairro = bairro;
        this.percentual = percentual;
    }

    public String getKML() {
        return KML;
    }

    public String getBairro() {
        return bairro;
    }

    public float getPercentual() {
        return percentual;
    }
    
}
