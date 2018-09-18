package com.rmv.code.generator.rmvImageTool.utils;


import net.coobird.thumbnailator.geometry.Position;

import java.awt.*;
import java.util.logging.Logger;

public class PositionsFactory {
    private static Logger log = Logger.getLogger(PositionsFactory.class.getSimpleName());
    public static Position getRmvImagePosition(int xTotal, int yTotal,int xIndex, int yIndex){
        Position position = new Position() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {
                int x = insetLeft;
                int y = insetTop;
                x += enclosingWidth/xTotal * (xIndex -1);
                y += enclosingHeight/yTotal * (yIndex -1);
                return new Point(x, y);
            }
        };
        return position;

    }
    public static Position getBossHpPosition(){
        Position position = new Position() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {
                int x = (enclosingWidth / 2) - (width / 2);
                int y = insetTop;
                return new Point(x, y);
            }
        };
        return position;
    }

}
