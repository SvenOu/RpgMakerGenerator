package com.rmv.code.generator.rmvImageTool.utils;


import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.name.Rename;

import java.util.logging.Logger;

public class RenameFactory {
    private static Logger log = Logger.getLogger(RenameFactory.class.getSimpleName());
    public static Rename getRmvRename(int xIndex, int yIndex){

        return new Rename() {
            @Override
            public String apply(String name, ThumbnailParameter param) {
                return appendSuffix(name, new StringBuffer()
                        .append("_")
                        .append(yIndex)
                        .append("_")
                        .append(xIndex)
                        .toString());

            }
        };

    }

}
