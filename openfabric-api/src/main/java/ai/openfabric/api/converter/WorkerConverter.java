package ai.openfabric.api.converter;

import ai.openfabric.api.dto.WorkerDto;
import ai.openfabric.api.model.Worker;
import org.springframework.stereotype.Component;

@Component
public class WorkerConverter {
    public WorkerDto convertToDto(Worker entity) {
        WorkerDto dto = new WorkerDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCmd(entity.getCmd());
        dto.setContainerId(entity.getContainerId());
        dto.setEnv(entity.getEnv());
        dto.setImage(entity.getImage());
        dto.setPorts(entity.getPorts());
        dto.setStatus(entity.getStatus());
        dto.setHostName(entity.getHostName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public Worker convertToModel(WorkerDto dto) {
        Worker entity = new Worker();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCmd(dto.getCmd());
        entity.setContainerId(dto.getContainerId());
        entity.setEnv(dto.getEnv());
        entity.setImage(dto.getImage());
        entity.setPorts(dto.getPorts());
        entity.setStatus(dto.getStatus());
        entity.setHostName(dto.getHostName());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setDeletedAt(dto.getDeletedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
