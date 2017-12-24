package com.ustc.wr;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import javax.swing.*;
@SuppressWarnings("serial")
public class Client extends JFrame implements ActionListener,Runnable{
	/*2014年12月18日
	 * 作者：王润
	 * 第二次修改
	 * */
	String s = "sucess";
	final String addressPort="3002";
	boolean hh = true;
	int port = 51942;
	JTextArea area;
	JButton button;
	JPanel pane;
	JTextField text;
	byte tom[] = new byte[10000];
	InetAddress group = null;
	MulticastSocket socket = null;
	ServerSocket server = null;
	Socket you = null;
	DataOutputStream out = null;
	DataInputStream in = null;
	Thread thread = new Thread(this);
	Client(){
		init();
		setBounds(100,50,360,380);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try{
			group = InetAddress.getByName("239.255.8.0");
			socket = new MulticastSocket(port);
			socket.setTimeToLive(255);
			socket.joinGroup(group);
			server = new ServerSocket(Integer.parseInt(addressPort));
			DatagramPacket packet = null;
			byte data[] = addressPort.getBytes();
			packet = new DatagramPacket(data,data.length,group,port);
			socket.send(packet);
			you = server.accept();
			in = new DataInputStream(you.getInputStream());
			out = new DataOutputStream(you.getOutputStream());
			thread.start();
		}
		catch(Exception e){
			System.out.println(e);
		}
		sendInfo();
	}
	public void run(){
		try{
			while(true){
				int b = in.read(tom,0,10000);
				String ss = new String(tom,0,b);
				area.append("Server:"+ss+"\n");
			}
		}
		catch(Exception e){
		}
	}
	public void init(){
		setTitle("Client");
		area = new JTextArea(10,10);
		area.setEditable(false);
		button = new JButton("send");
		button.addActionListener(this);
		text = new JTextField(10);
		pane = new JPanel();
		pane.add(text);
		pane.add(button);
		add(new JScrollPane(area),BorderLayout.CENTER);
		add(pane,BorderLayout.SOUTH);
	}
	public void actionPerformed(ActionEvent e){
		try {
			String ss = text.getText();
			text.setText(null);
			area.append("Client:"+ss+"\n");
			out.write(ss.getBytes());
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public void sendInfo() {
		while (true) {
			try {
				String ss = SystemInfo.GetSystemInfo();
				text.setText(null);
				area.append("Client:" + ss + "\n");
				out.write(ss.getBytes());
				Thread.sleep(1000);
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		new Client();
	}
}
