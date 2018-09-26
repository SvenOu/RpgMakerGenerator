package com.rmv.code.generator.rmvImageTool.service.impl;

import com.rmv.code.generator.rmvImageTool.service.RmvAudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;


@Component
public class RmvAudioServiceImpl implements RmvAudioService {
    private static Logger log = Logger.getLogger(RmvAudioServiceImpl.class.getSimpleName());

    //设置APPID/AK/SK
    public static final String APP_ID = "";

    @Autowired
    public RmvAudioServiceImpl() { }

    @Override
    public void generatorAudio() {

    }
}
