package com.example.backend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    public static final Map<String, String> USER_INFO_MAP = new ConcurrentHashMap<>();
}
