/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.utils.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author cfoch
 */
public class UIFuncs {
    public static class Dialogs {

        /**
         * Utilidad para crear un menu genérico.
         * @param <K> Un Enum que implemente toString, por ejemplo CREAR,
         *            EDITAR, BORRAR.
         */
        public static class SimplePopupMenuFactory<K extends Enum<K>> {
            private ArrayList<JFXButton> buttonsSorted;
            private HashMap<K, JFXButton> buttonsMap;
            private JFXPopup popup;

            public SimplePopupMenuFactory(K [] enumValues) {
                int i;
                popup = null;
                buttonsSorted = new ArrayList<>();
                buttonsMap = new HashMap<>();

                System.out.println("Length: " + enumValues.length);
                for (i = 0; i < enumValues.length; i++) {
                    JFXButton button;
                    button = new JFXButton(enumValues[i].toString());
                    System.out.println("but:" + enumValues[i].toString());
                    buttonsSorted.add(button);
                    buttonsMap.put(enumValues[i], button);
                };
            }

            public JFXButton getButton(K enumValue) {
                return buttonsMap.get(enumValue);
            }

            public JFXPopup getPopup() {
                if (popup == null) {
                    VBox vBox;

                    vBox = new VBox();
                    buttonsSorted.forEach((button) -> {
                        vBox.getChildren().add(button);
                        button.setPadding(new Insets(20));
                        button.setPrefSize(145, 40);
                    });
                    popup = new JFXPopup();
                    popup.setPopupContent(vBox);
                }
                return popup;
            }
        }

        public static class HEADINGS {
            public static final String EMPTY = "";
            public static final String EXITO = "Éxito";
            public static final String ERROR = "Error";
        }

        public static class BUTTON {
            public static final String ACEPTAR = "Aceptar";
            public static final String CANCELAR = "Cancelar";
            public static final String CERRAR = "Cerrar";
            public static final String CREAR = "Crear";
            public static final String EDITAR = "Editar";
        }

        public static class MESSAGES {
            public static final String DB_GENERIC_ERROR =
                    "Lo sentimos. Hubo un problema con la conexión.";
            public static final String CRUD_DELETE_ERROR =
                    "No se pudo borrar este elemento. ¿Tal vez otros dependen "
                    + "de este?";
            public static final String CRUD_UPDATE_ERROR =
                    "Lo sentimos. Hubo un problema al intentar actualizar este "
                    + "elemento. ¿Existe?";

            public static final String CRUD_CREATE_SUCCESS =
                    "Creación exitosa.";
            public static final String CRUD_UPDATE_SUCCESS =
                    "Este element ha sido actualizado con éxito.";
            public static final String CRUD_DELETE_SUCCESS =
                    "Este elemento ha sido borrado con éxito.";
        }

        /**
         * Shows a dialog displaying a message.
         * @param pane The StackPane
         * @param heading The title.
         * @param body The text of the body.
         * @param textButton The text of the single button.
         */
        public static void showMsg(StackPane pane, String heading, String body,
                String textButton) {
            showDialog(pane, heading, new Text(body),
                    new JFXButton(textButton), true);
        }

        /**
         * Shows a dialog with a generic node.
         * @param pane The StackPane
         * @param heading The title.
         * @param nodeBody A generic node.
         * @param textButton The text of the single button.
         */
        public static void showDialog(StackPane pane, String heading,
                javafx.scene.Node nodeBody, JFXButton button, boolean close) {
            JFXDialog dialog;
            dialog = buildDialog(pane, heading, nodeBody, button, close);
            dialog.show();
        }

        public static JFXDialog buildDialog(StackPane pane, String heading,
                javafx.scene.Node nodeBody, JFXButton button, boolean close) {
            JFXDialogLayout content;
            JFXDialog dialog;

            content = new JFXDialogLayout();
            content.setHeading(new Text(heading));
            content.setBody(nodeBody);

            dialog = new JFXDialog(pane, content,
                    JFXDialog.DialogTransition.CENTER);
            if (close) {
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        dialog.close();
                    }
                });
            }
            content.setActions(button);
            return dialog;
        }
    }

    public static <T> Node createNodeFromFXML(String pathFXML) {
        Pair<Node, T> pair;
        pair = createNodeControllerFromFXML(pathFXML);
        if (pair == null) {
            return null;
        }
        return pair.getLeft();
    }

    public static <T> Pair<Node, T> createNodeControllerFromFXML(
            String pathFXML) {
        Pair<Node, FXMLLoader> pair = createNodeLoaderFromFXML(pathFXML);
        return Pair.of(pair.getLeft(), pair.getRight().<T>getController());
    }

    public static <T> Pair<Node, FXMLLoader> createNodeLoaderFromFXML(
            String pathFXML) {
        Node node;
        URL resource;
        String resourcePath;
        FXMLLoader loader;

        resource = UIFuncs.class.getResource(pathFXML);

        loader = new FXMLLoader(resource);

        try {
            node = (Node) loader.load();
        } catch (IOException ex) {
            Logger.getLogger(UIFuncs.class.getName())
                    .log(Level.SEVERE, null, ex);
            return null;
        }
        return Pair.of(node, loader);
    }

    public static <T> Node createNodeFromControllerFXML(T controller,
            String pathFXML) {
        Node node;
        URL resource;
        String resourcePath;
        FXMLLoader loader;

        resource = UIFuncs.class.getResource(pathFXML);

        loader = new FXMLLoader(resource);
        loader.setController(controller);

        try {
            node = (Node) loader.load();
        } catch (IOException ex) {
            Logger.getLogger(UIFuncs.class.getName())
                    .log(Level.SEVERE, null, ex);
            return null;
        }
        return node;
    }

}
