# !DEPRECATED!

This project has been merged into the [Quarkus-Zeebe](https://github.com/quarkiverse/quarkus-zeebe) extension.


# zeebe-dev-monitor

Zeebe developer monitor build with [Quarkus](https://quarkus.io/) for local development. It uses in memory database [SQLite](https://www.sqlite.org/) to store the 
zeebe [debug exporter](https://github.com/camunda-community-hub/zeebe-test-container#debug-exporter) records.

![Architecture](./docs/diagram.svg)

## Configuration

```yaml
env:
  QUARKUS_ZEEBE_CLIENT_BROKER_GATEWAY_ADDRESS=zeebe:26500
```
## Screenshots

![Screenshot](./docs/zeebe-dev-monitor-processes-800.png)
![Screenshot](./docs/zeebe-dev-monitor-instance-800.png)
![Screenshot](./docs/zeebe-dev-monitor-incident-800.png)
![Screenshot](./docs/zeebe-dev-monitor-complete-job-800.png)

## Containers

```shell
docker pull ghcr.io/lorislab/zeebe-dev-monitor:latest
docker run -p 8080:8080 ghcr.io/lorislab/zeebe-dev-monitor:latest 
```


