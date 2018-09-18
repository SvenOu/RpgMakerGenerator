package com.rmv.code.generator.rmvImageTool.service.impl;

import com.rmv.code.generator.rmvImageTool.service.RmvImageEnemyService;
import com.rmv.code.generator.rmvImageTool.utils.PositionsFactory;
import com.rmv.code.generator.rmvImageTool.vo.RMVConfig;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
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
public class RmvImageEnemyServiceImpl implements RmvImageEnemyService {
    private static Logger log = Logger.getLogger(RmvImageEnemyServiceImpl.class.getSimpleName());

    @Value("${rmv.image.mainDir}")
    private String mainDir;

    @Value("${rmv.image.enemiesDir}")
    private String enemiesDir;

    @Value("${rmv.image.enemyAtbDir}")
    private String enemyAtbDir;

    @Value("${rmv.image.bossHpDir}")
    private String bossHpDir;

    @Value("${rmv.image.template.baseDir}")
    private String baseTemplateDir;


    @Value("${rmv.image.template.atbFaceBorderName}")
    private String atbFaceBorderName;

    @Value("${rmv.image.template.bossHpFaceBorderName}")
    private String bossHpFaceBorderName;

    @Autowired
    public RmvImageEnemyServiceImpl() { }

    @Override
    public void generatorEnemyFaceDir() throws IOException {
        File mainGenerateDir = new File(enemiesDir);

        generateEnemyAtb(mainGenerateDir);
        generateBossHpDir(mainGenerateDir);
    }

    private void generateBossHpDir(File mainGenerateDir) throws IOException {
        File bossHpTempFile = new File(mainDir + "/Generate/Temp/bossHpTemp");
        if(!bossHpTempFile.exists()){
            bossHpTempFile.mkdirs();
        }
        File borderFile = new File(baseTemplateDir + bossHpFaceBorderName);
        BufferedImage borderFileBuf = ImageIO.read(borderFile);

        int width = RMVConfig.FACE_SOURCE_img_bossHp_width;
        int height = RMVConfig.FACE_SOURCE_img_bossHp_height;

        File[] gFaceFiles = mainGenerateDir.listFiles();
        Consumer<BufferedImage> buffIta = new Consumer<BufferedImage>() {
            private int index = 0;

            @Override
            public void accept(BufferedImage bufferedImage) {

                BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                g2.setStroke(s);

                g2.setClip(new Arc2D.Double(0, 0, width, height, 0, 360, Arc2D.OPEN));
                g2.drawImage(borderFileBuf, 0, 0, width, height, null);

                g2.setClip(new Arc2D.Double(7, 7, width-15, height-15, 0, 360, Arc2D.OPEN));


                AffineTransform transform = new   AffineTransform(-1,0,0,1,bufferedImage.getWidth()-1,0);//水平翻转
//        AffineTransform transform = new   AffineTransform(1,0,0,-1,0,sourceImage.getHeight()-1);//垂直翻转
//         AffineTransform transform = new   AffineTransform(-1,0,0,-1,sourceImage.getWidth()-1,sourceImage.getHeight()-1);//旋转180度
                AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                bufferedImage = op.filter(bufferedImage, null);

                g2.drawImage(bufferedImage, 0, 0, width, height, null);


                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/"+ bossHpDir);
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
                .crop(PositionsFactory.getBossHpPosition())
                .size(width, height)
                .toFiles(bossHpTempFile, Rename.NO_CHANGE);;

        Thumbnails.of(bossHpTempFile.listFiles())
                .scale(1.0)
                .iterableBufferedImages()
                .forEach(buffIta);

    }

    private void generateEnemyAtb(File mainGenerateDir) throws IOException {
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

                g2.setClip(new Rectangle2D.Float(0,0,width, width));
                g2.drawImage(borderFileBuf, 0, 0, width, width, null);

                int[] xPoints = {0,width/2,width,width/2};
                int[] yPoints = {width/2,0,width/2,width};
                g2.setClip(new Polygon(xPoints, yPoints, 4));

                g2.setColor(new Color(255,255,255, 100));
                g2.fillOval(0, 0, width, width);

                g2.drawImage(bufferedImage, 0, 0, width, width, null);

                JPanel dPanel = new JPanel();
                dPanel.paintAll(g2);

                try {
                    File actorhudFloder = new File(mainDir + "/Generate/"+ enemyAtbDir);
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
                .crop(Positions.TOP_RIGHT)
                .size(width, width)
                .outputQuality(1)
                .iterableBufferedImages()
                .forEach(buffIta);
    }

}
