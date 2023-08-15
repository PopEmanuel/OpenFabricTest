package ai.openfabric.api.service;

import ai.openfabric.api.exception.CrudOperationException;
import ai.openfabric.api.model.WorkerStatistic;
import ai.openfabric.api.model.Worker;
import ai.openfabric.api.repository.WorkerRepository;
import ai.openfabric.api.util.WorkerStatus;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void addWorker(Worker worker) {
        CreateContainerResponse container = dockerService.createContainer(worker);

        if (container != null) {
            worker.setContainerId(container.getId());
            workerRepository.save(worker);
        }
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

    private List<Worker> getAllWorkers() {
        return this.workerRepository.findAll();
    }

    @Scheduled(fixedRate = 300000)
    private void filterContainers() {
        log.info("Checking and updating sudden stopped or deleted containers");

        List<Container> containers = this.dockerService.getContainers();
        List<Worker> workers = getAllWorkers();

        for (Worker worker : workers) {
            String containerId = worker.getContainerId();
            Container workerContainer = containers.stream()
                    .filter(container -> container.getId().equals(containerId))
                    .findFirst()
                    .orElseGet(() -> {
                        log.info("Deleting worker with id = " + worker.getId()
                                + " because docker container was deleted");
                        this.workerRepository.delete(worker);
                        return null;
                    });
            if (workerContainer != null) {
                boolean changeStatus = false;
                String currentStatus = worker.getStatus() == WorkerStatus.Stopped.ordinal() ? "exited" : "running";
                if (currentStatus.equals("running")) {
                    if (!workerContainer.getState().equals("running")) {
                        changeStatus = true;
                    }
                } else {
                    if (workerContainer.getState().equals("running")) {
                        //Stopped states can be also represented by Created, Exited(1), Dead etc.
                        changeStatus = true;
                    }
                }
                if (changeStatus) {
                    log.info("Updating status of worker with id = " + worker.getId()
                            + " because docker container status changed externally");
                    worker.setStatus(worker.getStatus() == WorkerStatus.Stopped.ordinal() ?
                            WorkerStatus.Running.ordinal() : WorkerStatus.Stopped.ordinal());
                    workerRepository.save(worker);
                }

            }
        }

    }
}
