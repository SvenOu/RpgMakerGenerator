package com.rmv.code.generator;

import com.rmv.code.generator.rmvImageTool.service.RmvAudioService;
import com.rmv.code.generator.rmvImageTool.service.RmvImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RmvImageToolTests {

    @Autowired
    private RmvImageService rmvImageService;

    @Test
    public void cropFaces(){
        try {
            rmvImageService.generatorFaceDir();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private RmvAudioService rmvAudioService;
    @Test
    public void generateAudio(){
        rmvAudioService.generatorAudio();
    }
}
