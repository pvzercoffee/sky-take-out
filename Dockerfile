# 1. 基础镜像：选用 openjdk 11 的精简版 (slim)，体积小
FROM openjdk:11-jre-slim

# 2. 作者信息（可选）
MAINTAINER pvzercoffee

# 3. 设置工作目录
WORKDIR /app

# 4. 设定时区为上海（非常重要，否则订单时间会少8小时）
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 5. 将 maven 打包生成的 jar 包复制到容器中，并重命名为 app.jar
# 注意：这里的路径是相对于 Dockerfile 的路径
COPY sky-server/target/sky-server-1.0-SNAPSHOT.jar app.jar

# 6. 暴露端口（仅仅是声明，实际运行还需要 -p 映射）
EXPOSE 8080

# 7. 启动命令
# --spring.profiles.active=dev 指定环境，你可以改为 prod
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]
