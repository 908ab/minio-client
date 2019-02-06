package miyakawalab.tool.minio;

import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.Setter;
import miyakawalab.tool.minio.model.MinioFileContent;
import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParserException;

import javax.ws.rs.InternalServerErrorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// todo try-catch
public class MinioProvider {
    // MinioClient
    @Setter
    private MinioClient minioClient;

    public Long insertOne(MinioFileContent fileContent, Long subjectId, String userId) {
        String bucketName = this.getBucketName(userId, subjectId);
        try {
            this.checkBucket(bucketName);
            Long nextId = this.getNextId(bucketName);
            String objectName = String.valueOf(nextId);
            InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(fileContent.getBase64()));
            this.minioClient.putObject(bucketName, objectName, inputStream, fileContent.getContentType());
            return nextId;
        } catch (InvalidBucketNameException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InsufficientDataException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoResponseException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (XmlPullParserException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (ErrorResponseException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InternalException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InvalidArgumentException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<MinioFileContent> findMany(Long subjectId, String userId) {
        try {
            String bucketName = this.getBucketName(userId, subjectId);
            this.checkBucket(bucketName);
            return StreamSupport.stream(this.minioClient.listObjects(bucketName).spliterator(), false)
                .map(result -> {
                    try {
                        String objectName = result.get().objectName();
                        return this.toDomain(this.minioClient.getObject(bucketName, objectName));
                    } catch (InvalidBucketNameException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    } catch (NoSuchAlgorithmException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    } catch (InsufficientDataException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    } catch (IOException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    } catch (InvalidKeyException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    } catch (NoResponseException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    } catch (XmlPullParserException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    } catch (ErrorResponseException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    } catch (InternalException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    } catch (InvalidArgumentException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    }
                }).collect(Collectors.toList());
        } catch (XmlPullParserException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Optional<MinioFileContent> findById(Long fileId, Long subjectId, String userId) {
        try {
            String bucketName = this.getBucketName(userId, subjectId);
            this.checkBucket(bucketName);
            InputStream inputStream = this.minioClient.getObject(bucketName, String.valueOf(fileId));
            return Optional.of(this.toDomain(inputStream));
        } catch (InvalidBucketNameException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InsufficientDataException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoResponseException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (XmlPullParserException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (ErrorResponseException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InternalException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InvalidArgumentException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteById(Long fileId, Long subjectId, String userId) {
        try {
            String bucketName = this.getBucketName(userId, subjectId);
            this.checkBucket(bucketName);
            this.minioClient.removeObject(bucketName, String.valueOf(fileId));
        } catch (InvalidBucketNameException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InsufficientDataException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoResponseException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (XmlPullParserException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (ErrorResponseException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InternalException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InvalidArgumentException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private String getBucketName(String userId, Long subjectId) {
        return userId + "-" + subjectId;
    }

    private void checkBucket(String bucketName) {
        try {
            if (!this.minioClient.bucketExists(bucketName)) {
                this.minioClient.makeBucket(bucketName);
            }
        } catch (InvalidBucketNameException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InsufficientDataException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (NoResponseException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (XmlPullParserException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (ErrorResponseException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (InternalException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (RegionConflictException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private Long getNextId(String bucketName) throws XmlPullParserException {
        return StreamSupport.stream(this.minioClient.listObjects(bucketName).spliterator(), false)
            .map(result -> {
                try {
                    return result.get();
                } catch (InvalidBucketNameException e) {
                    throw new InternalServerErrorException(e.getMessage());
                } catch (NoSuchAlgorithmException e) {
                    throw new InternalServerErrorException(e.getMessage());
                } catch (InsufficientDataException e) {
                    throw new InternalServerErrorException(e.getMessage());
                } catch (IOException e) {
                    throw new InternalServerErrorException(e.getMessage());
                } catch (InvalidKeyException e) {
                    throw new InternalServerErrorException(e.getMessage());
                } catch (NoResponseException e) {
                    throw new InternalServerErrorException(e.getMessage());
                } catch (XmlPullParserException e) {
                    throw new InternalServerErrorException(e.getMessage());
                } catch (ErrorResponseException e) {
                    throw new InternalServerErrorException(e.getMessage());
                } catch (InternalException e) {
                    throw new InternalServerErrorException(e.getMessage());
                }
            })
            .map(item -> Long.valueOf(item.objectName()))
            .max(Comparator.comparing(Function.identity()))
            .orElse(-1L) + 1L;
    }

    private MinioFileContent toDomain(InputStream inputStream) {
        try {
            String contentType = URLConnection.guessContentTypeFromStream(inputStream);
            String base64 = Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputStream));
            return new MinioFileContent(contentType, base64);
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

