package coordenadas;

import java.util.ArrayList;
import java.util.List;

public class MapaMundo{

    private Double lat;
    private Double lon;

    public void setLon(Double lon) {
        this.lon = lon;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }

    public List<Double> cacularPosicion() {
        List<Double> posicion =   new ArrayList<Double>(2);
        lat = Math.max(-90, Math.min(90, lat));
        lon = ((lon + 180) % 360 + 360) % 360 - 180;

        double x = (lon + 180.0) / 360.0 * 960;
        double y =  (90.0 - lat) / 180.0 * 480;
        posicion.add(x);
        posicion.add(y);

        return posicion;

    }
}
