package com.sky.test;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.PasswordConstant;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class HttpClientTest {

    /**
     * 测试通过HttpClient发送get请求
     */
    @Test
    public void testGet() throws IOException {
        //创建HttpClient对象
        HttpClient httpClient = HttpClients.createDefault();

        //创建请求对象
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");

        //发送请求
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpGet);

        //解析返回结果
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("服务端返回的状态码为：" + statusCode);

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);

        System.out.println("服务端返回的数据为：" + body);

        response.close();
    }

    /**
     * 测试通过HttpClient发送post请求
     */
    @Test
    public void testPost() throws IOException{
        //创建HttpClient对象
        HttpClient httpClient = HttpClients.createDefault();

        //创建请求对象
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");

        //封装请求实体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","admin");
        jsonObject.put("password", PasswordConstant.DEFAULT_PASSWORD);

        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        stringEntity.setContentEncoding("utf-8");
        stringEntity.setContentType("application/json");

        //设置请求实体
        httpPost.setEntity(stringEntity);

        //发送请求
        CloseableHttpResponse response = (CloseableHttpResponse)httpClient.execute(httpPost);

        //解析返回结果
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("服务端返回的状态码为：" + statusCode);

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);

        System.out.println("响应数据为："+body);

        response.close();

    }
}
