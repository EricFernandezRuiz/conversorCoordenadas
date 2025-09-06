package coordenadas;

import mil.nga.mgrs.MGRS;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradosMinutos {
    public  double grdLong;
    public  double minLong;
    public  double grdLat;
    public  double minLat;
    private static final String pattern = "^\\s*(([-]?0?[0-8][0-9]|90)°\\s*([0-5]?[0-9](\\.\\d+))?'\\s*)\\s*(([+-]?0{0,2}[0-9]|0?[0-9][0-9]|1[0-7][0-9]|180)°\\s*([0-5]?[0-9](\\.\\d+)?)'\\s*?)$";

    public GradosMinutos(double grdLat, double minLat, double grdLong, double minLong){
        this.grdLong = grdLong;
        this.minLong = minLong;
        this.grdLat = grdLat;
        this.minLat = minLat;
    }

    public GradosMinutos(){
        this.grdLong = 0;
        this.minLong = 0;
        this.grdLat = 0;
        this.minLat = 0;
    }

    public void setGrdLong(double grdLong) {
        this.grdLong =  grdLong;
    }

    public void setMinLong(double minLong) {
        this.minLong = minLong;
    }

    public void setGrdLat(double grdLat) {
        this.grdLat = grdLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public List<Double> getLatLong(String texto){
        List<Double> res = new ArrayList<Double>(4);
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(texto);
        m.matches();

        String lngGrd = m.group(2);
        String lngMin = m.group(3);
        String latGrd = m.group(6);
        String latMin = m.group(7);

        double lngGrdD = Double.parseDouble(lngGrd);
        double lngMinD = Double.parseDouble(lngMin);
        double latGrdD = Double.parseDouble(latGrd);
        double latMinD = Double.parseDouble(latMin);

        res.add(lngGrdD);
        res.add(lngMinD);
        res.add(latGrdD);
        res.add(latMinD);

        return res;
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

    public List<Integer> aGradosMinutosSegundos (){
        int resGrd;
        int resMin;
        int resSeg;
        List<Integer> res = new ArrayList<Integer>(6);

        resGrd = (int) Math.floor(Math.abs(grdLong));
        resMin = (int) Math.floor(Math.abs(minLong));
        resSeg = (int) (minLong - resMin) * 60;
        res.add(resGrd);
        res.add(resMin);
        res.add(resSeg);

        //resFinal = Integer.toString(resGrd) + "° " + Integer.toString(resMin) + "′ " + Integer.toString(resSeg) + "″ ";

        resGrd = (int) Math.floor(Math.abs(grdLat));
        resMin = (int) Math.floor(Math.abs(minLat));
        resSeg = (int) (minLat - resMin) * 60;

        res.add(resGrd);
        res.add(resMin);
        res.add(resSeg);

        //resFinal = resFinal + Integer.toString(resGrd) + "° " + Integer.toString(resMin) + "′ " + Integer.toString(resSeg) + "″ ";

        return res;
    }

    public  List<Double> aGrados (){
        double resGrd;
        double signA = 1.0;
        double signB =1.0;
        List<Double> res = new ArrayList<Double>(2);

        if (grdLong < 0){
             signA = -1;
        }
        if (grdLat < 0){
            signB = -1;
        }

        resGrd = (minLong/60 + Math.abs(grdLong)) * signA;
        //resFinal = String.format("%.3f", resGrd) + "° ";
        res.add(resGrd);

        resGrd =  (minLat/60 + Math.abs(grdLat)) * signB;
        //resFinal = resFinal + String.format("%.3f", resGrd) + "° ";
        res.add(resGrd);

        return res;
    }

    public MGRS aCuadriculaMilitar(){
        double lat;
        double lon;
        double signA = 1.0;
        double signB =1.0;
        if (grdLong < 0){
            signA = -1;
        }
        if (grdLat < 0){
            signB = -1;
        }

        lon = (minLong/60 + Math.abs(grdLong)) * signA;

        lat =  (minLat/60 + Math.abs(grdLat)) * signB;

        return MGRS.from(lat, lon);
    }
}
