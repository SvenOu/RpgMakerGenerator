package com.rmv.code.generator.rmvImageTool.service.impl;

import com.rmv.code.generator.rmvImageTool.service.RmvImageMenuService;
import com.rmv.code.generator.rmvImageTool.vo.RMVConfig;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Logger;


@Component
public class RmvImageMenuServiceImpl implements RmvImageMenuService {
    private static Logger log = Logger.getLogger(RmvImageMenuServiceImpl.class.getSimpleName());

    @Value("${rmv.image.mainDir}")
    private String mainDir;

    @Value("${rmv.image.menuFace1}")
    private String menuFace1Dir;
    @Value("${rmv.image.menuFace2}")
    private String menuFace2Dir;
    @Value("${rmv.image.menuFace3}")
    private String menuFace3Dir;
    @Value("${rmv.image.menuFace4}")
    private String menuFace4Dir;

    @Value("${rmv.image.partyhudDir}")
    private String partyhudDir;

    @Value("${rmv.image.template.baseDir}")
    private String baseTemplateDir;

    @Value("${rmv.image.battleResultFaceDir}")
    private String battleResultFaceDir;

    @Value("${rmv.image.template.menu_face1_border}")
    private String menuFace1BorderName;
    @Value("${rmv.image.template.menu_face2_border}")
    private String menuFace2BorderName;
    @Value("${rmv.image.template.menu_face3_border}")
    private String menuFace3BorderName;
    @Value("${rmv.image.template.menu_face4_border}")
    private String menuFace4BorderName;

    @Value("${rmv.image.template.party_hud_border}")
    private String partyHudBorder;

    @Autowired
    public RmvImageMenuServiceImpl() { }

    @Override
    public void generatorMenuFaceDir() throws IOException {
        File mainGenerateDir = new File(mainDir + "/Generate/Temp/SpitFaces");
        generateBattleResultFace(mainGenerateDir);
//        generatePartyHud(mainGenerateDir);
        generateMenuFace1(mainGenerateDir);
        generateMenuFace2(mainGenerateDir);
        generateMenuFace3(mainGenerateDir);
        generateMenuFace4(mainGenerateDir);

    }
    private void generateBattleResultFace(File mainGenerateDir) throws IOException {
        File borderFile = new File(baseTemplateDir + menuFace4BorderName);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);

        int width = RMVConfig.FACE_SOURCE_img_battle_result_face_width;
        int height = RMVConfig.FACE_SOURCE_img_battle_result_face_height;

        int faceWidth = (int )Math.round((width)* 0.85);
        int faceHeight = (int )Math.round((width)* 0.85);;

