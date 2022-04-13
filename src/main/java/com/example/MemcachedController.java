package com.example;

import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.OwnerReferenceBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpecBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.apache.commons.collections.CollectionUtils;

import javax.inject.Inject;

import static io.javaoperatorsdk.operator.api.reconciler.Constants.WATCH_CURRENT_NAMESPACE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//public class MemcachedController implements ResourceController<Memcached> {
//@ControllerConfiguration(namespaces = WATCH_CURRENT_NAMESPACE)
@ControllerConfiguration
public class MemcachedController implements Reconciler<Memcached> {

    @Inject
    KubernetesClient client;

    public MemcachedController(KubernetesClient client) {
        this.client = client;
    }

    // TODO Fill in the rest of the controller

    @Override
    public UpdateControl<Memcached> reconcile(Memcached memcached, Context context) {
        // TODO: fill in logic
        Deployment deployment = client.apps()
                .deployments()
                .inNamespace(memcached.getMetadata().getNamespace())
                .withName(memcached.getMetadata().getName())
                .get();

        if (deployment == null) {
            //System.out.println("deployment = "+deployment);
            Deployment newDeployment = createMemcachedDeployment(memcached);
            client.apps().deployments().create(newDeployment);
            return UpdateControl.noUpdate();
        }

        int currentReplicas = deployment.getSpec().getReplicas();
        int requiredReplicas = memcached.getSpec().getSize();

        System.out.println("currentReplicas = "+currentReplicas);
        System.out.println("requiredReplicas = "+requiredReplicas);

        if (currentReplicas != requiredReplicas) {
            deployment.getSpec().setReplicas(requiredReplicas);
            client.apps().deployments().createOrReplace(deployment);
            System.out.println("inside if = "+currentReplicas);
            System.out.println("inside if = "+requiredReplicas);
            return UpdateControl.noUpdate();
        }

        List<Pod> pods = client.pods()
                .inNamespace(memcached.getMetadata().getNamespace())
                .withLabels(labelsForMemcached(memcached))
                .list()
                .getItems();

        System.out.println("pods = "+pods);

        List<String> podNames =
                pods.stream().map(p -> p.getMetadata().getName()).collect(Collectors.toList());

        System.out.println("podnames"+podNames);
        if (memcached.getStatus() == null
                || !CollectionUtils.isEqualCollection(podNames, memcached.getStatus().getNodes())) {
            System.out.println(memcached.getStatus());
            if (memcached.getStatus() == null)
                memcached.setStatus(new MemcachedStatus());
            memcached.getStatus().setNodes(podNames);
            System.out.println(memcached.getStatus());
            System.out.println("inside second if = "+currentReplicas);
            System.out.println("inside second if = "+requiredReplicas);
            return UpdateControl.updateStatus(memcached);
        }

         return UpdateControl.noUpdate();
    }

    private Map<String, String> labelsForMemcached(Memcached m) {
        Map<String, String> labels = new HashMap<>();
        labels.put("app", "memcached");
        labels.put("memcached_cr", m.getMetadata().getName());
        return labels;
    }

    private Deployment createMemcachedDeployment(Memcached m) {
        return new DeploymentBuilder()
                .withMetadata(
                        new ObjectMetaBuilder()
                                .withName(m.getMetadata().getName())
                                .withNamespace(m.getMetadata().getNamespace())
                                .withOwnerReferences(
                                        new OwnerReferenceBuilder()
                                                .withApiVersion("v1")
                                                .withKind("Memcached")
                                                .withName(m.getMetadata().getName())
                                                .withUid(m.getMetadata().getUid())
                                                .build())
                                .build())
                .withSpec(
                        new DeploymentSpecBuilder()
                                .withReplicas(m.getSpec().getSize())
                                .withSelector(
                                        new LabelSelectorBuilder().withMatchLabels(labelsForMemcached(m)).build())
                                .withTemplate(
                                        new PodTemplateSpecBuilder()
                                                .withMetadata(
                                                        new ObjectMetaBuilder().withLabels(labelsForMemcached(m)).build())
                                                .withSpec(
                                                        new PodSpecBuilder()
                                                                .withContainers(
                                                                        new ContainerBuilder()
                                                                                .withImage("memcached:1.4.36-alpine")
                                                                                .withName("memcached")
                                                                                .withCommand("memcached", "-m=64", "-o", "modern", "-v")
                                                                                .withPorts(
                                                                                        new ContainerPortBuilder()
                                                                                                .withContainerPort(11211)
                                                                                                .withName("memcached")
                                                                                                .build())
                                                                                .build())
                                                                .build())
                                                .build())
                                .build())
                .build();
    }
}

