extern crate websocket;

use std::io::stdin;
use std::thread;

use websocket::client::ClientBuilder;
use websocket::OwnedMessage;

fn main() {
    let client = ClientBuilder::new("ws://echo.websocket.org/")
        .unwrap()
        .connect_insecure()
        .unwrap();

    let (mut receiver, mut sender) = client.split().unwrap();

    let receive_loop = thread::spawn(move || {
        for message in receiver.incoming_messages() {
            println!("received: {:?}", message);
        }
    });

    loop {
        let mut input = String::new();
        stdin().read_line(&mut input).unwrap();
        let line = input.trim();
        if line == "bye" {
            break;
        }
        let message = OwnedMessage::Text(line.to_string());
        sender.send_message(&message).unwrap();
    }
    receive_loop.join().unwrap();
}
