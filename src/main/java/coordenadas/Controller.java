package coordenadas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import mil.nga.mgrs.MGRS;

import java.util.List;

public class Controller {
    @FXML private ChoiceBox<String> cajaDeEntrada;
    @FXML private ChoiceBox<String> cajaDeSalida;
    @FXML private TextField inputField;
    @FXML private TextField outputField;
    @FXML private Circle circulo;
    @FXML private Pane mapContainer;

    private final MapaMundo mapa = new MapaMundo();
    private boolean offsetsReady = false;
    private double offsetX = 0.0;
    private double offsetY = 0.0;


    private static final List<String> listaInicial = List.of(
            "Degrees", "Degrees Minutes","Degrees Minutes Seconds","Military Grid"
    );
    GradosDecimales gd;
    GradosMinutos gm;
    GradosMinutosSegundos gms;
    CuadriculaMilitar mgrs;






    @FXML
    private void initialize() {
        // Ensure offsets are computed after the scene is ready
        mapContainer.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene != null) {
                javafx.application.Platform.runLater(() -> {
                    offsetsReady = false;
                    computeOffset();
                });
            }
        });
    }

    private void computeOffset() {
        if (offsetsReady) {
            return;
        }
        Scene sc = mapContainer.getScene();
        if (sc == null) {
            return;
        }

        Region root = (Region) sc.getRoot();

        // Account for GridPane padding (left/right/top) and CSS background position/sizing
        Insets ins = root.getInsets();
        Bounds rootScene = root.localToScene(root.getLayoutBounds());
        double contentLeftSceneX = rootScene.getMinX() + ins.getLeft();
        double contentTopSceneY  = rootScene.getMinY() + ins.getTop();
        double contentWidth = root.getWidth() - ins.getLeft() - ins.getRight();

        // Background on .root: right 0px, top 75px, size 960x480
        double imageSceneX = contentLeftSceneX + contentWidth - 960.0; // "right 0"
        double imageSceneY = contentTopSceneY + 75.0;                  // "top 75"

        Bounds paneScene = mapContainer.localToScene(mapContainer.getLayoutBounds());
        offsetX = imageSceneX - paneScene.getMinX();
        offsetY = imageSceneY - paneScene.getMinY();

        offsetsReady = true;
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private void showMarcador() {
        if (!offsetsReady) computeOffset();
        List<Double> pos = mapa.cacularPosicion(); // image-local (0..960, 0..480)
        double imgX = clamp(pos.get(0), 0.0, 960 - 1e-6); // avoid x == 960
        double imgY = clamp(pos.get(1), 0.0, 480 - 1e-6); // avoid y == 480
        circulo.setCenterX(offsetX + pos.get(0));
        circulo.setCenterY(offsetY + pos.get(1));

        System.out.println(offsetX + pos.get(0));
        System.out.println(offsetY + pos.get(1));

        circulo.setVisible(true);
    }

    public void ajusteDeSeleccion(ChoiceBox<String> choiceBoxEnt, ChoiceBox<String> choiceBoxSal, TextField tf){
        String opcionSeleccionada = choiceBoxEnt.getValue();
        String valorSalida = choiceBoxSal.getValue();
        ObservableList<String> listaAuxiliar = FXCollections.observableArrayList(listaInicial);
        if (opcionSeleccionada != null) {
            switch (opcionSeleccionada) {
                case "Degrees" -> {
                    tf.setPromptText("123.456° 78.012°");
                }
                case "Degrees Minutes" -> {
                    tf.setPromptText("123° 45' 67° 89'");
                }
                case "Degrees Minutes Seconds" -> {
                    tf.setPromptText("123° 45' 56\" 78° 01' 23\"");
                }
                case "Military Grid" -> {
                    tf.setPromptText("18S UJ 12345 67890");
                }
            }
        }

        // quitar la opcion seleccionada de la otra caja
        choiceBoxSal.getItems().removeAll();
        choiceBoxSal.setItems(listaAuxiliar);
        choiceBoxSal.getItems().remove(opcionSeleccionada);

        if (valorSalida != null) {
            choiceBoxSal.setValue(valorSalida);
        }
    }

    public void posiblesEntradas(String opcionSeleccionada, TextField tf){

        if (opcionSeleccionada != null) {
            switch (opcionSeleccionada) {
                case "Degrees" -> {
                    String grd = "^[-0-9.°+\\s]*$";

                    tf.setTextFormatter(new TextFormatter<String>(c ->
                            c.getControlNewText().matches(grd) ? c : null
                    ));
                }
                case "Degrees Minutes" -> {
                    String grdMin = "^[-0-9.+°'\\s]*$";

                    tf.setTextFormatter(new TextFormatter<String>(c ->
                            c.getControlNewText().matches(grdMin) ? c : null
                    ));
                }
                case "Degrees Minutes Seconds" -> {
                    String grdMinSeg = "^[-0-9.°+\\s'\"]*$";

                    tf.setTextFormatter(new TextFormatter<String>(c ->
                            c.getControlNewText().matches(grdMinSeg) ? c : null
                    ));
                }
                case "Military Grid" -> {
                    String mgrs = "^[0-9A-HJ-NP-Za-hj-np-z ]*$";

                    tf.setTextFormatter(new TextFormatter<String>(c ->
                            c.getControlNewText().matches(mgrs) ? c : null
                    ));
                }
            }
        }

        mascara(tf, opcionSeleccionada);
    }
    public void mascara(TextField tf, String opcionSeleccionada) {
        if((tf.getText().isEmpty() || tf.getText().equals("°") || tf.getText().equals("° '") || tf.getText().equals("° ' \"")) && haceFaltaCambiarTexto(opcionSeleccionada, tf.getText()) ) {
            switch (opcionSeleccionada) {
                case "Degrees" -> {
                    tf.setText("°  °");
                }
                case "Degrees Minutes" -> {
                    tf.setText("° '   ° '");
                }
                case "Degrees Minutes Seconds" -> {
                    tf.setText("° ' \"    ° ' \"");
                }
            }
        }
    }

    public boolean haceFaltaCambiarTexto (String opcionSeleccionada, String texto){
        boolean res = true;

        switch (opcionSeleccionada) {
            case "Degrees" -> {
                if(texto.equals("°") ){
                    res = false;
                }
            }
            case "Degrees Minutes" -> {
                if(texto.equals("° '") ){
                    res = false;
                }
            }
            case "Degrees Minutes Seconds" -> {
                if(texto.equals("° ' \"") ){
                    res = false;
                }
            }
        }
        return res;
    }

    public void entradaDeTexto(String opcionEntSeleccionada, String opcionSalSeleccionada , String texto, TextField tf){
        gd = new GradosDecimales();
        gm = new GradosMinutos();
        gms = new GradosMinutosSegundos();
        mgrs = new CuadriculaMilitar();

        List<Double> auxD;
        List<Integer> auxI;
        List<Double> resD;
        List<Integer> resI;

        if (opcionEntSeleccionada != null) {
            switch (opcionEntSeleccionada) {
                case "Degrees" -> {
                    if(gd.esValido(texto)){
                        auxD = gd.getLatLong(texto);
                        gd.setLongitud(auxD.get(0));
                        gd.setLatitud(auxD.get(1));

                        switch (opcionSalSeleccionada) {
                            case "Degrees Minutes" -> {
                                resD = gd.aGradosMinutos();
                                tf.setText(resD.get(0) + "° " + String.format("%.2f", resD.get(1)) + "′ " + resD.get(2) + "° " + String.format("%.2f", resD.get(3)) + "′ ");
                            }
                            case "Degrees Minutes Seconds" -> {
                                resI = gd.aGradosMinutosSegundos();
                                tf.setText(resI.get(0) + "° " + resI.get(1) + "′ " + resI.get(2) + "″ " + resI.get(3) + "° " + resI.get(4) + "′ " + resI.get(5) + "″ ");
                            }
                            case "Military Grid" -> {
                                tf.setText(gd.aCuadriculaMilitar().toString());
                            }
                        }
                        if(auxD.get(0) == 180.0){
                            auxD.set(0, 179.999);
                        }
                        mapa.setLon(auxD.get(0));
                        mapa.setLat(auxD.get(1));
                        showMarcador();
                    }
                }

                case "Degrees Minutes" -> {
                    if(gm.esValido(texto)){
                        auxD = gm.getLatLong(texto);
                        gm.setGrdLong(auxD.get(0));
                        gm.setMinLong(auxD.get(1));
                        gm.setGrdLat(auxD.get(2));
                        gm.setMinLat(auxD.get(3));

                        switch (opcionSalSeleccionada) {
                            case "Degrees" -> {
                                resD = gm.aGrados();
                                tf.setText(String.format("%.3f", resD.get(0)) + "° " + String.format("%.3f", resD.get(1)) + "° ");
                            }
                            case "Degrees Minutes Seconds" -> {
                                resI = gm.aGradosMinutosSegundos();
                                tf.setText(resI.get(0) + "° " + resI.get(1) + "′ " + resI.get(2) + "″ " + resI.get(3) + "° " + resI.get(4) + "′ " + resI.get(5) + "″ ");
                            }
                            case "Military Grid" -> {
                                tf.setText(gm.aCuadriculaMilitar().toString());
                            }
                        }
                        auxD = gm.aGrados();
                        if(auxD.get(0) == 180.0){
                            auxD.set(0, 179.999);
                        }
                        mapa.setLon(auxD.get(0));
                        mapa.setLat(auxD.get(1));
                        showMarcador();
                    }
                }
                case "Degrees Minutes Seconds" -> {
                    if(gms.esValido(texto)){
                        auxI = gms.getLatLong(texto);
                        gms.setGrdLong(auxI.get(0));
                        gms.setMinLong(auxI.get(1));
                        gms.setSegLong(auxI.get(2));
                        gms.setGrdLat(auxI.get(3));
                        gms.setMinLat(auxI.get(4));
                        gms.setSegLat(auxI.get(5));

                        switch (opcionSalSeleccionada) {
                            case "Degrees" -> {
                                resD = gms.aGrados();
                                tf.setText(String.format("%.3f", resD.get(0)) + "° " + String.format("%.3f", resD.get(1)) + "° ");
                            }
                            case "Degrees Minutes" -> {
                                resD = gms.aGradosMinutos();
                                tf.setText(resD.get(0) + "° " + String.format("%.2f", resD.get(1)) + "′ " + resD.get(2) + "° " + String.format("%.2f", resD.get(3)) + "′ ");
                            }
                            case "Military Grid" -> {
                                tf.setText(gms.aCuadriculaMilitar().toString());
                            }
                        }
                        auxD = gms.aGrados();
                        if(auxD.get(0) == 180.0){
                            auxD.set(0, 179.999);
                        }
                        mapa.setLon(auxD.get(0));
                        mapa.setLat(auxD.get(1));
                        showMarcador();
                    }
                }
                case "Military Grid" -> {
                    if(mgrs.esValido(texto)){
                        try{
                            mgrs.setMGRS(MGRS.parse(texto));
                        } catch (Exception e){
                            System.out.println("Error al parsear MGRS: " + e.getMessage());
                        }
                        switch (opcionSalSeleccionada) {
                            case "Degrees" -> {
                                resD = mgrs.aGrados();
                                tf.setText(resD.get(0) + "°" + resD.get(1) + "°");
                            }
                            case "Degrees Minutes" -> {
                                resD = mgrs.aGradosMinutos();
                                tf.setText(resD.get(0) + "° " + String.format("%.2f", resD.get(1)) + "′ " + resD.get(2) + "° " + String.format("%.2f", resD.get(3)) + "′ ");
                            }
                            case "Degrees Minutes Seconds" -> {
                                resI = mgrs.aGradosMinutosSegundos();
                                tf.setText(resI.get(0) + "° " + resI.get(1) + "′ " + resI.get(2) + "″ " + resI.get(3) + "° " + resI.get(4) + "′ " + resI.get(5) + "″ ");
                            }
                        }
                        auxD = mgrs.aGrados();
                        if(auxD.get(0) == 180.0){
                            auxD.set(0, 179.999);
                        }
                        mapa.setLon(auxD.get(0));
                        mapa.setLat(auxD.get(1));
                        showMarcador();
                    }
                }
            }
        }
    }



    @FXML
    public void convertFrom() {
        ajusteDeSeleccion(cajaDeEntrada, cajaDeSalida, inputField);
    }

    @FXML
    public void convertTo() {
        ajusteDeSeleccion(cajaDeSalida, cajaDeEntrada, outputField);
    }

    @FXML
    public void textoFrom() {
        posiblesEntradas(cajaDeEntrada.getValue(), inputField);
    }

    @FXML
    public void typedFrom(){
        gd = new GradosDecimales();
        gm = new GradosMinutos();
        gms = new GradosMinutosSegundos();
        mgrs = new CuadriculaMilitar();

        String opcionEntSeleccionada = cajaDeEntrada.getValue();
        String opcionSalSeleccionada = cajaDeSalida.getValue();
        String texto = inputField.getText();

        entradaDeTexto(opcionEntSeleccionada , opcionSalSeleccionada , texto , outputField);
    }

    @FXML
    public void textoTo(){
        posiblesEntradas(cajaDeSalida.getValue(), outputField);
    }

    @FXML
    public void typedTo(){
        gd = new GradosDecimales();
        gm = new GradosMinutos();
        gms = new GradosMinutosSegundos();
        mgrs = new CuadriculaMilitar();

        String opcionEntSeleccionada = cajaDeSalida.getValue();
        String opcionSalSeleccionada = cajaDeEntrada.getValue();
        String texto = outputField.getText();

        entradaDeTexto(opcionEntSeleccionada , opcionSalSeleccionada , texto , inputField);
    }
}
