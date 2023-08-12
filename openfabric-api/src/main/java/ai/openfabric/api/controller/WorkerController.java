package ai.openfabric.api.controller;

import ai.openfabric.api.converter.WorkerConverter;
import ai.openfabric.api.converter.WorkerStatisticConverter;
import ai.openfabric.api.dto.WorkerDto;
import ai.openfabric.api.dto.WorkerStatisticDto;
import ai.openfabric.api.model.WorkerStatistic;
import ai.openfabric.api.model.Worker;
import ai.openfabric.api.service.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {
    private final WorkerService workerService;
    private final WorkerConverter workerConverter;
    private final WorkerStatisticConverter workerStatisticConverter;

    public WorkerController(WorkerService workerService, WorkerConverter workerConverter, WorkerStatisticConverter workerStatisticConverter) {
        this.workerService = workerService;
        this.workerConverter = workerConverter;
        this.workerStatisticConverter = workerStatisticConverter;
    }

    @PostMapping(path = "/hello")
    public @ResponseBody String hello(@RequestBody String name) {
        return "Hello!" + name;
    }

    @GetMapping
    public ResponseEntity<List<WorkerDto>> getAllWorkers(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        List<WorkerDto> workers = workerService.getWorkers(page, size).stream()
                .map(workerConverter::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workers);
    }

    @PostMapping
    public ResponseEntity<?> addWorker(@RequestBody WorkerDto dto) {
        workerService.addWorker(workerConverter.convertToModel(dto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeWorker(@RequestParam String workerId) {
        workerService.removeWorker(workerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/start")
    public ResponseEntity<?> startWorker(@RequestParam String workerId) {
        workerService.startWorker(workerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/stop")
    public ResponseEntity<?> stopWorker(@RequestParam String workerId) {
        workerService.stopWorker(workerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/info")
    public ResponseEntity<WorkerDto> getWorkerInformation(@RequestParam String workerId) {
        return ResponseEntity.ok(workerConverter.convertToDto(workerService.getWorkerById(workerId)));
    }

    @GetMapping(path = "/statistics")
    public ResponseEntity<List<WorkerStatisticDto>> getWorkerStatistics(@RequestParam String workerId) {
        List<WorkerStatisticDto> statistics = workerService.getWorkerStatistics(workerId).stream()
                .map(workerStatisticConverter::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(statistics);
    }
}
