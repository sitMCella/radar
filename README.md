# Radar Application

<img src="https://github.com/sitMCella/radar/wiki/images/radar.png" width="500">

## Table of contents

* [Introduction](#introduction)
* [Requirements](#requirements)
* [Run Application](#run-application)
* [Load Test](#load-test)

## Introduction

The Radar application allows registered devices to communicate with a Radar Hub. The devices send the geographical position of nearby objects to the Radar Hub. A frontend web application displays the position of the detected objects in a 2d canvas. The frontend application continuosly receives data from a reactive backend.

## Requirements

- Docker (Docker compose)

## Run Application

```sh
docker-compose -f docker-compose.yml up
```

Open http://localhost:80 to view the application in your browser.

## Load Test

Run the load test to simulate a heavy load of signals from the devices:

```sh
cd radardevice
./load_test.sh
```
