package ai.openfabric.api.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerDto {
    public String id;

    public String name;

    public String image;

    public String ports;

    public String env;

    public String hostName;

    public String cmd;
}
