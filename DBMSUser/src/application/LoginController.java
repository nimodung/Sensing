package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection; //�ܺ� dbconnect.jar������ add������ ������ ã�� �� ����
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

	private Stage primaryStage; //������ ������ //�� ���� ���̾�α� ����
	
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
				//DB�� ������ ���� ��ü
				Connection connection = null;
				Statement st = null;
				//jdbc : java db connection 
				//192.168.0.47 : ��
				String url = "jdbc:mysql://localhost:3306?serverTimezone=UTC";
				String loginID = tf_id.getText().toString();
				String loginPW = pwf_pw.getText().toString();
				String sql = "";
				
				try { //�ü������ ������ �ϵ����� ����ϱ� ������ try/catch
					//driver �޾ƿ��� //DBMS���� �ٸ� >> DBMS �����翡�� �ȳ�����
					Class.forName("com.mysql.cj.jdbc.Driver");
					//������ �����
					connection = DriverManager.getConnection(url, loginID, loginPW);
					
					st = connection.createStatement();
					
					/*
					sql = "select * from tabledb.usertbl;";
					//ResultSet : arr // usertbl�� row���� ��� ���� // �� row���� ��ä�� �������
					ResultSet result = st.executeQuery(sql);
					
					while(result.next()) { //next() : result�� ���� row�� ������ true, ������ false return 
						String sqlResult = result.getString("userid"); //���� row�� userid�� column ��
						System.out.print(sqlResult + " ");
						sqlResult = result.getString("name");
						System.out.println(sqlResult);
					}
					
					//ResultSet, Statement, Connection �ݵ�� �����ֱ�
					result.close();
					st.close();
					connection.close();
					*/
				}catch (SQLException e) { // getConnection catch
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("login");
					alert.setHeaderText("  Login Failed !!!");
					alert.setContentText("���̵�� ��й�ȣ�� Ȯ���ϼ���.");
					alert.show();
					//System.out.println("SQLException");
					e.printStackTrace();
				} catch (ClassNotFoundException e) { //driver �޾ƿ��� catch
					System.out.println("ClassNotFoundException");
					e.printStackTrace();
				}
				
				/*finally { //try, catch �� �����ϰ� ���������� ������ ���� �ϴ� �� //�� �ؾ��ϴ� �͵� 
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
						
						//UserController controller = loadUser.getController(); //root.fxml�� ����� ��Ʈ�ѷ� ��ü�� �־��ִ� ��
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
