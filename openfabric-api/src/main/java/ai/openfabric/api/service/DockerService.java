package ai.openfabric.api.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.springframework.stereotype.Service;

@Service
public class DockerService {
    private final String dockerHost = "tcp://localhost:2375";
    private DockerClientConfig dockerClientConfig;
    private DockerClient dockerClient;
    public DockerService(){
        this.dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .build();
        this.dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();
    }

    public void ping(){
        System.out.println("Pinging..");
        dockerClient.pingCmd().exec();
        System.out.println("Stopped Pinging..");
    }

    public void createContainer(){

    }

    public void startContainer(int containterId){

    }

    public void stopContainer(int containterId){

    }

}
