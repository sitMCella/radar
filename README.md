# Radar Application

![Radar](https://github.com/sitMCella/radar/wiki/images/radar.png | width=100)

## Table of contents

* [Introduction](#introduction)
* [Requirements](#requirements)
* [Run application](#run-application)

## Introduction

The Radar application allows registered devices to communicate with a Radar Hub. The Hub receives the geographical position of nearby objects from the devices. A frontend web application displays the position of the detected objects in a 2d canvas. The frontend application continuosly receives data from a reactive backend.

## Requirements

- Docker (Docker compose)

## Run Applications

```sh
docker-compose -f docker-compose.yml up
```

Open http://localhost:80 to view the application in your browser.
