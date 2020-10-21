package com.linln.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CacheUpdateRunner implements CommandLineRunner {
    @Autowired
    RedisUtil redisUtil;
    @Override
    public void run(String... args) throws Exception {
//        System.out.println("wo jin lai le");
        Set<String> keys = redisUtil.getKeys("Heart:");
        System.out.println(keys.toString());
        for (String key : keys) {
            redisUtil.del(key);
        }
//        System.out.println("wo wan shi le");
//        redisUtil.del();
    }
}
