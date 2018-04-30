/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tul.sti.sem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tul.sti.sem.exception.XCellException;
import tul.sti.sem.session.Session;

/**
 *
 * @author alesf_000
 */
public class XCell extends ListCell<String> {

    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Label time = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button("Stáhnout");
    Button chartButton = new Button("graf");
    String lastItem;
    ArrayList<Integer> itemRowsEvolution;
    String urlRow;
    Session session;

    public XCell(Session session) {
        super();
        this.session = session;
        hbox.getChildren().addAll(label, pane, time, chartButton, button);
        hbox.setSpacing(10);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction((ActionEvent event) -> {
            try {
                saveFile(lastItem, urlRow);
            } catch (XCellException ex) {
                new ErrorMessageWindow(ex.getMessage()).showMessageWindow();
            }

        });
        chartButton.setOnAction((ActionEvent event) -> {
            String title = makeTitle();
            String stageTitle = makeStageTitle();
            String portfolio = makePortfolio();
            MyLineChart chart = new MyLineChart(itemRowsEvolution, title, stageTitle, portfolio);
            chart.printChart();
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            String[] splitItem = item.split(";");
            lastItem = splitItem[0];
            urlRow = splitItem[2];
            if (item.contains(".java")) {
                chartButton.setVisible(true);
                itemRowsEvolution = new ArrayList<>();
                for (int i = 3; i < splitItem.length; i++) {
                    itemRowsEvolution.add(Integer.parseInt(splitItem[i]));
                }
//                itemRowsEvolution = session.getFileList().get(Integer.getInteger(splitItem[3])).getRowsEvolution();
            } else {
                chartButton.setVisible(false);
            }

            label.setText(splitItem[0] != null ? splitItem[0] : "<null>");
            time.setText(splitItem[1] != null ? splitItem[1] : "<null>");
            setGraphic(hbox);
        }
    }

    private void saveFile(String lastItem, String Source) throws XCellException {
        System.out.println(lastItem);
        String[] splitItem = lastItem.split("/");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(splitItem[splitItem.length-1]);
        File file;
        if (!session.getPathToSave().isEmpty()) {
            file = new File(session.getPathToSave());
            fileChooser.setInitialDirectory(file);
        }
        Stage stage = new Stage();
        //Show save file dialog
        try {
            file = fileChooser.showSaveDialog(stage);
            String absolutePath = file.getAbsolutePath();
            String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
            session.setPathToSave(filePath);
            StringBuilder sb = new StringBuilder(filePath);
            for (int i = 0; i < splitItem.length; i++) {
                sb.append("\\");
                sb.append(splitItem[i]);
            }
            file = new File(sb.toString());
            file.getParentFile().mkdirs();
            ArrayList<String> line = getSurceByString(Source);
            try (PrintWriter writer = new PrintWriter(file)) {
                for (int i = 0; i < line.size(); i++) {
                    writer.println(line.get(i));
                }
            }
            System.out.println(file.getAbsolutePath());
        } catch (NullPointerException ex) {
            // zavrene okno bez zadani cesty
        } catch (IOException ex) {
            throw new XCellException("012");
        }
    }

    private ArrayList<String> getSurceByString(String Source) throws MalformedURLException, IOException {
        InputStream is = new URL(Source).openStream();
        ArrayList<String> lines = new ArrayList<>();
        try {
            String cp;
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((cp = rd.readLine()) != null) {
                lines.add(cp);
            }
        } finally {
            is.close();
        }
        return lines;
    }

    private String makeTitle() {
        StringBuilder sb = new StringBuilder("Vývoj počtu řádků v souboru ");
        sb.append(lastItem);
        return sb.toString();
    }

    private String makeStageTitle() {
        StringBuilder sb = new StringBuilder("Graf vývoje počtu řádku ");
        sb.append(lastItem);
        return sb.toString();
    }

    private String makePortfolio() {
        StringBuilder sb = new StringBuilder(session.getRepoName());
        sb.append(" ");
        sb.append(lastItem);
        return sb.toString();
    }

}
