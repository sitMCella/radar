#!/bin/bash

# Requirements:
# - Install Cargo
# - Build release the Radar Device application using Cargo

for i in {1001..1002}
do
   ./target/release/radardevice --id "d$i" --latitude 50.34 --longitude 80.30 --radius 50 --file-name "dev1_signals.csv" >> load_test.log 2>&1 &
done
