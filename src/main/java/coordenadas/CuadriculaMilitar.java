package coordenadas;

import mil.nga.mgrs.MGRS;
import mil.nga.sf.Point;

import java.util.ArrayList;
import java.util.List;

public class CuadriculaMilitar {
    public MGRS mgrs;
    public Point p;

    public CuadriculaMilitar(int zone, char band, char column, char row, long easting, long northing) {
         mgrs = new MGRS(zone, band, column, row, easting, northing);
    }

    public CuadriculaMilitar() {
        mgrs = new MGRS(0, 'Z', 'Z', 'Z', 0, 0);
    }

    public void setMGRS(MGRS mgrs) {
        this.mgrs = mgrs;
    }

    public boolean esValido(String texto){
        return MGRS.isMGRS(texto);
    }
    
    public List<Double> aGrados(){
        List<Double> res = new ArrayList<Double>(2);
        p = mgrs.toPoint();

        res.add(p.getX());
        res.add(p.getY());
        //resFinal = p.getX() + "°" + p.getY() + "°";

        return res;
    }

    public List<Double> aGradosMinutos(){
        p = mgrs.toPoint();
        GradosDecimales gd = new GradosDecimales(p.getX(), p.getY());
        return gd.aGradosMinutos();
    }

    public List<Integer> aGradosMinutosSegundos(){
        p = mgrs.toPoint();
        GradosDecimales gd = new GradosDecimales(p.getX(), p.getY());
        return gd.aGradosMinutosSegundos();
    }
}
