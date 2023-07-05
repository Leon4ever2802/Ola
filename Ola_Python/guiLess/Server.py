
import socket
import threading


class Server:

    clients = []

    def __init__(self, host, port):
        self.host = host
        self.port = port

    def check_users(self, conn):
        names = ""
        if len(self.clients) > 1:
            for client, name in self.clients:
                if client == conn:
                    continue
                elif self.clients.index((client, name)) == len(self.clients)-2:
                    names += name
                else:
                    names += name + ", "
        else:
            names = "Noone else"
        names += " already connected!"
        return names

    def thread_run(self, conn, addr):
        data = conn.recv(1024).decode().replace("\n", "").replace("\r","")
        print("Client: " + data)
        self.clients.append((conn, data.split("%")[2]))
        username = data.split("%")[2]
        for client, name in self.clients:
            if client == conn:
                client.sendall(bytes("%CONN%Server: Connected!" + "\n", 'utf-8'))
                client.sendall(bytes("%CHECK%Server: " + self.check_users(conn) + "\n", 'utf-8'))
            else:
                client.sendall(bytes("%CONN%Server: " + username + " connected!\n", 'utf-8'))

        while True:
            data = conn.recv(1024).decode().replace("\n", "").replace("\r","")
            print("Client: " + data)
            if data == "%0%":
                index_left = None
                for i, content in enumerate(self.clients):
                    if content[0] == conn:
                        index_left = i
                        break
                for client, name in self.clients:
                    if client == conn:
                        continue
                    else:
                        client.sendall(bytes("%D%Server: " + username + " disconnected!\n", 'utf-8'))
                del self.clients[index_left]
                break
            else:
                for client, name in self.clients:
                    if client == conn:
                        continue
                    else:
                        client.sendall(bytes(username + ": " + data + "\n", 'utf-8'))
        print(f"Connection got closed with {addr}")

    def start(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.bind((self.host, self.port))
            s.listen()
            while True:
                conn, addr = s.accept()
                print(f"Connected by {addr}")
                threading.Thread(target=self.thread_run, args=(conn, addr)).start()


HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 65432  # Port to listen on (non-privileged ports are > 1023)

server = Server(HOST, PORT)
server.start()
