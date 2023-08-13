package ai.openfabric.api.service;

import ai.openfabric.api.exception.DockerContainerException;
import ai.openfabric.api.model.WorkerStatistic;
import ai.openfabric.api.model.Worker;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.InvocationBuilder.AsyncResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;

@Component
@ComponentScan
@Slf4j
public class DockerService {
    private String dockerHost;
    private String dockerUsername;
    private String dockerPassword;
    private DockerClientConfig dockerClientConfig;
    private DockerClient dockerClient;

    public DockerService(@Value("${docker.host.name}") String dockerHost,
                         @Value("${docker.host.username}") String dockerUsername,
                         @Value("${docker.host.password}") String dockerPassword) {
        this.dockerHost = dockerHost;
        this.dockerUsername = dockerUsername;
        this.dockerPassword = dockerPassword;
        this.dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .build();
        this.dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();
    }

    public void ping() {
        log.info("Pinging..");
        dockerClient.pingCmd().exec();
        log.info("Stopped Pinging..");
    }


    public void startContainer(String containerId) {
        try {
            dockerClient.startContainerCmd(containerId).exec();
        } catch (NotModifiedException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new DockerContainerException("Container with this id already started:" + containerId);
        } catch (NotFoundException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new DockerContainerException(e.getMessage());
        }
    }

    public void stopContainer(String containerId) {
        try {
            dockerClient.stopContainerCmd(containerId).exec();
        } catch (NotModifiedException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new DockerContainerException("Container with this id already stopped:" + containerId);
        } catch (NotFoundException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new DockerContainerException(e.getMessage());
        }
    }

    public CreateContainerResponse createContainer(Worker worker) {
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(worker.getImage());
        setCommandParameters(worker, containerCmd);

        try {
            return containerCmd.exec();
        } catch (ConflictException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new DockerContainerException("Container with this name already exists:" + containerCmd.getName());
        } catch (NotFoundException e) {
            try {
                pullImageFromDocker(worker.getImage());
                return containerCmd.exec();
            } catch (InterruptedException ex) {
                log.error(Arrays.toString(ex.getStackTrace()));
                throw new DockerContainerException(ex.getMessage());
            }
        }
    }

    public Statistics getWorkerStatistics(Worker worker) {
        StatsCmd statsCmd = dockerClient.statsCmd(worker.getContainerId());

        AsyncResultCallback<Statistics> callback = new AsyncResultCallback<>();

        try {
            statsCmd.exec(callback);
            Statistics statistic = callback.awaitResult();
            callback.close();
            return statistic;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new DockerContainerException("Container statistics couldn't be retrieved");
        }
    }

    private void pullImageFromDocker(String image) throws InterruptedException {
        LoginToDockerHub();

        log.info("Pulling image from docker hub");
        dockerClient.pullImageCmd(image).exec(new PullImageResultCallback()).awaitCompletion();
    }

    private void LoginToDockerHub() {
        AuthConfig config = new AuthConfig()
                .withUsername(dockerUsername)
                .withPassword(dockerPassword);

        AuthCmd authCmd = dockerClient.authCmd()
                .withAuthConfig(config);

        log.info("Logging into Docker Hub");
        authCmd.exec();
    }

    private void setCommandParameters(Worker worker, CreateContainerCmd containerCmd) {
        if (worker.getCmd() != null) {
            containerCmd.withCmd(worker.getCmd());
        }

        if (worker.getName() != null) {
            containerCmd.withName(worker.getName());
        }

        if (worker.getHostName() != null) {
            containerCmd.withHostName(worker.getHostName());
        }

        if (worker.getEnv() != null) {
            containerCmd.withEnv(worker.getEnv());
        }

        if (worker.getPorts() != null) {
            containerCmd.withPortBindings(PortBinding.parse(worker.getPorts()));
        }
    }
}
