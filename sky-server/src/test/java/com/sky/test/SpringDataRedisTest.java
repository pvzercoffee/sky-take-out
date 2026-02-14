package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){

        System.out.println(redisTemplate);

        ValueOperations valueOperations = redisTemplate.opsForValue();
        HashOperations hashOperations = redisTemplate.opsForHash();
        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations setOperations = redisTemplate.opsForSet();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }

    /**
     * 操作字符串类型的数据
     */
    @Test
    public void testString(){
        //set get setnx setex
        redisTemplate.opsForValue().set("city","beijing");
        String city = (String)redisTemplate.opsForValue().get("city");

        System.out.println(city);

        redisTemplate.opsForValue().set("code","1234",3, TimeUnit.MINUTES);
        String code = (String) redisTemplate.opsForValue().get("code");
        System.out.println(code);

        redisTemplate.opsForValue().setIfAbsent("lock","0");
        redisTemplate.opsForValue().setIfAbsent("lock","1");
    }

    /**
     * 操作哈希类型的数据
     */
    @Test
    public void testHash(){
        //hset hget hdel hkeys hvals
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("100","name","tom");
        hashOperations.put("100","age","22");

        String name = (String) hashOperations.get("100","name");

        Set keys = hashOperations.keys("100");
        List values = hashOperations.values("100");

        System.out.println(keys);
        System.out.println(values);

        hashOperations.delete("100","age");

    }
    /**
     * 操作双向链表类型的数据
     */
    @Test
    public void testList(){
        //lpush rpush lpop rpop lrange llen
        ListOperations listOperations = redisTemplate.opsForList();

//        listOperations.leftPushAll("mylist","a","b","c");
//        listOperations.leftPush("mylist","d");
//        listOperations.rightPush("mylist","e");
//
//        List range = listOperations.range("mylist",0,-1);
//        System.out.println(range);

        Long size = listOperations.size("mylist");

        System.out.println(size);

    }
    /**
     * 操作集合类型的数据
     */
    @Test
    public void testSet(){
        //sadd smembers  scard sinter sunion srem
        SetOperations setOperations = redisTemplate.opsForSet();

        setOperations.add("set1","a","b","c","d");
        setOperations.add("set2","x","y","z");

        Set members = setOperations.members("set1");
        System.out.println("set1:"+members);

        Long size = setOperations.size("set1");
        System.out.println("set1's set:"+size);

        Set intersect = setOperations.intersect("set1","set2");
        System.out.println("set1 and set2 'intersect:"+intersect);

        Set union = setOperations.union("set1","set2");
        System.out.println("set1 and set2 'union:"+union);

        setOperations.remove("set1","a");

    }

    /**
     * 操作有序集合类型的数据
     */
    @Test
    public void testZSet(){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        String name = "myzset";
        zSetOperations.add(name,"a",10);
        zSetOperations.add(name,"b",11);
        zSetOperations.add(name,"c",9.9);
        zSetOperations.add(name,"d",20);
        zSetOperations.add(name,"e",5);

        Set zset1 = zSetOperations.range(name,0,-1);
        System.out.println(zset1);

        zSetOperations.incrementScore(name,"a",-5);

        zSetOperations.remove(name,"d","e");
    }

    /**
     * 通用命令操作
     */
    @Test
    public void testCommon(){
        //keys exists type del
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);

        Boolean name  = redisTemplate.hasKey("name");
        System.out.println(name);

        Boolean set3 = redisTemplate.hasKey("set3");
        System.out.println(set3);

        for(Object key: keys){
            System.out.println(key+":"+redisTemplate.type(key));
        }

        redisTemplate.delete("set2");
    }
}
