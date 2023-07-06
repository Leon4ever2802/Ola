# Author: Leon Reusch

import socket
import threading
import PySimpleGUI as sg


class Server:
    """
    Author: Leon Reusch
    """

    def __init__(self, host, port, window):
        """
        :param host:
        :param port:
        :param window:
        """
        self.host = host
        self.port = port
        self.window = window
        self.clients = []
        self.client_id = 0

    def check_users(self, conn):
        """
        :param conn:
        :return:
        """
        names = ""
        if len(self.clients) > 1:
            for client, name in self.clients:
                if client == conn:
                    continue
                elif self.clients.index((client, name)) == len(self.clients) - 2:
                    names += name
                else:
                    names += name + ", "
        else:
            names = "No one else"
        names += " already connected!"
        return names

    def thread_run(self, conn, addr, id):
        """
        :param conn:
        :param addr:
        :param id:
        :return:
        """
        data = conn.recv(1024).decode().replace("\n", "").replace("\r", "")
        self.window["Log"].print("Client" + str(id) + ": " + data)
        self.clients.append((conn, data.split("%")[2]))
        username = data.split("%")[2]
        for client, name in self.clients:
            if client == conn:
                client.sendall(bytes("%CONN%Server: Connected!" + "\n", 'utf-8'))
                client.sendall(bytes("%CHECK%Server: " + self.check_users(conn) + "\n", 'utf-8'))
                client.sendall(bytes("%NAME%" + username + "\n", 'utf-8'))
            else:
                client.sendall(bytes("%CONN%Server: " + username + " connected!\n", 'utf-8'))

        while True:
            data = conn.recv(1024).decode().replace("\n", "").replace("\r", "")
            self.window["Log"].print("Client" + str(id) + ": " + data)
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
        self.window["Log"].print("Connection got closed with Client" + str(id) + f" at {addr}")

    def start(self):
        """
        :return:
        """
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.bind((self.host, self.port))
            s.listen()
            while True:
                conn, addr = s.accept()
                self.window["Log"].print(f"Connected by {addr}")
                threading.Thread(target=self.thread_run, args=(conn, addr, self.client_id)).start()
                self.client_id += 1


HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 65432  # Port to listen on (non-privileged ports are > 1023)

sg.theme("DarkAmber")
server_window = sg.Window(title="Ola - Server", finalize=True,
                          layout=[[sg.Multiline(size=(50, 30), key="Log", disabled=True)]])

server = Server(HOST, PORT, server_window)
accepter = threading.Thread(target=server.start, args=())
accepter.daemon =True
accepter.start()

while True:
    event, values = server_window.read()
    if event == sg.WINDOW_CLOSED:
        break
