package com.example.examquestionbank.service;

import com.example.examquestionbank.entity.SystemSetting;
import com.example.examquestionbank.repository.SystemSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    /**
     * 获取所有系统设置
     */
    public List<SystemSetting> getAllSettings() {
        return systemSettingRepository.findAll();
    }

    /**
     * 根据ID获取系统设置
     */
    public Optional<SystemSetting> getSettingById(Long id) {
        return systemSettingRepository.findById(id);
    }

    /**
     * 根据键获取系统设置
     */
    public Optional<SystemSetting> getSettingByKey(String key) {
        return systemSettingRepository.findBySettingKey(key);
    }

    /**
     * 创建或更新系统设置
     */
    public SystemSetting saveSetting(SystemSetting setting) {
        // 检查是否已存在相同的键
        Optional<SystemSetting> existingSetting = systemSettingRepository.findBySettingKey(setting.getSettingKey());
        if (existingSetting.isPresent()) {
            // 如果存在，更新现有设置
            SystemSetting updateSetting = existingSetting.get();
            updateSetting.setSettingValue(setting.getSettingValue());
            updateSetting.setDescription(setting.getDescription());
            return systemSettingRepository.save(updateSetting);
        } else {
            // 如果不存在，创建新设置
            return systemSettingRepository.save(setting);
        }
    }

    /**
     * 根据ID删除系统设置
     */
    public void deleteSetting(Long id) {
        systemSettingRepository.deleteById(id);
    }

    /**
     * 批量更新系统设置
     */
    public List<SystemSetting> updateSettings(List<SystemSetting> settings) {
        return systemSettingRepository.saveAll(settings);
    }
}
