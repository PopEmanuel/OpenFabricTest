package ai.openfabric.api.controller;

import ai.openfabric.api.service.DockerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {
    DockerService dockerService;

    @PostMapping(path = "/hello")
    public @ResponseBody String hello(@RequestBody String name) {
        dockerService = new DockerService();
        dockerService.ping();
        return "Hello!" + name;
    }


}
