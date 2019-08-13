package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection; //외부 dbconnect.jar파일을 add해주지 않으면 찾을 수 없음
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	private Stage primaryStage; //실행한 윈도우 //이 위에 다이얼로그 띄우기
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	

	@FXML TextField tf_id;
	@FXML PasswordField pwf_pw;
	@FXML Button btn_login, btn_signin;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
			
		btn_login.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				//DB에 접속할 접속 객체
				Connection connection = null;
				Statement st = null;
				//jdbc : java db connection 
				//192.168.0.47 : 쌤
				String url = "jdbc:mysql://localhost:3306?serverTimezone=UTC";
				String loginID = tf_id.getText().toString();
				String loginPW = pwf_pw.getText().toString();
				String sql = "";
				
				try { //운영체제에서 가상의 하드웨어로 취급하기 때문에 try/catch
					//driver 받아오기 //DBMS마다 다름 >> DBMS 제조사에서 안내해줌
					Class.forName("com.mysql.cj.jdbc.Driver");
					//연결점 만들기
					connection = DriverManager.getConnection(url, loginID, loginPW);
					
					st = connection.createStatement();
					
					/*
					sql = "select * from tabledb.usertbl;";
					//ResultSet : arr // usertbl의 row들이 들어 있음 // 각 row들이 통채로 들어있음
					ResultSet result = st.executeQuery(sql);
					
					while(result.next()) { //next() : result에 다음 row가 있으면 true, 없으면 false return 
						String sqlResult = result.getString("userid"); //현재 row의 userid의 column 값
						System.out.print(sqlResult + " ");
						sqlResult = result.getString("name");
						System.out.println(sqlResult);
					}
					
					//ResultSet, Statement, Connection 반드시 끊어주기
					result.close();
					st.close();
					connection.close();
					*/
				}catch (SQLException e) { // getConnection catch
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("login");
					alert.setHeaderText("  Login Failed !!!");
					alert.setContentText("아이디와 비밀번호를 확인하세요.");
					alert.show();
					//System.out.println("SQLException");
					e.printStackTrace();
				} catch (ClassNotFoundException e) { //driver 받아오는 catch
					System.out.println("ClassNotFoundException");
					e.printStackTrace();
				}
				
				/*finally { //try, catch 중 실행하고 최종적으로 무조건 실행 하는 것 //꼭 해야하는 것들 
					try {
						if(st != null) st.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					try {
						if(connection != null) connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}*/
				try {
					
					if(st != null) {
						FXMLLoader loadUser = new FXMLLoader(getClass().getResource("TemperInsert.fxml"));
						Parent user = loadUser.load();
						
						//UserController controller = loadUser.getController(); //root.fxml에 적용된 컨트롤러 객체를 넣어주는 것
						//controller.setStatement(st);
						
						//InsertController controller = loadUser.getController();
						//controller.setStatement(st);
						
						TemperInsertController controller = loadUser.getController();
						controller.setStatement(st, primaryStage);
						
						Scene sceneUser = new Scene(user);
						
						primaryStage.setScene(sceneUser);
						primaryStage.show();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}

}
