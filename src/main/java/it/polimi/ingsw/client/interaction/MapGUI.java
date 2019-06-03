package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.resources.R;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class MapGUI extends GridPane {

    public MapGUI(int mapID) {
        super();

        switch (mapID) {
            case 0:
                buildMap0();
            case 1:
                buildMap1();
            case 2:
                buildMap2();
            case 3:
                buildMap3();
        }
    }

    private void buildMap0() {
        this.add(getCell("1-x1y1"), 0, 2);
        this.add(getCell("1-x1y2"), 0, 1);
        this.add(getCell("1-x1y3"), 0, 0);
        this.add(getCell("1-x2y1"), 1, 2);
        this.add(getCell("1-x2y2"), 1, 1);
        this.add(getCell("1-x2y3"), 1, 0);
        this.add(getCell("1-x3y1"), 2, 2);
        this.add(getCell("1-x3y2"), 2, 1);
        this.add(getCell("1-x3y3"), 2, 0);
        this.add(getCell("1-x4y1"), 3, 2);
        this.add(getCell("1-x4y2"), 3, 1);
        this.add(getCell("1-x4y3"), 3, 0);
        this.setWidth(1500);
    }

    private void buildMap1() {
        this.add(getCell("1-x1y1"), 0, 2);
        this.add(getCell("1-x1y2"), 0, 1);
        this.add(getCell("1-x1y3"), 0, 0);
        this.add(getCell("1-x2y1"), 1, 2);
        this.add(getCell("1-x2y2"), 1, 1);
        this.add(getCell("1-x2y3"), 1, 0);
        this.add(getCell("2-x3y1"), 2, 2);
        this.add(getCell("2-x3y2"), 2, 1);
        this.add(getCell("2-x3y3"), 2, 0);
        this.add(getCell("2-x4y1"), 3, 2);
        this.add(getCell("2-x4y2"), 3, 1);
        this.add(getCell("2-x4y3"), 3, 0);
        this.setWidth(1500);
    }

    private void buildMap2() {
        this.add(getCell("2-x1y1"), 0, 2);
        this.add(getCell("2-x1y2"), 0, 1);
        this.add(getCell("2-x1y3"), 0, 0);
        this.add(getCell("2-x2y1"), 1, 2);
        this.add(getCell("2-x2y2"), 1, 1);
        this.add(getCell("2-x2y3"), 1, 0);
        this.add(getCell("2-x3y1"), 2, 2);
        this.add(getCell("2-x3y2"), 2, 1);
        this.add(getCell("2-x3y3"), 2, 0);
        this.add(getCell("2-x4y1"), 3, 2);
        this.add(getCell("2-x4y2"), 3, 1);
        this.add(getCell("2-x4y3"), 3, 0);
        this.setWidth(1500);
    }

    private void buildMap3() {
        this.add(getCell("2-x1y1"), 0, 2);
        this.add(getCell("2-x1y2"), 0, 1);
        this.add(getCell("2-x1y3"), 0, 0);
        this.add(getCell("2-x2y1"), 1, 2);
        this.add(getCell("2-x2y2"), 1, 1);
        this.add(getCell("2-x2y3"), 1, 0);
        this.add(getCell("1-x3y1"), 2, 2);
        this.add(getCell("1-x3y2"), 2, 1);
        this.add(getCell("1-x3y3"), 2, 0);
        this.add(getCell("1-x4y1"), 3, 2);
        this.add(getCell("1-x4y2"), 3, 1);
        this.add(getCell("1-x4y3"), 3, 0);
        this.setWidth(1500);
    }

    private ImageView getCell(String resID) {
        ImageView img = new ImageView(R.image(resID));
        img.setPreserveRatio(true);
        img.setFitHeight(150);
        img.setOnMouseEntered(e -> {
            img.setOpacity(0.5);
        });
        img.setOnMouseExited(e -> {
            img.setOpacity(1);
        });
        return img;
    }
}