package org.huytv.kafkastreamdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedFileDTO {
    private String id;
    private String fileName;
    private long size;
    private LocalDateTime updatedDate;
    private int userID;
}
