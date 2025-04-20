# Radar Device

## Introdution

The following project provides the configuration of the Radar Device.
The project is developed using Rust.

## Development

### Requirements

- Rust 1.86.0
- Cargo 1.86.0

### Build Application

```sh
cargo build
```

### Run Application

```sh
cargo run -- --id <device_id> --latitude <latitude> --longitude <longitude> --radius <radius> --file-name <coordinates_file_name>
```

## Format Code

```sh
cargo fmt
```

## Example Devices

### Device 1

```sh
cargo run -- --id 9b02c045-abc5-4ae9-b179-8b5d051326b4 --latitude 50.34 --longitude 80.30 --radius 50 --file-name ./dev1_signals.csv
```

Radius: 50

### Device 2

```sh
cargo run -- --id 87900a57-c21e-4746-8105-68f2c673ceed --latitude 240.12 --longitude 108.78 --radius 60 --file-name ./dev2_signals.csv
```

Radius: 60

### Device 3

```sh
cargo run -- --id be9e49aa-9380-4b82-9393-57fde7c9fc20 --latitude 120.72 --longitude 03.32 --radius 40 --file-name ./dev3_signals.csv
```
Radius: 40
