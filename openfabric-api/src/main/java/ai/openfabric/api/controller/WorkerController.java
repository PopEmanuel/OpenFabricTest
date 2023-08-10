package ai.openfabric.api.controller;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.service.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {
    WorkerService workerService;

    @PostMapping(path = "/hello")
    public @ResponseBody String hello(@RequestBody String name) {
        return "Hello!" + name;
    }

    @GetMapping
    public ResponseEntity<List<Worker>> getAllWorkers(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size){
        return ResponseEntity.ok(workerService.getWorkers(page, size));
    }

    @PostMapping
    public ResponseEntity<?> addWorker(@RequestBody Worker worker){
        workerService.addWorker(worker);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeWorker(@RequestParam String workerId){
        workerService.removeWorker(workerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/start")
    public ResponseEntity<?> startWorker(@RequestParam String workerId){
        workerService.startWorker(workerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/stop")
    public ResponseEntity<?> stopWorker(@RequestParam String workerId){
        workerService.stopWorker(workerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/info")
    public ResponseEntity<Worker> getWorkerInformation(@RequestParam String workerId){
        return ResponseEntity.ok(workerService.getWorkerById(workerId));
    }
}
