
import socket
import threading


class Server:

    clients = []

    def __init__(self, host, port):
        self.host = host
        self.port = port

    def thread_run(self, conn, addr):
        while True:
            data = conn.recv(1024)
            if data.decode() == "":
                break
            for client in self.clients:
                if client == conn:
                    continue
                else:
                    client.sendall(data)
            print("Client: " + data.decode().replace("\n", ""))
        print(f"Connection got closed with {addr}")

    def start(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.bind((self.host, self.port))
            s.listen()
            while True:
                conn, addr = s.accept()
                print(f"Connected by {addr}")
                self.clients.append(conn)
                threading.Thread(target=self.thread_run, args=(conn, addr)).start()


HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 65432  # Port to listen on (non-privileged ports are > 1023)

server = Server(HOST, PORT)
server.start()
