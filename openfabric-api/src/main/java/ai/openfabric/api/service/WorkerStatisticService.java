package ai.openfabric.api.service;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerStatistic;
import ai.openfabric.api.repository.WorkerStatisticRepository;
import com.github.dockerjava.api.model.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkerStatisticService {
    private final WorkerStatisticRepository workerStatisticRepository;

    public WorkerStatisticService(WorkerStatisticRepository workerStatisticRepository) {
        this.workerStatisticRepository = workerStatisticRepository;
    }

    public void addWorkerStatistic(Statistics statistics, Worker worker) {
        WorkerStatistic workerStatistic = new WorkerStatistic();
        workerStatistic.setWorker(worker);
        workerStatistic.setCpu(statistics.getCpuStats().getCpuUsage().getTotalUsage());
        workerStatistic.setMemoryLimit(statistics.getMemoryStats().getLimit());
        workerStatistic.setMemoryUsage(statistics.getMemoryStats().getMaxUsage());
        workerStatisticRepository.save(workerStatistic);
    }

    public List<WorkerStatistic> getAllWorkerStatistics(String workerId) {
        return workerStatisticRepository.findAll().stream()
                .filter(statistic -> statistic.getWorker() != null && statistic.getWorker().getId().equals(workerId))
                .collect(Collectors.toList());
    }
}
