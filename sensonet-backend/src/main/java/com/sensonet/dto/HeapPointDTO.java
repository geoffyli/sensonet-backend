package com.sensonet.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "quota")
public class HeapPointDTO {

    @Column(name = "deviceId")
    private String deviceId;

    @Column(name = "quotaId")
    private String quotaId;

    @Column(name = "quotaName")
    private String quotaName;

    @Column(name = "heapValue")
    private Long heapValue;


}
