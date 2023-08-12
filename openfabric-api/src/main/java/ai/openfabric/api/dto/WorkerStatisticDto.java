package ai.openfabric.api.dto;


import ai.openfabric.api.model.Datable;
import ai.openfabric.api.model.Worker;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
public class WorkerStatisticDto extends Datable implements Serializable {
    private String id;

    private Long cpu;

    private Long memoryUsage;

    private Long memoryLimit;

    private String workerId;
}
