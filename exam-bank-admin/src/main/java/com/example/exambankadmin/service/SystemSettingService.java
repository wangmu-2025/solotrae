package com.example.exambankadmin.service;

import com.example.exambankadmin.entity.SystemSetting;

import java.util.List;
import java.util.Optional;

public interface SystemSettingService {
    /**
     * 获取所有系统设置
     */
    List<SystemSetting> getAllSettings();

    /**
     * 根据ID获取系统设置
     */
    Optional<SystemSetting> getSettingById(Long id);

    /**
     * 根据键获取系统设置
     */
    Optional<SystemSetting> getSettingByKey(String key);

    /**
     * 创建或更新系统设置
     */
    SystemSetting saveSetting(SystemSetting setting);

    /**
     * 根据ID删除系统设置
     */
    void deleteSetting(Long id);

    /**
     * 批量更新系统设置
     */
    List<SystemSetting> updateSettings(List<SystemSetting> settings);
}