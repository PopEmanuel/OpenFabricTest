package ai.openfabric.api.service;

import ai.openfabric.api.exception.CrudOperationException;
import ai.openfabric.api.model.WorkerStatistic;
import ai.openfabric.api.model.Worker;
import ai.openfabric.api.repository.WorkerRepository;
import ai.openfabric.api.util.WorkerStatus;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WorkerService {
    private final DockerService dockerService;
    private final WorkerRepository workerRepository;
    private final WorkerStatisticService workerStatisticService;

    public WorkerService(DockerService dockerService, WorkerRepository workerRepository, WorkerStatisticService workerStatisticService) {
        this.dockerService = dockerService;
        this.workerRepository = workerRepository;
        this.workerStatisticService = workerStatisticService;
    }

    public Worker addWorker(Worker worker) {
        CreateContainerResponse container = dockerService.createContainer(worker);

        if (container != null) {
            worker.setContainerId(container.getId());
            workerRepository.save(worker);
        }
        return worker;
    }

    public void removeWorker(String id) {
        Worker worker = getWorkerById(id);
        workerRepository.delete(worker);
    }

    public void startWorker(String id) {
        Worker worker = getWorkerById(id);
        dockerService.startContainer(worker.getContainerId());

        setWorkerStatus(worker, WorkerStatus.Running.ordinal());
    }

    public void stopWorker(String id) {
        Worker worker = getWorkerById(id);
        dockerService.stopContainer(worker.getContainerId());

        setWorkerStatus(worker, WorkerStatus.Stopped.ordinal());
    }

    public List<Worker> getWorkers(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        return workerRepository.findAll(pageable).getContent();
    }

    public Worker getWorkerById(String id) {
        return workerRepository.findById(id).orElseThrow(() -> new CrudOperationException("Worker with this id doesn't exist"));
    }

    public List<WorkerStatistic> getWorkerStatistics(String id) {
        Worker worker = getWorkerById(id);
        Statistics statistics = dockerService.getWorkerStatistics(worker);
        workerStatisticService.addWorkerStatistic(statistics, worker);

        return workerStatisticService.getAllWorkerStatistics(worker.getId());
    }

    private void setWorkerStatus(Worker worker, Integer status) {
        worker.setStatus(status);
        workerRepository.save(worker);
    }
}
