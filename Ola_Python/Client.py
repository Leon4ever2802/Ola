
import socket
import threading


class Client:

    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.socket = None

    def listener(self):
        while True:
            data = self.socket.recv(1024).decode().replace("\n","")
            print("Server: " + data)

    def writer(self):
        while True:
            to_send = input()
            self.socket.sendall(bytes(to_send + "\n", 'utf-8'))
            if to_send == "%0%":
                break
        print("Disconnected!")

    def start(self):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect((self.host, self.port))
        print("Connected!")
        threading.Thread(target=self.writer, args=()).start()
        threading.Thread(target=self.listener, args=()).start()


HOST = "127.0.0.1"  # The server's hostname or IP address
PORT = 65432  # The port used by the server

client = Client(HOST, PORT)
client.start()
