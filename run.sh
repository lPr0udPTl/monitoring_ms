#!/bin/bash

python script.py
sudo docker rmi fabiopina151/grafana
cd Grafana
sudo docker build -t fabiopina151/grafana . 
cd ..
sudo docker stack deploy -c infrastructure.yml infra
