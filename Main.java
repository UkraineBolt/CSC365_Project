/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc365project1;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.stage.Stage;

/**
 *
 * @author alex
 */
public class Main extends Application {

    private static final double Length = 800;
    private static final double Width = 1000;

    Button drawbutt(String x, double w, double l) {
        Button b = new Button(x);
        b.setPrefSize(w, l);
        return b;
    }

    Line drawLine(double sx, double sy, double ex, double ey) {
        Line l = new Line(sx, sy, ex, ey);
        l.setStrokeWidth(4);
        return l;
    }

    @Override
    public void start(Stage stage) throws Exception {//stage variable name used for a lot
        GridPane gp = new GridPane();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        gp.getRowConstraints().addAll(row1, row2);

        VBox pane = new VBox();
        pane.setSpacing(1);
        pane.setPrefWidth(Width);
        Text t = new Text("Website Similarty");
        t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.MEDIUM, FontPosture.REGULAR, 12));
        t.setFill(Color.BLACK);
        Text s = new Text("search, update, tree or quit");
        s.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.MEDIUM, FontPosture.REGULAR, 12));
        s.setFill(Color.BLACK);
        TextField f = new TextField();
        TextField output = new TextField();
        output.setEditable(false);
        Text an = new Text("Answer box");
        an.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.MEDIUM, FontPosture.REGULAR, 12));
        an.setFill(Color.BLACK);

        Handler h = new Handler();
        String data;

        pane.getChildren().addAll(t, s, f, an, output);//puts nodes on the screen
        pane.setMaxWidth(Width);

        gp.addRow(0, pane);

        try {
            h.btreefilereader();
        } catch (Exception e) {
            System.out.println("bt didnt load");
        }
        if (!h.fileEmpty()) {
            data = "Database exists";
        } else {
            data = "No database found: Update required";
        }
        output.clear();
        output.replaceSelection(data);
        f.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == KeyCode.ENTER) {//send text to search code.                
                System.out.println(f.getText());//getText allows to grab what was typed
                String input = f.getText();
                f.clear();

                switch (input) {
                    case "search":
                        s.setText("Enter the url that you want compared below");
                        f.setOnKeyPressed((KeyEvent b) -> {
                            if (b.getCode() == KeyCode.ENTER) {
                                String site = f.getText();
                                if (!site.equals("quit")) {
                                    try {
                                        if (site.startsWith("http")) {
                                            if (h.checkBtree(site)) {
                                                try {
                                                    String x = h.ss(site);
                                                    f.clear();
                                                    output.clear();
                                                    output.replaceSelection("Found in Btree--cluster: " + x + " & " + site + " is most relatable to itself as it was found in the btree");
                                                } catch (Exception m) {
                                                    System.out.println("checkbtree broke by " + m);
                                                }
                                            } else {
                                                System.out.println("number was not found");
                                                f.clear();
                                                String c = h.ss(site);
                                                String ss = h.ssa(site);
                                                output.clear();
                                                output.replaceSelection("Not found in btree--cluster: " + c + " & " + site + " is most relatable to " + ss);
                                            }
                                        } else {
                                            f.clear();
                                            output.clear();
                                            output.replaceSelection("not valid website");
                                        }
                                    } catch (Exception ex) {
                                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                        output.clear();
                                        output.replaceSelection("Something broke-->needs debug");
                                    }
                                } else {
                                    stage.close();
                                }
                            }
                        });
                        break;
                    case "update":
                        try {
                            output.clear();
                            output.replaceSelection("Update in progress");
                            h.update();
                            output.clear();
                            output.replaceSelection("Update complete");
                        } catch (Exception ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            output.clear();
                            output.replaceSelection("Something broke-->needs debug");
                        }
                        break;
                    case "quit":
                        stage.close();
                        break;
                    case "tree":
                        try {
                            if (h.streeEmpty()) {
                                try {
                                    System.out.println("making tree");
                                    h.makeTree();
                                } catch (Exception ex) {
                                    System.out.println("broke when making tree " + ex);
                                    stage.close();
                                }

                            } else {
                                try {
                                    System.out.println("calling tree");
                                    h.callTree();
                                } catch (Exception ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                    stage.close();
                                }
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        s.setText("Enter url you want to search");
                        f.setOnKeyPressed((KeyEvent b) -> {
                            if (b.getCode() == KeyCode.ENTER) {
                                if (gp.getChildren().size() == 2) {
                                    gp.getChildren().remove(1);
                                }
                                int count=1;
                                boolean bar=false;
                                String in = f.getText();
                                ArrayList<String> temp = h.getPath(in);
                                if (temp != null) {
                                    boolean last=false;
                                    output.clear();
                                    output.replaceSelection("number of trees " + h.st.AOST + " " + temp.toString());
                                        for (int i = 0; i < temp.size()-1; i++) {//graphs nodes
                                            HBox p = new HBox();
                                            int wordlength = temp.get(i).length();
                                            double width = wordlength * 6.5;
                                            int wordlength2 = temp.get(i+1).length();
                                            double width2 = wordlength2*6.5;
                                            Button bb = drawbutt(temp.get(i), width, 12);
                                            Line l = drawLine(20, -5, 1, -5);
                                            Button bb2 = drawbutt(temp.get(i+1), width2, 12);
                                            Line l2 = drawLine(200, 400, 200, 400);
                                            p.getChildren().addAll(bb, l,bb2,l2);   
                                            if(i<temp.size()-1){
                                                p.getChildren().remove(p.getChildren().size()-1);
                                            }
                                            gp.add(p, 0, count);
                                            count++;
                                            
                                        }
                                    
                                } else {
                                    output.replaceSelection("Path not found");
                                }
                            }
                        });
                        break;
                    default:
                        output.clear();
                        output.replaceSelection("Not valid input, type search, update or quit.");
                        break;
                }
            }
        });
        stage.setScene(new Scene(gp, Width, Length));//sets the window
        stage.setTitle("csc365");
        stage.setResizable(false);
        stage.show();//shows the fucking window

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
