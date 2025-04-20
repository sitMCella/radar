use clap::Parser;
use core::fmt;
use serde::Deserialize;
use serde_json::json;
use std::{error::Error, fs::File, thread, time};

#[derive(Parser)]
#[command(name = "device")]
struct Args {
    #[arg(long, value_parser = max_length_36)]
    id: String,

    #[arg(long)]
    latitude: f64,

    #[arg(long)]
    longitude: f64,

    #[arg(long)]
    file_name: String,
}

#[derive(Deserialize)]
struct Device {
    id: String,
}

#[derive(Deserialize)]
struct Signal {
    id: u64,
}

#[derive(Debug)]
enum DeviceError {
    Register(String),
    Signal(String),
}

impl fmt::Display for DeviceError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            DeviceError::Register(msg) => write!(f, "Cannot register device: {}", msg),
            DeviceError::Signal(msg) => write!(f, "Cannot send signal: {}", msg),
        }
    }
}

impl Error for DeviceError {}

fn max_length_36(input: &str) -> Result<String, String> {
    if input.len() <= 36 {
        Ok(input.to_string())
    } else {
        Err(format!(
            "Input '{}' is too long. Max allowed is 36 characters.",
            input
        ))
    }
}

async fn register_device(
    id: &String,
    latitude: f64,
    longitude: f64,
) -> Result<String, Box<dyn Error>> {
    let client = reqwest::Client::new();
    let body = json!({
        "id": id,
        "latitude": latitude,
        "longitude": longitude
    });
    // Development environment: use the URL "http://localhost:8080/api/devices"
    let response = client
        .post("http://hub:8080/api/devices")
        .json(&body)
        .send()
        .await?;
    println!("{:#?}", response);
    match response.status() {
        reqwest::StatusCode::CREATED => {
            let device: Device = response.json().await?;
            Ok(device.id)
        }
        reqwest::StatusCode::CONFLICT => Err(Box::new(DeviceError::Register(format!(
            "a device with id \"{}\" has already been registered.",
            id
        )))),
        _ => Err(Box::new(DeviceError::Register(
            response.status().as_str().to_owned(),
        ))),
    }
}

async fn send_signal(
    device_id: &String,
    obj_id: u64,
    latitude: f64,
    longitude: f64,
) -> Result<u64, Box<dyn Error>> {
    let client = reqwest::Client::new();
    let body = json!({
        "deviceId": device_id,
        "objId": obj_id,
        "latitude": latitude,
        "longitude": longitude
    });
    // Development environment: use the URL "http://localhost:8080/api/devices"
    let response = client
        .post("http://hub:8080/api/signals")
        .json(&body)
        .send()
        .await?;
    println!("{:#?}", response);
    match response.status() {
        reqwest::StatusCode::CREATED => {
            let signal: Signal = response.json().await?;
            Ok(signal.id)
        }
        _ => Err(Box::new(DeviceError::Signal(
            response.status().as_str().to_owned(),
        ))),
    }
}

#[tokio::main]
async fn main() {
    let args = Args::parse();

    println!(
        "Device id: {} - latitude: {} - longitude: {}",
        args.id, args.latitude, args.longitude
    );

    let file = match File::open(args.file_name) {
        Ok(file) => file,
        Err(err) => {
            println!("The file does not exists: {}", err);
            std::process::exit(1);
        }
    };

    let device_id = &args.id;

    thread::sleep(time::Duration::from_millis(30000));

    let registered = register_device(device_id, args.latitude, args.longitude).await;
    match registered {
        Ok(id) => println!("Registered device: {}", id),
        Err(e) => {
            println!("{}", e);
            std::process::exit(1);
        }
    }

    thread::sleep(time::Duration::from_millis(5000));

    let mut reader = csv::Reader::from_reader(file);

    for result in reader.records() {
        thread::sleep(time::Duration::from_millis(3500));
        match result {
            Ok(record) => {
                let obj_id = match record[0].parse::<u64>() {
                    Ok(num) => num,
                    Err(_) => continue,
                };
                let latitude = match record[1].parse::<f64>() {
                    Ok(num) => num,
                    Err(_) => continue,
                };
                let longitude = match record[2].parse::<f64>() {
                    Ok(num) => num,
                    Err(_) => continue,
                };
                let signal_sent = send_signal(device_id, obj_id, latitude, longitude).await;
                match signal_sent {
                    Ok(id) => println!("Sent signal with id: {}", id),
                    Err(e) => {
                        println!("{}", e);
                    }
                }
            }
            Err(e) => {
                println!("{}", e);
                continue;
            }
        }
    }
}
