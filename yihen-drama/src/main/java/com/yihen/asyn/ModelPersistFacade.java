package com.yihen.asyn;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yihen.config.properties.MinioProperties;
import com.yihen.constant.MinioConstant;
import com.yihen.constant.episode.EpisodeRedisConstant;
import com.yihen.entity.*;
import com.yihen.enums.EpisodeStep;
import com.yihen.enums.TaskType;
import com.yihen.http.HttpExecutor;
import com.yihen.mapper.EpisodeMapper;
import com.yihen.mapper.StoryboardMapper;
import com.yihen.mapper.VideoTaskMapper;
import com.yihen.service.ModelInstanceDefaultService;
import com.yihen.service.StoryBoardCharacterService;
import com.yihen.service.StoryBoardSceneService;
import com.yihen.util.MinioUtil;
import com.yihen.util.RedisUtils;
import com.yihen.util.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModelPersistFacade {
    @Autowired
    private ModelInstanceDefaultService modelInstanceDefaultService;



    /**
     * 异步执行，但事务仍然有效（关键：方法在 Spring Bean 上）
     * 检查是否有默认模型，没有则创建
     */
    @Async("modelExecutor") // 你已有线程池的话配置成 Spring Async Executor
    @Transactional(rollbackFor = Exception.class)
    public void addDefaultModel(ModelInstance modelInstance) {
        boolean checkedExistUnderType = modelInstanceDefaultService.checkExistUnderType(modelInstance.getModelType());

        // 没有默认，则设置首个为默认
        if (!checkedExistUnderType) {
            ModelInstanceDefault modelInstanceDefault = new ModelInstanceDefault();
            modelInstanceDefault.setModelInstanceId(modelInstance.getId());
            modelInstanceDefault.setModelType(modelInstance.getModelType());
            modelInstanceDefaultService.addDefault(modelInstanceDefault);
        }
    }

}
