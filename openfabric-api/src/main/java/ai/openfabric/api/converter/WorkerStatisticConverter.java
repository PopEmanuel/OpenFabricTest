package ai.openfabric.api.converter;

import ai.openfabric.api.dto.WorkerStatisticDto;
import ai.openfabric.api.model.WorkerStatistic;
import org.springframework.stereotype.Component;

@Component
public class WorkerStatisticConverter {
    public WorkerStatisticDto convertToDto(WorkerStatistic entity) {
        WorkerStatisticDto dto = new WorkerStatisticDto();
        dto.setId(entity.getId());
        dto.setWorkerId(entity.getWorker().getId());
        dto.setCpu(entity.getCpu());
        dto.setMemoryUsage(entity.getMemoryUsage());
        dto.setMemoryLimit(entity.getMemoryLimit());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public WorkerStatistic convertToModel(WorkerStatisticDto dto) {
        WorkerStatistic entity = new WorkerStatistic();
        entity.setId(dto.getId());
        entity.setCpu(dto.getCpu());
        entity.setMemoryUsage(dto.getMemoryUsage());
        entity.setMemoryLimit(dto.getMemoryLimit());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setDeletedAt(dto.getDeletedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
