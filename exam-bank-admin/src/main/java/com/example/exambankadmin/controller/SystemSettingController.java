package com.example.exambankadmin.controller;

import com.example.exambankadmin.entity.SystemSetting;
import com.example.exambankadmin.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system-settings")
public class SystemSettingController {

    @Autowired
    private SystemSettingService systemSettingService;

    /**
     * 获取所有系统设置
     */
    @GetMapping
    public ResponseEntity<List<SystemSetting>> getAllSettings() {
        List<SystemSetting> settings = systemSettingService.getAllSettings();
        return ResponseEntity.ok(settings);
    }

    /**
     * 根据ID获取系统设置
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemSetting> getSettingById(@PathVariable Long id) {
        SystemSetting setting = systemSettingService.getSettingById(id)
                .orElseThrow(() -> new RuntimeException("SystemSetting not found with id: " + id));
        return ResponseEntity.ok(setting);
    }

    /**
     * 根据键获取系统设置
     */
    @GetMapping("/key/{key}")
    public ResponseEntity<SystemSetting> getSettingByKey(@PathVariable String key) {
        SystemSetting setting = systemSettingService.getSettingByKey(key)
                .orElseThrow(() -> new RuntimeException("SystemSetting not found with key: " + key));
        return ResponseEntity.ok(setting);
    }

    /**
     * 创建或更新系统设置
     */
    @PostMapping
    public ResponseEntity<SystemSetting> createOrUpdateSetting(@RequestBody SystemSetting setting) {
        SystemSetting savedSetting = systemSettingService.saveSetting(setting);
        return ResponseEntity.ok(savedSetting);
    }

    /**
     * 批量更新系统设置
     */
    @PostMapping("/batch")
    public ResponseEntity<List<SystemSetting>> batchUpdateSettings(@RequestBody List<SystemSetting> settings) {
        List<SystemSetting> updatedSettings = systemSettingService.updateSettings(settings);
        return ResponseEntity.ok(updatedSettings);
    }

    /**
     * 根据ID删除系统设置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable Long id) {
        systemSettingService.deleteSetting(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}