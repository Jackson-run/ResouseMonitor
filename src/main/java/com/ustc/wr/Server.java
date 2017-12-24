package com.ustc.wr;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
@SuppressWarnings("serial")
public class Server extends JFrame implements ActionListener {
	int port;
	InetAddress group = null;
	MulticastSocket socket = null;
	JTextArea area;
	Thread thread;
	Socket soc;
	JTextField text;
	JLabel label;
	JButton button;
	InetAddress re;
	DataOutputStream out = null;
	DataInputStream in = null;
	boolean st = false;

	public Server() {
		setTitle("Server");
		area = new JTextArea(10, 10);
		area.setEditable(false);
		button = new JButton("send");
		label = new JLabel("connnecting.......");
		label.setForeground(Color.blue);
		button.addActionListener(this);
		JPanel north = new JPanel();
		JPanel sourth = new JPanel();
		text = new JTextField(10);
		sourth.add(text);
		sourth.add(button);
		north.add(label);
		add(north, BorderLayout.NORTH);
		add(new JScrollPane(area), BorderLayout.CENTER);
		add(sourth, BorderLayout.SOUTH);
		setBounds(100, 50, 360, 380);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		port = 51942;
		try {
			group = InetAddress.getByName("239.255.8.0");
			socket = new MulticastSocket(port);
			socket.joinGroup(group);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		byte data[] = new byte[8192];
		DatagramPacket packet = null;
		String preAdressPort="";
		packet = new DatagramPacket(data, data.length, group, port);
		int addressIndex = 1;
		try {
			while(true) {
				socket.receive(packet);
				re = packet.getAddress();
				String addressPort = new String(packet.getData(), 0, packet.getLength());
				System.out.println(addressPort);
				label.setText("Success");
				String dd = new String(re.toString().toCharArray(), 1, re
						.toString().toCharArray().length - 1);
				if(!preAdressPort.equals(addressPort)){
					try {
						new Thread(new SocketConnect(this,dd,Integer.parseInt(addressPort))).start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				preAdressPort = addressPort;
			}
			//soc = new Socket(dd, 4332);
			/*in = new DataInputStream(soc.getInputStream());
			out = new DataOutputStream(soc.getOutputStream());*/
		} 
		catch (Exception ese) {

		}
		/*while (true) {
			try {
				byte[] a = new byte[1000];
				int b = in.read(a, 0, 1000);
				String ss = new String(a, 0, b);
				area.append("Client:" + ss + "\n");
			} 
			catch (Exception ee) {

			}
		}*/
	}

	public void actionPerformed(ActionEvent ee) {
			try {
				String ss = text.getText();
				area.append("Server:"+ss+"\n");
				text.setText(null);
				out.write(ss.getBytes());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	public void insertMessage(String Message){
		area.append(Message);
	}

	public static void main(String[] args) {
		new Server();
	}
}
