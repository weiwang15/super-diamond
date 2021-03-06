/**
 * Copyright (c) 2013 by 苏州科大国创信息技术有限公司.
 */

package com.github.diamond.web.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.diamond.utils.PlaceHolderUtil;
import com.github.diamond.utils.ProjectIdUtil;
import com.github.diamond.web.dao.ConfigDao;
import com.github.diamond.web.dao.ProjectDao;
import com.github.diamond.web.model.Config;
import com.github.diamond.web.service.ConfigService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Create on @2013-8-23 @上午10:26:17.
 *
 * @author bsli@ustcinfo.com
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ConfigDao configDao;


    public List<Map<String, Object>> queryConfigs(int projectId, String type, int moduleId, int offset, int limit, boolean isShow) {
        List<Map<String, Object>> pageConfigList;
        Map<String, String> commonStore = new HashMap<>();
        Map<String, String> store;
        if ("development".equals(type)) {
            List<Map<String, Object>> commonProjectId = projectDao.queryMultiCommonProjectId();
            if (commonProjectId != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
                commonStore = getCommonConfigMap(commonProjectId, type);
            }
            store = replaceByCommonConfigs(projectId, type, commonStore);
            pageConfigList = configDao.queryDevelopmentConfigs(projectId, type, moduleId, offset, limit, isShow);
            for (int i = 0; i < pageConfigList.size(); i++) {
                int index = String.valueOf(pageConfigList.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(pageConfigList.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    pageConfigList.get(i).remove("CONFIG_DESC");
                    pageConfigList.get(i).put("CONFIG_DESC", desc);
                }
                String str = PlaceHolderUtil.findPlaceHolderVar(store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                    pageConfigList.get(i).put("REAL_CONFIG_VALUE", realValue);
                } else {
                    pageConfigList.get(i).put("REAL_CONFIG_VALUE", store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                }
            }
        } else if ("test".equals(type)) {
            List<Map<String, Object>> commonProjectId = projectDao.queryMultiCommonProjectId();
            if (commonProjectId != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
                commonStore = getCommonConfigMap(commonProjectId, type);
            }
            store = replaceByCommonConfigs(projectId, type, commonStore);
            pageConfigList = configDao.queryTestConfigs(projectId, type, moduleId, offset, limit, isShow);
            for (int i = 0; i < pageConfigList.size(); i++) {
                int index = String.valueOf(pageConfigList.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(pageConfigList.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    pageConfigList.get(i).remove("CONFIG_DESC");
                    pageConfigList.get(i).put("CONFIG_DESC", desc);
                }
                String str = PlaceHolderUtil.findPlaceHolderVar(store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                    pageConfigList.get(i).put("REAL_TEST_VALUE", realValue);
                } else {
                    pageConfigList.get(i).put("REAL_TEST_VALUE", store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                }
            }
        } else if ("production".equals(type)) {
            List<Map<String, Object>> commonProjectId = projectDao.queryMultiCommonProjectId();
            if (commonProjectId != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
                commonStore = getCommonConfigMap(commonProjectId, type);
            }
            store = replaceByCommonConfigs(projectId, type, commonStore);
            pageConfigList = configDao.queryProductionConfigs(projectId, type, moduleId, offset, limit, isShow);
            for (int i = 0; i < pageConfigList.size(); i++) {
                int index = String.valueOf(pageConfigList.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(pageConfigList.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    pageConfigList.get(i).remove("CONFIG_DESC");
                    pageConfigList.get(i).put("CONFIG_DESC", desc);
                }
                String str = PlaceHolderUtil.findPlaceHolderVar(store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                    pageConfigList.get(i).put("REAL_PRODUCTION_VALUE", realValue);
                } else {
                    pageConfigList.get(i).put("REAL_PRODUCTION_VALUE", store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                }
            }
        } else {
            List<Map<String, Object>> commonProjectId = projectDao.queryMultiCommonProjectId();
            if (commonProjectId != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
                commonStore = getCommonConfigMap(commonProjectId, type);
            }
            store = replaceByCommonConfigs(projectId, type, commonStore);
            pageConfigList = configDao.queryBuildConfigs(projectId, type, moduleId, offset, limit, isShow);
            for (int i = 0; i < pageConfigList.size(); i++) {
                int index = String.valueOf(pageConfigList.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(pageConfigList.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    pageConfigList.get(i).remove("CONFIG_DESC");
                    pageConfigList.get(i).put("CONFIG_DESC", desc);
                }
                String str = PlaceHolderUtil.findPlaceHolderVar(store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                    pageConfigList.get(i).put("REAL_BUILD_VALUE", realValue);
                } else {
                    pageConfigList.get(i).put("REAL_BUILD_VALUE", store.get(pageConfigList.get(i).get("CONFIG_KEY")));
                }
            }
        }
        return pageConfigList;
    }

    public int queryConfigCount(int projectId, int moduleId, boolean isShow) {
        return configDao.queryConfigCount(projectId, moduleId, isShow);
    }

    public String queryConfigs(String projectCode, String type, String format) {
        Map<String, String> commonStore = new HashMap<>();
        Map<String, String> store = new HashMap<>();
        List<Map<String, Object>> commonProjectId = projectDao.queryMultiCommonProjectId();
        int projectId = projectDao.getProjectIdByProjectCode(projectCode);
        if (commonProjectId != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
            commonStore = getCommonConfigMap(commonProjectId, type);
        }

        if (projectId != -1) {
            store = replaceByCommonConfigs(projectId, type, commonStore);
        } else {
            //todo 抛异常
        }
        List<Map<String, Object>> configs = configDao.queryConfigs(projectCode, type);
        if ("development".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("CONFIG_VALUE");
                configs.get(i).put("CONFIG_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("CONFIG_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str, String.valueOf(configs.get(i).get("CONFIG_VALUE")));
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("CONFIG_VALUE");
                    configs.get(i).put("CONFIG_VALUE", realValue);
                    configs.get(i).put("REAL_CONFIG_VALUE", realValue);
                } else {
                    configs.get(i).put("REAL_CONFIG_VALUE", "");
                }
            }
        } else if ("production".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("PRODUCTION_VALUE");
                configs.get(i).put("PRODUCTION_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("PRODUCTION_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str, String.valueOf(configs.get(i).get("PRODUCTION_VALUE")));
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("PRODUCTION_VALUE");
                    configs.get(i).put("PRODUCTION_VALUE", realValue);
                    configs.get(i).put("REAL_PRODUCTION_VALUE", realValue);
                } else {
                    configs.get(i).put("REAL_PRODUCTION_VALUE", "");
                }
            }
        } else if ("test".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("TEST_VALUE");
                configs.get(i).put("TEST_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("TEST_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str, String.valueOf(configs.get(i).get("TEST_VALUE")));
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("TEST_VALUE");
                    configs.get(i).put("TEST_VALUE", realValue);
                    configs.get(i).put("REAL_TEST_VALUE", realValue);
                } else {
                    configs.get(i).put("REAL_TEST_VALUE", "");
                }
            }
        } else if ("build".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("BUILD_VALUE");
                configs.get(i).put("BUILD_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("BUILD_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str, String.valueOf(configs.get(i).get("BUILD_VALUE")));
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("BUILD_VALUE");
                    configs.get(i).put("BUILD_VALUE", realValue);
                    configs.get(i).put("REAL_BUILD_VALUE", realValue);
                } else {
                    configs.get(i).put("REAL_BUILD_VALUE", "");
                }
            }
        }
        if ("php".equals(format)) {
            return viewConfigPhp(configs, type);
        } else if ("json".equals(format)) {
            return viewConfigJson(configs, type);
        } else {
            return viewConfig(configs, type);
        }
    }

    public String queryConfigs(String projectCode, String type, String[] encryptPropNameArr, String format) {
        Map<String, String> commonStore = new HashMap<>();
        Map<String, String> store = new HashMap<>();
        /*int commonProjectId = projectDao.queryCommonProjectId();
        if (commonProjectId != -1) {
            commonStore = getCommonConfigMap(commonProjectId, type, encryptPropNameArr);
        }*/
        List<Map<String, Object>> commonProjectId = projectDao.queryMultiCommonProjectId();
        int projectId = projectDao.getProjectIdByProjectCode(projectCode);
        if (commonProjectId != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
            commonStore = getCommonConfigMap(commonProjectId, type, encryptPropNameArr);
        }

        if (projectId != -1) {
            store = replaceByCommonConfigs(projectId, type, commonStore, encryptPropNameArr);
        } else {
            //todo 抛异常
        }
        List<Map<String, Object>> configs = configDao.queryConfigs(projectCode, type);
        if ("development".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("CONFIG_VALUE");
                configs.get(i).put("CONFIG_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("CONFIG_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str,
//                            String.valueOf(configs.get(i).get("CONFIG_VALUE")), encryptPropNameArr);
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("CONFIG_VALUE");
                    configs.get(i).put("CONFIG_VALUE", realValue);
                    configs.get(i).put("REAL_CONFIG_VALUE", realValue);
                } else {
                    /*if (ArrayUtils.contains(encryptPropNameArr, configs.get(i).get("CONFIG_KEY"))) {
                        String tmpValue = String.valueOf(configs.get(i).get("CONFIG_VALUE"));
                        configs.get(i).remove("CONFIG_VALUE");
                        configs.get(i).put("CONFIG_VALUE", "$[" + tmpValue + "]");
                    }*/
                    configs.get(i).put("REAL_CONFIG_VALUE", "");
                }
            }
        } else if ("production".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("PRODUCTION_VALUE");
                configs.get(i).put("PRODUCTION_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("PRODUCTION_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str,
//                            String.valueOf(configs.get(i).get("PRODUCTION_VALUE")), encryptPropNameArr);
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("PRODUCTION_VALUE");
                    configs.get(i).put("PRODUCTION_VALUE", realValue);
                    configs.get(i).put("REAL_PRODUCTION_VALUE", realValue);
                } else {
                   /* if (ArrayUtils.contains(encryptPropNameArr, configs.get(i).get("CONFIG_KEY"))) {
                        String tmpValue = String.valueOf(configs.get(i).get("PRODUCTION_VALUE"));
                        configs.get(i).remove("PRODUCTION_VALUE");
                        configs.get(i).put("PRODUCTION_VALUE", "$[" + tmpValue + "]");
                    }*/
                    configs.get(i).put("REAL_PRODUCTION_VALUE", "");
                }
            }
        } else if ("test".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("TEST_VALUE");
                configs.get(i).put("TEST_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("TEST_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str,
//                            String.valueOf(configs.get(i).get("TEST_VALUE")), encryptPropNameArr);
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("TEST_VALUE");
                    configs.get(i).put("TEST_VALUE", realValue);
                    configs.get(i).put("REAL_TEST_VALUE", realValue);
                } else {
                    /*if (ArrayUtils.contains(encryptPropNameArr, configs.get(i).get("CONFIG_KEY"))) {
                        String tmpValue = String.valueOf(configs.get(i).get("TEST_VALUE"));
                        configs.get(i).remove("TEST_VALUE");
                        configs.get(i).put("TEST_VALUE", "$[" + tmpValue + "]");
                    }*/
                    configs.get(i).put("REAL_TEST_VALUE", "");
                }
            }
        } else if ("build".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("BUILD_VALUE");
                configs.get(i).put("BUILD_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("BUILD_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str,
//                            String.valueOf(configs.get(i).get("BUILD_VALUE")), encryptPropNameArr);
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("BUILD_VALUE");
                    configs.get(i).put("BUILD_VALUE", realValue);
                    configs.get(i).put("REAL_BUILD_VALUE", realValue);
                } else {
                    /*if (ArrayUtils.contains(encryptPropNameArr, configs.get(i).get("CONFIG_KEY"))) {
                        String tmpValue = String.valueOf(configs.get(i).get("BUILD_VALUE"));
                        configs.get(i).remove("BUILD_VALUE");
                        configs.get(i).put("BUILD_VALUE", "$[" + tmpValue + "]");
                    }*/
                    configs.get(i).put("REAL_BUILD_VALUE", "");
                }
            }
        }
        if ("php".equals(format)) {
            return viewConfigPhp(configs, type);
        } else if ("json".equals(format)) {
            return viewConfigJson(configs, type);
        } else {
            return viewConfig(configs, type);
        }
    }

    public String queryConfigs(String projectCode, String[] modules, String type, String format) {
        Map<String, String> commonStore = new HashMap<>();
        Map<String, String> store = new HashMap<>();
        List<Map<String, Object>> commonProjectId = projectDao.queryMultiCommonProjectId();
        int projectId = projectDao.getProjectIdByProjectCode(projectCode);
        if (commonProjectId != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
            commonStore = getCommonConfigMap(commonProjectId, type);
        }

        if (projectId != -1) {
            store = replaceByCommonConfigs(projectId, type, commonStore);
        } else {
            //todo 抛异常
        }

        List<Map<String, Object>> configs = configDao.queryConfigs(projectCode, type, modules);
        if ("development".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("CONFIG_VALUE");
                configs.get(i).put("CONFIG_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("CONFIG_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str, String.valueOf(configs.get(i).get("CONFIG_VALUE")));
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("CONFIG_VALUE");
                    configs.get(i).put("CONFIG_VALUE", realValue);
                    configs.get(i).put("REAL_CONFIG_VALUE", realValue);
                } else {
                    configs.get(i).put("REAL_CONFIG_VALUE", "");
                }
            }
        } else if ("production".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("PRODUCTION_VALUE");
                configs.get(i).put("PRODUCTION_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("PRODUCTION_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str, String.valueOf(configs.get(i).get("PRODUCTION_VALUE")));
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("RODUCTION_VALUE");
                    configs.get(i).put("RODUCTION_VALUE", realValue);
                    configs.get(i).put("REAL_PRODUCTION_VALUE", realValue);
                } else {
                    configs.get(i).put("REAL_PRODUCTION_VALUE", "");
                }
            }
        } else if ("test".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("TEST_VALUE");
                configs.get(i).put("TEST_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("TEST_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str, String.valueOf(configs.get(i).get("TEST_VALUE")));
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("TEST_VALUE");
                    configs.get(i).put("TEST_VALUE", realValue);
                    configs.get(i).put("REAL_TEST_VALUE", realValue);
                } else {
                    configs.get(i).put("REAL_TEST_VALUE", "");
                }
            }
        } else if ("build".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("BUILD_VALUE");
                configs.get(i).put("BUILD_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("BUILD_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str, String.valueOf(configs.get(i).get("BUILD_VALUE")));
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("BUILD_VALUE");
                    configs.get(i).put("BUILD_VALUE", realValue);
                    configs.get(i).put("REAL_BUILD_VALUE", realValue);
                } else {
                    configs.get(i).put("REAL_BUILD_VALUE", "");
                }
            }
        }

        if ("php".equals(format)) {
            return viewConfigPhp(configs, type);
        } else if ("json".equals(format)) {
            return viewConfigJson(configs, type);
        } else {
            return viewConfig(configs, type);
        }
    }

    public String queryConfigs(String projectCode, String[] modules, String[] encryptPropNameArr, String type, String format) {
        Map<String, String> commonStore = new HashMap<>();
        Map<String, String> store = new HashMap<>();

        List<Map<String, String>> variableList = new ArrayList<>();

        /*int commonProjectId = projectDao.queryCommonProjectId();
        if (commonProjectId != -1) {
            commonStore = getCommonConfigMap(commonProjectId, type, encryptPropNameArr);
        }*/
        List<Map<String, Object>> commonProjectId = projectDao.queryMultiCommonProjectId();
        int projectId = projectDao.getProjectIdByProjectCode(projectCode);
        if (commonProjectId != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
            commonStore = getCommonConfigMap(commonProjectId, type, encryptPropNameArr);
        }

        if (projectId != -1) {
            store = replaceByCommonConfigs(projectId, type, commonStore, encryptPropNameArr);
        } else {
            //todo 抛异常
        }

        List<Map<String, Object>> configs = configDao.queryConfigs(projectCode, type, modules);
        if ("development".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("CONFIG_VALUE");
                configs.get(i).put("CONFIG_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("CONFIG_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
                    Map<String, String> tempMap = new HashMap<>();

//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str,
//                            String.valueOf(configs.get(i).get("CONFIG_VALUE")), encryptPropNameArr);
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("CONFIG_VALUE");
                    configs.get(i).put("CONFIG_VALUE", realValue);
                    configs.get(i).put("REAL_CONFIG_VALUE", realValue);
                } else {
                   /* if (ArrayUtils.contains(encryptPropNameArr, configs.get(i).get("CONFIG_KEY"))) {
                        String tmpValue = String.valueOf(configs.get(i).get("CONFIG_VALUE"));
                        configs.get(i).remove("CONFIG_VALUE");
                        configs.get(i).put("CONFIG_VALUE", "$[" + tmpValue + "]");
                    }*/
                    configs.get(i).put("REAL_CONFIG_VALUE", "");
                }
            }
        } else if ("production".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("PRODUCTION_VALUE");
                configs.get(i).put("PRODUCTION_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("PRODUCTION_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str,
//                            String.valueOf(configs.get(i).get("PRODUCTION_VALUE")), encryptPropNameArr);
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("RODUCTION_VALUE");
                    configs.get(i).put("RODUCTION_VALUE", realValue);
                    configs.get(i).put("REAL_PRODUCTION_VALUE", realValue);
                } else {
                   /* if (ArrayUtils.contains(encryptPropNameArr, configs.get(i).get("CONFIG_KEY"))) {
                        String tmpValue = String.valueOf(configs.get(i).get("RODUCTION_VALUE"));
                        configs.get(i).remove("RODUCTION_VALUE");
                        configs.get(i).put("RODUCTION_VALUE", "$[" + tmpValue + "]");
                    }*/
                    configs.get(i).put("REAL_PRODUCTION_VALUE", "");
                }
            }
        } else if ("test".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("TEST_VALUE");
                configs.get(i).put("TEST_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("TEST_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str,
//                            String.valueOf(configs.get(i).get("TEST_VALUE")), encryptPropNameArr);
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("TEST_VALUE");
                    configs.get(i).put("TEST_VALUE", realValue);
                    configs.get(i).put("REAL_TEST_VALUE", realValue);
                } else {
                    /*if (ArrayUtils.contains(encryptPropNameArr, configs.get(i).get("CONFIG_KEY"))) {
                        String tmpValue = String.valueOf(configs.get(i).get("TEST_VALUE"));
                        configs.get(i).remove("TEST_VALUE");
                        configs.get(i).put("TEST_VALUE", "$[" + tmpValue + "]");
                    }*/
                    configs.get(i).put("REAL_TEST_VALUE", "");
                }
            }
        } else if ("build".equals(type)) {
            for (int i = 0; i < configs.size(); i++) {
                int index = String.valueOf(configs.get(i).get("CONFIG_DESC")).indexOf("\r\n");
                if (index != -1) {
                    String desc = StringUtils.replace(String.valueOf(configs.get(i).get("CONFIG_DESC")), "\r\n", "<br/>");
                    configs.get(i).remove("CONFIG_DESC");
                    configs.get(i).put("CONFIG_DESC", desc);
                }
                configs.get(i).remove("BUILD_VALUE");
                configs.get(i).put("BUILD_VALUE", store.get(configs.get(i).get("CONFIG_KEY")));
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(configs.get(i).get("BUILD_VALUE")));
                if (str != null && !ProjectIdUtil.isIdExistsInCommonId(projectId, commonProjectId)) {
//                    String realValue = PlaceHolderUtil.replaceVarWithValue(store, str,
//                            String.valueOf(configs.get(i).get("BUILD_VALUE")), encryptPropNameArr);
                    StrSubstitutor strSubstitutor = new StrSubstitutor(store);
                    String realValue = strSubstitutor.replace(store.get(String.valueOf(configs.get(i).get("CONFIG_KEY"))));
                    configs.get(i).remove("BUILD_VALUE");
                    configs.get(i).put("BUILD_VALUE", realValue);
                    configs.get(i).put("REAL_BUILD_VALUE", realValue);
                } else {
                    /*if (ArrayUtils.contains(encryptPropNameArr, configs.get(i).get("CONFIG_KEY"))) {
                        String tmpValue = String.valueOf(configs.get(i).get("BUILD_VALUE"));
                        configs.get(i).remove("BUILD_VALUE");
                        configs.get(i).put("BUILD_VALUE", "$[" + tmpValue + "]");
                    }*/
                    configs.get(i).put("REAL_BUILD_VALUE", "");
                }
            }
        }

        if ("php".equals(format)) {
            return viewConfigPhp(configs, type);
        } else if ("json".equals(format)) {
            return viewConfigJson(configs, type);
        } else {
            return viewConfig(configs, type);
        }
    }

    public String queryValue(String projectCode, String module, String key, String type) {
        Map<String, Object> config = configDao.queryValue(projectCode, module, key);
        if ("development".equals(type)) {
            return (String) config.get("CONFIG_VALUE");
        } else if ("production".equals(type)) {
            return (String) config.get("PRODUCTION_VALUE");
        } else if ("test".equals(type)) {
            return (String) config.get("TEST_VALUE");
        } else if ("build".equals(type)) {
            return (String) config.get("BUILD_VALUE");
        } else {
            return "";
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertConfig(String configKey, String configValue, String configDesc, boolean isShow, int projectId, int moduleId, String user) {
        configDao.insertConfig(configKey, configValue, configDesc, isShow, projectId, moduleId, user);
        projectDao.updateVersion(projectId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateConfig(String type, int configId, String configKey,
                             String configValue, String configDesc, boolean isShow,
                             int projectId, int moduleId, String user) {
        configDao.updateConfig(type, configId, configKey, configValue, configDesc, isShow, projectId, moduleId, user);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteConfig(int id, int projectId) {
        configDao.deleteConfig(id);
        projectDao.updateVersion(projectId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void moveConfig(int id, int newModuleId, int projectId) {
        configDao.moveConfig(id, newModuleId);
        projectDao.updateVersion(projectId);
    }

    private String viewConfig(List<Map<String, Object>> configs, String type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");

        boolean versionFlag = true;
        for (Map<String, Object> map : configs) {
            if (versionFlag) {
                if ("development".equals(type)) {
                    stringBuilder.append("#version = " + map.get("DEVELOPMENT_VERSION") + "\r\n");
                } else if ("production".equals(type)) {
                    stringBuilder.append("#version = " + map.get("PRODUCTION_VERSION") + "\r\n");
                } else if ("test".equals(type)) {
                    stringBuilder.append("#version = " + map.get("TEST_VERSION") + "\r\n");
                } else if ("build".equals(type)) {
                    stringBuilder.append("#version = " + map.get("BUILD_VERSION") + "\r\n");
                }
                versionFlag = false;
            }

            String desc = (String) map.get("CONFIG_DESC");
            desc = desc.replaceAll("\r\n", " ");
            if (StringUtils.isNotBlank(desc)) {
                stringBuilder.append("#" + desc + "\r\n");
            }

            if ("development".equals(type)) {
                stringBuilder.append(map.get("CONFIG_KEY") + " = " + map.get("CONFIG_VALUE") + "\r\n");
            } else if ("production".equals(type)) {
                stringBuilder.append(map.get("CONFIG_KEY") + " = " + map.get("PRODUCTION_VALUE") + "\r\n");
            } else if ("test".equals(type)) {
                stringBuilder.append(map.get("CONFIG_KEY") + " = " + map.get("TEST_VALUE") + "\r\n");
            } else if ("build".equals(type)) {
                stringBuilder.append(map.get("CONFIG_KEY") + " = " + map.get("BUILD_VALUE") + "\r\n");
            }
        }

        return stringBuilder.toString();
    }

    private String viewConfigPhp(List<Map<String, Object>> configs, String type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?php\r\n"
                + "return array(\r\n"
                + "\t//profile = " + type + "\r\n");

        boolean versionFlag = true;
        for (Map<String, Object> map : configs) {
            if (versionFlag) {
                if ("development".equals(type)) {
                    stringBuilder.append("\t//version = " + map.get("DEVELOPMENT_VERSION") + "\r\n");
                } else if ("production".equals(type)) {
                    stringBuilder.append("\t//version = " + map.get("PRODUCTION_VERSION") + "\r\n");
                } else if ("test".equals(type)) {
                    stringBuilder.append("\t//version = " + map.get("TEST_VERSION") + "\r\n");
                } else if ("build".equals(type)) {
                    stringBuilder.append("\t//version = " + map.get("BUILD_VALUE") + "\r\n");
                }

                versionFlag = false;
            }

            String desc = (String) map.get("CONFIG_DESC");
            if (StringUtils.isNotBlank(desc)) {
                stringBuilder.append("\t//" + desc + "\r\n");
            }
            if ("development".equals(type)) {
                stringBuilder.append("\t'" + map.get("CONFIG_KEY") + "' => " + convertType(map.get("CONFIG_VALUE")));
            } else if ("production".equals(type)) {
                stringBuilder.append("\t'" + map.get("CONFIG_KEY") + "' => " + convertType(map.get("PRODUCTION_VALUE")));
            } else if ("test".equals(type)) {
                stringBuilder.append("\t'" + map.get("CONFIG_KEY") + "' => " + convertType(map.get("TEST_VALUE")));
            } else if ("build".equals(type)) {
                stringBuilder.append("\t'" + map.get("CONFIG_KEY") + "' => " + convertType(map.get("BUILD_VALUE")));
            }
        }
        stringBuilder.append(");\r\n");
        return stringBuilder.toString();
    }

    private String convertType(Object value) {
        String conf = String.valueOf(value).trim();
        if ("true".equals(conf) || "false".equals(conf)) {
            return conf + ",\r\n";
        } else if (isNumeric(conf)) {
            return conf + ",\r\n";
        } else {
            return "'" + conf + "',\r\n";
        }
    }

    private String viewConfigJson(List<Map<String, Object>> configs, String type) {
        Map<String, Object> confMap = new LinkedHashMap<String, Object>();
        boolean versionFlag = true;
        for (Map<String, Object> map : configs) {
            if (versionFlag) {
                if ("development".equals(type)) {
                    confMap.put("version", map.get("DEVELOPMENT_VERSION"));
                } else if ("production".equals(type)) {
                    confMap.put("version", map.get("PRODUCTION_VERSION"));
                } else if ("test".equals(type)) {
                    confMap.put("version", map.get("TEST_VERSION"));
                } else if ("build".equals(type)) {
                    confMap.put("version", map.get("BUILD_VALUE"));
                }

                versionFlag = false;
            }

            if ("development".equals(type)) {
                confMap.put(map.get("CONFIG_KEY").toString(), map.get("CONFIG_VALUE"));
            } else if ("production".equals(type)) {
                confMap.put(map.get("CONFIG_KEY").toString(), map.get("PRODUCTION_VALUE"));
            } else if ("test".equals(type)) {
                confMap.put(map.get("CONFIG_KEY").toString(), map.get("TEST_VALUE"));
            } else if ("build".equals(type)) {
                confMap.put(map.get("CONFIG_KEY").toString(), map.get("BUILD_VALUE"));
            }
        }

        return JSONUtils.toJSONString(confMap);
    }

    public static final boolean isNumeric(String str) {
        if (str != null && !"".equals(str.trim())) {
            return str.matches("^[0-9]*$");
        } else {
            return false;
        }
    }

    @Transactional
    public Config getExportConfig(int projectId, int moduleId, int configId, String type) {
        String configKey = null;
        String configValue = null;
        String configDesc = null;

        String valueField;

        if ("development".equals(type)) {
            valueField = "CONFIG_VALUE";
        } else if ("production".equals(type)) {
            valueField = "PRODUCTION_VALUE";
        } else if ("test".equals(type)) {
            valueField = "TEST_VALUE";
        } else if ("build".equals(type)) {
            valueField = "BUILD_VALUE";
        } else {
            valueField = "CONFIG_VALUE";
        }
        List<Map<String, Object>> configs = configDao.getExportConfig(projectId, moduleId, configId, type, valueField);
        for (Map<String, Object> config : configs) {
            configKey = config.get("CONFIG_KEY").toString();
            configValue = config.get(valueField).toString();
            configDesc = config.get("CONFIG_DESC").toString();
        }

        return new Config(configKey, configValue, configDesc);
    }

    /*public Map<String, String> getCommonConfigMap(int projectId, String type) {
         Map<String, String> store = new HashMap<>();
         List<Map<String, Object>> allConfigList = configDao.queryConfigs(projectId, type);

         Map<String, String> storeTmp = new HashMap<>();
         for (int j = 0; j < allConfigList.size(); j++) {
             if ("development".equals(type)) {
                 storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")),
                         String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
             } else if ("production".equals(type)) {
                 storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")),
                         String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
             } else if ("test".equals(type)) {
                 storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")),
                         String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
             } else if ("build".equals(type)) {
                 storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")),
                         String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
             }
         }

         for (int j = 0; j < allConfigList.size(); j++) {
             if ("development".equals(type)) {
                 String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                 if (str != null && storeTmp.size() != 0) {
                     StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                     String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                     store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                 } else {
                     store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                 }
             } else if ("production".equals(type)) {
                 String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                 if (str != null && storeTmp.size() != 0) {
                     StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                     String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                     store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                 } else {
                     store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                 }
             } else if ("test".equals(type)) {
                 String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                 if (str != null && storeTmp.size() != 0) {
                     StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                     String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                     store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                 } else {
                     store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                 }
             } else if ("build".equals(type)) {
                 String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                 if (str != null && storeTmp.size() != 0) {
                     StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                     String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                     store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                 } else {
                     store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                 }
             }
         }
         return store;
     }*/
    public Map<String, String> getCommonConfigMap(List<Map<String, Object>> multiProjectId, String type) {
        Map<String, String> store = new HashMap<>();
        for (int id = 0; id < multiProjectId.size(); id++) {
            int projectId = Integer.valueOf(String.valueOf(multiProjectId.get(id).get("ID")));
            List<Map<String, Object>> allConfigList = configDao.queryConfigs(projectId, type);
            String projCode = projectDao.getProjectCodeByProjectId(projectId);
            Map<String, String> storeTmp = new HashMap<>();
            for (int j = 0; j < allConfigList.size(); j++) {
                if ("development".equals(type)) {
                    storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")),
                            String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                } else if ("production".equals(type)) {
                    storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")),
                            String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                } else if ("test".equals(type)) {
                    storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")),
                            String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                } else if ("build".equals(type)) {
                    storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")),
                            String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                }
            }

            for (int j = 0; j < allConfigList.size(); j++) {
                if ("development".equals(type)) {
                    String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                    if (str != null && storeTmp.size() != 0) {
                        StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                        String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                    } else {
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                    }
                } else if ("production".equals(type)) {
                    String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                    if (str != null && storeTmp.size() != 0) {
                        StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                        String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                    } else {
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                    }
                } else if ("test".equals(type)) {
                    String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                    if (str != null && storeTmp.size() != 0) {
                        StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                        String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                    } else {
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                    }
                } else if ("build".equals(type)) {
                    String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                    if (str != null && storeTmp.size() != 0) {
                        StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                        String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                    } else {
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                    }
                }
            }
        }


        return store;
    }

    /*public Map<String, String> getCommonConfigMap(int projectId, String type, String[] encryptPropNameArr) {
        Map<String, String> store = new HashMap<>();
        List<Map<String, Object>> allConfigList = configDao.queryConfigs(projectId, type);

        Map<String, String> storeTmp = new HashMap<>();
        for (int j = 0; j < allConfigList.size(); j++) {
            if ("development".equals(type)) {
                String value;
                if (ArrayUtils.contains(encryptPropNameArr, "common:" + allConfigList.get(j).get("CONFIG_KEY"))) {
                    value = "$[" + String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")) + "]";
                } else {
                    value = String.valueOf(allConfigList.get(j).get("CONFIG_VALUE"));
                }
                storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), value);
            } else if ("production".equals(type)) {
                String value;
                if (ArrayUtils.contains(encryptPropNameArr, "common:" + allConfigList.get(j).get("CONFIG_KEY"))) {
                    value = "$[" + String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")) + "]";
                } else {
                    value = String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE"));
                }
                storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), value);
            } else if ("test".equals(type)) {
                String value;
                if (ArrayUtils.contains(encryptPropNameArr, "common:" + allConfigList.get(j).get("CONFIG_KEY"))) {
                    value = "$[" + String.valueOf(allConfigList.get(j).get("TEST_VALUE")) + "]";
                } else {
                    value = String.valueOf(allConfigList.get(j).get("TEST_VALUE"));
                }
                storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), value);
            } else if ("build".equals(type)) {
                String value;
                if (ArrayUtils.contains(encryptPropNameArr, "common:" + allConfigList.get(j).get("CONFIG_KEY"))) {
                    value = "$[" + String.valueOf(allConfigList.get(j).get("BUILD_VALUE")) + "]";
                } else {
                    value = String.valueOf(allConfigList.get(j).get("BUILD_VALUE"));
                }
                storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), value);
            }
        }


        for (int j = 0; j < allConfigList.size(); j++) {
            if ("development".equals(type)) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                if (str != null && storeTmp.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                    store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                } else {
                    store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), storeTmp.get(String.valueOf(allConfigList.get(j).get("CONFIG_KEY"))));
                }
            } else if ("production".equals(type)) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                if (str != null && storeTmp.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                    store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                } else {
                    store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), storeTmp.get(String.valueOf(allConfigList.get(j).get("CONFIG_KEY"))));
                }
            } else if ("test".equals(type)) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                if (str != null && storeTmp.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                    store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                } else {
                    store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), storeTmp.get(String.valueOf(allConfigList.get(j).get("CONFIG_KEY"))));
                }
            } else if ("build".equals(type)) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                if (str != null && storeTmp.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                    store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                } else {
                    store.put("common:" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), storeTmp.get(String.valueOf(allConfigList.get(j).get("CONFIG_KEY"))));
                }
            }
        }
        return store;
    }*/

    public Map<String, String> getCommonConfigMap(List<Map<String, Object>> multiProjectId, String type, String[] encryptPropNameArr) {
        Map<String, String> store = new HashMap<>();
        for (int id = 0; id < multiProjectId.size(); id++) {
            int projectId = Integer.valueOf(String.valueOf(multiProjectId.get(id).get("ID")));
            List<Map<String, Object>> allConfigList = configDao.queryConfigs(projectId, type);
            String projCode = projectDao.getProjectCodeByProjectId(projectId);
            Map<String, String> storeTmp = new HashMap<>();
            for (int j = 0; j < allConfigList.size(); j++) {
                if ("development".equals(type)) {
                    String value;
                    if (ArrayUtils.contains(encryptPropNameArr, projCode + ":" + allConfigList.get(j).get("CONFIG_KEY"))) {
                        value = "$[" + String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")) + "]";
                    } else {
                        value = String.valueOf(allConfigList.get(j).get("CONFIG_VALUE"));
                    }
                    storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), value);
                } else if ("production".equals(type)) {
                    String value;
                    if (ArrayUtils.contains(encryptPropNameArr, projCode + ":" + allConfigList.get(j).get("CONFIG_KEY"))) {
                        value = "$[" + String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")) + "]";
                    } else {
                        value = String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE"));
                    }
                    storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), value);
                } else if ("test".equals(type)) {
                    String value;
                    if (ArrayUtils.contains(encryptPropNameArr, projCode + ":" + allConfigList.get(j).get("CONFIG_KEY"))) {
                        value = "$[" + String.valueOf(allConfigList.get(j).get("TEST_VALUE")) + "]";
                    } else {
                        value = String.valueOf(allConfigList.get(j).get("TEST_VALUE"));
                    }
                    storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), value);
                } else if ("build".equals(type)) {
                    String value;
                    if (ArrayUtils.contains(encryptPropNameArr, projCode + ":" + allConfigList.get(j).get("CONFIG_KEY"))) {
                        value = "$[" + String.valueOf(allConfigList.get(j).get("BUILD_VALUE")) + "]";
                    } else {
                        value = String.valueOf(allConfigList.get(j).get("BUILD_VALUE"));
                    }
                    storeTmp.put(String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), value);
                }
            }


            for (int j = 0; j < allConfigList.size(); j++) {
                if ("development".equals(type)) {
                    String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                    if (str != null && storeTmp.size() != 0) {
                        StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                        String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("CONFIG_VALUE")));
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                    } else {
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), storeTmp.get(String.valueOf(allConfigList.get(j).get("CONFIG_KEY"))));
                    }
                } else if ("production".equals(type)) {
                    String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                    if (str != null && storeTmp.size() != 0) {
                        StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                        String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("PRODUCTION_VALUE")));
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                    } else {
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), storeTmp.get(String.valueOf(allConfigList.get(j).get("CONFIG_KEY"))));
                    }
                } else if ("test".equals(type)) {
                    String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                    if (str != null && storeTmp.size() != 0) {
                        StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                        String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("TEST_VALUE")));
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                    } else {
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), storeTmp.get(String.valueOf(allConfigList.get(j).get("CONFIG_KEY"))));
                    }
                } else if ("build".equals(type)) {
                    String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                    if (str != null && storeTmp.size() != 0) {
                        StrSubstitutor strSubstitutor = new StrSubstitutor(storeTmp);
                        String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(j).get("BUILD_VALUE")));
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), realValue);
                    } else {
                        store.put(projCode + ":" + String.valueOf(allConfigList.get(j).get("CONFIG_KEY")), storeTmp.get(String.valueOf(allConfigList.get(j).get("CONFIG_KEY"))));
                    }
                }
            }
        }

        return store;
    }


    /*public Map<String, String> replaceCommonConfigs(int projectId, String type, Map<String, String> commonCofigStore) {
        Map<String, String> store = new HashMap<>();
        List<Map<String, Object>> allConfigList = configDao.queryCommonConfigs(projectId, type);
        if ("development".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("CONFIG_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    String realValue = PlaceHolderUtil.replaceVarWithValue(commonCofigStore, str,
                            String.valueOf(allConfigList.get(i).get("CONFIG_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), String.valueOf(allConfigList.get(i).get("CONFIG_VALUE")));
                }
            }
        } else if ("production".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    String realValue = PlaceHolderUtil.replaceVarWithValue(commonCofigStore, str,
                            String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE")));
                }
            }
        } else if ("test".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("TEST_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    String realValue = PlaceHolderUtil.replaceVarWithValue(commonCofigStore, str,
                            String.valueOf(allConfigList.get(i).get("TEST_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), String.valueOf(allConfigList.get(i).get("TEST_VALUE")));
                }
            }
        } else if ("build".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("BUILD_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    String realValue = PlaceHolderUtil.replaceVarWithValue(commonCofigStore, str,
                            String.valueOf(allConfigList.get(i).get("BUILD_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), String.valueOf(allConfigList.get(i).get("BUILD_VALUE")));
                }
            }
        }
        return store;
    }*/

    public Map<String, String> replaceByCommonConfigs(int projectId, String type, Map<String, String> commonCofigStore) {
        Map<String, String> store = new HashMap<>();
        List<Map<String, Object>> allConfigList = configDao.queryConfigs(projectId, type);
        if ("development".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("CONFIG_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(commonCofigStore);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(i).get("CONFIG_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), String.valueOf(allConfigList.get(i).get("CONFIG_VALUE")));
                }
            }
        } else if ("production".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(commonCofigStore);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE")));
                }
            }
        } else if ("test".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("TEST_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(commonCofigStore);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(i).get("TEST_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), String.valueOf(allConfigList.get(i).get("TEST_VALUE")));
                }
            }
        } else if ("build".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("BUILD_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(commonCofigStore);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(i).get("BUILD_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), String.valueOf(allConfigList.get(i).get("BUILD_VALUE")));
                }
            }
        }
        return store;
    }

    public Map<String, String> replaceByCommonConfigs(int projectId, String type, Map<String, String> commonCofigStore, String[] encryptPropNameArr) {
        Map<String, String> store = new HashMap<>();
        List<Map<String, Object>> allConfigList = configDao.queryConfigs(projectId, type);
        if ("development".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("CONFIG_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(commonCofigStore);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(i).get("CONFIG_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    String value;
                    if (ArrayUtils.contains(encryptPropNameArr, String.valueOf(allConfigList.get(i).get("CONFIG_KEY")))) {
                        value = "$[" + String.valueOf(allConfigList.get(i).get("CONFIG_VALUE")) + "]";
                    } else {
                        value = String.valueOf(allConfigList.get(i).get("CONFIG_VALUE"));
                    }
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), value);
                }
            }
        } else if ("production".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(commonCofigStore);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    String value;
                    if (ArrayUtils.contains(encryptPropNameArr, String.valueOf(allConfigList.get(i).get("CONFIG_KEY")))) {
                        value = "$[" + String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE")) + "]";
                    } else {
                        value = String.valueOf(allConfigList.get(i).get("PRODUCTION_VALUE"));
                    }
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), value);
                }
            }
        } else if ("test".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("TEST_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(commonCofigStore);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(i).get("TEST_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    String value;
                    if (ArrayUtils.contains(encryptPropNameArr, String.valueOf(allConfigList.get(i).get("CONFIG_KEY")))) {
                        value = "$[" + String.valueOf(allConfigList.get(i).get("TEST_VALUE")) + "]";
                    } else {
                        value = String.valueOf(allConfigList.get(i).get("TEST_VALUE"));
                    }
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), value);
                }
            }
        } else if ("build".equals(type)) {
            for (int i = 0; i < allConfigList.size(); i++) {
                String str = PlaceHolderUtil.findPlaceHolderVar(String.valueOf(allConfigList.get(i).get("BUILD_VALUE")));
                if (str != null && commonCofigStore.size() != 0) {
                    StrSubstitutor strSubstitutor = new StrSubstitutor(commonCofigStore);
                    String realValue = strSubstitutor.replace(String.valueOf(allConfigList.get(i).get("BUILD_VALUE")));
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), realValue);
                } else {
                    String value;
                    if (ArrayUtils.contains(encryptPropNameArr, String.valueOf(allConfigList.get(i).get("CONFIG_KEY")))) {
                        value = "$[" + String.valueOf(allConfigList.get(i).get("BUILD_VALUE")) + "]";
                    } else {
                        value = String.valueOf(allConfigList.get(i).get("BUILD_VALUE"));
                    }
                    store.put(String.valueOf(allConfigList.get(i).get("CONFIG_KEY")), value);
                }
            }
        }
        return store;
    }

    public boolean checkConfigKeyExist(String configKey, int projectId) {
        return configDao.checkConfigKeyExist(configKey, projectId);
    }

    public Map<String, Object> queryConfigByConfigId(int configId) {
        return configDao.queryConfigByConfigId(configId);
    }
}
