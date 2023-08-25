export KUBECONFIG=kube_config_cluster.yml
kubectl apply -f sample-api-poc.yaml
kubectl get pods -o wide --all-namespaces
