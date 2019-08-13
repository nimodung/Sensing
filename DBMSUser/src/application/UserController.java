package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UserController implements Initializable {

	@FXML TextField tf_column;
	@FXML Button btn_query;
	@FXML TextArea ta_result;
	@FXML ListView lv_column, lv_row;
	
	//ResultSet : arr // usertbl�� row���� ��� ���� // �� row���� ��ä�� �������
	ResultSet rs = null;
	
	ObservableList observableList = null;
	
	String sql = "";
	String taStr = "";
	String sqlColumn = "";
	String sqlResult = "";
	
	private Statement st;
	public void setStatement(Statement st) {
		this.st = st;
		
		observableList = FXCollections.observableArrayList();
		try {
			sql = "describe tabledb.usertbl;";
		
			rs = st.executeQuery(sql);
			taStr = "";
			while(rs.next()) {
				sqlResult = rs.getString("Field");
				observableList.add(sqlResult);
				lv_column.setItems(observableList);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		lv_column.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				try {
					observableList = FXCollections.observableArrayList();
					
					taStr = "";
					sql = "select * from tabledb.usertbl;";
					rs = st.executeQuery(sql);
					
					while(rs.next()) { //next() : result�� ���� row�� ������ true, ������ false return 
						sqlColumn = lv_column.getItems().get(newValue.intValue()).toString();
						sqlResult = rs.getString(sqlColumn); //���� row�� userid�� column ��
						//System.out.println(sqlResult);
						
						
						observableList.add(sqlResult);
						lv_row.setItems(observableList);
						//sqlResult = rs.getString("name");
						//System.out.println(sqlResult);
					}
					
					
					//ResultSet, Statement, Connection �ݵ�� �����ֱ�
					//result.close();
					//st.close();
					//connection.close();
				
				} catch (SQLException e) {
					
				}
				
			}
		});
	}
	
	
	
	public void handleBtnQuery (ActionEvent event)
	{
		
		try {
			observableList = FXCollections.observableArrayList();
			
			taStr = "";
			sql = "select * from tabledb.usertbl;";
			rs = st.executeQuery(sql);
			
			while(rs.next()) { //next() : result�� ���� row�� ������ true, ������ false return 
				sqlColumn = tf_column.getText().toString();
				sqlResult = rs.getString(sqlColumn); //���� row�� userid�� column ��
				//System.out.println(sqlResult);
				
				
				taStr += sqlResult + "\r\n";
				ta_result.setText(taStr);
				//sqlResult = rs.getString("name");
				//System.out.println(sqlResult);
			}
			
			
			//ResultSet, Statement, Connection �ݵ�� �����ֱ�
			//result.close();
			//st.close();
			//connection.close();
		
		} catch (SQLException e) {
			
		}
	
	}
	
	
	
}
