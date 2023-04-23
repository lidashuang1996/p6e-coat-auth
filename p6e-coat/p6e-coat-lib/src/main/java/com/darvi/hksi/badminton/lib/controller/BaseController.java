package com.darvi.hksi.badminton.lib.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础的 controller 类
 *
 * @author lidashuang
 * @version 1.0
 */
class BaseController {

    protected static final String AUTH_HEADER = "Authorization";
    protected static final String AUTH_HEADER_TOKEN_TYPE = "Bearer";
    protected static final String AUTH_HEADER_TOKEN_PREFIX = AUTH_HEADER_TOKEN_TYPE + " ";
    protected static final String ACCESS_TOKEN_PARAM1 = "accessToken";
    protected static final String ACCESS_TOKEN_PARAM2 = "access_token";
    protected static final String REFRESH_TOKEN_PARAM1 = "refreshToken";
    protected static final String REFRESH_TOKEN_PARAM2 = "refresh_token";

    protected static final String CONTENT_DISPOSITION_ATTACHMENT = "attachment";

    /**
     * 注入的日志对象
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
}
