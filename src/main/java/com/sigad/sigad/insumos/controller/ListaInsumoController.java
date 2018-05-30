/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.insumos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.helpers.InsumosHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class ListaInsumoController implements Initializable {
    
    /**
     * Initializes the controller class.
     */
    
    public static String viewPath = "/com/sigad/sigad/insumos/view/ListaInsumo.fxml";
    
    @FXML
    private StackPane hiddenSp;

    @FXML
    private JFXTreeTableView<InsumoViewer> tblInsumos;
    
    @FXML
    private JFXButton moreBtn;

    @FXML
    private JFXButton btnAdd;

    @FXML
    public static JFXDialog insumoDialog;
    
    public static boolean isInsumoCreate;
    
    // table elements;
    
    private ArrayList<String> selectedId;
    
    JFXTreeTableColumn<InsumoViewer,Boolean> selectCol = new JFXTreeTableColumn<>("Seleccionar");
    JFXTreeTableColumn<InsumoViewer,String> nombreCol = new JFXTreeTableColumn<>("Nombre");
    JFXTreeTableColumn<InsumoViewer,String> stockCol = new JFXTreeTableColumn<>("Stock Total");
    JFXTreeTableColumn<InsumoViewer,String> volumenCol = new JFXTreeTableColumn<>("Volumen");
    static ObservableList<InsumoViewer> insumosList;
    
    public static class InsumoViewer extends RecursiveTreeObject<InsumoViewer>{

        public SimpleStringProperty getNombre() {
            return nombre;
        }

        public SimpleStringProperty getDescripcion() {
            return descripcion;
        }

        public SimpleStringProperty getTiempoVida() {
            return tiempoVida;
        }

        public SimpleStringProperty getStockTotal() {
            return stockTotal;
        }

        public SimpleBooleanProperty getActivo() {
            return activo;
        }

        public SimpleStringProperty getVolumen() {
            return volumen;
        }

        public BooleanProperty getSeleccion() {
            return seleccion;
        }

//        public SimpleStringProperty getId() {
//            return id;
//        }

        public void setNombre(String nombre) {
            this.nombre = new SimpleStringProperty(nombre);
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = new SimpleStringProperty(descripcion);
        }

        public void setTiempoVida(String tiempoVida) {
            this.tiempoVida = new SimpleStringProperty(tiempoVida);
        }

        public void setStockTotal(String stockTotal) {
            this.stockTotal = new SimpleStringProperty(stockTotal);
        }

        public void setActivo(Boolean activo) {
            this.activo = new SimpleBooleanProperty(activo);
        }

        public void setVolumen(String volumen) {
            this.volumen = new SimpleStringProperty(volumen);
        }

        public void setSeleccion(BooleanProperty seleccion) {
            this.seleccion = seleccion;
        }

//        public void setId(SimpleStringProperty id) {
//            this.id = id;
//        }
        private SimpleStringProperty nombre;
        private SimpleStringProperty descripcion;
        private SimpleStringProperty tiempoVida;
        private SimpleStringProperty stockTotal;
        private SimpleBooleanProperty activo;
        private SimpleStringProperty volumen;
        private BooleanProperty seleccion;
        private SimpleIntegerProperty cantidad;
        //private SimpleStringProperty id;
        ImageView imagen;
        
        public InsumoViewer(String nombre,String descripcion, String tiempoVida,String stockTotal, Boolean activo, String volumen ,String imagePath, Integer cantidad) {
            this.nombre = new SimpleStringProperty(nombre);
            this.descripcion = new SimpleStringProperty(descripcion);
            this.tiempoVida = new SimpleStringProperty(tiempoVida);
            this.stockTotal = new SimpleStringProperty(stockTotal);
            this.activo = new SimpleBooleanProperty(activo);
            this.volumen = new SimpleStringProperty(volumen);
            this.cantidad = new SimpleIntegerProperty(cantidad);
            //this.id = new SimpleStringProperty(id);
        }

        public SimpleIntegerProperty getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = new SimpleIntegerProperty(cantidad);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        insumosList = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        fillData();
    }    
    
    private void setColumns(){
        selectCol.setPrefWidth(80);
        selectCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, Boolean> param) -> param.getValue().getValue().getSeleccion() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        selectCol.setCellFactory((TreeTableColumn<InsumoViewer,Boolean>param) -> {
            CheckBoxTreeTableCell<InsumoViewer,Boolean> cell = new CheckBoxTreeTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell; //To change body of generated lambdas, choose Tools | Templates.
        });
        nombreCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getNombre() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        stockCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getStockTotal() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        volumenCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getVolumen() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
       
    }
    
    private void addColumns(){
        final TreeItem<InsumoViewer> rootInsumo = new RecursiveTreeItem<>(insumosList,RecursiveTreeObject::getChildren);
        tblInsumos.setEditable(true);
        tblInsumos.getColumns().setAll(selectCol,nombreCol,stockCol,volumenCol);
        tblInsumos.setRoot(rootInsumo);
        tblInsumos.setShowRoot(false);
    }
    
    private void fillData(){
        InsumosHelper helper = new InsumosHelper();
        ArrayList<Insumo> listaInsumos = helper.getInsumos();
        if(listaInsumos != null){
            listaInsumos.forEach((i)-> {
                updateTable(i);
            });
        }
    }
    
    public static void updateTable(Insumo insumo){
        insumosList.add(new InsumoViewer(insumo.getNombre(),
                                         insumo.getDescripcion(),
                                         Integer.toString(insumo.getTiempoVida()),
                                         Integer.toString(insumo.getStockTotalFisico()),
                                         insumo.isActivo(),
                                         "0",
                                         insumo.getImagen(),0));
    }
    
    @FXML
    private void handleAction(ActionEvent event) {
        if(event.getSource() == btnAdd ) {
            try {
                CreateEditUserDialog(true);
            } catch (IOException ex) {
                Logger.getLogger(ListaInsumoController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
            }
        }
    }
    
    public void CreateEditUserDialog(boolean iscreate) throws IOException {
        isInsumoCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(isInsumoCreate){
            content.setHeading(new Text("Crear Insumo"));
        }else{
            content.setHeading(new Text("Editar Usuario"));
        }
        
        Node node;
        node = (Node) FXMLLoader.load(ListaInsumoController.this.getClass().getResource(CrearEditarInsumoController.viewPath));
        content.setBody(node);
        
        insumoDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        insumoDialog.show();
    }  
    
}