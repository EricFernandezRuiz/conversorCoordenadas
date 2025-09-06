package coordenadas;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mil.nga.mgrs.MGRS;

public class GradosMinutosSegundos {
    public int grdLong;
    public int minLong;
    public int segLong;
    public int grdLat;
    public int minLat;
    public int segLat;
    private static final String pattern = "^\\s*(([-]?(180|1[0-7][0-9]|[0-9]?[0-9]))°\\s*([0-5]?[0-9])'\\s*([0-5]?[0-9])\\\"\\s*?)\\s*((([-]?(90|[0-8]?[0-9]))°\\s*([0-5]?[0-9])'\\s*([0-5]?[0-9])\\\"\\s?))\\s*$";


    public GradosMinutosSegundos(int grdLat, int minLat, int segLat, int grdLong, int minLong, int segLong){
        this.grdLong = grdLong;
        this.minLong = minLong;
        this.segLong = segLong;
        this.grdLat = grdLat;
        this.minLat = minLat;
        this.segLat = segLat;
    }

    public GradosMinutosSegundos (){
        this.grdLong = 0;
        this.minLong = 0;
        this.segLong = 0;
        this.grdLat = 0;
        this.minLat = 0;
        this.segLat = 0;
    }

    public void setGrdLong(int grdLong) {
        this.grdLong =  grdLong;
    }

    public void setMinLong(int minLong) {
        this.minLong = minLong;
    }

    public void setSegLong(int segLong) {
        this.segLong = segLong;
    }

    public void setGrdLat(int grdLat) {
        this.grdLat = grdLat;
    }

    public void setMinLat(int minLat) {
        this.minLat = minLat;
    }

    public void setSegLat(int segLat) {
        this.segLat = segLat;
    }

    public boolean esValido(String texto){
        boolean res = false;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(texto);
        if(m.matches()){
            res = true;
        }
        return res;
    }

    public List<Integer> getLatLong(String texto){
        List<Integer> res = new ArrayList<Integer>(4);
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(texto);
        m.matches();

        String lngGrd = m.group(2);
        String lngMin = m.group(4);
        String lngSeg = m.group(5);
        String latGrd = m.group(9);
        String latMin = m.group(11);
        String latSeg = m.group(12);

        int lngGrdD = Integer.parseInt(lngGrd);
        int lngMinD = Integer.parseInt(lngMin);
        int lngSegD = Integer.parseInt(lngSeg);
        int latGrdD = Integer.parseInt(latGrd);
        int latMinD = Integer.parseInt(latMin);
        int latSegD = Integer.parseInt(latSeg);

        res.add(lngGrdD);
        res.add(lngMinD);
        res.add(lngSegD);
        res.add(latGrdD);
        res.add(latMinD);
        res.add(latSegD);

        return res;
    }

    public List<Double> aGradosMinutos (){
        double resMin;
        List<Double> res = new ArrayList<Double>(4);


        resMin =  (double) segLong/60 + minLong;
        res.add((double)grdLong);
        res.add(resMin);

        //resFinal = Integer.toString(grdLong) + "° " + String.format("%.2f", resMin) + "′ ";

        resMin =  (double) segLat/60 + minLat;
        res.add((double)grdLat);
        res.add(resMin);

        //resFinal = resFinal + Integer.toString(grdLat) + "° " + String.format("%.2f", resMin) + "′ ";

        return res;
    }

    public List<Double> aGrados (){
        double resMin;
        double resGrd;
        List<Double> res = new ArrayList<Double>(2);

        resMin =  (double) segLong/60 + minLong;
        resGrd = resMin/60 + grdLong;
        res.add(resGrd);

        //resFinal = String.format("%.3f", resGrd) + "° ";

        resMin =  (double) segLat/60 + minLat;
        resGrd = resMin/60 + grdLat;
        res.add(resGrd);
        //resFinal = resFinal + String.format("%.3f", resGrd) + "° ";

        return res;
    }

    public MGRS aCuadriculaMilitar(){
        double lat;
        double lon;
        double resMin;

        resMin =  (double) segLong/60 + minLong;
        lon = resMin/60 + grdLong;

        resMin =  (double) segLat/60 + minLat;
        lat = resMin/60 + grdLat;
        return MGRS.from(lon,lat);
    }
}
