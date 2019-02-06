package miyakawalab.tool.minio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MinioFileContent {
    private String contentType;
    private String base64;
}
