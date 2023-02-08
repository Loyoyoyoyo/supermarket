package com.lolo.supermarket.service;

import com.lolo.supermarket.entity.Activity;

import java.util.List;

public interface ActivityService {
    List<Activity> retrieveAll();
    List<Activity> retrieveTwo();
    int create(Activity activity);
    int delete(int id);
}
