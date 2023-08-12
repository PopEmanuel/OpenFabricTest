package ai.openfabric.api.dto;


import ai.openfabric.api.model.Datable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WorkerDto extends Datable implements Serializable {
    private String id;

    private String name;

    private String image;

    private String ports;

    private String env;

    private String hostName;

    private String cmd;

    private String containerId;

    private Integer status;

}
