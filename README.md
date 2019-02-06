# minio-client
maven repository


## Description
* Minioにアクセスするためのclient
* MinioProviderをDIで注入して処理を委譲する

## Usage
* MinioRepository.java
```
public class MinioRepository  {
    @Autowired
    private MinioProvider minioProvider;
    
    public void insertOne(String contentType, String base64, Long subjectId, String userId) {
        this.minioProvider.insertOne(new MinioFileContent(contentType, base64), subjectId, userId);
    }
}
```

* application-context.xml(Spring)
```
<!-- Minio -->
<bean id="minioClient" class="io.minio.MinioClient">
    <constructor-arg type="java.lang.String" name="endpoint" value="${minio.endpoint}" />
    <constructor-arg type="int" name="port" value="${minio.port}" />
    <constructor-arg type="java.lang.String" name="accessKey" value="${minio.accessKey}" />
    <constructor-arg type="java.lang.String" name="secretKey" value="${minio.secretKey}" />
</bean>
<bean id="minioProvider" class="miyakawalab.tool.minio.MinioProvider" depends-on="minioClient">
    <property name="minioClient" ref="minioClient" />
</bean>
```

## Install
* maven
```
<dependencies>
    <dependency>
        <groupId>miyakawalab.tool</groupId>
        <artifactId>minio-client</artifactId>
        <version>${version}</version>
    </dependency>
</dependencies>
<repositories>
    <repository>
        <id>minio-client</id>
        <url>https://raw.github.com/908ab/minio-client/mvn-repo/</url>
    </repository>
</repositories>
```


## Version
> 1.0