        File[] gFaceFiles = mainGenerateDir.listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {

                BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.setClip(new Rectangle2D.Float(0,0,width, height));
                g2.drawImage(borderFileBuf, 0, (height-295)/2-70, (int )Math.round(290), (int )Math.round(295), null);

                g2.setClip(new Arc2D.Double(20, (height-faceHeight)/2-78, faceWidth, faceHeight, 0, 360, Arc2D.OPEN));
                g2.drawImage(bufferedImage, 20, (height-faceHeight)/2-78, faceWidth, faceHeight, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/"+ battleResultFaceDir);
                    if(!actorhudFloder.exists()){
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() +"/"+ gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .size(faceWidth, faceHeight)
                .keepAspectRatio(true)
                .iterableBufferedImages()
                .forEach(buffIta);
    }

    @Deprecated
    private void generatePartyHud(File mainGenerateDir) throws IOException {
        File borderFile = new File(baseTemplateDir + partyHudBorder);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);

        int width = RMVConfig.FACE_SOURCE_img_party_hud_face_width;
        int height = RMVConfig.FACE_SOURCE_img_party_hud_face_height;

        File[] gFaceFiles = mainGenerateDir.listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {

                BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.drawImage(borderFileBuf, 0, 0, width, height, null);

                g2.setClip(new Arc2D.Double(0, 0, width, height, 0, 360, Arc2D.OPEN));
                g2.drawImage(bufferedImage, 0, 0, width, height, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/"+ partyhudDir);
                    if(!actorhudFloder.exists()){
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() +"/"+ gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .size(width, height)
                .outputQuality(1.0)
                .iterableBufferedImages()
                .forEach(buffIta);
    }

    private void generateMenuFace4(File mainGenerateDir) throws IOException {
        File borderFile = new File(baseTemplateDir + menuFace4BorderName);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);

        int width = RMVConfig.FACE_SOURCE_img_menu_face4_width;
        int height = RMVConfig.FACE_SOURCE_img_menu_face4_height;

        int faceWidth = (int )Math.round((width)* 0.85);
        int faceHeight = (int )Math.round((width)* 0.85);;

        File[] gFaceFiles = mainGenerateDir.listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {

                BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.setClip(new Rectangle2D.Float(0,0,width, height));
                g2.drawImage(borderFileBuf, 0, (height-295)/2, (int )Math.round(290), (int )Math.round(295), null);

                g2.setClip(new Arc2D.Double(22, (height-faceHeight)/2-5, faceWidth, faceHeight, 0, 360, Arc2D.OPEN));

                AffineTransform transform = new   AffineTransform(-1,0,0,1,bufferedImage.getWidth()-1,0);//水平翻转
                AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                bufferedImage = op.filter(bufferedImage, null);

                g2.drawImage(bufferedImage, 22, (height-faceHeight)/2-5, faceWidth, faceHeight, null);


                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/"+ menuFace4Dir);
                    if(!actorhudFloder.exists()){
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() +"/"+ gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .size(faceWidth, faceHeight)
                .keepAspectRatio(true)
                .iterableBufferedImages()
                .forEach(buffIta);
    }
    private void generateMenuFace3(File mainGenerateDir) throws IOException {
        File borderFile = new File(baseTemplateDir + menuFace3BorderName);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);

        int width = RMVConfig.FACE_SOURCE_img_menu_face3_width;
        int height = RMVConfig.FACE_SOURCE_img_menu_face3_height;

        int faceWidth = (int )Math.round((width-100)* 0.75);
        int faceHeight = (int )Math.round((width-100)* 0.75);;

        File[] gFaceFiles = mainGenerateDir.listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {

                BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.drawImage(borderFileBuf, 55, 0, (int )Math.round(280 *0.6), (int )Math.round(272 *0.6), null);

                g2.setClip(new Arc2D.Double(70, 14, faceWidth, faceHeight, 0, 360, Arc2D.OPEN));
                g2.drawImage(bufferedImage, 70, 14, faceWidth, faceHeight, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/"+ menuFace3Dir);
                    if(!actorhudFloder.exists()){
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() +"/"+ gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .size(faceWidth, faceHeight)
                .outputQuality(1.0)
                .iterableBufferedImages()
                .forEach(buffIta);
    }

    private void generateMenuFace2(File mainGenerateDir) throws IOException {
        File borderFile = new File(baseTemplateDir + menuFace2BorderName);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);
        int width = RMVConfig.FACE_SOURCE_img_menu_face2_width;
        int height = RMVConfig.FACE_SOURCE_img_menu_face2_height;

        File[] gFaceFiles = mainGenerateDir.listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {

                BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.drawImage(borderFileBuf, 0, 0, width, height, null);

                g2.setClip(new Arc2D.Double(0, 0, width, height, 0, 360, Arc2D.OPEN));
                g2.drawImage(bufferedImage, 0, 0, width, height, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/"+ menuFace2Dir);
                    if(!actorhudFloder.exists()){
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() +"/"+ gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .size(width,height)
                .iterableBufferedImages()
                .forEach(buffIta);
    }

    private void generateMenuFace1(File mainGenerateDir) throws IOException {
        File borderFile = new File(baseTemplateDir + menuFace1BorderName);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);

        int faceWidth = RMVConfig.FACE_SOURCE_IMAGE_WIDTH / RMVConfig.FACE_SOURCE_IMAGE_X;
        int faceHeight = RMVConfig.FACE_SOURCE_IMAGE_HEIGHT / RMVConfig.FACE_SOURCE_IMAGE_Y;

        int width = RMVConfig.FACE_SOURCE_img_menu_face1_width;
        int height = RMVConfig.FACE_SOURCE_img_menu_face1_height;

        File[] gFaceFiles = mainGenerateDir.listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {

                BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.drawImage(borderFileBuf, 0, 0, width, height, null);

                g2.setClip(new Rectangle2D.Float(0,0,width - 8, height - 10));
                g2.drawImage(bufferedImage, -15, -15, faceWidth, faceHeight, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/"+ menuFace1Dir);
                    if(!actorhudFloder.exists()){
                        actorhudFloder.mkdirs();
                    }
                    ImageIO.write(circleBuffer, "png", new File(actorhudFloder.getAbsolutePath() +"/"+ gFaceFiles[index].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                index++;
            }
        };

        Thumbnails.of(gFaceFiles)
                .scale(1.0)
                .iterableBufferedImages()
                .forEach(buffIta);
    }
}
