package com.example.mobile_subproject_nhom05.listener;

import com.example.mobile_subproject_nhom05.module.Motor;

import java.util.List;

public interface IMotorLoadListener {
    void onMotorLoadSuccess(List<Motor> motors);
    void onMotorLoadFailed(String message);
}
