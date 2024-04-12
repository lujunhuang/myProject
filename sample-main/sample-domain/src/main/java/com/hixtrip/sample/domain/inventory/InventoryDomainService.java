package com.hixtrip.sample.domain.inventory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 库存领域服务
 * 库存设计，忽略仓库、库存品、计量单位等业务
 */
@Component
@Slf4j
public class InventoryDomainService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取sku当前库存
     *
     * @param skuId
     */
    public Long getInventory(String skuId) {
        //todo 需要你在infra实现，只需要实现缓存操作, 返回的领域对象自行定义
        //可售库存,预占库存,占用库存
        String key = "product:" + skuId;

        //校验商品是否在缓存，没有则存放
        // 判断 Redis 中是否存在对应的 key
        if (!redisTemplate.hasKey(key)) {
            // 去数据库查询商品,可售库存,预占库存,占用库存
            Map<String, Long> countMap = queryInventoryFromDatabase(skuId);
            // 并将结果缓存到 Redis 中，设置有效期十分钟
            redisTemplate.expire(key,10,TimeUnit.MINUTES);
            redisTemplate.opsForHash().putAll(key,countMap);
        }
        Long num = (Long) redisTemplate.opsForHash().get(key,"sellableQuantity");
        return num;
    }

    /**
     * 修改库存
     *
     * @param skuId
     * @param sellableQuantity    可售库存
     * @param withholdingQuantity 预占库存
     * @param occupiedQuantity    占用库存
     * @return
     */
    @Transactional
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        //todo 需要你在infra实现，只需要实现缓存操作。

        String lockKey = "lock:inventory:" + skuId;
        try {
            // 获取分布式锁
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, 1);
            if (acquired != null && acquired) {
                // 设置过期时间，避免死锁
                redisTemplate.expire(lockKey, 30, TimeUnit.NANOSECONDS); // 设置30秒过期

                //1、效验 可售库存 = 预占库存+占用库存
                if (sellableQuantity != withholdingQuantity + occupiedQuantity) {
                    log.info("================可售库存不等于预占库存+占用库存,可售库存:{},预占库存:{},占用库存:{}====================,"
                            , sellableQuantity, withholdingQuantity, occupiedQuantity);
                    return false;
                }

                // 执行库存更新操作
                String key = "product:" + skuId;
                Map<String, Long> productMap = new HashMap<>();
                productMap.put("sellableQuantity", sellableQuantity);
                productMap.put("withholdingQuantity", withholdingQuantity);
                productMap.put("occupiedQuantity", occupiedQuantity);
                redisTemplate.opsForHash().putAll(key, productMap);
                return true;
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 释放锁
            redisTemplate.delete(lockKey);
        }
        return false;
    }

    // 模拟从数据库查询商品库存的方法，根据skuId获取该样品数据库库存
    private Map<String, Long> queryInventoryFromDatabase(String skuId) {
        Map<String, Long> productMap = new HashMap<>();
        productMap.put("sellableQuantity",100L);
        productMap.put("withholdingQuantity",40L);
        productMap.put("occupiedQuantity",60L);
        return productMap;
    }
}
