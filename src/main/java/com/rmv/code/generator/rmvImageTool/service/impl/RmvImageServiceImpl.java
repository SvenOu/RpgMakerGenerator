package com.rmv.code.generator.rmvImageTool.service.impl;

import com.rmv.code.generator.rmvImageTool.service.RmvImageEnemyService;
import com.rmv.code.generator.rmvImageTool.service.RmvImageMenuService;
import com.rmv.code.generator.rmvImageTool.service.RmvImageService;
import com.rmv.code.generator.rmvImageTool.utils.PositionsFactory;
import com.rmv.code.generator.rmvImageTool.utils.RenameFactory;
import com.rmv.code.generator.rmvImageTool.vo.RMVConfig;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Logger;


@Component
public class RmvImageServiceImpl implements RmvImageService {
    private static Logger log = Logger.getLogger(RmvImageServiceImpl.class.getSimpleName());

    @Value("${rmv.image.mainDir}")
    private String mainDir;

    @Value("${rmv.image.faceDir}")
    private String faceDir;

    @Value("${rmv.image.atbDir}")
    private String atbDir;

    @Value("${rmv.image.battleHudDir}")
    private String battleHudDir;

    @Value("${rmv.image.battleCommandsDir}")
    private String battleCommandsDir;

    @Value("${rmv.image.actorhudDir}")
    private String actorhudDir;

    @Value("${rmv.image.template.baseDir}")
    private String baseTemplateDir;

    @Value("${rmv.image.template.actorhudBorderName}")
    private String actorhudBorderName;

    @Value("${rmv.image.template.actorBattleCommandBorderName}")
    private String actorBattleCommandBorderName;

    @Value("${rmv.image.template.atbFaceBorderName}")
    private String atbFaceBorderName;

    private RmvImageEnemyService rmvImageEnemyService;
    private RmvImageMenuService rmvImageMenuService;

    @Autowired
    public RmvImageServiceImpl(RmvImageEnemyService rmvImageService, RmvImageMenuService rmvImageMenuService) {
        this.rmvImageEnemyService = rmvImageService;
        this.rmvImageMenuService = rmvImageMenuService;
    }

    @Override
    public void generatorFaceDir() throws IOException {

        int halfFaceHeight = RMVConfig.FACE_SOURCE_img_battleCommands_face_height;

        File file = new File(faceDir);
        File mainGenerateDir = new File(mainDir + "/Generate/Temp/SpitFaces");

        FileSystemUtils.deleteRecursively(mainGenerateDir);
        if (!mainGenerateDir.exists()) {
            mainGenerateDir.mkdirs();
        }
        for (int y = 1; y <= RMVConfig.FACE_SOURCE_IMAGE_Y; y++) {
            for (int x = 1; x <= RMVConfig.FACE_SOURCE_IMAGE_X; x++) {
                Position pos = PositionsFactory.getRmvImagePosition(RMVConfig.FACE_SOURCE_IMAGE_X, RMVConfig.FACE_SOURCE_IMAGE_Y, x, y);
                Thumbnails.of(file.listFiles())
                        .sourceRegion(pos,
                                RMVConfig.FACE_SOURCE_IMAGE_WIDTH / RMVConfig.FACE_SOURCE_IMAGE_X,
                                RMVConfig.FACE_SOURCE_IMAGE_HEIGHT / RMVConfig.FACE_SOURCE_IMAGE_Y)
                        .scale(1.0)
                        .toFiles(mainGenerateDir, RenameFactory.getRmvRename(x, y));

            }
        }

        generateActorHalfFaces(mainGenerateDir, halfFaceHeight);

        rmvImageMenuService.generatorMenuFaceDir();
        rmvImageEnemyService.generatorEnemyFaceDir();

        generateActorHud(mainGenerateDir);
        generateActorAtb(mainGenerateDir);
        generateBattlecommandFaces(mainGenerateDir);
        generateBattleHudFaces(mainGenerateDir);
    }

