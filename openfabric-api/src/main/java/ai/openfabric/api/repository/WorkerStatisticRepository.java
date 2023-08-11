package ai.openfabric.api.repository;

import ai.openfabric.api.model.WorkerStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerStatisticRepository extends JpaRepository<WorkerStatistic, String> {
}
