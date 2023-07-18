# Author: Leon Reusch

import socket
import threading
import PySimpleGUI as sg


class Client:
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
        self.socket = None
        self.window = window

    def listener(self):
        """
        :return:
        """
        while True:
            data_lst = self.socket.recv(1024).decode().split("\n")
            del data_lst[len(data_lst)-1]
            for data in data_lst:
                if "%D%" in data or "%CHECK%" in data or "%CONN%" in data or "%LIST%" in data:
                    self.window["Chat"].print(data.split("%")[2].replace("\n", ""))
                elif "%NAME%" in data:
                    self.window.TKroot.title("Ola - " + data.split("%")[2])
                else:
                    self.window["Chat"].print(data.replace("\n", ""))

    def write(self, message):
        """
        :param message:
        :return:
        """
        self.socket.sendall(bytes(message + "\n", 'utf-8'))
        if message == "%0%":
            return True
        else:
            return False

    def start(self):
        """
        :return:
        """
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect((self.host, self.port))
        print("Connected!")
        name = input("Enter username:")
        while name == "":
            print("A username is needed!")
            name = input("Enter username:")
        name = "%NAME%" + name
        self.socket.sendall(bytes(name + "\n", 'utf-8'))
        listener = threading.Thread(target=self.listener, args=())
        listener.daemon = True
        listener.start()
        return True


HOST = "127.0.0.1"  # The server's hostname or IP address
PORT = 65432  # The port used by the server

sg.theme("DarkAmber")
client_window = sg.Window(title="Ola - ", finalize=True,
                          layout=[[sg.Multiline(size=(50, 30), key="Chat", disabled=True)],
                                  [sg.Input(key="ToSend"), sg.Button("Send", key="Send")]])
client_window.bind("<Return>", "Enter")

client = Client(HOST, PORT, client_window)
threading.Thread(target=client.start, args=()).start()

while True:
    event, values = client_window.read()
    if event == sg.WINDOW_CLOSED:
        client.write("%0%")
        break
    elif event in ("Enter", "Send"):
        if " ".join(values["ToSend"].split()) == "":
            client_window["ToSend"].update("")
        else:
            if client.write(values["ToSend"]):
                break
            client_window["Chat"].print(values["ToSend"])
            client_window["ToSend"].update("")
