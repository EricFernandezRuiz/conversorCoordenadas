package coordenadas;

import mil.nga.mgrs.MGRS;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradosDecimales {

    private  double longitud;
    private  double latitud;
    private static final String pattern = "^\\s*([-+]?((1[0-7][0-9](\\.\\d+)?|180(\\.0+)?|[0-9]?[0-9](\\.\\d+)?)))[°]\\s*([-+]?([0-8]?[0-9](\\.\\d+)?|90(\\.0+)?))[°]\\s*?$";

    public GradosDecimales(double longitud, double latidud){
        this.longitud = longitud;
        this.latitud = latidud;
    }

    public GradosDecimales(){
        this.longitud = 0;
        this.latitud = 0;
    }

    public void setLongitud(double longitud) {
        this.longitud =  longitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
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

    public List<Double> getLatLong(String texto){
        List<Double> res = new ArrayList<Double>(2);
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(texto);
        m.matches();

        String lng = m.group(1);
        String lat = m.group(7);

        double latD = Double.parseDouble(lat);
        double lonD = Double.parseDouble(lng);

        res.add(lonD);
        res.add(latD);

        return res;
    }

    public List<Double> aGradosMinutos (){

        double resGrd;
        double resMin;
        List<Double> res = new ArrayList<Double>(4);


        resGrd = (int) Math.floor(Math.abs(longitud));
        resMin = Math.abs(longitud % 1) * 60;
        res.add(resGrd);
        res.add(resMin);

        //resFinal = Double.toString(resGrd) + "° " + String.format("%.2f", resMin) + "′ ";

        resGrd = (int) Math.floor(Math.abs(latitud));
        resMin = Math.abs(latitud % 1) * 60;
        res.add(resGrd);
        res.add(resMin);

        //resFinal = resFinal + Double.toString(resGrd) + "° " + String.format("%.2f", resMin) + "′ ";

        return res;
    }

    public List<Integer> aGradosMinutosSegundos (){
        int resGrd;
        int resMin;
        int resSeg;
        double auxMin;
        double auxSeg;
        List<Integer> res = new ArrayList<Integer>(6);


        resGrd = (int) Math.floor(Math.abs(longitud));
        auxMin = Math.abs(longitud % 1) * 60;
        resMin = (int) Math.floor(Math.abs(auxMin));
        auxSeg = Math.abs(auxMin % 1) * 60;
        resSeg = (int) Math.floor(Math.abs(auxSeg));
        res.add(resGrd);
        res.add(resMin);
        res.add(resSeg);

        //resFinal = Double.toString(resGrd) + "° " + Integer.toString(resMin) + "′ " + Integer.toString(resSeg) + "″ ";


        resGrd = (int) Math.floor(Math.abs(latitud));
        auxMin = Math.abs(latitud % 1) * 60;
        resMin = (int) Math.floor(Math.abs(auxMin));
        auxSeg = Math.abs(auxMin % 1) * 60;
        resSeg = (int) Math.floor(Math.abs(auxSeg));
        res.add(resGrd);
        res.add(resMin);
        res.add(resSeg);

        //resFinal = resFinal + Double.toString(resGrd) + "° " + Integer.toString(resMin) + "′ " + Integer.toString(resSeg) + "″ ";

        return res;
    }

    public MGRS aCuadriculaMilitar(){
        return MGRS.from(longitud, latitud);
    }

}
