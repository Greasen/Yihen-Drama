package com.yihen.core.model.strategy.embedding.impl;

import com.alibaba.fastjson.JSONObject;
import com.yihen.config.properties.MinioProperties;
import com.yihen.constant.MinioConstant;
import com.yihen.controller.vo.EmbeddingModelRequestVO;
import com.yihen.controller.vo.ImageModelRequestVO;
import com.yihen.core.model.strategy.embedding.EmbeddingModelStrategy;
import com.yihen.core.model.strategy.image.ImageModelStrategy;
import com.yihen.entity.*;
import com.yihen.http.HttpExecutor;
import com.yihen.mapper.ModelDefinitionMapper;
import com.yihen.service.ModelManageService;
import com.yihen.util.MinioUtil;
import com.yihen.util.UrlUtils;
import io.minio.GetObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class VolcanoEmbeddingModelStrategy implements EmbeddingModelStrategy {
    private static final String STRATEGY_TYPE = "volcano";
    private static final String MODEL_BASE_URL = "https://ark.cn-beijing.volces.com/api/v3";

    @Autowired
    private ModelManageService modelManageService;

    @Autowired
    private ModelDefinitionMapper modelDefinitionMapper;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private HttpExecutor httpExecutor;


    // 响应结果提取
    private String extractResponse(String response) throws Exception {

        // 1. 转成JSONObject对象
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject.containsKey("error")) {
            // 调用失败
            String errorMessage = jsonObject.getJSONObject("error").getString("message");
            throw new Exception(errorMessage);
        }

        String content = jsonObject.getJSONArray("data")
                .getJSONObject(0).getString("url");

        if (org.springframework.util.ObjectUtils.isEmpty(content)) {
            throw new Exception("返回结果结构正确，但是返回数据为空！再次尝试");
        }

        return content;

    }

    @Override
    public String create(EmbeddingModelRequestVO embeddingModelRequestVO) throws Exception {
        return "";
    }

    @Override
    public String getStrategyType() {
        return STRATEGY_TYPE;
    }

    @Override
    public boolean supports(ModelInstance modelInstance) {
        // 可以根据 modelDefId 或其他属性判断是否支持
        ModelDefinition modelDefinition = modelManageService.getById(modelInstance.getModelDefId());
        // 判断该模型实例对应的厂商BaseURL是否属于火山引擎
        if (MODEL_BASE_URL.equals(modelDefinition.getBaseUrl())) {
            return true;
        }
        return false;
    }
}
