package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TemperInsertController implements Initializable {

	static private Statement st;
	private Stage primaryStage;
	
	public void setStatement(Statement st, Stage primaryStage) {
		this.st = st;
		this.primaryStage = primaryStage;
	}
	
	@FXML Label lb_temper, lb_hour, lb_min, lb_sec, lb_humi, lb_start_time, lb_end_time;
	@FXML Button btn_temper, btn_humi;
	@FXML TextField tf_shour, tf_smin, tf_ssec, tf_ehour, tf_emin, tf_esec;
	
	
	static InputStream in;
	static OutputStream out;
	static SerialPort serialPort;
	static CommPort commPort;
	
	ResultSet rs = null;
	String sql = "";
	String sqlTemper = "";
	String sqlHumi = "";
	String sqlTime = "";
	static Label Temper, Hour, Min, Sec, Humi;
	
	XYChart.Series series = null;
	ObservableList yLabels = null;
	ObservableList xLabels = null;
	static boolean sensingFlag = false;
	
	int startTime, endTime;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Temper = lb_temper;
		Humi = lb_humi;
		Hour = lb_hour;
		Min = lb_min;
		Sec = lb_sec;
		
		xLabels = FXCollections.observableArrayList();
		yLabels = FXCollections.observableArrayList();
		try {
			new TemperInsertController().connect("COM7");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		(new Thread(new TemperUpdate())).start();
		
	
	}
	
	public void TemperBtnOnAction(ActionEvent event) {
		try {
			Stage temperDialog = new Stage();
			temperDialog.initModality(Modality.WINDOW_MODAL);
			temperDialog.initOwner(primaryStage);
			temperDialog.setTitle("측정 온도 변화 그래프");
			//시간 중복은 없다고 가정
			
			xLabels.clear();
			yLabels.clear();
			
			startTime = Integer.parseInt(tf_shour.getText()+tf_smin.getText()+tf_ssec.getText());
			endTime = Integer.parseInt(tf_ehour.getText()+tf_emin.getText()+tf_esec.getText());
			
			sql = "select temper, s_time from tabledb.sensingtbl where s_time >= " + startTime + " and s_time <= " + endTime + ";";
			rs = st.executeQuery(sql);
			
			int hour = 0, min = 0, sec = 0;
			while(rs.next()) {
				sqlTemper = rs.getString("temper");
				sqlTime = rs.getString("s_time");
				
				hour = Integer.parseInt(sqlTime)/10000;
				min = (Integer.parseInt(sqlTime)%10000)/100;
				sec = (Integer.parseInt(sqlTime)%100);	
				
				String sensingTimeStr = hour + ":" + min + ":" + sec;
				xLabels.add(sensingTimeStr);
				
				yLabels.add(Integer.parseInt(sqlTemper));
				//Date sensingTimeDate = new SimpleDateFormat("HH:mm:ss").parse(sensingTimeStr);
				
			}
			
			FXMLLoader loadTemper = new FXMLLoader(getClass().getResource("TemperChart.fxml"));
			Parent temper = loadTemper.load();
			
			Scene sceneTemper = new Scene(temper);
			
			temperDialog.setScene(sceneTemper);
			
			temperDialog.show();
		
			
			CategoryAxis xAxis = (CategoryAxis)temper.lookup("#temper_xAxis");
			xAxis.setCategories(xLabels);
			
			//series.setData(observableList);
			series = new XYChart.Series<String, Number>();
			
			for(int i = 0; i < xLabels.size(); i++)
				series.getData().add(new XYChart.Data(xLabels.get(i), yLabels.get(i)));
			
			BarChart tmperChart = (BarChart)temper.lookup("#chart_temper");
			tmperChart.getData().add(series);
			
					
		}
		catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void HumiBtnOnAction(ActionEvent event) {
		try {
			Stage humiDialog = new Stage();
			humiDialog.initModality(Modality.WINDOW_MODAL);
			humiDialog.initOwner(primaryStage);
			humiDialog.setTitle("측정 습도 변화 그래프");
			//시간 중복은 없다고 가정
			
			xLabels.clear();
			yLabels.clear();
			
			startTime = Integer.parseInt(tf_shour.getText()+tf_smin.getText()+tf_ssec.getText());
			endTime = Integer.parseInt(tf_ehour.getText()+tf_emin.getText()+tf_esec.getText());
			
			sql = "select humi, s_time from tabledb.sensingtbl where s_time >= " + startTime + " and s_time <= " + endTime + ";";
			rs = st.executeQuery(sql);
			
			int hour = 0, min = 0, sec = 0;
			while(rs.next()) {
				sqlHumi = rs.getString("humi");
				sqlTime = rs.getString("s_time");
				
				hour = Integer.parseInt(sqlTime)/10000;
				min = (Integer.parseInt(sqlTime)%10000)/100;
				sec = (Integer.parseInt(sqlTime)%100);	
				
				String sensingTimeStr = hour + ":" + min + ":" + sec;
				xLabels.add(sensingTimeStr);
				
				yLabels.add(Integer.parseInt(sqlHumi));
				//Date sensingTimeDate = new SimpleDateFormat("HH:mm:ss").parse(sensingTimeStr);
				
			}
			
			FXMLLoader loadHumi = new FXMLLoader(getClass().getResource("HumiChart.fxml"));
			Parent humi = loadHumi.load();
			
			Scene sceneHumi = new Scene(humi);
			
			humiDialog.setScene(sceneHumi);
			
			CategoryAxis xAxis = (CategoryAxis)humi.lookup("#humi_xAxis");
			xAxis.setCategories(xLabels);
			
			//series.setData(observableList);
			series = new XYChart.Series<String, Number>();
			
			for(int i = 0; i < xLabels.size(); i++)
				series.getData().add(new XYChart.Data(xLabels.get(i), yLabels.get(i)));
			
			LineChart humiChart = (LineChart)humi.lookup("#chart_humi");
			humiChart.getData().add(series);
			
			humiDialog.show();
			
		}
		catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// 시리얼 연결을 하기위한 함수
	private void connect(String portName) throws Exception {

		System.out.printf("Port : %s\n", portName);

		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

		if (portIdentifier.isCurrentlyOwned()) { // currentlyowned : 누가 쓰고있냐
			System.out.println("Error: Port is currently in use");
		} else {
			commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams( // 시리얼 포트 설정
						9600, // 통신 속도
						SerialPort.DATABITS_8, // 몇비트 통신인가,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				in = serialPort.getInputStream();

				out = serialPort.getOutputStream();

				(new Thread(new SerialReader(in))).start();
				(new Thread(new SerialWriter(out))).start();
				
				
			}
		}
	}
	
	public static class TemperUpdate implements Runnable {
		String sql = "";
		ResultSet rs = null;
		
		String[] columnArr = {"temper", "humi", "s_time"};
		String resultTemper = "";
		
		String currentTemper = "";
		String currentHumi = "";
		String currentTime = "";
		
		public TemperUpdate() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void run() {
			
			while(true) {
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(sensingFlag) {
					
					try {
						sensingFlag = false;
						currentTemper = Temper.getText();
						currentHumi = Humi.getText();
						currentTime = Hour.getText() + Min.getText() + Sec.getText();  
				
						//System.out.println(currentTemper + " / " + currentHumi + " / " + currentTime);
						sql = "insert into tabledb.sensingtbl (temper, humi, s_time) "
							+ "values(" + currentTemper + ", " + currentHumi + "," + currentTime + ");";
					
					
						st.executeUpdate(sql);	
					}
						
					catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}

	public static class SerialReader implements Runnable {
		InputStream in;
		
		
		public SerialReader(InputStream in) {
			this.in = in;
			
		}

		@Override
		public void run() {

			byte[] buffer = new byte[1024];
			int len = -1;
			String str = "";
			
			try {
				while ((len = this.in.read(buffer)) > -1) {
					 
					
					if(len > 0) { 
						str += new String(buffer, 0, len);
						
						if(str.charAt(str.length()-1) == '\n') {
							String[] strArr = str.split(" ");
							
							str = "";
						  
							if(strArr[0].equals("time") && strArr[4].equals("temper") && strArr[6].equals("humi")) { 
								Platform.runLater(new Runnable() {
						  
									@Override public void run() {
										try {
											if((Integer.parseInt(strArr[1]) / 10) == 0)
												Hour.setText("0" + strArr[1]);
											else 
												Hour.setText(strArr[1]);
											if((Integer.parseInt(strArr[2]) / 10) == 0)
												Min.setText("0" + strArr[2]);
											else 
												Min.setText(strArr[2]);
											if((Integer.parseInt(strArr[3]) / 10) == 0)
												Sec.setText("0" + strArr[3]);
											else 
												Sec.setText(strArr[3]);
											
											Temper.setText(strArr[5]);
											Humi.setText(strArr[7]);
											sensingFlag = true;
										}
										catch (Exception e) {
										
										}
									} 
							
								}); 
							}
							
							
						} 
						
					}
						
				}
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	public static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		@Override
		public void run() {
			try {
				int c = 0;
				System.out.println("\n Keyboard Input Read!!!");// 안내 문구 출력
				while ((c = System.in.read()) > -1) {
					this.out.write(c);
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	
}