    private void generateBattleHudFaces(File mainGenerateDir) throws IOException {

        int width = RMVConfig.FACE_SOURCE_img_battleHud_face_width;
        int height = RMVConfig.FACE_SOURCE_img_battleHud_face_height;

        File[] gFaceFiles = new File(mainDir + "/Generate/Temp/SpitHalfFaces").listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            private int recX = 0;
            private int recY = 0;

            private int recWidth = width / 5;
            private int recHeight = height;

            @Override
            public void accept(BufferedImage bufferedImage) {
                recX = 0;
                recY = 0;
                int nextX = 0;
                int nextY = 0;

                AffineTransform transform = new AffineTransform();

                BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                for (int i = 1; i <= 5; i++) {
                    nextX = i* recWidth;
                    nextY = recY + recHeight;

                    if(i == 1){
                        g2.setColor(new Color(255,255,255,1));
                        g2.drawImage(bufferedImage, recX, recY, nextX, nextY, 0, 0, recWidth, recWidth, null);
                    }else if(i == 2 ){
                        transform.rotate(Math.PI / 7.5, bufferedImage.getWidth()/2, bufferedImage.getHeight()/2);
                        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                        BufferedImage bfImg2 = op.filter(bufferedImage, null);
                        g2.setColor(new Color(255,255,255,25));
                        g2.drawImage(bfImg2, recX, recY, nextX, nextY, 0, 0, recWidth, recWidth, null);
                    }if(i == 3 ){
                        transform.rotate(-Math.PI / 7.5 * 1.5, bufferedImage.getWidth()/2, bufferedImage.getHeight()/2);
                        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                        BufferedImage bfImg2 = op.filter(bufferedImage, null);
                        g2.setColor(new Color(255,255,255,50));
                        g2.drawImage(bfImg2, recX, recY, nextX, nextY, 0, 0, recWidth, recWidth, null);
                    }if(i == 4 ){
                        g2.setColor(new Color(255,255,255,75));
                        g2.drawImage(bufferedImage, recX, recY, nextX, nextY, 0, 0, recWidth, recWidth, null);
                    }if(i == 5 ){
                        g2.drawImage(bufferedImage, recX, recY, nextX, nextY, 0, 0, recWidth, recWidth, null);
                        g2.setColor(new Color(0,0,0,75));
                    }
                    g2.fillOval(recX,recY, recHeight, recHeight);
                    recX = nextX;
                    recY = 0;
                }

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/" + battleHudDir);
                    if (!actorhudFloder.exists()) {
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() + "/" + gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .size(height, height)
                .outputQuality(1)
                .iterableBufferedImages()
                .forEach(buffIta);
    }

    private void generateBattlecommandFaces(File mainGenerateDir) throws IOException {

        int width = RMVConfig.FACE_SOURCE_img_battleCommands_face_width;
        int height = RMVConfig.FACE_SOURCE_img_battleCommands_face_height;

        int recX = height - height / 4;
        int recY = height / 4;
        int recWidth = width - height + height / 4;
        int recHeight = height / 2;
        File[] gFaceFiles = new File(mainDir + "/Generate/Temp/SpitHalfFaces").listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;
            private int r = 0;
            private int g = 0;
            private int b = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {
                if (b < 255) {
                    b++;
                } else {
                    if (g < 255) {
                        g++;
                    } else {
                        if (r < 255) {
                            r++;
                        } else {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                    }
                }
                ;
                BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                GradientPaint g1 = new GradientPaint(new Point2D.Float(recX, recY),
                        new Color(r, g, b, 255),
                        new Point2D.Float(recX + recWidth, recY + recHeight),
                        new Color(255, 255, 255, 0), true);

                g2.setPaint(g1);
                Stroke s = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g2.setStroke(s);
                g2.setClip(new Rectangle2D.Float(recX, recY, recWidth, recHeight));
                g2.fillOval(0, 0, width, height);

                g2.setClip(new Rectangle2D.Float(0, 0, width, height));

                AffineTransform transform = new   AffineTransform(-1,0,0,1,bufferedImage.getWidth()-1,0);//水平翻转
                AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                bufferedImage = op.filter(bufferedImage, null);

                g2.drawImage(bufferedImage, 0, 0, height, height, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/" + battleCommandsDir);
                    if (!actorhudFloder.exists()) {
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() + "/" + gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .scale(1.0)
                .outputQuality(1)
                .iterableBufferedImages()
                .forEach(buffIta);


    }

    private void generateActorHalfFaces(File mainGenerateDir, int height) throws IOException {
        int width = height;
        File[] gFaceFiles = mainGenerateDir.listFiles();

        int oriFaceWidth = RMVConfig.FACE_SOURCE_IMAGE_WIDTH / RMVConfig.FACE_SOURCE_IMAGE_X;
        int oriFaceHeight = RMVConfig.FACE_SOURCE_IMAGE_HEIGHT / RMVConfig.FACE_SOURCE_IMAGE_Y;

        File borderFile = new File(baseTemplateDir + actorBattleCommandBorderName);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);

        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {
                bufferedImage = bufferedImage.getSubimage(
                        (oriFaceWidth - width) / 8,
                        (oriFaceHeight - width) / 6,
                        width, width);

                BufferedImage circleBuffer = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.drawImage(borderFileBuf, 0, 0, width, width, null);

                g2.setClip(new Arc2D.Double(6, 6, width - 12, width - 12, 0, 360, Arc2D.OPEN));
                g2.drawImage(bufferedImage, 0, 0, width, width, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/Temp/SpitHalfFaces");
                    if (!actorhudFloder.exists()) {
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() + "/" + gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .scale(0.65)
                .outputQuality(1)
                .iterableBufferedImages()
                .forEach(buffIta);
    }

    private void generateActorAtb(File mainGenerateDir) throws IOException {
        File borderFile = new File(baseTemplateDir + atbFaceBorderName);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);
        int width = RMVConfig.FACE_SOURCE_img_atbFace_size;

        File[] gFaceFiles = mainGenerateDir.listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {

                BufferedImage circleBuffer = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.setClip(new Rectangle2D.Float(0, 0, width, width));
                g2.drawImage(borderFileBuf, 0, 0, width, width, null);

                int[] xPoints = {0, width / 2, width, width / 2};
                int[] yPoints = {width / 2, 0, width / 2, width};
                g2.setClip(new Polygon(xPoints, yPoints, 4));

                g2.setColor(new Color(255,255,255, 100));
                g2.fillOval(0, 0, width, width);

                g2.drawImage(bufferedImage, 0, 0, width, width, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/" + atbDir);
                    if (!actorhudFloder.exists()) {
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() + "/" + gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .size(width, width)
                .outputQuality(1)
                .iterableBufferedImages()
                .forEach(buffIta);
    }


    private void generateActorHud(File mainGenerateDir) throws IOException {
        File borderFile = new File(baseTemplateDir + actorhudBorderName);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);
        int width = RMVConfig.FACE_SOURCE_img_actorhud_size;

        File[] gFaceFiles = mainGenerateDir.listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {


                BufferedImage circleBuffer = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.setColor(Color.white);
                g2.fillOval(0, 0, width, width);

                g2.setClip(new Arc2D.Double(1.5, 1.5, width - 3, width - 3, 0, 360, Arc2D.OPEN));
                g2.drawImage(bufferedImage, 0, 0, width, width, null);

                g2.setClip(new Rectangle2D.Float(0, 0, width, width));
                g2.drawImage(borderFileBuf, 0, 0, width, width, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/" + actorhudDir);
                    if (!actorhudFloder.exists()) {
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() + "/" + gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .size(width, width)
                .outputQuality(1)
                .iterableBufferedImages()
                .forEach(buffIta);
    }
}
