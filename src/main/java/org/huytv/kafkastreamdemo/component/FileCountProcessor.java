package org.huytv.kafkastreamdemo.component;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.huytv.kafkastreamdemo.model.SavedFileDTO;
import org.springframework.stereotype.Component;

@Component
public class FileCountProcessor {
    private static final Serde<SavedFileDTO> FILE_DTO_SERDE = Serdes.serdeFrom(SavedFileDTO.class);


}
